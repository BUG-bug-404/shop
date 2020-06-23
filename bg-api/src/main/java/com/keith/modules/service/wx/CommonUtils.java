package com.keith.modules.service.wx;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.util.Locale;
import java.util.UUID;

public class CommonUtils {

	/* public static String dealNationalString(HttpServletRequest request, String messageProp, String[] params)
	  {
	    Locale locale = request.getLocale();
	    WebApplicationContext context = RequestContextUtils.getWebApplicationContext(request);
	    String value = context.getMessage(messageProp, params, locale);
	    return value;
	  }*/

	  public static byte[] Str2ByteArray(String Str)
	  {
	    String[] strList = Str.split(",");
	    byte[] reValue = new byte[strList.length];
	    for (int i = 0; i < strList.length; i++) {
	      reValue[i] = ((byte)Integer.parseInt(strList[i].trim()));
	    }
	    return reValue;
	  }

	  public static String createToken()
	  {
	    UUID uuid = UUID.randomUUID();
	    StringBuffer buf = new StringBuffer();
	    try {
	      MessageDigest md = MessageDigest.getInstance("MD5");
	      md.update(uuid.toString().getBytes());
	      byte[] b = md.digest();

	      for (int offset = 0; offset < b.length; offset++) {
	        int i = b[offset];
	        if (i < 0)
	          i += 256;
	        if (i < 16)
	          buf.append("0");
	        buf.append(Integer.toHexString(i));
	      }
	    }
	    catch (Exception localException) {
	    }
	    return buf.toString();
	  }

	  public static String getRealIp(HttpServletRequest request)
	  {
	    String ipAddress = null;

	    ipAddress = request.getHeader("x-forwarded-for");
	    if ((ipAddress == null) || (ipAddress.length() == 0) || ("unknown".equalsIgnoreCase(ipAddress))) {
	      ipAddress = request.getHeader("Proxy-Client-IP");
	    }
	    if ((ipAddress == null) || (ipAddress.length() == 0) || ("unknown".equalsIgnoreCase(ipAddress))) {
	      ipAddress = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if ((ipAddress == null) || (ipAddress.length() == 0) || ("unknown".equalsIgnoreCase(ipAddress))) {
	      ipAddress = request.getRemoteAddr();
	      if (ipAddress.equals("127.0.0.1"))
	      {
	        InetAddress inet = null;
	        try {
	          inet = InetAddress.getLocalHost();
	        } catch (UnknownHostException e) {
	          e.printStackTrace();
	        }
	        ipAddress = inet.getHostAddress();
	      }

	    }

	    if ((ipAddress != null) && (ipAddress.length() > 15) && 
	      (ipAddress.indexOf(",") > 0)) {
	      ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
	    }

	    return ipAddress;
	  }

	  
}
