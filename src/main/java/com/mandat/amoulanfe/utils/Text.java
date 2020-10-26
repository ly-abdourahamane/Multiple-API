package com.mandat.amoulanfe.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Text {
    public static boolean isValid(String string) {
        return StringUtils.isNotBlank(string) && !"null".equals(string);
    }

    public static String normalize(String string) {
        if (!isValid(string)) {
            return "";
        }
        return StringUtils.stripAccents(string.toLowerCase()).trim();
    }
}
