package com.neuq.question.service;


import com.neuq.question.web.rest.pojo.ExcelImportResult;

import java.util.List;
import java.util.Map;

/**
 * @author yegk7
 */
public interface QuestionService {

    /**
     * 从excel导入题目
     *
     * @param bytes           文件数据
     * @param questionBlankId 题库Id
     * @return 导入结果
     */
    ExcelImportResult importQuestionFromExcel(byte[] bytes, String questionBlankId);


    /**
     * 获取答案的id列表
     *
     * @param answer   答案列表
     * @param question 问题选项Id和问题选项对应的map
     * @return 答案id列表
     */
    List<String> getAnswerIds(List<String> answer, Map<String, String> question);

    /**
     * 判断传入的答案是否有相应的选项与之匹配
     * @param question 传入的问题选项
     * @param answer 传入的答案
     * @return true：有，false：无
     */
    boolean judgeQuestionAndAnswer(List<String> question, List<String> answer);

}
