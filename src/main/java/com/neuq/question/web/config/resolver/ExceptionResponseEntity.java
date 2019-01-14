package com.neuq.question.web.config.resolver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangshyi
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponseEntity {

    private String msg;

    private String code;

}
