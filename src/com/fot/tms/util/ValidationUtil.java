package com.fot.tms.util;

import java.util.regex.Pattern;




public class ValidationUtil {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[+]?[0-9]{9,15}$");

    private ValidationUtil() {}

    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        if (!isNotEmpty(email)) return false;
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidPhone(String phone) {
        if (!isNotEmpty(phone)) return true; 
        return PHONE_PATTERN.matcher(phone.trim().replaceAll("\\s+", "")).matches();
    }

    public static boolean isValidMark(double mark) {
        return mark >= 0.0 && mark <= 100.0;
    }

    public static boolean isValidSessionNumber(int session) {
        return session >= 1 && session <= 15;
    }

    public static boolean isValidCredits(int credits) {
        return credits >= 1 && credits <= 10;
    }

    public static boolean isValidSemester(int semester) {
        return semester >= 1 && semester <= 8;
    }

    public static boolean isValidWeight(double weight) {
        return weight >= 0.0 && weight <= 100.0;
    }
}
