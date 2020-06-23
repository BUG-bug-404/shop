package com.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证用户注册信息
 * @author Administrator
 *
 */
public class VerifyUtils {

	/**
	 * 用户不为Null，去前后空格，首字母必须为字母，长度为6~20之间
	 * @param loginName
	 * @return
	 */
	public static boolean verifyLoginName(String loginName){
		if(trimAndisNull(loginName) && firstStr(loginName) && loginName.length()>=6 && loginName.length()<=20){
			return true;
		}
		return false;
	}
	/**
	 * 密码不为Null，去前后空格，长度为6~20之间
	 * @param password
	 * @return
	 */
	public static boolean verifyPassword(String password){
		if(trimAndisNull(password) && password.length()>=6 && password.length()<=20){
			return true;
		}
		return false;
	}
	/**
	 * 判断是否为空
	 * @param temp
	 * @return
	 */
	public static boolean trimAndisNull(String temp){
		if(temp!=null && temp.trim().length()>0 && !"".equals(temp.trim())){
		    temp.trim();
			return true;
		}
		return false;
	}
	/**
	 * 不能为空，大于0长度不能超过指定长度
	 * @param temp
	 * @param length
	 * @return
	 */
	public static boolean trimAndisNull(String temp,int length){
		if(temp!=null && temp.trim().length()>0 && !"".equals(temp.trim()) && temp.trim().length()>0 && temp.trim().length()<=length){
		    temp.trim();
			return true;
		}
		return false;
	}
	/**
	 * 不能为空，大于等于指定的最小长度不能超过指定最大长度
	 * @param temp
	 * @param smailLength
	 * @param bigLength
	 * @return
	 */
	public static boolean trimAndisNull(String temp,int smailLength,int bigLength){
		if(temp!=null && temp.trim().length()>0 && !"".equals(temp.trim()) && temp.trim().length()>=smailLength && temp.trim().length()<=bigLength){
		    temp.trim();
			return true;
		}
		return false;
	}
	/**
	 * 可以为空，如果不是空，长度不能超过指定长度
	 * @param temp
	 * @param length
	 * @return
	 */
	public static boolean isNullOrLength(String temp,int length){
		if(temp==null || temp.trim().length()<=0 || "".equals(temp.trim())){
			return true;
		}else{
			if(temp.trim().length()<=length){
			    temp.trim();
				return true;
			}
		}
		return false;
	}
	/**
	 * 可以为空，如果不是空，大于等于指定的最小长度不能超过指定最大长度
	 * @param temp
	 * @param length
	 * @return
	 */
	public static boolean isNullOrLength(String temp,int smailLength,int bigLength){
		if(temp==null || temp.trim().length()<=0 || "".equals(temp.trim())){
			return true;
		}else{
			if(temp.trim().length()>=smailLength && temp.trim().length()<=bigLength){
			    temp.trim();
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 验证是否为数字
	 * @param num
	 * @return
	 */
	public static boolean verifyNumber(String num){
	     return   num.matches("^[0-9]*$");
	}
	
	/**
	 * 验证是否为decimal类型
	 * @param num
	 * @return
	 */
	public static boolean verifyDecimal(Double num){
	     return   num.toString().matches("^([0-9]*)|([0-9]*\\.[0-9]*)$");
	}
	/**
	 * 验证是否为decimal类型
	 * @param num
	 * @return
	 */
	public static boolean verifyDecimal(String num){
	     return   num.matches("^([0-9]*)|([0-9]*\\.[0-9]*)$");
	}
	
	/**
	 * 验证邮箱
	 * @param email
	 * @return
	 */
	public static boolean verifyEmail(String email){
	     String   regex="[a-zA-Z][\\w_]+@\\w+(\\.\\w+)+";   
	     Pattern   p=Pattern.compile(regex);   
	     Matcher   m=p.matcher(email);   
	     return   m.matches(); 
	}
	
	/**
     * 验证手机号码
     * 
     * 移动号码段:139、138、137、136、135、134、150、151、152、157、158、159、182、183、187、188、147
     * 联通号码段:130、131、132、136、185、186、145
     * 电信号码段:133、153、180、189
     * 
     * @param cellphone
     * @return
     */
    public static boolean verifyPhone(String cellphone) {
		 String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$"; 
		 Pattern   p=Pattern.compile(regex);   
	     Matcher   m=p.matcher(cellphone);   
	     return   m.matches(); 
    }
    
	/**
	 * 判断首个字符是否为字母
	 * @param s
	 * @return
	 */
	private  static boolean  firstStr(String   s){   
			char   c   =   s.charAt(0);   
			int   i   =(int)c;   
			if((i>=65&&i<=90)||(i>=97&&i<=122)){   
				return   true;   
			}else{   
				return   false;   
			}   
	  }
	
}
