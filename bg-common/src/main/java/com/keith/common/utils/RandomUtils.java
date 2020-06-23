package com.keith.common.utils;

import cn.hutool.core.util.RandomUtil;

/**
 * 随机数工具类
 *
 * @author JohnSon
 *
 */
public class RandomUtils {

    /**
     * 获取0-9 随机码
     * @param length 长度
     * @return
     */
    public static String randomInt(int length){
        StringBuffer stb=new StringBuffer();
        for (int i=0 ;i<length; i++){
            stb.append(RandomUtil.randomInt(0,9));
        }
        return stb.toString();
    }


//    public static void main(String[] args) {
//        System.out.println(RandomUtils.randomInt(6));
//    }
}
