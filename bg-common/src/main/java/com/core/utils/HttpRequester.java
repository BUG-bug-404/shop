package com.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/** 
 * HTTP请求对象 
 *  
 * @author OUWCH 
 */  
public class HttpRequester { 
	
	private static final Logger logger= LoggerFactory.getLogger(HttpRequester.class);
	
	 // 连接超时时间
	 private static final int CONNECTION_TIMEOUT = 100000;
	 //读取超时时间
	 private static final int READ_TIMEOUT = 100000;
	 // 参数编码
	 private static final String ENCODE_CHARSET = "utf-8";  
   
   
    /** 
     * 发送GET请求 
     *  
     * @param urlString 
     *            URL地址 
     * @return 响应对象 
     * @throws IOException 
     */  
    public static HttpRespons sendGet(String urlString) throws IOException {
        return  send(urlString, "GET", null, null);  
    }  
   
    /** 
     * 发送GET请求 
     *  
     * @param urlString 
     *            URL地址 
     * @param params 
     *            参数集合 
     * @return 响应对象 
     * @throws IOException 
     */  
    public HttpRespons sendGet(String urlString, Map<String, String> params)
            throws IOException {  
        return  send(urlString, "GET", params, null);  
    }  
   
    /** 
     * 发送GET请求 
     *  
     * @param urlString 
     *            URL地址 
     * @param params 
     *            参数集合 
     * @param propertys 
     *            请求属性 
     * @return 响应对象 
     * @throws IOException 
     */  
    public HttpRespons sendGet(String urlString, Map<String, String> params,
                               Map<String, String> propertys) throws IOException {
        return  send(urlString, "GET", params, propertys);  
    }  
   
    /** 
     * 发送POST请求 
     *  
     * @param urlString 
     *            URL地址 
     * @return 响应对象 
     * @throws IOException 
     */  
    public static HttpRespons sendPost(String urlString) throws IOException {
        return  send(urlString, "POST", null, null);  
    }  
   
    /** 
     * 发送POST请求 
     *  
     * @param urlString 
     *            URL地址 
     * @param params 
     *            参数集合 
     * @return 响应对象 
     * @throws IOException 
     */  
    public static HttpRespons sendPost(String urlString, Map<String, String> params)
            throws IOException {  
        return send(urlString, "POST", params, null);  
    }  
   
