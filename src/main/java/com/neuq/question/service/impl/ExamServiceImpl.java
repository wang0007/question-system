package com.neuq.question.service.impl;


import com.alibaba.fastjson.JSON;
import com.neuq.question.data.dao.*;
import com.neuq.question.data.pojo.*;
import com.neuq.question.data.pojo.common.QuestionBasicDO;
import com.neuq.question.error.QuestionBlankEnableErrorException;
import com.neuq.question.service.ExamService;
import com.neuq.question.web.rest.pojo.ExamQuestionResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author wangshyi
 * @date 2018/11/19  11:09
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {

    private final QuestionRepository questionRepository;

    private final QuestionBlankRepository questionBlankRepository;

    private final StringRedisTemplate redisTemplate;

    private final ExamResultRepository examResultRepository;

    private final QuestionResultRepository questionResultRepository;

    private final ExamRepository examRepository;

    /**
     * 考试开始，获取考试题目，导入redis中
     *
     * @param examDO   考试相关属性信息
     * @param user     答题人详细信息
     * @param examId   考试id
     * @param sequence 考试场次
     * @return 考试第一题
     */
    @Override
    public QuestionDO getExamQuestions(ExamDO examDO, InAPIUser user, String examId, String sequence) {

        String questionBlankId = examDO.getQuestionBlankId();

        //获取本场考试所对应的题库状态
        boolean enable = questionBlankRepository.queryEnableById(questionBlankId);

        //如果题库不可用，则抛出异常
        if (!enable) {
            throw new QuestionBlankEnableErrorException("The QuestionBlank is unable");
        }

        //获取所有单、双选题目列表，并打乱原来的顺序,并取出考试设置的数量题目
        List<QuestionDO> singleQuestionList = questionRepository.querySingleQuestionList(questionBlankId);
        Collections.shuffle(singleQuestionList);
        List<QuestionDO> singleQuestions = singleQuestionList.subList(0, examDO.getSingleQuestionCount());

        List<QuestionDO> multipleQuestionList = questionRepository.queryMultipleQuestionList(questionBlankId);
        Collections.shuffle(multipleQuestionList);
        List<QuestionDO> multipleQuestions = multipleQuestionList.subList(0, examDO.getMultipleQuestionCount());

        Boolean order = examDO.getOrder();

        List<QuestionDO> list;

        //如果是顺序考试，则先单选后多选按优先级大排序
        if (order) {
            list = orderQuestions(singleQuestions, multipleQuestions);
        } else { //乱序考试，则单双选合并后打乱顺序
            list = disOrderQuestions(singleQuestions, multipleQuestions);
        }

        //存入redis中，使用json存储list<object>形式，设置相应的key:成员id_考试id_题库id
        String key = getRedisKey(user.getMemberId(), examId, questionBlankId, sequence);
        redisTemplate.opsForValue().set(key, JSON.toJSONString(list));
        String questionList = redisTemplate.opsForValue().get(key);
        List<QuestionDO> questionDOList = JSON.parseArray(questionList, QuestionDO.class);

        QuestionDO questionDO = questionDOList.get(0);

        //新建并存储考试结果部分信息
        ExamResultDO examResultDO = new ExamResultDO();

        examResultDO.setSequence(sequence);
        examResultDO.setExamId(examId);
        examResultDO.setUser(user);
        examResultDO.setTotalTime(System.currentTimeMillis());

        examResultRepository.saveExamResult(examResultDO);

        //存储第一题部分信息
        saveOfStart(questionDO, user, sequence);

        return questionDO;

    }

    @Override
    public ExamQuestionResult getNextQuestion(String examId, InAPIUser user, Integer questionIndex, List<String> userAnswers, String questionId, String sequence) {

        String questionBlankId = getQuestionBlankId(examId);

        String key = getRedisKey(user.getMemberId(), examId, questionBlankId, sequence);

        List<QuestionDO> questionDOList = getRedisValue(key);

        //通过题目下标获取请求的题目信息
        QuestionDO requestQuestionDO = questionDOList.get(questionIndex);

        //请求的题目所对应的用户题目结果信息
        QuestionResultDO requestQuestionResultDO = questionResultRepository.queryResultById(requestQuestionDO.getId(), user.getMemberId(), sequence);

        //当前题目的结果信息
        QuestionResultDO presentQuestionResultDO = questionResultRepository.queryResultById(questionId, user.getMemberId(), sequence);

        //如果请求的用户题目作答结果对象为空则请求的为未保存过结果的题，则判断当前题结果信息，并进入下一题计时与保存
        if (requestQuestionResultDO == null) {

            //判断原来用户的选择和现在的选择判断,如果为false则用户更改了所选答案
            boolean flag = judgeUserAnswer(presentQuestionResultDO.getChoose(), userAnswers);

            if (!flag) {

                //如果用户的选择改动，则更新当前的题目结果信息
                saveOfEnd(key, questionIndex, user.getMemberId(), userAnswers, questionId, sequence);

            }

            //保存请求的题目结果部分信息
            saveOfStart(requestQuestionDO, user, sequence);

            ExamQuestionResult examQuestionResult = new ExamQuestionResult();
            BeanUtils.copyProperties(requestQuestionDO, examQuestionResult);
            examQuestionResult.setQuestionId(requestQuestionDO.getId());
            examQuestionResult.setSequence(sequence);

            return examQuestionResult;

        }

        //当请求的问题结果已存在时，不需要进行请求的题目结果保存、计时操作，只判断当前题目是否改动
        boolean flag = judgeUserAnswer(presentQuestionResultDO.getChoose(), userAnswers);

        if (!flag) {

            //如果用户的选择改动，则保存当前的题目结果信息
            saveOfEnd(key, questionIndex, user.getMemberId(), userAnswers, questionId, sequence);
        }

        ExamQuestionResult examQuestionResult = new ExamQuestionResult();
        BeanUtils.copyProperties(requestQuestionDO, examQuestionResult);
        examQuestionResult.setQuestionId(requestQuestionDO.getId());
        examQuestionResult.setSequence(sequence);
        examQuestionResult.setUserChoose(requestQuestionResultDO.getChoose());

        return examQuestionResult;

    }

    @Override
    public void endExam(String examId, String questionId, String memberId, List<String> userAnswers, Long totalTime, String sequence) {

        lastQuestionSave(examId, questionId, memberId, userAnswers, sequence);

        ExamResultDO examResultDO = examResultRepository.queryExamResult(examId, memberId, sequence);

        lastExamResultSave(examId, memberId, examResultDO, totalTime, sequence);

    }

    /**
     * 获取本题应得的得分，得分模式
     *
     * @param userAnswers 用户的答案，只传选项id
     * @param rightAnswer 正确答案，保存的为选项id列表
     * @param score       本题的总分
     * @return 所得分
     */
    private Integer getQuestionScore(List<String> userAnswers, List<String> rightAnswer, Integer score) {

        boolean flag = rightAnswer.containsAll(userAnswers) && userAnswers.containsAll(rightAnswer);

        if (flag) {
            return score;
        }
        return 0;
    }

    /**
     * 竞速模式下，下一题请求
     *
     * @param examId        考试id
     * @param user          人员信息
     * @param questionIndex 请求的题目索引
     * @param userAnswers   用户选择
     * @param questionId    问题id
     * @param sequence      考试场次
     * @return 请求的题目信息
     */
    @Override
    public QuestionDO getNextSpeedQuestion(String examId, InAPIUser user, Integer questionIndex, List<String> userAnswers, String questionId, String sequence) {

        String questionBlankId = getQuestionBlankId(examId);

        String redisKey = getRedisKey(user.getMemberId(), examId, questionBlankId, sequence);

        List<QuestionDO> list = getRedisValue(redisKey);

        //请求的题目信息
        QuestionDO requestQuestionDO = list.get(questionIndex);

        //当前题目信息
        QuestionDO presentQuestionDO = questionRepository.queryById(questionId, questionBlankId);
        QuestionResultDO presentQuestionResultDO = questionResultRepository.queryResultById(presentQuestionDO.getId(), user.getMemberId(), sequence);

        //访问下一题，此时保存并更新当前用户答案
        saveSpeedPresentQuestion(presentQuestionDO, presentQuestionResultDO, userAnswers);

        //请求的题目计时
        saveOfStart(requestQuestionDO, user, sequence);

        return requestQuestionDO;
    }

    /**
     * 竞速模式的结束
     *
     * @param examId      本场考试id
     * @param questionId  问题id
     * @param memberId    用户信息
     * @param userAnswers 用户的选择
     * @param totalTime   前端传入的时间
     * @param sequence    考试场次
     */
    @Override
    public void endSpeedExam(String examId, String questionId, String memberId, List<String> userAnswers, Long totalTime, String sequence) {

        ExamResultDO examResultDO = examResultRepository.queryExamResult(examId, memberId, sequence);

        String questionBlankId = getQuestionBlankId(examId);
        //所选答案正确，直接保存
        QuestionDO questionDO = questionRepository.queryById(questionId, questionBlankId);
        QuestionResultDO questionResultDO = questionResultRepository.queryResultById(questionId, memberId, sequence);
        saveSpeedPresentQuestion(questionDO, questionResultDO, userAnswers);

        lastExamResultSave(examId, memberId, examResultDO, totalTime, sequence);

    }

    /**
     * 竞速模式结果判断
     *
     * @param userAnswers 用户选择
     * @param questionId  当前题目id
     * @param examId      考试id
     * @return true:结果正确；false:结果错误
     */
    @Override
    public boolean judgePresentResult(List<String> userAnswers, String questionId, String examId) {

        String questionBlankId = getQuestionBlankId(examId);
        QuestionDO questionDO = questionRepository.queryById(questionId, questionBlankId);

        return judgeRightAnswer(questionDO.getAnswer(), userAnswers);
    }

    @Override
    public List<ExamDO> sortExamList(List<ExamDO> list) {

        // 进行中的考试排在前面，按开始时间正序排列
        List<ExamDO> sortList = list.stream()
                .filter(examDO -> examDO.getEndTime().after(new Date()) &&
                        examDO.getStartTime().before(new Date()))
                .sorted(Comparator.comparing(ExamDO::getStartTime))
                .collect(Collectors.toList());

        // 未开始的考试排在中间，按开始时间倒序排列
        sortList.addAll(list.stream()
                .filter(examDO -> examDO.getStartTime().after(new Date()))
                .sorted(Comparator.comparing(ExamDO::getStartTime).reversed())
                .collect(Collectors.toList()));

        // 完成的考试排在进行中考试之后，按结束时间倒序排列
        sortList.addAll(list.stream()
                .filter(examDO -> examDO.getEndTime().before(new Date()))
                .sorted(Comparator.comparing(ExamDO::getEndTime).reversed())
                .collect(Collectors.toList()));

        return sortList;
    }

    /**
     * 判断当前的用户选择与答案是否一致,得分模式
     *
     * @param answer      正确答案
     * @param userAnswers 用户选择
     * @return true相等 false不相等
     */
    private boolean judgeRightAnswer(List<String> answer, List<String> userAnswers) {

        return answer.containsAll(userAnswers) && userAnswers.containsAll(answer);
    }

    /**
     * 竞速模式下，保存当前题目结果，update操作
     *
     * @param presentQuestionDO       当前的题目信息
     * @param presentQuestionResultDO 当前题目所对应的题目结果信息
     * @param userAnswers             用户的选择
     */
    private void saveSpeedPresentQuestion(QuestionDO presentQuestionDO, QuestionResultDO presentQuestionResultDO, List<String> userAnswers) {

        presentQuestionResultDO.setChoose(userAnswers);
        presentQuestionResultDO.setRightAnswer(presentQuestionDO.getAnswer());
        presentQuestionResultDO.setCostTime(System.currentTimeMillis() - presentQuestionResultDO.getCostTime());
        presentQuestionResultDO.setScore(presentQuestionDO.getScore());

        questionResultRepository.updateResult(presentQuestionResultDO);

    }


    /**
     * 保存本次考试结果信息
     *
     * @param examId       考试id
     * @param memberId     用户信息
     * @param examResultDO 考试结果信息
     * @param totalTime    前端传来的考试总时间
     * @param sequence     考试场次
     */
    private void lastExamResultSave(String examId, String memberId, ExamResultDO examResultDO, Long totalTime, String sequence) {

        String questionBlankId = getQuestionBlankId(examId);

        String redisKey = getRedisKey(memberId, examId, questionBlankId, sequence);

        List<QuestionDO> redisValue = getRedisValue(redisKey);

        //后端判断的时间
        Long endTime = (System.currentTimeMillis() - examResultDO.getTotalTime());
        long judge = 2000;
        if (endTime >= totalTime && endTime - totalTime < judge) {
            examResultDO.setTotalTime(totalTime);
        } else if (endTime < totalTime && totalTime - endTime < judge) {
            examResultDO.setTotalTime(totalTime);
        } else {
            examResultDO.setTotalTime(endTime);
        }
        examResultDO.setQuestionIds(getQuestionIds(redisValue));
        examResultDO.setTotalScore(getTotalScore(examResultDO.getQuestionIds(), memberId, sequence));

        examResultRepository.updateExamResult(examResultDO);

    }

    /**
     * 获得考试总分
     *
     * @param questionIds 答题列表
     * @param memberId    用户id
     * @param sequence    考试场次
     * @return 得分
     */
    private Integer getTotalScore(List<String> questionIds, String memberId, String sequence) {

        return questionIds.stream()
                .mapToInt(questionId -> {
                            QuestionResultDO questionResultDO = questionResultRepository.queryResultById(questionId, memberId, sequence);
                            return questionResultDO.getScore();
                        }
                ).sum();

    }

    /**
     * 获得本次考试的所有题目id列表
     *
     * @param redisValue 本考考试的题目信息
     * @return 题目id信息
     */
    private List<String> getQuestionIds(List<QuestionDO> redisValue) {

        return redisValue.stream()
                .map(QuestionBasicDO::getId)
                .collect(Collectors.toList());

    }

    /**
     * 保存最后一道题的结果信息,得分模式下
     *
     * @param examId      考试id
     * @param questionId  问题id
     * @param memberId    人员id
     * @param userAnswers 用户的选择
     * @param sequence    考试场次
     */
    private void lastQuestionSave(String examId, String questionId, String memberId, List<String> userAnswers, String sequence) {

        String questionBlankId = getQuestionBlankId(examId);

        QuestionDO questionDO = questionRepository.queryById(questionId, questionBlankId);

        QuestionResultDO questionResultDO = questionResultRepository.queryResultById(questionId, memberId, sequence);

        questionResultDO.setCostTime(System.currentTimeMillis() - questionResultDO.getCostTime());
        questionResultDO.setChoose(userAnswers);
        questionResultDO.setScore(getQuestionScore(userAnswers, questionResultDO.getRightAnswer(), questionDO.getScore()));
        questionResultDO.setRightAnswer(questionDO.getAnswer());

        questionResultRepository.updateResult(questionResultDO);

    }

    /**
     * 判断原来用户的选择和现在的选择判断
     *
     * @param choose      保存的用户选择
     * @param userAnswers 用户当前的选择
     * @return 是否改动，改动为false，未改动为true
     */
    private boolean judgeUserAnswer(List<String> choose, List<String> userAnswers) {

        return choose != null && choose.containsAll(userAnswers) && userAnswers.containsAll(choose);
    }

    /**
     * 保存请求下一题时的该题目结果部分信息
     *
     * @param questionDO 问题实体类
     * @param user       人员信息
     * @param examOrder  考试场次
     */
    private void saveOfStart(QuestionDO questionDO, InAPIUser user, String examOrder) {

        QuestionResultDO questionResultDO = new QuestionResultDO();

        questionResultDO.setQuestionId(questionDO.getId());
        questionResultDO.setUser(user);
        questionResultDO.setSequence(examOrder);
        questionResultDO.setRightAnswer(questionDO.getAnswer());
        questionResultDO.setCostTime(System.currentTimeMillis());

        questionResultRepository.saveResult(questionResultDO);

    }

    /**
     * 得分模式下，请求下一题时，保存当前题目结果改动信息,之前已保存过考试开始信息
     *
     * @param key           redis中的key值
     * @param questionIndex 请求的题目下标索引，当前为下一题下标，索引减一保存当前题
     * @param memberId      用户信息
     * @param userAnswers   用户选择
     * @param questionId    问题id
     * @param sequence      考试场次
     */
    private void saveOfEnd(String key, Integer questionIndex, String memberId, List<String> userAnswers, String questionId, String sequence) {

        List<QuestionDO> redisValue = getRedisValue(key);

        QuestionDO questionDO = redisValue.get(questionIndex - 1);

        QuestionResultDO questionResultDO = questionResultRepository.queryResultById(questionId, memberId, sequence);

        questionResultDO.setCostTime(System.currentTimeMillis() - questionResultDO.getCostTime());
        questionResultDO.setChoose(userAnswers);
        questionResultDO.setScore(getQuestionScore(userAnswers, questionResultDO.getRightAnswer(), questionDO.getScore()));

        questionResultRepository.updateResult(questionResultDO);

    }


    /**
     * 生成redis中的key
     *
     * @param memberId        用户id
     * @param examId          考试id
     * @param questionBlankId 题库id
     * @param sequence        本场考试场次，由UUID生成
     * @return key
     */
    private String getRedisKey(String memberId, String examId, String questionBlankId, String sequence) {

        return memberId + "_" + examId + "_" + questionBlankId + "_" + sequence;

    }

    /**
     * 从缓存中得到问题列表
     *
     * @param key redis中的key值
     * @return 问题列表
     */
    private List<QuestionDO> getRedisValue(String key) {

        String jsonList = redisTemplate.opsForValue().get(key);

        return JSON.parseArray(jsonList, QuestionDO.class);

    }

    /**
     * 根据考试id获取本场考试关联的题库id
     *
     * @param examId 考试id
     * @return 题库id
     */
    private String getQuestionBlankId(String examId) {

        ExamDO examDO = examRepository.queryById(examId);

        return examDO.getQuestionBlankId();

    }

    /**
     * 乱序排列题目，获得题目列表
     *
     * @param singleQuestions   单选题列表
     * @param multipleQuestions 多选题目列表
     * @return 乱序题目列表
     */
    private List<QuestionDO> disOrderQuestions(List<QuestionDO> singleQuestions, List<QuestionDO> multipleQuestions) {

        singleQuestions.addAll(multipleQuestions);

        Collections.shuffle(singleQuestions);

        return singleQuestions;

    }

    /**
     * 顺序排列题目列表,按优先级由大到小排列
     *
     * @param singleQuestions   单选题目
     * @param multipleQuestions 多选题目
     * @return 合并后的最终题目列表，单选在前多选在后
     */
    private List<QuestionDO> orderQuestions(List<QuestionDO> singleQuestions, List<QuestionDO> multipleQuestions) {

        singleQuestions.sort(Comparator.comparing(QuestionDO::getPriority));
        Collections.reverse(singleQuestions);

        multipleQuestions.sort(Comparator.comparing(QuestionDO::getPriority));
        Collections.reverse(multipleQuestions);

        singleQuestions.addAll(multipleQuestions);

        return singleQuestions;

    }
}
