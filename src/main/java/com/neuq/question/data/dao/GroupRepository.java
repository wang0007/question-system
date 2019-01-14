package com.neuq.question.data.dao;


import com.neuq.question.data.pojo.GroupDO;

import java.util.List;

/**
 * @author wangshyi
 */
public interface GroupRepository {

    /**
     * 新增分组
     *
     * @param groupDO 分组信息
     */
    void save(GroupDO groupDO);

    /**
     * 通过分组id查询分组信息
     *
     * @param groupId 分组id
     * @return 分组信息
     */
    GroupDO queryById(String groupId);

    /**
     * 更改分组信息
     *
     * @param groupDO 分组信息
     * @return 更改条数
     */
    long update(GroupDO groupDO);

    /**
     * 通过分组id删除相应分组
     *
     * @param groupId 分组id
     */
    void delete(String groupId);

    /**
     * 查询所有分组信息
     *
     * @return 分组列表
     */
    List<GroupDO> queryAllGroup();

    /**
     * 根据分组id添加成员id
     *
     * @param groupId   分组id
     * @param memberIds 成员id列表
     * @return 添加的个数
     */
    long addUser(String groupId, List<String> memberIds);

    /**
     * 移除分组ID下的成员
     *
     * @param groupId   分组id
     * @param memberIds 成员id
     * @return 移除个数
     */
    long removeUser(String groupId, List<String> memberIds);
}
