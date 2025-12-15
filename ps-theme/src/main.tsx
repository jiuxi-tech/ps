import { createRoot } from "react-dom/client";
import { StrictMode } from "react";
import { KcPage } from "./kc.gen";

// 取消下面代码块的注释，可以通过 `yarn dev` 来测试特定页面
// 不要忘记重新注释掉，否则你的打包体积会增大

import { getKcContextMock } from "./login/KcPageStory";

if (import.meta.env.DEV) {

    window.kcContext = getKcContextMock({
        pageId: "login.ftl",
        overrides: {}
    });
}

// 设置 body 样式
document.body.style.margin = '0';
document.body.style.padding = '0';

createRoot(document.getElementById("root")!).render(
    <StrictMode>
        {!window.kcContext ? (
            <div style={{
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                minHeight: '100vh',
                background: 'linear-gradient(135deg,  #c6e5fe 0%,  #66a1ea 100%)',
                fontFamily: 'system-ui, -apple-system, sans-serif'
            }}>
                <div style={{
                    background: 'white',
                    borderRadius: '8px',
                    padding: '48px',
                    boxShadow: '0 10px 40px rgba(0, 0, 0, 0.1)',
                    textAlign: 'center',
                    maxWidth: '500px'
                }}>
                    <div style={{
                        fontSize: '64px',
                        marginBottom: '24px'
                    }}>⚠️</div>
                    <h1 style={{
                        margin: '0 0 16px 0',
                        fontSize: '24px',
                        color: '#333',
                        fontWeight: '600'
                    }}>SSO 服务异常</h1>
                    <p style={{
                        margin: '0 0 32px 0',
                        fontSize: '14px',
                        color: '#666',
                        lineHeight: '1.6'
                    }}>系统暂时无法连接到身份认证服务，请稍后重试或联系管理员</p>
                    <button onClick={() => window.location.reload()} style={{
                        background: '#C00000',
                        color: 'white',
                        border: 'none',
                        padding: '10px 32px',
                        borderRadius: '4px',
                        fontSize: '14px',
                        cursor: 'pointer',
                        transition: 'background 0.3s'
                    }} onMouseOver={(e) => e.currentTarget.style.background = '#910000'} onMouseOut={(e) => e.currentTarget.style.background = '#667eea'}>
                        重新尝试
                    </button>
                </div>
            </div>
        ) : (
            <KcPage kcContext={window.kcContext} />
        )}
    </StrictMode>
);
