package com.jiuxi.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jiuxi.common.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @Description: JwtToken的生成和解析
 * @Author:杨攀
 * @Since:2018年8月21日上午11:18:39
 *
 * 已过期，框架2.0.0以前试用这个，2.1.0以后试用 JwtUtil
 *
 */
@Deprecated
public class JwtTokenUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtils.class);

    /**
     * @Fields SECRET : Token秘钥，请勿泄露!!!，请勿随便修改!!!
     */
    private static final String SECRET = "JWT-SECRET-Topinfo";

    /**
     * @Fields calendarField : 按小时
     */
    private static final int calendarField = Calendar.HOUR_OF_DAY;

    /**
     * @Fields calendarInterval : token 过期时间, 默认 3 天失效
     */
    private static final int calendarInterval = 72;

    /**
     * 设置允许误差时间， 单位秒， 默认 60秒
     */
    private static final int leeway = 60;

    /**
     * 生成临时token，各种id都为空
     *
     * @param exinfo
     * @return
     */
    public static String createToken(String exinfo) {
        return JwtTokenUtils.createToken("", "", "", "", "", "", exinfo);
    }


    /**
     * 生成临时token，自定义时间
     * @author 杨攀
     * @date 2021/9/2 15:48
     * @param personId
     * @param exinfo  扩展信息
     * @param field  calendar field，如：Calendar.HOUR_OF_DAY
     * @param amount  数量， 如 3
     * @return java.lang.String
     */
    public static String createToken(String personId, String exinfo, int field, int amount) {
        return JwtTokenUtils.createToken("", "", personId, "", "", "", exinfo, field, amount);
    }

    /**
     * token 类型：
     * 系统token：需要用户名/密码，手机号/验证等获取token
     * 接口token：用topinfoId/topinfoSecurity获取token，***该接口可单独分配接口权限***
     * <p>
     * <p>
     * JWT构成: header, payload, signature 标准中注册的声明 (建议但不强制使用):<br>
     * iss: jwt签发者<br>
     * sub: jwt所面向的用户<br>
     * aud: 接收jwt的一方<br>
     * exp: jwt的过期时间，这个过期时间必须要大于签发时间<br>
     * nbf: 定义在什么时间之前，该jwt都是不可用的.<br>
     * iat: jwt的签发时间<br>
     * jti: jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击。<br>
     *
     * @param tenantId 租户id
     * @param ascnId   政府存机构ID，企业存单位id，存在分公司的，存所在分公司单位id
     * @param personId 人员id
     * @param deptIds  部门id,多个逗号隔开
     * @param cityCode 当前登陆人所在部门行政区划code，如果在多个部门下，选部门登陆之前不设置值。选择部门登陆后在设置值。
     * @param roleIds  角色id,多个逗号隔开
     * @return
     * @throws Exception
     * @Description: JWT生成Token
     * @Author:杨攀
     * @Since: 2020年8月21日上午10:48:26
     */
    public static String createToken(String tenantId, String ascnId, String personId, String deptIds, String cityCode, String roleIds, String exinfo) {
        return createToken(tenantId, ascnId, personId, deptIds, cityCode, roleIds, exinfo, calendarField, calendarInterval);
    }


    /**
     * token 类型：
     * 系统token：需要用户名/密码，手机号/验证等获取token
     * 接口token：用topinfoId/topinfoSecurity获取token，***该接口可单独分配接口权限***
     * <p>
     * <p>
     * JWT构成: header, payload, signature 标准中注册的声明 (建议但不强制使用):<br>
     * iss: jwt签发者<br>
     * sub: jwt所面向的用户<br>
     * aud: 接收jwt的一方<br>
     * exp: jwt的过期时间，这个过期时间必须要大于签发时间<br>
     * nbf: 定义在什么时间之前，该jwt都是不可用的.<br>
     * iat: jwt的签发时间<br>
     * jti: jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击。<br>
     *
     * @param tenantId 租户id
     * @param ascnId   政府存机构ID，企业存单位id，存在分公司的，存所在分公司单位id
     * @param personId 人员id
     * @param deptIds  部门id,多个逗号隔开
     * @param cityCode 当前登陆人所在部门行政区划code，如果在多个部门下，选部门登陆之前不设置值。选择部门登陆后在设置值。
     * @param roleIds  角色id,多个逗号隔开
     * @param exinfo  扩展信息
     * @param field  calendar field，如：Calendar.HOUR_OF_DAY
     * @param amount  数量， 如 5
     * @return
     * @throws Exception
     * @Description: JWT生成Token
     * @Author:杨攀
     * @Since: 2020年8月21日上午10:48:26
     */
    public static String createToken(String tenantId, String ascnId, String personId, String deptIds, String cityCode, String roleIds, String exinfo, int field, int amount) {

        // tenantId 不为空，加密
        tenantId = StringUtils.isBlank(tenantId) ? tenantId : SmUtils.encryptHexSM4(tenantId);

        // tenantId 不为空，加密
        ascnId = StringUtils.isBlank(ascnId) ? ascnId : SmUtils.encryptHexSM4(ascnId);

        // personId 不为空，加密
        personId = StringUtils.isBlank(personId) ? personId : SmUtils.encryptHexSM4(personId);

        // deptId 不为空，加密
        deptIds = StringUtils.isBlank(deptIds) ? deptIds : SmUtils.encryptHexSM4(deptIds);

        // cityCode 不为空，加密
        cityCode = StringUtils.isBlank(cityCode) ? cityCode : SmUtils.encryptHexSM4(cityCode);

        // roleIds 不为空，加密
        roleIds = StringUtils.isBlank(roleIds) ? roleIds : SmUtils.encryptHexSM4(roleIds);

        // 自定义的内容
        exinfo = StringUtils.isBlank(exinfo) ? exinfo : SmUtils.encryptHexSM4(exinfo);

        Calendar nowTime = Calendar.getInstance();
        //签发时间
        Date iatDate = nowTime.getTime();

        nowTime.add(field, amount);

        //过期时间
        Date expiresDate = nowTime.getTime();

        // header Map
        Map<String, Object> map = new HashMap<String, Object>(16);
        map.put("alg", "HS256");
        map.put("typ", "JWT");

        // header
        String token = JWT.create().withHeader(map)
                // payload
                .withClaim("tid", tenantId)
                .withClaim("aid", ascnId)
                .withClaim("pid", personId)
                .withClaim("did", deptIds)
                .withClaim("cce", cityCode)
                .withClaim("rid", roleIds)
                .withClaim("exinfo", exinfo)
                // sign time
                .withIssuedAt(iatDate)
                // expire time
                .withExpiresAt(expiresDate)
                // signature
                .sign(Algorithm.HMAC256(SECRET));

        return token;
    }

