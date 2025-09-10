package com.jiuxi.security.sso.service;

import com.jiuxi.security.sso.config.KeycloakSsoProperties;
import com.jiuxi.security.sso.principal.KeycloakUserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Keycloak OAuth2 服务
 * 处理授权码交换访问令牌等OAuth2相关操作
 * 
 * @author SSO Integration
 * @since 2.2.2
 */
@Service
public class KeycloakOAuth2Service {
    
    private static final Logger logger = LoggerFactory.getLogger(KeycloakOAuth2Service.class);
    
    @Autowired
    private KeycloakSsoProperties properties;
    
    @Autowired
    private KeycloakJwtService jwtService;
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    /**
     * 使用授权码交换访问令牌
     * 
     * @param code 授权码
     * @param redirectUri 重定向URI
     * @return 访问令牌响应
     */
    public TokenResponse exchangeCodeForToken(String code, String redirectUri) {
        logger.info("开始使用授权码交换访问令牌: code={}, redirectUri={}", code, redirectUri);
        
        try {
            // 构建token端点URL
            String tokenUrl = properties.getIssuerUri() + "/protocol/openid-connect/token";
            logger.info("Token端点URL: {}", tokenUrl);
            
            // 构建请求参数
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("code", code);
            params.add("redirect_uri", redirectUri);
            params.add("client_id", properties.getClientId());
            params.add("client_secret", properties.getClientSecret());
            
            logger.info("请求参数: grant_type=authorization_code, code={}, redirect_uri={}, client_id={}, client_secret={}", 
                code, redirectUri, properties.getClientId(), 
                properties.getClientSecret() != null ? "[HIDDEN]" : "null");
            
            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
            
            logger.info("发送Token交换请求到: {}", tokenUrl);
            
            // 发送请求
            ResponseEntity<Map> response = restTemplate.exchange(
                tokenUrl, 
                HttpMethod.POST, 
                request, 
                Map.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> tokenData = response.getBody();
                
                TokenResponse tokenResponse = new TokenResponse();
                tokenResponse.setAccessToken((String) tokenData.get("access_token"));
                tokenResponse.setRefreshToken((String) tokenData.get("refresh_token"));
                tokenResponse.setTokenType((String) tokenData.get("token_type"));
                tokenResponse.setExpiresIn((Integer) tokenData.get("expires_in"));
                tokenResponse.setScope((String) tokenData.get("scope"));
                
                logger.info("成功获取访问令牌: tokenType={}, expiresIn={}", 
                    tokenResponse.getTokenType(), tokenResponse.getExpiresIn());
                
                return tokenResponse;
            } else {
                logger.error("获取访问令牌失败: status={}, body={}", 
                    response.getStatusCode(), response.getBody());
                throw new RuntimeException("Failed to exchange code for token");
            }
            
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            logger.error("HTTP客户端错误 - 状态码: {}, 响应体: {}, 错误信息: {}", 
                e.getStatusCode(), e.getResponseBodyAsString(), e.getMessage());
            logger.error("请求详情 - URL: {}, 客户端ID: {}, 重定向URI: {}", 
                properties.getIssuerUri() + "/protocol/openid-connect/token", 
                properties.getClientId(), redirectUri);
            throw new RuntimeException("Error exchanging code for token: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            logger.error("交换访问令牌时发生异常: {}", e.getMessage(), e);
            throw new RuntimeException("Error exchanging code for token", e);
        }
    }
    
    /**
     * 从访问令牌获取用户信息
     * 
     * @param accessToken 访问令牌
     * @return 用户主体信息
     */
    public KeycloakUserPrincipal getUserInfoFromToken(String accessToken) {
        logger.info("开始从访问令牌获取用户信息");
        
        try {
            // 使用JWT服务解析令牌
            KeycloakUserPrincipal userPrincipal = jwtService.verifyAndParseToken(accessToken);
            
            logger.info("成功获取用户信息: username={}, email={}", 
                userPrincipal.getUsername(), userPrincipal.getEmail());
            
            return userPrincipal;
            
        } catch (Exception e) {
            logger.error("从访问令牌获取用户信息时发生异常: {}", e.getMessage(), e);
            throw new RuntimeException("Error getting user info from token", e);
        }
    }
    
    /**
     * Token响应类
     */
    public static class TokenResponse {
        private String accessToken;
        private String refreshToken;
        private String tokenType;
        private Integer expiresIn;
        private String scope;
        
        // Getters and Setters
        public String getAccessToken() {
            return accessToken;
        }
        
        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }
        
        public String getRefreshToken() {
            return refreshToken;
        }
        
        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }
        
        public String getTokenType() {
            return tokenType;
        }
        
        public void setTokenType(String tokenType) {
            this.tokenType = tokenType;
        }
        
        public Integer getExpiresIn() {
            return expiresIn;
        }
        
        public void setExpiresIn(Integer expiresIn) {
            this.expiresIn = expiresIn;
        }
        
        public String getScope() {
            return scope;
        }
        
        public void setScope(String scope) {
            this.scope = scope;
        }
    }
}