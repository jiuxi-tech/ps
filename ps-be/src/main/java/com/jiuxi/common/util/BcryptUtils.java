package com.jiuxi.common.util;

import cn.hutool.crypto.digest.BCrypt;

import java.security.SecureRandom;

/**
 * @Description Bcrypt加密，是单向Hash加密算法，不可反向破解生成明文
 * Bcrypt有四个变量：
 * 1、saltRounds: 正数，代表hash杂凑次数，数值越高越安全，默认10次。
 * 2、myPassword: 明文密码字符串。
 * 3、salt: 盐，一个128bits随机字符串，22字符
 * 4、myHash: 经过明文密码password和盐salt进行hash，默认10次下，循环加盐hash10次，得到myHash
 * @ClassName BcryptUtils
 * @Author pand
 * @Date 2020/7/29 10:29
 * @Version 1.0
 */
public class BcryptUtils {

    /**
     * 对象
     */
    private volatile static BCrypt bCrypt;

    private final static int LOG_ROUNDS = 12;
    private final static String ALGORITHM = "9TzQyQuxduVJqQay";

    static {
        newInstance();
    }

    public static BCrypt newInstance() {
        if (bCrypt == null) {
            synchronized (BcryptUtils.class) {
                if (bCrypt == null) {
                    bCrypt = new BCrypt();
                }
            }
        }
        return bCrypt;
    }

    /**
     * 对明文密码进行加密,并返回加密后的密码
     *
     * @param password 明文密码
     * @return
     */
    public static String encode(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(LOG_ROUNDS, new SecureRandom(ALGORITHM.getBytes())));
    }

    /**
     * 将明文密码跟加密后的密码进行匹配，如果一致返回true,否则返回false
     *
     * @param password       明文密码
     * @param encodePassword 密闻
     * @return
     */
    public static boolean match(String password, String encodePassword) {
        return BCrypt.checkpw(password, encodePassword);
    }

    public static void main(String[] args) {
        System.out.println(encode("8153d677e1e655f32b9d5ce3bd7eb6f80d03439c85a9c4c5166fac805fb2ad9e"));
    }

}
