package com.neuq.question.data.pojo;

import com.neuq.question.data.pojo.common.BasicDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author wangshyi
 * @since 2018/8/1 18:58
 */
@Data
@Document(collection = "conference.guest")
@CompoundIndexes({
        @CompoundIndex(name = "conferenceId_mobile_unique_index", def = "{'conferenceId' : 1, 'mobile': 1}", unique = true)
})
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ConferenceGuestDO extends BasicDO {

    public static final String FIELD_NAME = "name";
    public static final String FIELD_MOBILE = "mobile";
    public static final String FIELD_EMAIL = "email";
    public static final String FIELD_COMPANY = "company";
    public static final String FIELD_POSITION = "position";
    public static final String FIELD_DEPARTMENT = "department";
    public static final String FIELD_GENDER = "gender";
    public static final String FIELD_BIRTHDAY = "birthday";


    @Size(max = 50)
    private String name;

    @Size(max = 50)
    @NotNull
    private String mobile;

    @Size(max = 50)
    private String email;

    @Size(max = 50)
    private String department;

    @Size(max = 50)
    private String company;

    @Size(max = 50)
    private String position;

    @Size(max = 10)
    private String gender;

    private String birthday;

}
