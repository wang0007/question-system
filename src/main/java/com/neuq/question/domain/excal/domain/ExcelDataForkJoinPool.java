package com.neuq.question.domain.excal.domain;

import com.neuq.question.domain.excal.support.CommonUpesnForkJoinPool;

/**
 * InAPI专用的线程池,防止影响其他业务
 * @author wangshyi
 * @date 2018/12/27  13:35
 */

public class ExcelDataForkJoinPool extends CommonUpesnForkJoinPool {

    public ExcelDataForkJoinPool(int parallelism) {
        super(parallelism, "Excel-ForkJoin-");
    }

}