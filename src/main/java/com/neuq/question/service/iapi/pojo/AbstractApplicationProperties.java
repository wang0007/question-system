package com.neuq.question.service.iapi.pojo;

/**
 * @author wangshyi
 * @date 2019/1/11  16:08
 */

        import com.neuq.question.error.ECConfigurationException;
        import org.apache.commons.lang3.StringUtils;

        import java.util.Collections;
        import java.util.Set;
        import java.util.stream.Collectors;
        import java.util.stream.Stream;

/**
 * @author wangshyi
 */
        public class AbstractApplicationProperties {

        public static final char URL_SPLIT_CHAT = '/';

        public Set<String> parseCommaSplitParam(String source) {

        if (StringUtils.isBlank(source)) {
        return Collections.emptySet();
        }

        String[] split = source.split(",");

        return Stream.of(split).map(String::trim).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
        }


        public String concatURL(String urlPrefix, String... urlFragment) {

        if (urlPrefix == null) {
        throw new ECConfigurationException("invalid url prefix, can not be null");
        }


        StringBuilder builder = new StringBuilder(urlPrefix);

        for (String fragment : urlFragment) {
        concatURLFragment(builder, fragment);
        }

        return builder.toString();
        }

        private void concatURLFragment(StringBuilder builder, String fragment) {

        if (StringUtils.isBlank(fragment)) {
        return;
        }

        if (fragment.length() == 1 && fragment.charAt(0) == URL_SPLIT_CHAT) {
        return;
        }

        String trimFragment = fragment.trim();

        //参数之后
        if (builder.indexOf("?") != -1) {
        builder.append(trimFragment);
        return;
        }

        if (trimFragment.startsWith("?")) {
        builder.append(trimFragment);
        return;
        }

        concatPaths(builder, trimFragment);
        }

        private void concatPaths(StringBuilder builder, String trimFragment) {
        char c1 = builder.charAt(builder.length() - 1);

        char c2 = trimFragment.charAt(0);

        if (c1 == URL_SPLIT_CHAT && c2 == URL_SPLIT_CHAT) {
        builder.append(trimFragment.substring(1));
        return;
        }

        if (c1 == URL_SPLIT_CHAT || c2 == URL_SPLIT_CHAT) {
        builder.append(trimFragment);
        return;
        }

        builder.append(URL_SPLIT_CHAT).append(trimFragment);
        }


        }
