

package com.keith.modules.service.upload.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.keith.modules.service.upload.UploadService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.UUID;


@Service("uploadService")
public class UploadServiceImpl  implements UploadService {

	public Object upload (MultipartFile multipartFile){
		String endpoint = "oss-cn-hangzhou.aliyuncs.com";
		// oss accessKeyId
		String accessKeyId = "LTAI4GFW3ofxKQ5zZ8jcyiv9";
		// oss accessKeySecret
		String accessKeySecret = "8znvFkMZLUHuj7ZSE0kXtx3OZUs8Vx";
		// oss 存储bucket
		String bucket = "hzsy-appimage";
		// oss 访问域名
		String domain = "http://hzsy-appimage.oss-cn-hangzhou.aliyuncs.com/";

		// 本地备份文件夹
		String localFolder = "D:\\upload";

		OSS ossClient = null;

		try {

			// 初始化 ossClient
			ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

			// 打散目录
			String[] directory = this.getDateFolder();

			// 使用UUID重命名文件
			String fileName = UUID.randomUUID().toString().replace("-", "") + "." + this.getSuffix(multipartFile.getOriginalFilename());

			// 上传的URI
			String uri = String.join("/", "image", fileName);

			// 上传文件
			ossClient.putObject(bucket, uri, multipartFile.getInputStream());

			// 序列化到本地备份
			Path path = Paths.get(localFolder, directory);
			if (Files.notExists(path)) {
				// 目录不存在，创建
				Files.createDirectories(path);
			}
			Files.write(path.resolve(fileName), multipartFile.getBytes(), StandardOpenOption.CREATE_NEW);

			// 返回完整的访问地址
			return domain + uri;
		} catch (OSSException | ClientException e) {
			return "文件上传异常";
		} catch (IOException e) {
			return "文件IO异常";
		} finally {
			if (ossClient != null) {
				// 释放资源
				ossClient.shutdown();
			}
		}
	}

	/**
	 * 根据当前日期，打散目录
	 * yyyy/MM/dd
	 * @return
	 */
	private String[] getDateFolder() {
		String[] retVal = new String[3];

		LocalDate localDate = LocalDate.now();
		retVal[0] = localDate.getYear() + "";

		int month = localDate.getMonthValue();
		retVal[1] = month < 10 ? "0" + month : month + "";

		int day = localDate.getDayOfMonth();
		retVal[2] = day < 10 ? "0" + day : day + "";

		return retVal;
	}

	/**
	 * 获取文件后缀
	 * @param fileName
	 * @return
	 */
	private String getSuffix(String fileName) {
		int index = fileName.lastIndexOf(".");
		if (index != -1) {
			String suffix = fileName.substring(index + 1);
			if (!suffix.isEmpty()) {
				return suffix;
			}
		}
		throw new IllegalArgumentException("非法的文件名称：" + fileName);
	}

}
