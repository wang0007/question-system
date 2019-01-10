package com.neuq.question.data.dao.impl;

import com.mongodb.client.result.UpdateResult;
import com.neuq.question.data.dao.GroupRepository;
import com.neuq.question.data.dao.common.AbstractMongoRepository;
import com.neuq.question.data.pojo.GroupDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author wangshyi
 * @date 2018/11/1  20:11
 */
@Slf4j
@Repository
public class GroupRepositoryImpl extends AbstractMongoRepository<GroupDO> implements GroupRepository {
    public GroupRepositoryImpl(MongoTemplate template) {
        super(GroupDO.class, template);
    }

    @Override
    public void save(GroupDO groupDO) {

        groupDO.setCtime(System.currentTimeMillis());
        groupDO.setUtime(System.currentTimeMillis());

        template.save(groupDO);
    }

    @Override
    public GroupDO queryById(String groupId) {

        Criteria criteria = Criteria.where(GroupDO.FIELD_ID).is(groupId);

        return template.findOne(Query.query(criteria), clazz);
    }

    @Override
    public long update(GroupDO groupDO) {

        Criteria criteria = Criteria.where(GroupDO.FIELD_ID).is(groupDO.getId());

        Update update = Update.update(GroupDO.FIELD_MEMBER_ID, groupDO.getMemberId())
                .set(GroupDO.FIELD_NAME, groupDO.getName())
                .set(GroupDO.FIELD_UTIME, System.currentTimeMillis());

        UpdateResult updateResult = template.upsert(Query.query(criteria), update, clazz);

        long n = updateResult.getModifiedCount();
        if (n == 0) {
            log.warn("No matched groupId {} found when update the group ", groupDO.getId());
        }

        return n;
    }

    @Override
    public void delete(String groupId) {

        Criteria criteria = Criteria.where(GroupDO.FIELD_ID).is(groupId);
        template.remove(Query.query(criteria), clazz);
    }

    @Override
    public List<GroupDO> queryAllGroup() {

        return template.findAll(clazz);
    }

    @Override
    public long addUser(String groupId, List<String> memberIds) {

        Criteria criteria = Criteria.where(GroupDO.FIELD_ID).is(groupId);

        Update update = new Update();

        update.push(GroupDO.FIELD_MEMBER_ID).each(memberIds.toArray())
                .set(GroupDO.FIELD_UTIME, System.currentTimeMillis());

        UpdateResult updateResult = template.updateMulti(Query.query(criteria), update, clazz);

        long n = updateResult.getModifiedCount();
        if (n == 0) {
            log.warn("No matched groupId {} found when update the group ", groupId);
        }
        return n;
    }

    @Override
    public long removeUser(String groupId, List<String> memberIds) {

        Criteria criteria = Criteria.where(GroupDO.FIELD_ID).is(groupId);

        Update update = new Update().pullAll(GroupDO.FIELD_MEMBER_ID, memberIds.toArray())
                .set(GroupDO.FIELD_UTIME, System.currentTimeMillis());

        UpdateResult updateResult = template.updateMulti(Query.query(criteria), update, clazz);

        long n = updateResult.getModifiedCount();
        if (n == 0) {
            log.warn("No matched groupId {} found when update the group ", groupId);
        }
        return n;
    }
}
