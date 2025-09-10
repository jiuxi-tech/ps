/**
 * Keycloak 认证工具模块
 * 提供统一的 Keycloak 认证管理功能
 * 
 * 使用方式：
 * let kc = this.$kc()
 * kc.logout().then()
 */

import { log } from 'console';
import Keycloak from 'keycloak-js';

// 单例实例
let keycloakInstance = null;
let initPromise = null;

// Keycloak 配置（默认配置）
let keycloakConfig = {
    url: 'http://localhost:8180/',
    realm: 'ps-realm',
    clientId: 'ps-be'
};

// 初始化配置（默认配置）
let initConfig = {
    responseMode: 'fragment',
    onLoad: 'check-sso',
    silentCheckSsoRedirectUri: window.location.origin + '/silent-check-sso.html',
    checkLoginIframe: false
};

/**
 * 初始化 Keycloak 实例
 * @returns {Promise<Keycloak>} 已认证的 Keycloak 实例
 */
async function initKeycloak() {
    try {
        console.log('正在初始化 Keycloak...');
        
        // 创建 Keycloak 实例
        keycloakInstance = new Keycloak(keycloakConfig);
        
        // 初始化认证
        const authenticated = await keycloakInstance.init(initConfig);
        
        if (authenticated) {
            console.log('Keycloak 认证成功');
            console.log('用户信息:', keycloakInstance.tokenParsed);
            
            // 设置 token 刷新
            setupTokenRefresh();
            
            return keycloakInstance;
        } else {
            console.log('用户未认证，需要登录');
            // 可以选择自动跳转登录或返回未认证状态
            // keycloakInstance.login();
            return keycloakInstance;
        }
    } catch (error) {
        console.error('Keycloak 初始化失败:', error);
        throw new Error(`Keycloak 认证服务初始化失败: ${error.message}`);
    }
}

/**
 * 设置 token 自动刷新
 */
function setupTokenRefresh() {
    if (!keycloakInstance) return;
    
    // 每30秒检查一次 token 是否需要刷新
    setInterval(() => {
        keycloakInstance.updateToken(70)
            .then(refreshed => {
                if (refreshed) {
                    console.log('Token 已刷新');
                } else {
                    console.log('Token 仍然有效');
                }
            })
            .catch(error => {
                console.error('Token 刷新失败:', error);
                // Token 刷新失败，可能需要重新登录
                keycloakInstance.login();
            });
    }, 30000);
}

/**
 * 获取 Keycloak 实例（单例模式）
 * @returns {Promise<Keycloak>} Keycloak 实例
 */
export async function getKeycloakInstance() {
    // 如果已经有实例且已认证，直接返回
    if (keycloakInstance && keycloakInstance.authenticated) {
        console.log('使用已认证的 Keycloak 实例');
        
        return keycloakInstance;
    }
    
    // 如果正在初始化，等待初始化完成
    if (initPromise) {
        return await initPromise;
    }
    
    // 开始初始化
    initPromise = initKeycloak();
    
    try {
        const instance = await initPromise;
        // 添加 Admin API 方法到实例
        addAdminApiMethods(instance);
        initPromise = null; // 重置初始化 Promise
        return instance;
    } catch (error) {
        initPromise = null; // 重置初始化 Promise
        throw error;
    }
}

/**
 * 为 Keycloak 实例添加 Admin API 方法
 * @param {Keycloak} instance Keycloak 实例
 */
