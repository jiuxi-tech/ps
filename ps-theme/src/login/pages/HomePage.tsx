import type { KcContext } from "../KcContext";
import type { I18n } from "../i18n";

export default function HomePage(props: {
    kcContext: Extract<KcContext, { pageId: "info.ftl" }>;
    i18n: I18n;
    Template: (templateProps: { children: React.ReactNode }) => JSX.Element;
    classes?: Record<string, string>;
}) {
    const { kcContext, i18n, Template, classes } = props;
    const { url, realm } = kcContext;

    // 获取账户 URL（使用 realm 路径拼接）
    const accountUrl = `${url.loginAction.split('/login-actions/')[0]}/account`;
    const logoutUrl = `${url.loginAction.split('/login-actions/')[0]}/protocol/openid-connect/logout`;

    return (
        <Template {...{ kcContext, i18n, classes, doUseDefaultCss: false }}>
            <div className="home-page-container" style={{ padding: '0px' }}>
                {/* 页面标题 */}
                <h1 id="kc-page-title" className="home-page-title" style={{ display: 'none' }}>
                    欢迎访问统一身份认证平台
                </h1>

                {/* 系统信息卡片 */}
                <div className="home-user-info" style={{ display: 'none' }}>
                    <h2 className="home-section-title">系统信息</h2>
                    <div className="home-info-item">
                        <span className="home-info-label">域：</span>
                        <span className="home-info-value">
                            {realm.displayName || realm.name}
                        </span>
                    </div>
                </div>

                {/* 快捷入口 */}
                <div className="home-quick-links">
                    <h2 className="home-section-title">快捷入口</h2>
                    <div className="home-links-grid">
                        <a href={accountUrl} className="home-link-item">
                            <div className="home-link-icon">👤</div>
                            <div className="home-link-text">账户管理</div>
                        </a>
                        <a href={`${accountUrl}/password`} className="home-link-item">
                            <div className="home-link-icon">🔐</div>
                            <div className="home-link-text">修改密码</div>
                        </a>
                        <a href={`${accountUrl}/security`} className="home-link-item">
                            <div className="home-link-icon">🛡️</div>
                            <div className="home-link-text">安全设置</div>
                        </a>
                        <a href={logoutUrl} className="home-link-item home-link-logout">
                            <div className="home-link-icon">🚪</div>
                            <div className="home-link-text">退出登录</div>
                        </a>
                    </div>
                </div>

                {/* 提示信息 */}
                {kcContext.message && (
                    <div className="home-user-info" style={{ marginTop: '20px', display: 'none' }}>
                        <h2 className="home-section-title">消息提示</h2>
                        <div className="home-info-item">
                            <span className="home-info-value">
                                {kcContext.message.summary}
                            </span>
                        </div>
                    </div>
                )}
            </div>
        </Template>
    );
}
