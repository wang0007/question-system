package com.neuq.question.web.rest.management.answer.exam;


import com.neuq.question.data.dao.ExamRepository;
import com.neuq.question.data.dao.QuestionRepository;
import com.neuq.question.data.dao.UserRepsitory;
import com.neuq.question.data.pojo.ExamDO;
import com.neuq.question.data.pojo.InAPIUser;
import com.neuq.question.data.pojo.QuestionDO;
import com.neuq.question.error.ExamUserAnswerErrorException;
import com.neuq.question.error.QuestionQuantityException;
import com.neuq.question.error.TimeExpiredException;
import com.neuq.question.error.TimeNotArrivedException;
import com.neuq.question.service.ExamService;
import com.neuq.question.web.rest.pojo.ExamQuestionResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * @author wangshyi
 */
@RestController
@RequestMapping(value = "rest/v1/answer/exam")
@RequiredArgsConstructor
@Api(value = "考试信息接口", description = "考试信息接口")
public class ExamController {

    private final ExamRepository repository;

    private final QuestionRepository questionRepository;

    private final ExamService examService;

    private final UserRepsitory userRepsitory;

    @ApiOperation(value = "新建考试信息", notes = "新建考试信息")
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ExamDO insert(@RequestBody ExamDO examDO) {

        checkQuestionQuantity(examDO);

        repository.save(examDO);

        return examDO;
    }

    @ApiOperation(value = "通过考试Id获取相应考试信息", notes = "通过考试Id获取相应考试信息")
    @GetMapping(value = "{examId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ExamDO queryById(@PathVariable("examId") String examId) {

        return repository.queryById(examId);
    }

    @ApiOperation(value = "修改考试信息", notes = "修改考试信息")
    @PutMapping(value = "{examId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ExamDO update(@PathVariable("examId") String examId,
                         @RequestBody ExamDO examDO) {

        checkQuestionQuantity(examDO);

        repository.update(examId, examDO);

        return examDO;
    }

    @ApiOperation(value = "通过考试Id删除对应考试信息", notes = "通过考试Id删除对应考试信息")
    @DeleteMapping(value = "{examId}")
    public void delete(@PathVariable("examId") String examId) {

        repository.delete(examId);
    }

    @ApiOperation(value = "获取符合条件下所有考试信息列表", notes = "获取符合条件下所有考试信息列表")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ExamDO> queryList(@RequestParam(value = "keyword", required = false) String keyword,
                                  @RequestParam(value = "start", defaultValue = "0") int start,
                                  @RequestParam(value = "size", defaultValue = "10") int size) {

        List<ExamDO> examDOList = repository.queryList(start, size, keyword);

        return examService.sortExamList(examDOList);
    }

    /**
     * 所有模式下，考试开始，导入本场考试题目
     *
     * @param examId 考试id
     * @return 考试题目列表
     */
    @ApiOperation(value = "考试开始从相应题库导入本场考试的题目", notes = "考试开始从相应题库导入本场考试的题目")
    @GetMapping(value = "{examId}/start", produces = MediaType.APPLICATION_JSON_VALUE)
    public ExamQuestionResult queryQuestions(@RequestParam("memberId") String memberId,
                                             @PathVariable(value = "examId") String examId) {

        InAPIUser user = userRepsitory.queryById(memberId);

        ExamDO examDO = repository.queryById(examId);

        checkExamTime(examDO);

        String sequence = UUID.randomUUID().toString();
        QuestionDO questionDO = examService.getExamQuestions(examDO, user, examId, sequence);

        ExamQuestionResult examQuestionResult = new ExamQuestionResult();
        BeanUtils.copyProperties(questionDO, examQuestionResult);
        examQuestionResult.setQuestionId(questionDO.getId());
        examQuestionResult.setSequence(sequence);

        return examQuestionResult;
    }


