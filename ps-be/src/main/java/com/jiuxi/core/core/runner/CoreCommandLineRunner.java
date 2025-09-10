package com.jiuxi.core.core.runner;

import com.jiuxi.common.util.AesUtils;
import com.jiuxi.common.util.SmUtils;
import com.jiuxi.core.autoconfig.CoreConfigurationProperties;
import com.jiuxi.core.bean.Secretkey;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

/**
 * 初始化类，当前初始化国密加密算法，aes加密算法
 *
 * @author pand
 * @date 2020-09-07 17:32
 */
public class CoreCommandLineRunner implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoreCommandLineRunner.class);

    @Autowired
    private CoreConfigurationProperties properties;

    @Override
    public void run(String... args) throws Exception {

        Secretkey secretKey = properties.getSecretkey();
        // sm2,sm3,sm4默认加载，使用者只需要指定key就可以了，不指定，按照默认key生成实例
        // 默认项
        String sm2PrivateKey = secretKey.getSm2_private_key();
        String sm2PublicKey = secretKey.getSm2_public_key();
        String sm2publicKeyQ = secretKey.getSm2_public_qkey();

        // 生成公钥
        LOGGER.info("公钥: {}", sm2PublicKey);
        // 生成公钥Q，以q值做为js端的加密公钥
        LOGGER.info("公钥Q: {}", sm2publicKeyQ);

        String sm4SecretKey = secretKey.getSm4_secretKey();
        String sm4SecretIv = secretKey.getSm4_secretIv();

        // 加载sm2, sm3，sm4
        SmUtils.newInstanceSM2(sm2PrivateKey, sm2PublicKey);

        SmUtils.newInstanceSM4(sm4SecretKey, sm4SecretIv);

        // AES 加密 选择项
        String aesSecretKey = secretKey.getAes_secretKey();
        String aesSecretIv = secretKey.getAes_secretIv();
        if (StringUtils.isNotBlank(aesSecretKey) && StringUtils.isNotBlank(aesSecretIv)) {
            // 使用者配置了aes的key和vi，默认加载aes
            AesUtils.newInstance(aesSecretKey, aesSecretIv);
        }

        LOGGER.info("系统初始化完成");
        LOGGER.info("系统启动成功");
        LOGGER.info("系统启动完毕");
        LOGGER.info("System initialization is complete");
        LOGGER.info("System Started successfully");
        LOGGER.info("System startup is complete");
    }
}
