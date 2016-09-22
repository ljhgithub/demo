package com.ljh.www.imkit.util.string;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ljh on 2016/9/22.
 */

public class IdentityCard {
    public static boolean verifyIdNumber(String idNumber) {
        if (null == idNumber || idNumber.trim().isEmpty()) {
            return false;
        }
        boolean isAvailable;
        int len = idNumber.length();
        final int OLD_ID_NUMBER_LEN = 15;
        final int NEW_ID_NUMBER_LEN = 18;
        String regExp = "(\\d{17}([Xx]|\\d))|(\\d{15})";
        isAvailable = regexMacth(idNumber, regExp);
        if (!isAvailable) {
            return false;
        }
        if (NEW_ID_NUMBER_LEN == len) {
            char[] numChar = idNumber.toCharArray();
            int[] numbers = new int[17];
            int[] coefficients = new int[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7,
                    9, 10, 5, 8, 4, 2};
            String[] verifyCodes = new String[]{"1", "0", "X", "9", "8", "7",
                    "6", "5", "4", "3", "2"};
            String last;
            int remainder;
            try {
                for (int i = 0; i < 17; i++) {
                    numbers[i] = Integer.parseInt(numChar[i] + "");
                }

            } catch (Exception e) {
                return false;
            }
            int sum = 0;
            for (int i = 0; i < 17; i++) {
                sum += numbers[i] * coefficients[i];
            }

            remainder = sum % 11;
            last = (numChar[17] + "");
            if (verifyCodes[remainder].equalsIgnoreCase(last)) {
                return true;
            }
            return false;

        } else if (OLD_ID_NUMBER_LEN == len) {
            regExp = "(11|12|13|14|15|21|22|23|31|32|33|34|35|36|37|41|42|43|44|45|46|50|51|52|53|54|61|62|63|64|65|71|81|82)\\d{13}";
            isAvailable = regexMacth(idNumber, regExp);
            if (isAvailable) {
                isAvailable = false;
                int year = Integer.parseInt(idNumber.substring(6, 8));
                int month = Integer.parseInt(idNumber.substring(8, 10));
                int day = Integer.parseInt(idNumber.substring(10, 12));
                if (year >= 0 && year <= 99) {
                    if (month >= 1 && month <= 12) {
                        if (day >= 0 && day <= 31) {
                            isAvailable = true;
                        }
                    }
                }
            }

            return isAvailable;
        } else {
            return false;
        }
    }

    private static boolean regexMacth(String idNumber, String regExp) {

        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(idNumber);
        return matcher.matches();
    }
}