    /** 
     * 发送POST请求 
     *  
     * @param urlString 
     *            URL地址 
     * @param params 
     *            参数集合 
     * @param propertys 
     *            请求属性 
     * @return 响应对象 
     * @throws IOException 
     */  
    public static HttpRespons sendPost(String urlString, Map<String, String> params,
                                       Map<String, String> propertys) throws IOException {
        return  send(urlString, "POST", params, propertys);  
    } 
    /**
     * 发送POST请求，并带body参数
     * @param urlString URL地址 
     * @param postBody post提交的参数内容
     * @return
     * @throws IOException
     */
    public static HttpRespons sendPost(String urlString, String postBody) throws IOException {
        return  sendPostBuffer(urlString, postBody);  
    } 
    
    
    
    
    /**
     * 
     * @param urlStr  请求路径
     * @param fileUrl 文件夹的路径
     * @param fileName 文件的路径
     * @param method  GET/PSOT
     * @return
     * @throws Exception
     */
    public static File  httpCopyFile(String urlStr,String fileUrl,String fileName,String method){
      HttpURLConnection con = null;
      InputStream inputStream =null;
      FileOutputStream fileOut = null;
      File file = null;
	  try {
		// 构造URL  
		  URL url = new URL(urlStr);  
		  // 打开连接  
		  con = (HttpURLConnection) url.openConnection();   
		  con.setRequestMethod(method);
		  con.setDoOutput(true);  
		  con.setDoInput(true);  
		  con.setUseCaches(false); 
		  con.setConnectTimeout(CONNECTION_TIMEOUT);// 设置连接超时
		  con.setReadTimeout(READ_TIMEOUT); // 设置读取超时
		  con.setRequestProperty("Charset", ENCODE_CHARSET);
		  // 输入流  
		  inputStream = con.getInputStream();
		  if(inputStream!=null){
			  file = new File(fileName);
			  //判断文件夹是否存在
			  File  urlFile = new File(fileUrl);
              if (!urlFile.exists()) {
              	urlFile.mkdirs();
              }
		     
		  }else{
		      return file;
		  }
		  fileOut = new FileOutputStream(file);
		  byte[] buff = new byte[100];  
		  int rc = 0;  
		  while ((rc = inputStream.read(buff, 0, 100)) > 0) {  
			  fileOut.write(buff, 0, rc);  
		  }
	} catch (Exception e) {
		logger.error("请求外部地址时，出现错误 msg={}--"+"原因-----",e);
	}finally{
        if(con!=null){
        	con.disconnect();
        }
        /*
         * 必须关闭文件流
         * 否则JDK运行时，文件被占用其他进程无法访问
         */
        try {
        	if(inputStream!=null){
        		inputStream.close();
        	}
        	if(fileOut!=null){
        		fileOut.close();
        	}
        } catch (IOException execption) {
        	logger.error("请求外部地址时-释放文件流，出现错误 msg={}--"+"原因-----",execption);
        }
    }  
      return file;
	}
    
   
    /** 
     * 发送HTTP请求 
     *  
     * @param urlString 
     * @return 响映对象 
     * @throws IOException 
     */  
    private static HttpRespons send(String urlString, String method,
                                    Map<String, String> parameters, Map<String, String> propertys)
            throws IOException {  
        HttpURLConnection urlConnection = null;  
   
        try {
			if (method.equalsIgnoreCase("GET") && parameters != null) {  
			    StringBuffer param = new StringBuffer();  
			    int i = 0;  
			    for (String key : parameters.keySet()) {  
			        if (i == 0)  
			            param.append("?");  
			        else  
			            param.append("&");  
			        param.append(key).append("=").append(parameters.get(key));  
			        i++;  
			    }  
			    urlString += param;  
			}  
			URL url = new URL(urlString);  
			urlConnection = (HttpURLConnection) url.openConnection();  
			urlConnection.setRequestMethod(method);  
			urlConnection.setDoOutput(true);  
			urlConnection.setDoInput(true);  
			urlConnection.setUseCaches(false);  
			urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);// 设置连接超时
			urlConnection.setReadTimeout(READ_TIMEOUT); // 设置读取超时
			urlConnection.setRequestProperty("Charset", ENCODE_CHARSET);
			if (propertys != null)  
			    for (String key : propertys.keySet()) {  
			        urlConnection.addRequestProperty(key, propertys.get(key));  
			    }  
   
			if (method.equalsIgnoreCase("POST") && parameters != null) {  
			    StringBuffer param = new StringBuffer();  
			    for (String key : parameters.keySet()) {  
			        param.append("&");  
			        param.append(key).append("=").append(parameters.get(key));  
			    }  
			    urlConnection.getOutputStream().write(param.toString().getBytes());  
			   
			}
		} catch (Exception e) {
			logger.error("请求外部地址时-真正触发GET或者POST请求，出现错误 msg={}--"+"原因-----",e);
		}finally{
			  urlConnection.getOutputStream().flush();  
			  urlConnection.getOutputStream().close(); 
		}
   
