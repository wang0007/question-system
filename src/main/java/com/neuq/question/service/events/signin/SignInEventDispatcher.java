package com.neuq.question.service.events.signin;

import com.neuq.question.data.pojo.ActivitySignInSettingDO;
import com.neuq.question.service.events.signin.pojo.SignInEvent;
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
 * @author liuhaoi
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SignInEventDispatcher {

    private final ApplicationContext applicationContext;

    private final ExecutorService threadPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() * 4,
            Runtime.getRuntime().availableProcessors() * 8, 5, TimeUnit.MINUTES,
            new LinkedBlockingQueue<>(1000), new ThreadPoolExecutor.AbortPolicy());


    public void dispatch(SignInEvent event) {

        ActivitySignInSettingDO setting = event.getSetting();

        if (setting == null) {
            return;
        }

        if (!setting.enable()) {
            return;
        }

        //重复签到不触发事件
        if (event.alreadySignIn()) {
            return;
        }

        Map<String, SignInEventListener> listenerMap = applicationContext.getBeansOfType(SignInEventListener.class);

        Collection<SignInEventListener> listeners = listenerMap.values();

        listeners.forEach(listener -> {
            threadPool.submit(() -> {
                try {
                    listener.onSignIn(event);
                } catch (Exception e) {
                    log.error("Exception when process sign in event", e);
                }
            });
        });


    }


}
