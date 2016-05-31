package com.example.yj.mapapp.test;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IsPhoneNumber {
    /**
     * 是不是中国的手机号
     * @param number
     * @return
     */
    public static boolean isChinesePhoneNumber(String number) {
        if (TextUtils.isEmpty(number))
            return false;
        if (number.length() != 11 || !number.startsWith("1"))
            return false;
        if (isPhoneNumber(number)) {
            int secDigit = Integer.parseInt(number.substring(1, 2));
            return secDigit >= 3 && secDigit <= 9;
        }
        return false;
    }

    /**
     * 判断时候为手机号
     *
     * @param number the input number to be tested
     * @return 返回true则是手机号
     */
    public static boolean isPhoneNumber(String number) {
        if (TextUtils.isEmpty(number)) {
            return false;
        }
        Pattern PHONE = Pattern.compile(
                "(\\+[0-9]+[\\- \\.]*)?" + "(\\([0-9]+\\)[\\- \\.]*)?" + "([0-9][0-9\\- \\.][0-9\\- \\.]+[0-9])");
        Matcher match = PHONE.matcher(number);
        return match.matches();
    }
}