/*

    public static String createToken(Date iatDate, Date expiresDate) {

        // header Map
        Map<String, Object> map = new HashMap<String, Object>(16);
        map.put("alg", "HS256");
        map.put("typ", "JWT");

        // header
        String token = JWT.create().withHeader(map)
                // payload
                .withClaim("exinfo", "")
                // sign time
                .withIssuedAt(iatDate)
                // expire time
                .withExpiresAt(expiresDate)
                // signature
                .sign(Algorithm.HMAC256(SECRET));

        return token;
    }
*/

    /**
     * 解密token
     *
     * @param token
     * @return
     * @Description: 解密Token
     * @Author:杨攀
     * @Since: 2018年8月21日上午10:59:00
     */
    public static Map<String, Claim> verifyToken(String token) {

        DecodedJWT jwt;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).acceptLeeway(leeway).build();
            jwt = verifier.verify(token);
        } catch (Exception e) {
            // token 校验失败, 抛出Token验证非法异常
            LOGGER.error("校验Token异常:" + ExceptionUtils.getStackTrace(e));
            throw new RuntimeException("登录凭证已过期，请重新登录！");
        }

        return jwt.getClaims();
    }


    /**
     * @param token
     * @return
     * @Description: 解密Token
     * @Author:杨攀
     * @Since: 2018年8月21日上午10:59:00
     */
    public static Boolean checkToken(String token) throws TokenExpiredException {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).acceptLeeway(leeway).build();
            // LOGGER.info("================这里token报错！token = {}", token);
            verifier.verify(token);
        } catch (AlgorithmMismatchException e) {
            // token 解析失败
            // LOGGER.error("校验Token异常:" + ExceptionUtils.getStackTrace(e));
            throw new RuntimeException("登录凭证解析失败，请重新登录！");
        } catch (SignatureVerificationException e) {
            // token 验签失败
            // LOGGER.error("校验Token异常:" + ExceptionUtils.getStackTrace(e));
            throw new RuntimeException("登录凭证验签失败，请重新登录！");
        } catch (InvalidClaimException e) {
            // claim 包含的值异常
            // LOGGER.error("校验Token异常:" + ExceptionUtils.getStackTrace(e));
            throw new RuntimeException("登录凭证信息异常，请重新登录！");
        }

        return true;
    }

    /**
     * @param token
     * @return
     * @Description: 获得token中的TenantId
     * @Author:杨攀
     * @Since: 2018年8月30日上午9:49:09
     */
    public static String getTenantId(String token) {
        Map<String, Claim> claims = verifyToken(token);
        Claim jsonClaim = claims.get("tid");
        if (jsonClaim == null) {
            return "";
        }
        return SmUtils.decryptStrSM4(jsonClaim.asString());
    }

    /**
     * @param token
     * @return
     * @Description: 获得token中的TenantId
     * @Author:杨攀
     * @Since: 2018年8月30日上午9:49:09
     */
    public static String getAscnId(String token) {
        Map<String, Claim> claims = verifyToken(token);
        Claim jsonClaim = claims.get("aid");
        if (jsonClaim == null) {
            return "";
        }
        return SmUtils.decryptStrSM4(jsonClaim.asString());
    }

    /**
     * @param token
     * @return java.lang.String
     * @description: 获取人员id
     * @author 杨攀
     * @date 2020/5/26 17:20
     */
    public static String getPersonId(String token) {
        Map<String, Claim> claims = verifyToken(token);
        Claim jsonClaim = claims.get("pid");
        if (jsonClaim == null) {
            return "";
        }
        return SmUtils.decryptStrSM4(jsonClaim.asString());
    }


    /**
     * @param token
     * @return java.lang.String
     * @description: 获取角色ids
     * @author 杨攀
     * @date 2020/5/28 15:49
     */
    public static String getRoles(String token) {
        Map<String, Claim> claims = verifyToken(token);
        Claim jsonClaim = claims.get("rid");
        if (jsonClaim == null) {
            return "";
        }
        return SmUtils.decryptStrSM4(jsonClaim.asString());
    }


    /**
     * 获取自定义的token信息
     *
     * @param token
     * @return java.lang.String
     * @description: 获取用户所在部门s
     * @author 杨攀
     * @date 2020/5/28 15:52
     */
    public static String getExinfo(String token) {
        Map<String, Claim> claims = verifyToken(token);
        Claim jsonClaim = claims.get("exinfo");
        if (jsonClaim == null) {
            return "";
        }
        return SmUtils.decryptStrSM4(jsonClaim.asString());
    }


    /**
     * @param token
     * @return java.lang.String
     * @description: 获取用户所在部门s
     * @author 杨攀
     * @date 2020/5/28 15:52
     */
    public static String getDepts(String token) {
        Map<String, Claim> claims = verifyToken(token);
        Claim jsonClaim = claims.get("did");
        if (jsonClaim == null) {
            return "";
        }
        return SmUtils.decryptStrSM4(jsonClaim.asString());
    }

    /**
     * @param token
     * @return java.lang.String
     * @description: 获取当前用户所在部门的行政区划code
     * @author 杨攀
     * @date 2020/5/28 15:52
     */
    public static String getCityCode(String token) {
        Map<String, Claim> claims = verifyToken(token);
        Claim jsonClaim = claims.get("cce");
        if (jsonClaim == null) {
            return "";
        }
        return SmUtils.decryptStrSM4(jsonClaim.asString());
    }

}
