package com.neuq.question.support;

import com.neuq.question.domain.excal.support.CommonUpesnForkJoinPool;
import org.springframework.stereotype.Component;

/**
 * @author wangshyi
 */
@Component
public class ConferenceForkJoinPool extends CommonUpesnForkJoinPool {


    public ConferenceForkJoinPool() {
        super(16, "conference-");
    }
}
