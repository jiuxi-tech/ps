package com.jiuxi.common.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * @ClassName: VideoService
 * @Description: 视频服务接口
 * @Author 杨占锐
 * @Date 2024/5/13 14:52
 * @Copyright: www.tuxun.net Inc. All rights reserved.
 */
public interface VideoService {

    InputStream getVideoCover(MultipartFile file);
}
