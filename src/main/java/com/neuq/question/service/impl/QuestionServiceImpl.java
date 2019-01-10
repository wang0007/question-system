package com.neuq.question.service.impl;


import com.google.common.collect.Lists;
import com.neuq.question.data.dao.QuestionRepository;
import com.neuq.question.data.pojo.QuestionDO;
import com.neuq.question.domain.excal.domain.ExcelData;
import com.neuq.question.domain.enums.QuestionType;
import com.neuq.question.error.AnswerNotExistException;
import com.neuq.question.service.QuestionService;
import com.neuq.question.web.rest.pojo.ExcelImportResult;
import com.neuq.question.web.rest.pojo.ExcelQuestionResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.neuq.question.domain.enums.QuestionType.MULTIPLE;
import static com.neuq.question.domain.enums.QuestionType.PUZZLE;
import static com.neuq.question.domain.enums.QuestionType.SINGLE;


/**
 * @author wangshyi
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    private final MessageSource messageSource;


    /**
     * 从excel文件导入题目
     *
     * @param bytes           文件数据
     * @param questionBlankId 题库Id
     * @return 导入结果, 成功或失败的数量
     */
    @Override
    public ExcelImportResult importQuestionFromExcel(byte[] bytes, String questionBlankId) {

        Map<String, Integer> fieldNames = new HashMap<>(17);

        fieldNames.put("name", 0);
        fieldNames.put("description", 1);
        fieldNames.put("questionType", 2);
        fieldNames.put("score", 3);
        fieldNames.put("priority", 4);
        fieldNames.put("question1", 5);
        fieldNames.put("question2", 6);
        fieldNames.put("question3", 7);
        fieldNames.put("question4", 8);
        fieldNames.put("question5", 9);
        fieldNames.put("question6", 10);
        fieldNames.put("answer1", 11);
        fieldNames.put("answer2", 12);
        fieldNames.put("answer3", 13);
        fieldNames.put("answer4", 14);
        fieldNames.put("answer5", 15);
        fieldNames.put("answer6", 16);

        ExcelData<ExcelQuestionResult> data = new ExcelData<>(bytes, Lists.newArrayList(0), 2,
                ExcelQuestionResult.class, fieldNames);

        ExcelImportResult importResult = new ExcelImportResult();

        importResult.setSucceed(0);
        importResult.setFailed(0);

        data.processData(row -> {
            try {

                ExcelQuestionResult excelQuestionResult = row.getData();

                boolean flag = judgeQuestionAndAnswer(getQuestionList(excelQuestionResult), getAnswerList(excelQuestionResult));

                if (!flag) {
                    throw new AnswerNotExistException("The imported answer did not match the corresponding option");
                }

                QuestionDO questionDO = transToQuestionDo(excelQuestionResult);

                questionDO.setQuestionBlankId(questionBlankId);

                QuestionType questionType = questionDO.getQuestionType();
                List<String> answer = questionDO.getAnswer();

                if (questionType.equals(SINGLE)) {
                    List<String> list = new ArrayList<>();
                    list.add(answer.get(0));
                    questionDO.setAnswer(list);
                }
                questionRepository.save(questionDO);
                importResult.successOnce();

            } catch (Exception e) {
                importResult.failedOnce();
            }
        });

        if (importResult.getFailed() > 0) {

            byte[] excelResult = data.exportResult();

            importResult.setExcelData(excelResult);
        }

        return importResult;

    }

    /**
     * 将Excel表中传入的答案信息转为list用于校验
     *
     * @param excelQuestionResult Excel中数据
     * @return AnswerList
     */
    private List<String> getAnswerList(ExcelQuestionResult excelQuestionResult) {

        List<String> list = new ArrayList<>();

        list.add(excelQuestionResult.getAnswer1());
        list.add(excelQuestionResult.getAnswer2());
        list.add(excelQuestionResult.getAnswer3());
        list.add(excelQuestionResult.getAnswer4());
        list.add(excelQuestionResult.getAnswer5());
        list.add(excelQuestionResult.getAnswer6());

        return list.stream()
                .filter(Objects::nonNull).collect(Collectors.toList());

    }

    /**
     * 将Excel表中传入的选项信息转为list用于校验
     *
     * @param excelQuestionResult Excel中数据
     * @return QuestionList
     */
    private List<String> getQuestionList(ExcelQuestionResult excelQuestionResult) {

        List<String> list = new LinkedList<>();

        list.add(excelQuestionResult.getQuestion1());
        list.add(excelQuestionResult.getQuestion2());
        list.add(excelQuestionResult.getQuestion3());
        list.add(excelQuestionResult.getQuestion4());
        list.add(excelQuestionResult.getQuestion5());
        list.add(excelQuestionResult.getQuestion6());

        return list.stream().filter(Objects::nonNull).collect(Collectors.toList());


    }

    /**
     * 将excel中的各个数据信息转换为QuestionDO
     *
     * @param excelQuestionResult excel导入后形成的数据类
     * @return QuestionDO
     */
    private QuestionDO transToQuestionDo(ExcelQuestionResult excelQuestionResult) {

        QuestionDO questionDO = new QuestionDO();

        questionDO.setScore(excelQuestionResult.getScore());

        questionDO.setName(excelQuestionResult.getName());

        questionDO.setDescription(excelQuestionResult.getDescription());

        questionDO.setPriority(excelQuestionResult.getPriority());

        questionDO.setQuestion(handleQuestionMap(excelQuestionResult));

        questionDO.setAnswer(getExcelAnswerIds(questionDO.getQuestion(), excelQuestionResult));

        questionDO.setQuestionType(changeType(excelQuestionResult.getQuestionType()));

        return questionDO;

    }

    /**
     * 将传入的string类型的题目类型转换为枚举类型
     *
     * @param questionType 题目类型（string类型）
     * @return 枚举类型
     */
    private QuestionType changeType(String questionType) {

        QuestionType type = null;

        if (messageSource
                .getMessage("question_type_single", new Object[]{}, LocaleContextHolder.getLocale())
                .equals(questionType)) {
            type = SINGLE;

        } else if (messageSource
                .getMessage("question_type_multiple", new Object[]{}, LocaleContextHolder.getLocale())
                .equals(questionType)) {
            type = MULTIPLE;

        } else if (messageSource
                .getMessage("question_type_puzzle", new Object[]{}, LocaleContextHolder.getLocale())
                .equals(questionType)) {
            type = PUZZLE;
        }

        return type;
    }

    /**
     * 根据导入的答案和选项列表获取正确答案所对应的选项id
     *
     * @param question            选项列表
     * @param excelQuestionResult 导入的类信息
     * @return 正确答案对应的选项id列表
     */
    private List<String> getExcelAnswerIds(Map<String, String> question, ExcelQuestionResult excelQuestionResult) {

        List<String> list = new LinkedList<>();

        list.add(excelQuestionResult.getAnswer1());
        list.add(excelQuestionResult.getAnswer2());
        list.add(excelQuestionResult.getAnswer3());
        list.add(excelQuestionResult.getAnswer4());
        list.add(excelQuestionResult.getAnswer5());
        list.add(excelQuestionResult.getAnswer6());

        List<String> answers = list.stream()
                .filter(Objects::nonNull).collect(Collectors.toList());

        return getAnswerIds(answers, question);

    }

    @Override
    public List<String> getAnswerIds(List<String> answer, Map<String, String> question) {

        return answer.stream()
                .map(s -> {
                    final String[] answerId = new String[1];
                    question.forEach((key, value) -> {
                                if (value.equals(s)) {
                                    answerId[0] = key;
                                }
                            }
                    );
                    return answerId[0];
                }).collect(Collectors.toList());
    }

    @Override
    public boolean judgeQuestionAndAnswer(List<String> question, List<String> answer) {

        return question.containsAll(answer);

    }

    /**
     * 生成带选项id的map形式的题目选项
     *
     * @param excelQuestionResult 从excel导入的所有string类型的选项
     * @return map类型的选项
     */
    private Map<String, String> handleQuestionMap(ExcelQuestionResult excelQuestionResult) {

        List<String> list = new LinkedList<>();

        list.add(excelQuestionResult.getQuestion1());
        list.add(excelQuestionResult.getQuestion2());
        list.add(excelQuestionResult.getQuestion3());
        list.add(excelQuestionResult.getQuestion4());
        list.add(excelQuestionResult.getQuestion5());
        list.add(excelQuestionResult.getQuestion6());

        Map<String, String> questionMap = new LinkedHashMap<>(16);

        for (String question : list) {
            if (question != null) {
                String randomKey = RandomStringUtils.randomAlphanumeric(16);
                questionMap.put(randomKey, question);
            }
        }

        return questionMap;
    }


}
