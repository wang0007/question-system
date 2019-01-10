package com.neuq.question.service.events.activity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Locale;
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
public class ActivityCreateEventDispatcher {

    private final ApplicationContext applicationContext;

    private final ExecutorService threadPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors() * 2, 5, TimeUnit.MINUTES,
            new LinkedBlockingQueue<>(1000), new ThreadPoolExecutor.AbortPolicy());

    public void dispatch(ActivityCreateEvent event) {

        Map<String, ActivityCreateEventListener> listenerMap = applicationContext
                .getBeansOfType(ActivityCreateEventListener.class);

        Collection<ActivityCreateEventListener> listeners = listenerMap.values();

        Locale locale = LocaleContextHolder.getLocale();

        listeners.forEach(listener -> {
            try {
                threadPool.submit(() -> {
                    LocaleContextHolder.setLocale(locale);
                    try {
                        listener.onCreate(event);
                    } catch (Exception e) {
                        log.error("Exception when process activity create event", e);
                    }
                });
            } catch (Exception e) {
                log.error("Exception when dispatch activity create event " + event, e);
            }
        });

    }

}
