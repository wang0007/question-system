package com.neuq.question.support;

import com.neuq.question.domain.excal.support.CommonUpesnForkJoinPool;
import org.springframework.stereotype.Component;

/**
 * @author liuhaoi
 */
@Component
public class ConferenceForkJoinPool extends CommonUpesnForkJoinPool {


    public ConferenceForkJoinPool() {
        super(16, "conference-");
    }
}
