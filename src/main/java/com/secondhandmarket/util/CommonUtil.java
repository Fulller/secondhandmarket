package com.secondhandmarket.util;

import java.security.SecureRandom;
import java.text.Normalizer;
import java.util.regex.Pattern;

public class CommonUtil {
    public static String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }

    public static String getForgotPasswordKey(String code) {
        return "forgot-password/" + code;
    }

    private static final Pattern NON_ALPHANUMERIC = Pattern.compile("[^\\p{Alnum}]+");

    public static String toSlug(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        String slug = input.toLowerCase();
        slug = Normalizer.normalize(slug, Normalizer.Form.NFD);
        slug = NON_ALPHANUMERIC.matcher(slug).replaceAll("-");
        slug = slug.replaceAll("^-+", "").replaceAll("-+$", "");
        return slug;
    }

    public static String toProductSlug(String productName) {
        return CommonUtil.toSlug(productName) + "." + CommonUtil.generateVerificationCode();
    }
}
