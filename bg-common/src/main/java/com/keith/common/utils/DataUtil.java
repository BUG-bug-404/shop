package com.keith.common.utils;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 数据工具类
 *
 * @author JohnSon
 */
public class DataUtil {


    public static int versionCompareTo(String version1, String version2) {
        version1 = version1 == null ? "" : version1.replaceAll("[^\\d\\.]+", "");
        version2 = version2 == null ? "" : version2.replaceAll("[^\\d\\.]+", "");
        String[] version1Array = version1.split("\\.");
        String[] version2Array = version2.split("\\.");
        List<Integer> version1List = Lists.newArrayList();
        List<Integer> version2List = Lists.newArrayList();
        for (int i = 0; i < version1Array.length; i++) {
            version1List.add(Integer.parseInt(version1Array[i]));
        }
        for (int i = 0; i < version2Array.length; i++) {
            version2List.add(Integer.parseInt(version2Array[i]));
        }
        int size = version1List.size() > version2List.size() ? version1List.size() : version2List.size();
        while (version1List.size() < size) {
            version1List.add(0);
        }
        while (version2List.size() < size) {
            version2List.add(0);
        }
        for (int i = 0; i < size; i++) {
            if (version1List.get(i) > version2List.get(i)) {
                return 1;
            }
            if (version1List.get(i) < version2List.get(i)) {
                return -1;
            }
        }
        return 0;
    }

    public static void main(String[] args) {

        System.out.println(DataUtil.versionCompareTo("1.0.2.3","1.0.2.3"));
    }
}
