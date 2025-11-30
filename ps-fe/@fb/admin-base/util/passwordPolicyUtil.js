/**
 * 密码策略验证工具类
 * 用于前端动态密码验证，从后端获取密码策略配置并应用
 * 
 * @author System
 * @since 2.2.2-SNAPSHOT
 */

// 缓存密码策略配置
let cachedPolicy = null;
let policyLoadPromise = null;

/**
 * 从后端获取密码策略配置
 * 使用单例模式，避免重复请求
 * @returns {Promise} 密码策略配置对象
 */
async function getPasswordPolicy() {
    // 如果已有缓存，直接返回
    if (cachedPolicy) {
        return cachedPolicy;
    }
    
    // 如果正在加载中，返回正在进行的Promise
    if (policyLoadPromise) {
        return policyLoadPromise;
    }
    
    // 创建加载Promise - 使用默认策略并记录日志
    policyLoadPromise = (async function() {
        try {
            // 尝试使用app.service.request
            if (typeof app !== 'undefined' && app.service && app.service.request) {
                console.log('使用app.service.request获取密码策略...');
                const requestConfig = {
                    url: '/api/sys/password-policy',
                    method: 'get',
                    loading: false
                };
                
                const result = await app.service.request(requestConfig);
                if (result && result.code === 1 && result.data) {
                    cachedPolicy = result.data;
                    console.log('密码策略API请求成功:', cachedPolicy);
                    return cachedPolicy;
                } else {
                    console.warn('API返回失败，使用默认策略:', result);
                    cachedPolicy = getDefaultPolicy();
                    return cachedPolicy;
                }
            } else {
                console.warn('app.service.request不可用，使用默认策略');
                cachedPolicy = getDefaultPolicy();
                return cachedPolicy;
            }
        } catch (error) {
            console.error('获取密码策略配置异常，使用默认策略:', error);
            cachedPolicy = getDefaultPolicy();
            return cachedPolicy;
        } finally {
            // 清除加载Promise
            policyLoadPromise = null;
        }
    })();
    
    return policyLoadPromise;
}

/**
 * 获取默认密码策略
 * 当后端接口不可用时使用
 * 注意：这里的默认值应与后端 security-dev.yml 中的配置保持一致
 */
function getDefaultPolicy() {
    return {
        length: {
            min: 6,     // 与后端开发环境配置一致
            max: 20
        },
        complexity: {
            enabled: true,
            requireUppercase: false,  // 与后端开发环境配置一致
            requireLowercase: true,
            requireDigit: true,
            requireSpecial: false,
            allowedSpecialChars: '@$!%*?&',
            customRegex: ''
        },
        weakPassword: {
            enabled: true,
            checkUsernameSimilarity: true,
            minStrengthLevel: 1  // 与后端开发环境配置一致
        }
    };
}

/**
 * 清除缓存的密码策略
 * 在策略更新后可调用此方法刷新缓存
 */
function clearPolicyCache() {
    cachedPolicy = null;
    policyLoadPromise = null;
}

/**
 * 验证密码是否符合策略
 * @param {String} password 要验证的密码
 * @param {String} username 用户名（可选，用于相似度检查）
 * @param {Object} policy 密码策略配置（可选，不传则自动获取）
 * @returns {Object} 验证结果 { valid: Boolean, message: String }
 */
