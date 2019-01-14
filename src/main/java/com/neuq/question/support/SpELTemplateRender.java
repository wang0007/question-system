package com.neuq.question.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 基于spEL的消息模板渲染引擎
 *
 * @author wangshyi
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SpELTemplateRender {

    /**
     * 中间的问号为不贪婪模式,尽量少的匹配,防止两个表达式中间的内容也会被匹配
     */
    private static final Pattern PATTERN = Pattern.compile("\\{\\{.*?\\}\\}");

    public String resolveSpel(String template, Object event) {

        if (StringUtils.isEmpty(template)) {
            return "";
        }

        Matcher matcher = PATTERN.matcher(template);

        EvaluationContext context = new StandardEvaluationContext(event);
        ExpressionParser expressionParser = new SpelExpressionParser();

        String result = template;

        while (matcher.find()) {
            String group = matcher.group();

            String el = group.substring(2, group.length() - 2);

            Object obj;
            try {
                Expression expression = expressionParser.parseExpression(el);
                obj = expression.getValue(context);
            } catch (Exception e) {
                log.error("spEL parse exception with el {} and context {}", el, context);
                obj = "-";
            }

            if (obj == null) {
                continue;
            }
            String value = String.valueOf(obj);

            result = result.replace(group, value);
        }

        return result;
    }


}
