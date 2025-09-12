package com.jiuxi.common.util;

import cn.hutool.core.util.IdUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @ClassName: JwtUtil 工具类
 * @Description: 密码强度 工具类
 * @Author: 杨攀
 * @Date: 2023/10/12 13:34
 * @Copyright: 2023 www.tuxun.net Inc. All rights reserved.
 */
public class JwtUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtil.class);

    /**
     * @Fields SECRET : Token秘钥，请勿泄露!!!，请勿随便修改!!!
     */
    private static final String SECRET = "JWT-SECRET-Topinfo";

    /**
     * 设置允许误差时间， 单位秒， 默认 300 秒
     */
    private static final int LEEWAY = 300;

    /**
     * 版本
     * @author 杨攀
     * @date 2023/10/12 17:26
     * @param null
     * @return
     */
    public static final String VERSION = "v3";


    /**
     * jwt的唯一身份标识
     *
     * @author 杨占锐
     * @date 2025/2/7 17:33
     */
    public static final String JTI = "jti";


    /**
     * jwt签发者
     * @author 杨攀
     */
    public static final String JWT_ISS = "com.jiuxi";

    /**
     *  jwt 验证
     */
    public static JWTVerifier verifier = null;

    /**
     * token 类型：
     * 系统token：需要用户名/密码，手机号/验证等获取token
     * 接口token：用topinfoId/topinfoSecurity获取token，***该接口可单独分配接口权限***
     * <p>
     * <p>
     * JWT构成: header, payload, signature 标准中注册的声明 (建议但不强制使用):<br>
     * iss: jwt签发者<br>
     * sub: jwt所面向的用户 <br>
     * aud: 接收jwt的一方<br>
     * exp: jwt的过期时间，这个过期时间必须要大于签发时间<br>
     * nbf: 定义在什么时间之前，该jwt都是不可用的.<br>
     * iat: jwt的签发时间<br>
     * jti: jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击。<br>
     *
     * @param timeout 超时, 单位分钟
     * @return
     * @throws Exception
     * @Description: JWT生成Token
     * @Author:杨攀
     * @Since: 2020年8月21日上午10:48:26
     */
    public static String createToken(String json, int timeout) {

        // 获取当前时间作为 jwt的签发时间
        LocalDateTime now = LocalDateTime.now();
        Date iatDate = CommonDateUtil.toDate(now);

        // 测试，用秒
        // LocalDateTime exp = now.plusSeconds(timeout);
        // 加上 超时时间
        LocalDateTime exp = now.plusMinutes(timeout);

        Date expDate = CommonDateUtil.toDate(exp);

        // header Map
        Map<String, Object> map = new HashMap<String, Object>(16);
        map.put("alg", "HS256");
        map.put("typ", "JWT");

        // 密文json
        String encryptJson =  SmUtils.encryptHexSM4(json);

        // header
        String token = JWT.create().withHeader(map)
                // jwt签发者
                .withClaim("iss", JWT_ISS)
                // 版本
                .withClaim("version", VERSION)
                // payload
                .withClaim("sub", encryptJson)
                // jti: jwt的唯一身份标识
                .withClaim(JTI, IdUtil.fastSimpleUUID())
                // sign time
                .withIssuedAt(iatDate)
                // expire time
                .withExpiresAt(expDate)
                // signature
                .sign(Algorithm.HMAC256(SECRET));

        return token;
    }


    /**
     * 解密 Token
     * @author 杨攀
     * @date 2023/10/12 15:23
     * @param token
     * @return java.util.Map<java.lang.String,com.auth0.jwt.interfaces.Claim>
     */
    private static DecodedJWT verifyToken(String token) {
        try {
            if(verifier == null){
                verifier = JWT.require(Algorithm.HMAC256(SECRET)).acceptLeeway(LEEWAY).build();
            }
            DecodedJWT jwt = verifier.verify(token);
            return jwt;
        } catch (Exception e) {
            // token 校验失败, 抛出Token验证非法异常
            LOGGER.error("校验Token异常", e);
            throw new RuntimeException("登录凭证已过期，请重新登录！");
        }
    }

    /**
     * 解密 Token
     * @author 杨攀
     * @date 2023/10/12 15:22
     * @param token
     * @return java.lang.Boolean
     */
    public static Boolean checkToken(String token) throws RuntimeException {
        try {
            if(verifier == null){
                verifier = JWT.require(Algorithm.HMAC256(SECRET)).acceptLeeway(LEEWAY).build();
            }
            verifier.verify(token);
        } catch (AlgorithmMismatchException e) {
            // token 解析失败
            throw new RuntimeException("登录凭证解析失败！");
        } catch (SignatureVerificationException e) {
            // token 验签失败
            throw new RuntimeException("登录凭证验签失败！");
        } catch (InvalidClaimException e) {
            // claim 包含的值异常
            throw new RuntimeException("登录凭证信息异常！");
        }
        return true;
    }


    /**
     * 获取 token 明文 json
     * @author 杨攀
     * @date 2023/10/12 17:34
     * @param token
     * @return java.lang.String
     */
    public static String getToken(String token) {

        DecodedJWT jwt = verifyToken(token);
        Map<String, Claim> claims = jwt.getClaims();

        Claim claim = claims.get("sub");
        if (null == claim){
            return null;
        }
        String sub = claim.asString();
        // 明文json
        String json =  SmUtils.decryptStrSM4(sub);
        return json;
    }

    /**
     * 获取 载荷/Payload
     * @author 杨攀
     * @date 2023/10/12 17:34
     * @param token
     * @return java.lang.String
     */
    public static Map<String, Claim> getPayloadClaim(String token) {
        DecodedJWT jwt = verifyToken(token);
        return jwt.getClaims();
    }

    /**
     * 获取token唯一标识
     *
     * @param token
     * @return java.lang.String
     * @author 杨占锐
     * @date 2025/2/7 17:36
     */
    public static String getJti(String token) {
        Map<String, Claim> payloadClaim = JwtUtil.getPayloadClaim(token);
        String jti = payloadClaim.get(JwtUtil.JTI).asString();
        return jti;
    }
}