function validatePassword(password, username = '', policy = null) {
    if (!password) {
        return { valid: false, message: '密码不能为空' };
    }
    
    // 如果未传入策略，使用缓存的策略或默认策略
    if (!policy) {
        policy = cachedPolicy || getDefaultPolicy();
    }
    
    // 1. 验证长度
    if (password.length < policy.length.min) {
        return { valid: false, message: `密码长度不能小于${policy.length.min}位` };
    }
    if (password.length > policy.length.max) {
        return { valid: false, message: `密码长度不能大于${policy.length.max}位` };
    }
    
    // 2. 验证复杂度
    if (policy.complexity.enabled) {
        // 如果有自定义正则，优先使用
        if (policy.complexity.customRegex) {
            const regex = new RegExp(policy.complexity.customRegex);
            if (!regex.test(password)) {
                return { valid: false, message: '密码不符合复杂度要求' };
            }
        } else {
            // 使用配置的复杂度规则
            if (policy.complexity.requireUppercase && !/[A-Z]/.test(password)) {
                return { valid: false, message: '密码必须包含大写字母' };
            }
            if (policy.complexity.requireLowercase && !/[a-z]/.test(password)) {
                return { valid: false, message: '密码必须包含小写字母' };
            }
            if (policy.complexity.requireDigit && !/\d/.test(password)) {
                return { valid: false, message: '密码必须包含数字' };
            }
            if (policy.complexity.requireSpecial) {
                const specialCharsRegex = new RegExp(`[${policy.complexity.allowedSpecialChars.replace(/[-[\]{}()*+?.,\\^$|#\s]/g, '\\$&')}]`);
                if (!specialCharsRegex.test(password)) {
                    return { valid: false, message: `密码必须包含特殊字符(${policy.complexity.allowedSpecialChars})` };
                }
            }
        }
    }
    
    // 3. 验证与用户名的相似度
    if (policy.weakPassword.enabled && policy.weakPassword.checkUsernameSimilarity && username) {
        const lowerPassword = password.toLowerCase();
        const lowerUsername = username.toLowerCase();
        if (lowerPassword.includes(lowerUsername) || lowerUsername.includes(lowerPassword)) {
            return { valid: false, message: '密码不能包含用户名' };
        }
    }
    
    return { valid: true, message: '验证通过' };
}

/**
 * 创建fb-form的密码验证规则
 * @param {String} username 用户名（可选）
 * @returns {Promise<Object>} fb-form的validator规则
 */
async function createPasswordValidator(username = '') {
    // 先获取策略
    const policy = await getPasswordPolicy();
    
    return {
        validator: (rule, value, callback) => {
            if (!value) {
                return callback('请输入密码');
            }
            
            const result = validatePassword(value, username, policy);
            if (result.valid) {
                return callback();
            } else {
                return callback(result.message);
            }
        },
        trigger: 'blur'
    };
}

/**
 * 获取密码复杂度要求的描述文本
 * @returns {Promise<String>} 密码复杂度描述
 */
async function getPasswordPolicyDescription() {
    const policy = await getPasswordPolicy();
    const requirements = [];
    
    requirements.push(`长度${policy.length.min}-${policy.length.max}位`);
    
    if (policy.complexity.enabled) {
        if (policy.complexity.requireUppercase) {
            requirements.push('包含大写字母');
        }
        if (policy.complexity.requireLowercase) {
            requirements.push('包含小写字母');
        }
        if (policy.complexity.requireDigit) {
            requirements.push('包含数字');
        }
        if (policy.complexity.requireSpecial) {
            requirements.push(`包含特殊字符(${policy.complexity.allowedSpecialChars})`);
        }
    }
    
    return '密码要求：' + requirements.join('、');
}

/**
 * 计算密码强度
 * @param {String} password 密码
 * @returns {Number} 密码强度等级 1=弱，2=中，3=强
 */
function calculatePasswordStrength(password) {
    if (!password) {
        return 0;
    }
    
    let strength = 0;
    
    // 长度加分
    if (password.length >= 8) strength++;
    if (password.length >= 12) strength++;
    
    // 复杂度加分
    if (/[a-z]/.test(password)) strength++;
    if (/[A-Z]/.test(password)) strength++;
    if (/\d/.test(password)) strength++;
    if (/[^a-zA-Z0-9]/.test(password)) strength++;
    
    // 归一化到1-3
    if (strength <= 2) return 1; // 弱
    if (strength <= 4) return 2; // 中
    return 3; // 强
}

export {
    getPasswordPolicy,
    clearPolicyCache,
    validatePassword,
    createPasswordValidator,
    getPasswordPolicyDescription,
    calculatePasswordStrength
};
