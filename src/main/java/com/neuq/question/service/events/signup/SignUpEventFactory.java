package com.neuq.question.service.events.signup;


import com.neuq.question.data.dao.ConferenceRepository;
import com.neuq.question.data.pojo.ConferenceSignUpRecordDO;
import com.neuq.question.service.events.signup.pojo.SignUpEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author yegk7
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SignUpEventFactory {

    private final ConferenceRepository conferenceRepository;

    public SignUpEvent build(String conferenceId,
                             ConferenceSignUpRecordDO signUpRecordDO) {

        return new SignUpEvent(conferenceRepository, conferenceId, signUpRecordDO);
    }

}
