package com.briandevinssures.hockeystats.scraper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Strings {
    private static final Pattern HEIGHT_PATTERN = Pattern.compile("(\\d)' (\\d{1,2})\"");
    public static int heightToInches(String height) {
        Matcher matcher = HEIGHT_PATTERN.matcher(height);
        if (matcher.matches()) {
            String feet = matcher.group(1);
            String inches = matcher.group(2);
            return Integer.parseInt(feet) * 12 + Integer.parseInt(inches);
        }
        return -1;
    }
}
