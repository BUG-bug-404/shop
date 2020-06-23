package com.core.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil extends StringUtils
{
  private static SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
  private static final char[] chr = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

  private static final String[] imgSuffix = { ".jpg", ".bmp", ".gif", ".png", ".jpeg", ".ico" };
  private static final String PATTERN_LINE_START = "^";
  private static final String PATTERN_LINE_END = "$";
  private static final char[] META_CHARACTERS = { '$', '^', '[', ']', '(', ')', '{', '}', '|', '+', '.', '\\' };

  public static String byte32(String value)
  {
    if (isEmpty(value))
      return value;
    byte[] bytes = value.getBytes();
    boolean err = false;
    for (int i = 0; i < bytes.length; i++) {
      if (bytes[i] == 63) {
        bytes[i] = 32;
        err = true;
      }
    }
    if (err) {
      return new String(bytes);
    }
    return value;
  }

  public static String null2string(Object v)
  {
    if (v == null) {
      return "";
    }
    return v.toString();
  }

  public static String getRandomNumber(int length)
  {
    Random random = new Random();
    StringBuffer buffer = new StringBuffer();

    for (int i = 0; i < length; i++) {
      buffer.append(random.nextInt(10));
    }
    return buffer.toString();
  }

  public static boolean isEmptyAfterTrim(String str)
  {
    return (str == null) || (str.trim().length() == 0);
  }

  public static String getRandomNumber()
  {
    return getRandomNumber(10);
  }

  public static String getRandomChar(int length)
  {
    Random random = new Random();
    StringBuffer buffer = new StringBuffer();
    for (int i = 0; i < length; i++) {
      buffer.append(chr[random.nextInt(36)]);
    }
    return buffer.toString();
  }

  public static String getRandomChar()
  {
    return getRandomChar(10);
  }

  public static String getPK()
  {
    return dateformat.format(new Date()) + getRandomChar(18);
  }

  public static String getPK(int len)
  {
    SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
    return dateformat.format(new Date()) + getRandomChar(len);
  }

  public static boolean isEmpty(String input)
  {
    return (input == null) || (input.trim().length() == 0);
  }

  public static boolean regexMatch(String pattern, String str)
  {
    String result = "^";
    char[] chars = pattern.toCharArray();
    for (char ch : chars) {
      if (Arrays.binarySearch(META_CHARACTERS, ch) >= 0) {
        result = result + "\\" + ch;
      }
      else
        switch (ch) {
        case '*':
          result = result + ".*";
          break;
        case '?':
          result = result + ".{0,1}";
          break;
        default:
          result = result + ch;
        }
    }
    result = result + "$";
    return Pattern.matches(result, str);
  }

  public static String upperFirstChar(String str)
  {
    if ((str != null) && (str.length() > 0)) {
      return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    return str;
  }

  public static String lowerFirstChar(String str)
  {
    if ((str != null) && (str.length() > 0)) {
      return str.substring(0, 1).toLowerCase() + str.substring(1);
    }
    return str;
  }

  public static boolean isIP(String ip)
  {
    Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
    Matcher matcher = pattern.matcher(ip);
    return matcher.matches();
  }

  public static boolean isIn(String[] strArray, String str) {
    if ((strArray == null) || (strArray.length == 0)) {
      return false;
    }
    String[] arrayOfString = strArray; int j = strArray.length; for (int i = 0; i < j; i++) { String s = arrayOfString[i];
      if (s.equals(str)) {
        return true;
      }
    }
    return false;
  }

  public static Boolean isImg(Object name)
  {
    if (name == null) {
      return Boolean.valueOf(false);
    }
    if ((name instanceof String)) {
      return isImg((String)name);
    }
    return Boolean.valueOf(false);
  }

  public static Boolean isImg(String name)
  {
    if (isEmpty(name)) {
      return Boolean.valueOf(false);
    }
    name = name.toLowerCase();
    for (String suffix : imgSuffix) {
      if (name.endsWith(suffix)) {
        return Boolean.valueOf(true);
      }
    }

    return Boolean.valueOf(false);
  }

  public static boolean hasImg(Map<String, Object> rowData)
  {
    if ((rowData == null) || (rowData.size() <= 0))
      return false;
    for (String key : rowData.keySet()) {
      if (isImg(rowData.get(key)).booleanValue()) {
        return true;
      }
    }
    return false;
  }

  public static Set<String> strArrayToSet(String[] args)
  {
    if ((args == null) || (args.length == 0)) {
      return new HashSet();
    }
    Set set = new HashSet();
    String[] arrayOfString = args; int j = args.length; for (int i = 0; i < j; i++) { String str = arrayOfString[i];
      if (!isEmpty(str)) {
        set.add(str);
      }
    }
    return set;
  }

  public static String setToString(Set<String> args, String split_char)
  {
    if ((args == null) || (args.isEmpty())) {
      return "";
    }
    return StringUtils.join(args.toArray(new String[args.size()]), split_char);
  }

  public static String delFirstAndLastComma(String contents)
  {
    if ((contents.startsWith(",")) || (contents.startsWith("，"))) {
      contents = contents.substring(1);
    }
    if ((contents.endsWith(",")) || (contents.endsWith("，"))) {
      contents = contents.substring(0, contents.length() - 1);
    }
    if ((!contents.startsWith(",")) && (!contents.startsWith("，")) && (!contents.endsWith(",")) && (!contents.endsWith("，"))) {
      return contents;
    }
    return delFirstAndLastComma(contents);
  }

  public static int findCharCount(String str, String sub)
  {
    if ((str == null) || (sub == null) || (str.length() == 0) || (sub.length() == 0)) {
      return 0;
    }
    int count = 0; int pos = 0; int idx = 0;
    while ((idx = str.indexOf(sub, pos)) != -1) {
      count++;
      pos = idx + sub.length();
    }
    return count;
  }

  public static int count(String s) {
    try {
      byte[] bytes = s.getBytes("Unicode");
      int n = 0;
      for (int i = 2; 
        i < bytes.length; i++) {
        if (i % 2 == 1) {
          if (bytes[i] != 0)
            n++;
        }
        else {
          n++;
        }
      }
      return n;
    } catch (UnsupportedEncodingException e) {
    }
    return 0;
  }

  public static String substring(String s, int byteLength)
  {
    if (s == null)
      return null;
    if (s.equals(""))
      return "";
    try {
      byte[] bytes = s.getBytes("Unicode");
      int n = 0;
      int i = 2;
      for (i = 2; (i < bytes.length) && (n < byteLength); i++) {
        if (i % 2 == 1) {
          if (bytes[i] != 0)
            n++;
        }
        else {
          n++;
        }
      }
      if (i % 2 == 1) {
        if (bytes[i] != 0)
          i--;
        else
          i++;
      }
      return new String(bytes, 0, i, "Unicode"); } catch (UnsupportedEncodingException e) {
    }
    return null;
  }

  public static String substringHtml(String s, int byteLength)
  {
    if (s == null)
      return null;
    if (s.equals(""))
      return "";
    s = s.replaceAll("<.*?>|</.*?>", "");
    try {
      byte[] bytes = s.getBytes("Unicode");
      int n = 0;
      int i = 2;
      for (i = 2; (i < bytes.length) && (n < byteLength); i++) {
        if (i % 2 == 1) {
          if (bytes[i] != 0)
            n++;
        }
        else {
          n++;
        }
      }
      if (i % 2 == 1) {
        if (bytes[i] != 0)
          i--;
        else
          i++;
      }
      return new String(bytes, 0, i, "Unicode"); } catch (UnsupportedEncodingException e) {
    }
    return null;
  }

  public static String escapeDir(String value)
  {
    String[] codes = { "/", "\\", ":", "*", "?", "\"", "<", ">", "|" };
    for (int i = 0; i < codes.length; i++) {
      value = value.replace(codes[i], "");
    }
    return value;
  }

  public static String gjzDir(String value)
  {
    String[] codes = { ",", "，", ";", "；", " ", "?", "|", "&" };
    for (int i = 0; i < codes.length; i++) {
      value = value.replace(codes[i], ",");
    }
    return value;
  }

  public static String bankDir(String value)
  {
    String[] codes = { " " };
    for (int i = 0; i < codes.length; i++) {
      value = value.replace(codes[i], "");
    }
    return value;
  }

  public static String[] splitString(String input, String delim)
  {
    if (isEmpty(input)) {
      return new String[0];
    }
    ArrayList a1 = new ArrayList();
    for (StringTokenizer stringTokenizer = new StringTokenizer(input, delim); stringTokenizer.hasMoreTokens(); a1.add(stringTokenizer.nextToken()));
    String[] result = new String[a1.size()];
    for (int i = 0; i < result.length; i++) {
      result[i] = ((String)a1.get(i));
    }
    return result;
  }

  public static String[] split(String input, String delim)
  {
    return StringUtils.split(input, delim);
  }

  public static List<String> splitList(String input, String delim)
  {
    if (isEmpty(input)) {
      return null;
    }
    ArrayList a1 = new ArrayList();
    for (StringTokenizer stringTokenizer = new StringTokenizer(input, delim); stringTokenizer.hasMoreTokens(); a1.add(stringTokenizer.nextToken()));
    return a1;
  }

  public static String joinArray(String[] strs)
  {
    if (strs == null)
      return null;
    if (strs.length == 0)
      return "";
    String ss = "";
    String[] arrayOfString = strs; int j = strs.length; for (int i = 0; i < j; i++) { String s = arrayOfString[i];
      ss = ss + "," + s;
    }
    return ss.substring(1);
  }

  public static String joinArray(String[] args, String split_char)
  {
    if ((args == null) || (args.length == 0)) {
      return "";
    }
    return StringUtils.join(args, split_char);
  }

  public static List<String> stringToList(String args) {
    String[] strs = args.split(",");
    List list = new ArrayList();
    for (String str : strs) {
      list.add(str);
    }
    return list;
  }

  public static String stringListToString(List<String> args) {
    String name = "";
    for (String str : args) {
      name = name + "," + str;
    }
    if (!isEmpty(name)) {
      name = name.substring(1);
    }
    return name;
  }

  public static boolean isInStringArray(String[] stringArray, String str)
  {
    if ((stringArray == null) || (stringArray.length == 0)) {
      return false;
    }
    String[] arrayOfString = stringArray; int j = stringArray.length; for (int i = 0; i < j; i++) { String s = arrayOfString[i];
      if (s.equalsIgnoreCase(str)) {
        return true;
      }
    }
    return false;
  }

  public String adjustComma(String param)
  {
    if (param == null)
      return "";
    String tmpStr = param.replaceAll("，", ",");
    String[] ss = tmpStr.split(",");
    String str = "";
    for (String s : ss) {
      if (!isEmpty(s.trim())) {
        str = str + "," + s.trim();
      }
    }
    return str.length() > 0 ? str.substring(1) : "";
  }

  public static String replaceAllChar(String str, String colType)
  {
    if (isEmpty(str)) {
      return "";
    }
    if ("clob".equalsIgnoreCase(colType))
      return str.replaceAll("<p>", "").replaceAll("</p>", "").replaceAll("<P>", "").replaceAll("</P>", "").replaceAll("&", "＆").replaceAll("<", "＜");
    if ("varchar".equalsIgnoreCase(colType))
      return str.replaceAll("&", "＆").replaceAll("<", "＜");
    if (isEmpty(colType)) {
      return str.replaceAll("&", "＆").replaceAll("<", "＜");
    }
    return str;
  }

  public static String replaceAllChar(Object o, String colType) {
    if (o == null) {
      return "";
    }
    String str = o.toString();
    return replaceAllChar(str, colType);
  }

  public static String encodeDownloadFileName(String fileName, HttpServletRequest request) throws UnsupportedEncodingException {
    String agent = request.getHeader("User-Agent");

    if ((agent != null) && 
      (agent.toUpperCase().indexOf("MSIE") != -1)) {
      return URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
    }

    return new String(fileName.getBytes("UTF-8"), "ISO8859-1");
  }

  public static String doubleToString(double number)
  {
    BigDecimal bigDecimal = new BigDecimal(number);
    String num = bigDecimal.toString();
    if (num.contains(".")) {
      return num.substring(0, num.indexOf("."));
    }
    return num;
  }

  public static double toDouble(double number)
  {
    String rs = number+"";
    if (rs.indexOf(".") + 3 <= rs.length() - 1)
    {
      rs = rs.substring(0, rs.indexOf(".") + 3);
    }
    return Double.valueOf(rs).doubleValue();
  }
  public static void main(String[] args) {
    BigDecimal mo = new BigDecimal("10098.63");
    mo = mo.multiply(new BigDecimal("100"));
    System.out.println(mo);
  }
}