    /**
     * 得分模式下获取下一题或者上一题的请求 ，并同时保存相应的数据
     *
     * @param examId        当前考试id
     * @param questionId    当前题目id
     * @param questionIndex 请求的下一题或上一题下标（0<index<list.size）
     * @param userAnswers   用户当前题目选择的答案
     * @return 请求的题目信息
     */
    @GetMapping(value = "{examId}/score/next/{questionId}/{questionIndex}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "得分模式下，获取下一题（上一题）相关信息，同时保存上一题答案", notes = "得分模式下，获取下一题（上一题）相关信息，同时保存上一题答案")
    public ExamQuestionResult nextScoreQuestion(@PathVariable(value = "examId") String examId,
                                                @PathVariable(value = "questionId") String questionId,
                                                @RequestParam("memberId") String memberId,
                                                @RequestParam(value = "sequence") String sequence,
                                                @PathVariable(value = "questionIndex") Integer questionIndex,
                                                @RequestParam(value = "userAnswers", defaultValue = "") List<String> userAnswers) {

        InAPIUser user = userRepsitory.queryById(memberId);

        return examService.getNextQuestion(examId, user, questionIndex, userAnswers, questionId, sequence);
    }

    /**
     * 得分模式下考试，考试结束操作
     *
     * @param examId     本场考试id
     * @param questionId 当前题目id，最后一题
     */
    @GetMapping(value = "{examId}/score/end/{questionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "得分模式下，考试结束请求", notes = "得分模式下，考试结束请求")
    public void endScoreQuestion(@PathVariable(value = "examId") String examId,
                                 @PathVariable(value = "questionId") String questionId,
                                 @RequestParam(value = "sequence") String sequence,
                                 @RequestParam("memberId") String memberId,
                                 @RequestParam(value = "userAnswers") List<String> userAnswers,
                                 @RequestParam(value = "totalTime") Long totalTime) {

        InAPIUser user = userRepsitory.queryById(memberId);

        examService.endExam(examId, questionId, user.getMemberId(), userAnswers, totalTime, sequence);

    }


    /**
     * 竞速模式下获取下一题或者上一题的请求 ，并同时保存相应的数据
     *
     * @param examId        当前考试id
     * @param questionId    当前题目id
     * @param questionIndex 请求的下一题或上一题下标（0<index<list.size）
     * @param userAnswers   用户当前题目选择的答案
     * @return 请求的题目信息
     */
    @GetMapping(value = "{examId}/speed/next/{questionId}/{questionIndex}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "竞速模式下，获取下一题（上一题）相关信息，同时保存当前题目结果", notes = "竞速模式下，获取下一题（上一题）相关信息，同时保存当前题目结果")
    public ExamQuestionResult nextSpeedQuestion(@PathVariable(value = "examId") String examId,
                                                @RequestParam("memberId") String memberId,
                                                @PathVariable(value = "questionId") String questionId,
                                                @PathVariable(value = "questionIndex") Integer questionIndex,
                                                @RequestParam(value = "sequence") String sequence,
                                                @RequestParam(value = "userAnswers") List<String> userAnswers) {

        InAPIUser user = userRepsitory.queryById(memberId);

        //判断用户选择是否正确
        boolean flag = examService.judgePresentResult(userAnswers, questionId, examId);

        if (!flag) {
            throw new ExamUserAnswerErrorException("the user answer is error!");
        }

        QuestionDO questionDO = examService.getNextSpeedQuestion(examId, user, questionIndex, userAnswers, questionId, sequence);

        ExamQuestionResult examQuestionResult = new ExamQuestionResult();
        BeanUtils.copyProperties(questionDO, examQuestionResult);
        examQuestionResult.setQuestionId(questionDO.getId());
        examQuestionResult.setSequence(sequence);

        return examQuestionResult;

    }

    /**
     * 竞速模式下考试，考试结束操作
     *
     * @param examId     本场考试id
     * @param questionId 当前题目id，最后一题
     */
    @GetMapping(value = "{examId}/speed/end/{questionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "竞速模式下，考试结束请求", notes = "竞速模式下，考试结束请求")
    public void endQuestion(@PathVariable(value = "examId") String examId,
                            @RequestParam("memberId") String memberId,
                            @PathVariable(value = "questionId") String questionId,
                            @RequestParam(value = "sequence") String sequence,
                            @RequestParam(value = "userAnswers") List<String> userAnswers,
                            @RequestParam(value = "totalTime") Long totalTime) {

        InAPIUser user = userRepsitory.queryById(memberId);

        //判断用户选择是否正确
        boolean flag = examService.judgePresentResult(userAnswers, questionId, examId);

        if (!flag) {
            throw new ExamUserAnswerErrorException("the user answer is error!");
        }
        examService.endSpeedExam(examId, questionId, user.getMemberId(), userAnswers, totalTime, sequence);

    }


    /**
     * 判断考试时间是否在设定时间内
     *
     * @param examDO 考试信息
     */
    private void checkExamTime(ExamDO examDO) {

        long ts = System.currentTimeMillis();
        if (ts < examDO.getStartTime().getTime()) {
            throw new TimeNotArrivedException("exam begin time not arrive");
        }

        if (ts > examDO.getEndTime().getTime()) {
            throw new TimeExpiredException("exam time expired");
        }
    }

    /**
     * 判断题目数量是否满足题库中数量
     *
     * @param examDO 考试相关信息
     */
    private void checkQuestionQuantity(ExamDO examDO) {

        String questionBlankId = examDO.getQuestionBlankId();

        Integer singleCount = examDO.getSingleQuestionCount();

        Integer multipleCount = examDO.getMultipleQuestionCount();

        List<QuestionDO> singleList = questionRepository.querySingleQuestionList(questionBlankId);

        List<QuestionDO> multipleList = questionRepository.queryMultipleQuestionList(questionBlankId);

        //单选题溢出
        if (singleList.size() < singleCount && multipleList.size() >= multipleCount) {
            throw new QuestionQuantityException("设置的单选题数量超出当前题库单选题总数量！");
        }
        //多选题溢出
        if (singleList.size() >= singleCount && multipleList.size() < multipleCount) {
            throw new QuestionQuantityException("设置的多选题数量超出当前题库多选题总数量！");
        }
        //同时溢出
        if (singleList.size() < singleCount) {
            throw new QuestionQuantityException("设置的单选、多选题目数量均超出当前题库的总数量！");
        }

    }
}
