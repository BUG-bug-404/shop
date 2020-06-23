package com.keith.modules.service.upload;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    /**
     * 上传图片
     * @param multipartFile
     * @return
     */
     Object upload (MultipartFile multipartFile);
}
