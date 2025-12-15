import { Suspense, lazy, useEffect } from "react";
import "./resources/css/login.css";
import type { ClassKey } from "keycloakify/login";
import type { KcContext } from "./KcContext";
import { useI18n } from "./i18n";
import DefaultPage from "keycloakify/login/DefaultPage";
import Template from "./Template";
import HomePage from "./pages/HomePage";
const UserProfileFormFields = lazy(
    () => import("keycloakify/login/UserProfileFormFields")
);

const doMakeUserConfirmPassword = true;

export default function KcPage(props: { kcContext: KcContext }) {
    const { kcContext } = props;

    const { i18n } = useI18n({ kcContext });

    // 设置浏览器标题为指定中文标题（覆盖默认）
    useEffect(() => {
        document.title = "中共陕西省委党校（陕西行政学院）业务中台 - 统一身份认证";
    }, []);

    return (
        <Suspense>
            {(() => {
                switch (kcContext.pageId) {
                    case "info.ftl":
                        return (
                            <HomePage
                                kcContext={kcContext}
                                i18n={i18n}
                                Template={Template}
                                classes={classes}
                            />
                        );
                    default:
                        return (
                            <DefaultPage
                                kcContext={kcContext}
                                i18n={i18n}
                                classes={classes}
                                Template={Template}
                                doUseDefaultCss={true}
                                UserProfileFormFields={UserProfileFormFields}
                                doMakeUserConfirmPassword={doMakeUserConfirmPassword}
                            />
                        );
                }
            })()}
        </Suspense>
    );
}

const classes = {} satisfies { [key in ClassKey]?: string };
