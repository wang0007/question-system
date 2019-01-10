package com.neuq.question.service.events.signup;


import com.neuq.question.data.pojo.ConferenceSignUpRecordDO;
import com.neuq.question.service.events.signup.pojo.SignUpEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author yegk7
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SignUpEventDispatcher {

    private final ApplicationContext applicationContext;

    private final ExecutorService threadPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors() * 2, 5, TimeUnit.MINUTES,
            new LinkedBlockingQueue<>(1000), new ThreadPoolExecutor.AbortPolicy());

    public void dispatch(SignUpEvent event) {

        ConferenceSignUpRecordDO signUpRecordDO = event.getSignUpRecordDO();

        if (signUpRecordDO == null) {
            return;
        }

        Map<String, SignUpEventListener> listenerMap = applicationContext.getBeansOfType(SignUpEventListener.class);

        Collection<SignUpEventListener> listeners = listenerMap.values();
        listeners.forEach(signUpEventListener -> threadPool.submit(() -> {
            try {
                signUpEventListener.onSignUp(event);
            } catch (Exception e) {
                log.error("Exception when process sign up event", e);
            }
        }));

    }
}