function addAdminApiMethods(instance) {
    // 获取 axios 实例（从多个可能的位置获取）
    const axios = window.Vue?.prototype?.$axios || 
                  window.$axios || 
                  window.app?.$axios || 
                  (typeof app !== 'undefined' ? app.$axios : null);
    
    if (!axios) {
        console.warn('axios 实例未找到，Admin API 功能将不可用');
        return;
    }
    
    // 创建带认证头的 axios 实例
    const createAuthenticatedRequest = (method, url, data = null, config = {}) => {
        const headers = {
            'Authorization': `Bearer ${instance.token}`,
            'Content-Type': 'application/json',
            ...config.headers
        };
        
        const requestConfig = {
            method,
            url: (`${keycloakConfig.url}${url.replace(/\/\//, '/')}`),//,
            headers,
            ...config
        };
        
        if (data && (method === 'POST' || method === 'PUT' || method === 'PATCH')) {
            requestConfig.data = data;
        }
        
        return axios(requestConfig);
    };
    
    // 通用 HTTP 方法
    instance.get = (url, config = {}) => createAuthenticatedRequest('GET', url, null, config);
    instance.post = (url, data = null, config = {}) => createAuthenticatedRequest('POST', url, data, config);
    instance.put = (url, data = null, config = {}) => createAuthenticatedRequest('PUT', url, data, config);
    instance.delete = (url, config = {}) => createAuthenticatedRequest('DELETE', url, null, config);
    instance.patch = (url, data = null, config = {}) => createAuthenticatedRequest('PATCH', url, data, config);
    
    // 具体的 Admin API 方法
    
    // 用户管理
    instance.getUsers = (realm, params = {}) => {
        const queryString = new URLSearchParams(params).toString();
        const url = `admin/realms/${realm}/users${queryString ? '?' + queryString : ''}`;
        return instance.get(url);
    };
    
    instance.getUser = (realm, userId) => {
        return instance.get(`admin/realms/${realm}/users/${userId}`);
    };
    
    instance.createUser = (realm, userData) => {
        return instance.post(`admin/realms/${realm}/users`, userData);
    };
    
    instance.updateUser = (realm, userId, userData) => {
        return instance.put(`admin/realms/${realm}/users/${userId}`, userData);
    };
    
    instance.deleteUser = (realm, userId) => {
        return instance.delete(`admin/realms/${realm}/users/${userId}`);
    };
    
    // 认证管理
    instance.getAuthenticationFlows = (realm) => {
        return instance.get(`admin/realms/${realm}/authentication/flows`);
    };
    
    instance.getAuthenticationExecutions = (realm, flowAlias) => {
        return instance.get(`admin/realms/${realm}/authentication/flows/${flowAlias}/executions`);
    };
    
    instance.deleteExecution = (realm, executionId) => {
        return instance.delete(`admin/realms/${realm}/authentication/executions/${executionId}`);
    };
    
    instance.createExecution = (realm, executionData) => {
        return instance.post(`admin/realms/${realm}/authentication/executions`, executionData);
    };
    
    // 客户端管理
    instance.getClients = (realm, params = {}) => {
        const queryString = new URLSearchParams(params).toString();
        const url = `admin/realms/${realm}/clients${queryString ? '?' + queryString : ''}`;
        return instance.get(url);
    };
    
    instance.getClient = (realm, clientId) => {
        return instance.get(`admin/realms/${realm}/clients/${clientId}`);
    };
    
    instance.createClient = (realm, clientData) => {
        return instance.post(`admin/realms/${realm}/clients`, clientData);
    };
    
    instance.updateClient = (realm, clientId, clientData) => {
        return instance.put(`admin/realms/${realm}/clients/${clientId}`, clientData);
    };
    
    instance.deleteClient = (realm, clientId) => {
        return instance.delete(`admin/realms/${realm}/clients/${clientId}`);
    };
    
    // 角色管理
    instance.getRealmRoles = (realm, params = {}) => {
        const queryString = new URLSearchParams(params).toString();
        const url = `admin/realms/${realm}/roles${queryString ? '?' + queryString : ''}`;
        return instance.get(url);
    };
    
    instance.getClientRoles = (realm, clientId, params = {}) => {
        const queryString = new URLSearchParams(params).toString();
        const url = `admin/realms/${realm}/clients/${clientId}/roles${queryString ? '?' + queryString : ''}`;
        return instance.get(url);
    };
    
    instance.createRealmRole = (realm, roleData) => {
        return instance.post(`admin/realms/${realm}/roles`, roleData);
    };
    
    instance.createClientRole = (realm, clientId, roleData) => {
        return instance.post(`admin/realms/${realm}/clients/${clientId}/roles`, roleData);
    };
    
    // 组管理
    instance.getGroups = (realm, params = {}) => {
        const queryString = new URLSearchParams(params).toString();
        const url = `admin/realms/${realm}/groups${queryString ? '?' + queryString : ''}`;
        return instance.get(url);
    };
    
    instance.getGroup = (realm, groupId) => {
        return instance.get(`admin/realms/${realm}/groups/${groupId}`);
    };
    
    instance.createGroup = (realm, groupData) => {
        return instance.post(`admin/realms/${realm}/groups`, groupData);
    };
    
    instance.updateGroup = (realm, groupId, groupData) => {
        return instance.put(`admin/realms/${realm}/groups/${groupId}`, groupData);
    };
    
    instance.deleteGroup = (realm, groupId) => {
        return instance.delete(`admin/realms/${realm}/groups/${groupId}`);
    };
    
    // Realm 管理
    instance.getRealm = (realm) => {
        return instance.get(`admin/realms/${realm}`);
    };
    
    instance.updateRealm = (realm, realmData) => {
        return instance.put(`admin/realms/${realm}`, realmData);
    };
    
    // 攻击检测
    instance.clearBruteForceForAllUsers = (realm) => {
        return instance.delete(`admin/realms/${realm}/attack-detection/brute-force/users`);
    };
    
    instance.clearBruteForceForUser = (realm, userId) => {
        return instance.delete(`admin/realms/${realm}/attack-detection/brute-force/users/${userId}`);
    };
    
    instance.getBruteForceStatus = (realm, userId) => {
        return instance.get(`admin/realms/${realm}/attack-detection/brute-force/users/${userId}`);
    };
    
    console.log('Keycloak Admin API 方法已添加到实例');
}



