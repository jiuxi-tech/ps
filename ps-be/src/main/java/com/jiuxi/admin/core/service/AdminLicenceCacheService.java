package com.jiuxi.admin.core.service;

/**
 * @author pand
 * @description 许可证
 * packageName com.jiuxi.mybatis.service
 * @className LocalLicenceCacheService
 * @date 2025/1/10 17:28
 */
public interface AdminLicenceCacheService {

    /**
     * 获取当前服务器的mac地址生成系统序列号
     *
     * @return
     */
    String generatorSystemSerialNumber();

}
