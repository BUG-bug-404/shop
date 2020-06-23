package com.core.util;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;

public class S3Utils {
	
	private static final Logger logger= LoggerFactory.getLogger(S3Utils.class);
	
	private String m_bucketName;
	
	private static S3Utils configS3Utils = null;

	private static OSSClient m_client;

	private S3Utils() {
		m_client = null;
		init();
	}

	private void init() {
		if (m_client == null) {
			m_bucketName ="zhikexzhibo"; //ProbaseUtils.getInstance().get("s3.m_bucketName");
			String m_endpointurl ="https\\://oss-cn-hangzhou.aliyuncs.com"; //ProbaseUtils.getInstance().get("s3.m_endpointurl");
			String accessKey = "LTAI39FapTmXrJL3";//ProbaseUtils.getInstance().get("s3.accessKey");
			String secrectKey ="RLlUYjdNgE4UeZtPns8gwzP9DeS9BS"; //ProbaseUtils.getInstance().get("s3.secrectKey");
			ClientConfiguration config = new ClientConfiguration();
			m_client = new OSSClient(m_endpointurl, accessKey, secrectKey,
					config);
		}
	}

	public static S3Utils getInstance() {
			configS3Utils = new S3Utils();
		return configS3Utils;
	}

	/*
	 * 往S3写文件
	 */
	public boolean putObjectContent(InputStream p_stream,
			String p_strDestFileName, long p_lFileSize) {
		boolean bRet = false;

		try {
			ObjectMetadata om = new ObjectMetadata();
			om.setContentLength(p_lFileSize);
			om.setCacheControl("max-age=86400");
			m_client.putObject(m_bucketName, p_strDestFileName,
					p_stream, om);
			bRet = true;
		} catch (Exception ex) {
			logger.error("用户修改头像出现异常:<<<<<<<<<原因msg{}:"+ex);
		}finally{
			m_client.shutdown();
		}

		return bRet;

	}
	
	public boolean putObjectContent(File file,
			String saveUrl) {
		boolean bRet = false;
		try {
			m_client.putObject(m_bucketName, saveUrl, file);
			bRet = true;
		} catch (Exception ex) {
			logger.error("上传PDF文件:<<<<<<<<<原因msg{}:"+ex);
		}finally{
			m_client.shutdown();
		}

		return bRet;

	}

	/*
	 * 从S3删除文件
	 */
	public boolean delFile(String p_strS3FileKey) {
		boolean bRet = false;
		try {
			m_client.deleteObject(m_bucketName, p_strS3FileKey);
			bRet = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}finally{
			m_client.shutdown();
		}

		return bRet;

	}

}
