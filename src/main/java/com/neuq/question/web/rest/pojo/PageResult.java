package com.neuq.question.web.rest.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author wangshyi
 */
@Data
@NoArgsConstructor
public class PageResult<T> {

    private Integer start;

    private Integer size;

    private List<T> data;

    private Long total;

    public PageResult(List<T> data, Long total) {
        this.data = data;
        this.total = total;
    }
}
