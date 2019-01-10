package com.neuq.question.web.rest.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**excel导入的数据类
 * @author wangshyi
 * @date 2018/11/13  16:50
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelQuestionResult {

        private String name;

        private String description;

        private String questionType;

        private Integer score;

        private Integer priority;

        private String question1;

        private String question2;

        private String question3;

        private String question4;

        private String question5;

        private String question6;

        private String answer1;

        private String answer2;

        private String answer3;

        private String answer4;

        private String answer5;

        private String answer6;



}
