package com.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Format {

    /**
     * 手机号码验证
     * @param mobile
     * @return
     */
    public static boolean MatcherPhone(String mobile){
        String regex = "0?(13|14|15|17|18|19)[0-9]{9}";
        if(mobile.length() != 11){
            return false;
        }else{
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(mobile);
            boolean isMatch = m.matches();
             if(!isMatch) {
                 return false;
             }
        }
        return true;
    }

    public static void main(String[] args) {
        String a = "18797812535";
//        System.out.println(MatcherPhone(a));
    }
}
