package com.neuq.question.service.impl;

import com.neuq.question.data.dao.UserRepsitory;
import com.neuq.question.data.pojo.InAPIUser;
import com.neuq.question.service.InAPIUserService;
import com.neuq.question.web.rest.pojo.inAPIUserList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangshyi
 * @date 2018/12/28  9:53
 */
@Service
@RequiredArgsConstructor
public class InAPIUserRestServiceImpl implements InAPIUserService {

    private final UserRepsitory userRepsitory;

    @Override
    public Map<String, InAPIUser> getUsersByMemberIds(List<String> memberIds) {

        Map<String, InAPIUser> map = new HashMap<>();

        memberIds.forEach(
                memberId->{
                    InAPIUser user = userRepsitory.queryById(memberId);
                    map.put(memberId,user);
                }
        );

        return map;
    }

    @Override
    public inAPIUserList queryAllMemberIds() {
        inAPIUserList inAPIUserList = new inAPIUserList();

        List<String> memberIds = new ArrayList<>();
        long total = 0;
        List<InAPIUser> list = userRepsitory.queryAll();
        for (InAPIUser user: list){
            memberIds.add(user.getMemberId());
            total++;
        }
        long count = list.size();
        if (count!= total){
            total = count;
        }
        inAPIUserList.setMembers(list);
        inAPIUserList.setMemberIds(memberIds);
        inAPIUserList.setTotal(total);

        return inAPIUserList;
    }

}