        return  makeContent(urlString, urlConnection);  
    }  
    
    
    private static HttpRespons sendPostBuffer(String urlString, String postBody) throws IOException {
        HttpURLConnection urlConnection = null;  
        try {
			URL url = new URL(urlString);  
			urlConnection = (HttpURLConnection) url.openConnection();  
			urlConnection.setRequestMethod("POST");  
			urlConnection.setDoOutput(true);  
			urlConnection.setDoInput(true);  
			urlConnection.setUseCaches(false);  
			urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);// 设置连接超时
			urlConnection.setReadTimeout(READ_TIMEOUT); // 设置读取超时
			urlConnection.setRequestProperty("Charset", ENCODE_CHARSET);
			urlConnection.getOutputStream().write(postBody.toString().getBytes(ENCODE_CHARSET));
		} catch (Exception e) {
			logger.error("请求外部地址时-POST请求，出现错误 msg={}--"+"原因-----",e);
		}finally{
			urlConnection.getOutputStream().flush();  
			urlConnection.getOutputStream().close();  
		}
        return  makeContent(urlString, urlConnection);  
    } 
    
    
    
    
    
   
    /** 
     * 得到响应对象 
     *  
     * @param urlConnection 
     * @return 响应对象 
     * @throws IOException 
     */  
    private static HttpRespons makeContent(String urlString,
                                           HttpURLConnection urlConnection) throws IOException {
        HttpRespons httpResponser = new HttpRespons();
        try {  
        	 urlConnection.connect();
             //System.out.println("Set-Cookie-----------"+ urlConnection.getHeaderField("Set-Cookie"));
             //从headers中取出来，并分割，为什么要分割，Chrome打开F12自己看看就明白了
            // String[] aaa = urlConnection.getHeaderField("Set-Cookie").split(";");
            InputStream in = urlConnection.getInputStream();  
            BufferedReader bufferedReader = new BufferedReader(  
                    new InputStreamReader(in,"utf-8"));  
            httpResponser.contentCollection = new Vector<String>();  
            StringBuffer temp = new StringBuffer();  
            String line = bufferedReader.readLine();  
            while (line != null) {  
                httpResponser.contentCollection.add(line);  
                temp.append(line).append("\r\n");  
                line = bufferedReader.readLine();  
            }  
            bufferedReader.close();  
            httpResponser.urlString = urlString;  
   
           /* httpResponser.defaultPort = urlConnection.getURL().getDefaultPort();  
            httpResponser.file = urlConnection.getURL().getFile();  
            httpResponser.host = urlConnection.getURL().getHost();  
            httpResponser.path = urlConnection.getURL().getPath();  
            httpResponser.port = urlConnection.getURL().getPort();  
            httpResponser.protocol = urlConnection.getURL().getProtocol();  
            httpResponser.query = urlConnection.getURL().getQuery();  
            httpResponser.ref = urlConnection.getURL().getRef();  
            httpResponser.userInfo = urlConnection.getURL().getUserInfo();  */
            
            httpResponser.content = temp.toString(); 
           /* httpResponser.contentEncoding = ecod;  
            httpResponser.code = urlConnection.getResponseCode();  
            httpResponser.message = urlConnection.getResponseMessage();  
            httpResponser.contentType = urlConnection.getContentType();  
            httpResponser.method = urlConnection.getRequestMethod();  
            httpResponser.connectTimeout = urlConnection.getConnectTimeout();  
            httpResponser.readTimeout = urlConnection.getReadTimeout();  */
            return httpResponser;  
        } catch (IOException e) {
        	logger.error("请求外部地址时-包装请求数据时，出现错误 msg={}--"+"原因-----",e);
            return null;
        } finally {  
            if (urlConnection != null)  
                urlConnection.disconnect();  
        }  
    }  
   
    public static void main(String[] args) {  
        try {       
            Map<String, String> parameters =new HashMap<String, String>();
            parameters.put("username", "zhangsan");
            parameters.put("password", "123456");
            parameters.put("captcha", "20160624");
            HttpRespons hr = HttpRequester.sendGet("http://news.cctv.com/2017/02/15/ARTIvTj2T48ldw8GmQqaMcxM170215.shtml");
        
            System.out.println(hr.getUrlString());       
            System.out.println(hr.getProtocol());       
            System.out.println(hr.getHost());       
            System.out.println(hr.getPort());       
            System.out.println(hr.getContentEncoding());       
            System.out.println(hr.getMethod());       
                   
            System.out.println(hr.getContent());       
        
        } catch (Exception e) {       
            e.printStackTrace();       
        }   
    }  
    
    
}  
