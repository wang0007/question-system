package com.neuq.question.service.events.signup.pojo;


import com.neuq.question.data.dao.ConferenceRepository;
import com.neuq.question.data.pojo.ConferenceDO;
import com.neuq.question.data.pojo.ConferenceSignUpRecordDO;
import lombok.Data;

import java.util.Optional;
import java.util.Set;

/**
 * @author wangshyi
 */
@Data
public class SignUpEvent {

    private final ConferenceRepository conferenceRepository;

    private final ConferenceSignUpRecordDO signUpRecordDO;

    private final String conferenceId;

    private String signUpUserName;

    private ConferenceDO conference;

    private String qzId;

    private Set<String> adminMemberIds;

    private String creator;

    public SignUpEvent(ConferenceRepository conferenceRepository,
                       String conferenceId,
                       ConferenceSignUpRecordDO signUpRecordDO) {
        this.conferenceId = conferenceId;
        this.conferenceRepository = conferenceRepository;
        this.signUpRecordDO = signUpRecordDO;
    }

    public String getSignUpUserName() {

        if (signUpUserName == null) {
            Optional<ConferenceSignUpRecordDO.FormFieldValue> optional = signUpRecordDO.getFields().stream()
                    .filter(formFieldValue -> ConferenceSignUpRecordDO.FIXED_NAME_ID.equals(formFieldValue.getFieldId()))
                    .findFirst();
            optional.ifPresent(formFieldValue -> signUpUserName = formFieldValue.getValue());
        }
        return signUpUserName;
    }

    public ConferenceDO getConference() {

        if (conference == null) {
            conference = conferenceRepository.queryByConferenceId(conferenceId);
        }
        return conference;
    }

    public String getCreator() {

        if (creator == null) {
            creator = getConference().getCreator();
        }
        return creator;
    }

}