/**
 * 执行登录
 * @param {Object} options 登录选项
 * @returns {Promise}
 */
export async function login(options = {}) {
    const kc = await getKeycloakInstance();
    return kc.login(options);
}

/**
 * 执行登出
 * @param {Object} options 登出选项
 * @returns {Promise}
 */
export async function logout(options = {}) {
    const kc = await getKeycloakInstance();
    return kc.logout(options);
}

/**
 * 检查是否已认证
 * @returns {Promise<boolean>}
 */
export async function isAuthenticated() {
    try {
        const kc = await getKeycloakInstance();
        return kc.authenticated || false;
    } catch (error) {
        console.error('检查认证状态失败:', error);
        return false;
    }
}

/**
 * 获取用户信息
 * @returns {Promise<Object|null>}
 */
export async function getUserInfo() {
    try {
        const kc = await getKeycloakInstance();
        if (kc.authenticated) {
            return kc.tokenParsed || null;
        }
        return null;
    } catch (error) {
        console.error('获取用户信息失败:', error);
        return null;
    }
}

/**
 * 获取访问令牌
 * @returns {Promise<string|null>}
 */
export async function getToken() {
    try {
        const kc = await getKeycloakInstance();
        if (kc.authenticated) {
            return kc.token || null;
        }
        return null;
    } catch (error) {
        console.error('获取访问令牌失败:', error);
        return null;
    }
}

/**
 * 强制刷新令牌
 * @returns {Promise<boolean>}
 */
export async function refreshToken() {
    try {
        const kc = await getKeycloakInstance();
        if (kc.authenticated) {
            return await kc.updateToken(5);
        }
        return false;
    } catch (error) {
        console.error('刷新令牌失败:', error);
        return false;
    }
}

/**
 * 设置 Keycloak 配置
 * @param {Object} config Keycloak 配置对象
 * @param {string} config.url Keycloak 服务器地址
 * @param {string} config.realm Keycloak realm 名称
 * @param {string} config.clientId Keycloak 客户端 ID
 */
export function setKeycloakConfig(config) {
    if (keycloakInstance) {
        console.warn('Keycloak 实例已初始化，配置更改将在下次初始化时生效');
    }
    
    // 合并配置
    keycloakConfig = {
        ...keycloakConfig,
        ...config
    };
    
    console.log('Keycloak 配置已更新:', keycloakConfig);
}

/**
 * 设置 Keycloak 初始化配置
 * @param {Object} config 初始化配置对象
 * @param {string} config.responseMode 响应模式
 * @param {string} config.onLoad 加载时行为
 * @param {string} config.silentCheckSsoRedirectUri 静默检查 SSO 重定向 URI
 * @param {boolean} config.checkLoginIframe 是否检查登录 iframe
 */
export function setKeycloakInitConfig(config) {
    if (keycloakInstance) {
        console.warn('Keycloak 实例已初始化，初始化配置更改将在下次初始化时生效');
    }
    
    // 合并配置
    initConfig = {
        ...initConfig,
        ...config
    };
    
    console.log('Keycloak 初始化配置已更新:', initConfig);
}

/**
 * 获取当前 Keycloak 配置
 * @returns {Object} 当前的 Keycloak 配置
 */
export function getKeycloakConfig() {
    return { ...keycloakConfig };
}

/**
 * 获取当前 Keycloak 初始化配置
 * @returns {Object} 当前的初始化配置
 */
export function getKeycloakInitConfig() {
    return { ...initConfig };
}

/**
 * 直接获取 initConfig 对象引用
 * @returns {Object} initConfig 对象
 */
export function getInitConfig() {
    return initConfig;
}

/**
 * 重置 Keycloak 实例（用于测试或重新初始化）
 */
export function resetKeycloakInstance() {
    keycloakInstance = null;
    initPromise = null;
}

// 默认导出主要的获取实例方法
export default {
    getKeycloakInstance,
    login,
    logout,
    isAuthenticated,
    getUserInfo,
    getToken,
    refreshToken,
    setKeycloakConfig,
    setKeycloakInitConfig,
    getKeycloakConfig,
    getKeycloakInitConfig,
    getInitConfig,
    resetKeycloakInstance
};