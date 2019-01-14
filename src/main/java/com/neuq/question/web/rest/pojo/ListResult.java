package com.neuq.question.web.rest.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author wangshyi
 */
@Data
@AllArgsConstructor
public class ListResult<T> {

    private List<T> data;

}
