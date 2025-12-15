package com.jiuxi.common.constants;

/**
 * @ClassName: ApiConstants
 * @Description: API常量定义
 * @Author: Qdd
 * @Date: 2025/01/07
 * @Copyright: 2025 www.jiuxi.com Inc. All rights reserved.
 */
public class ApiConstants {

    /**
     * API版本
     */
    public static final String API_VERSION_V1 = "/api/v1";
    public static final String API_VERSION_V2 = "/api/v2";

    /**
     * 通用路径前缀
     */
    public static final String ADMIN_PREFIX = "/admin";
    public static final String USER_PREFIX = "/user";
    public static final String PUBLIC_PREFIX = "/public";
    public static final String TEST_PREFIX = "/test";

    /**
     * 资源操作路径
     */
    public static final String LIST = "/list";
    public static final String PAGE = "/page";
    public static final String DETAIL = "/detail";
    public static final String CREATE = "/create";
    public static final String UPDATE = "/update";
    public static final String DELETE = "/delete";
    public static final String BATCH_DELETE = "/batch-delete";
    public static final String ENABLE = "/enable";
    public static final String DISABLE = "/disable";
    public static final String STATUS = "/status";
    public static final String EXPORT = "/export";
    public static final String IMPORT = "/import";
    public static final String DOWNLOAD = "/download";
    public static final String UPLOAD = "/upload";

    /**
     * 路径参数
     */
    public static final String ID_PARAM = "/{id}";
    public static final String CODE_PARAM = "/{code}";
    public static final String NAME_PARAM = "/{name}";

    /**
     * 请求头常量
     */
    public static final String HEADER_TOKEN = "Authorization";
    public static final String HEADER_USER_ID = "X-User-Id";
    public static final String HEADER_TENANT_ID = "X-Tenant-Id";
    public static final String HEADER_REQUEST_ID = "X-Request-Id";
    public static final String HEADER_CLIENT_IP = "X-Client-IP";
    public static final String HEADER_USER_AGENT = "User-Agent";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_ACCEPT = "Accept";

    /**
     * 内容类型
     */
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String CONTENT_TYPE_XML = "application/xml";
    public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";
    public static final String CONTENT_TYPE_MULTIPART = "multipart/form-data";
    public static final String CONTENT_TYPE_TEXT = "text/plain";
    public static final String CONTENT_TYPE_HTML = "text/html";
    public static final String CONTENT_TYPE_EXCEL = "application/vnd.ms-excel";
    public static final String CONTENT_TYPE_PDF = "application/pdf";

    /**
     * 分页参数
     */
    public static final String PAGE_NUM = "pageNum";
    public static final String PAGE_SIZE = "pageSize";
    public static final String SORT_FIELD = "sortField";
    public static final String SORT_ORDER = "sortOrder";
    public static final String SEARCH_KEYWORD = "keyword";

    /**
     * 默认分页配置
     */
    public static final int DEFAULT_PAGE_NUM = 1;
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int MAX_PAGE_SIZE = 1000;
    public static final String DEFAULT_SORT_ORDER = "desc";

    /**
     * 排序方向
     */
    public static final String SORT_ASC = "asc";
    public static final String SORT_DESC = "desc";

    /**
     * 状态值
     */
    public static final String STATUS_ENABLED = "1";
    public static final String STATUS_DISABLED = "0";
    public static final String STATUS_DELETED = "-1";

    /**
     * 布尔值字符串
     */
    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String YES = "yes";
    public static final String NO = "no";
    public static final String ON = "on";
    public static final String OFF = "off";

    /**
     * 日期时间格式
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String ISO_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    /**
     * 字符编码
     */
    public static final String CHARSET_UTF8 = "UTF-8";
    public static final String CHARSET_GBK = "GBK";
    public static final String CHARSET_ISO = "ISO-8859-1";

    /**
     * 文件相关
     */
    public static final String FILE_SEPARATOR = "/";
    public static final String FILE_EXTENSION_SEPARATOR = ".";
    public static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    public static final String[] ALLOWED_IMAGE_TYPES = {"jpg", "jpeg", "png", "gif", "bmp", "webp"};
    public static final String[] ALLOWED_DOCUMENT_TYPES = {"pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt"};

    /**
     * 缓存相关
     */
    public static final String CACHE_PREFIX = "ps:";
    public static final String USER_CACHE_PREFIX = CACHE_PREFIX + "user:";
    public static final String ROLE_CACHE_PREFIX = CACHE_PREFIX + "role:";
    public static final String PERMISSION_CACHE_PREFIX = CACHE_PREFIX + "permission:";
    public static final String CONFIG_CACHE_PREFIX = CACHE_PREFIX + "config:";
    public static final String DICT_CACHE_PREFIX = CACHE_PREFIX + "dict:";

    /**
     * 缓存过期时间（秒）
     */
    public static final long CACHE_EXPIRE_SHORT = 5 * 60; // 5分钟
    public static final long CACHE_EXPIRE_MEDIUM = 30 * 60; // 30分钟
    public static final long CACHE_EXPIRE_LONG = 2 * 60 * 60; // 2小时
    public static final long CACHE_EXPIRE_DAY = 24 * 60 * 60; // 1天

    /**
     * 正则表达式
     */
    public static final String REGEX_EMAIL = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    public static final String REGEX_PHONE = "^1[3-9]\\d{9}$";
    public static final String REGEX_ID_CARD = "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
    public static final String REGEX_PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d@$!%*?&]{8,}$";
    public static final String REGEX_USERNAME = "^[a-zA-Z0-9_]{4,20}$";
    public static final String REGEX_IP = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$";
    public static final String REGEX_URL = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$";

    /**
     * 系统配置键
     */
    public static final String CONFIG_SYSTEM_NAME = "system.name";
    public static final String CONFIG_SYSTEM_VERSION = "system.version";
    public static final String CONFIG_SYSTEM_LOGO = "system.logo";
    public static final String CONFIG_SYSTEM_COPYRIGHT = "system.copyright";
    public static final String CONFIG_UPLOAD_PATH = "upload.path";
    public static final String CONFIG_UPLOAD_MAX_SIZE = "upload.maxSize";
    public static final String CONFIG_PASSWORD_POLICY = "password.policy";
    public static final String CONFIG_SESSION_TIMEOUT = "session.timeout";

    /**
     * 操作类型
     */
    public static final String OPERATION_CREATE = "CREATE";
    public static final String OPERATION_UPDATE = "UPDATE";
    public static final String OPERATION_DELETE = "DELETE";
    public static final String OPERATION_QUERY = "QUERY";
    public static final String OPERATION_LOGIN = "LOGIN";
    public static final String OPERATION_LOGOUT = "LOGOUT";
    public static final String OPERATION_EXPORT = "EXPORT";
    public static final String OPERATION_IMPORT = "IMPORT";

    /**
     * 私有构造函数，防止实例化
     */
    private ApiConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}