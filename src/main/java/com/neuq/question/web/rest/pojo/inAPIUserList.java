package com.neuq.question.web.rest.pojo;

import com.neuq.question.data.pojo.InAPIUser;
import lombok.Data;

import java.util.List;

/**
 * @author wangshyi
 * @date 2018/12/28  10:33
 */
@Data
public class inAPIUserList {

    private List<String> memberIds;

    private List<InAPIUser> members;

    private long total;
}
