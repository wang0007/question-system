package com.neuq.question.web.rest.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 增量请求的数据结果
 *
 * @author liuhaoi
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncrementalResult<T> {

    /**
     * 下次请求的时间戳, 类似于数据库的游标操作, 用来防止请求时间间隔导致的人员遗漏和重复;
     * 服务器记录接收到请求的时间作为结束时间,客户端请求时间作为开始时间
     */
    private Long nextTs;

    /**
     * 数据
     */
    private List<T> data;

    /**
     * 实时总数量，例如签到时实时已签到人数
     */
    private long count;


    public IncrementalResult(Long nextTs, List<T> data) {
        this.nextTs = nextTs;
        this.data = data;
    }
}
