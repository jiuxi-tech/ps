package com.jiuxi.core.core.customizer;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;

/**
 *  1、解决内嵌的Tomcat，在get路径中添加特殊字符参数导致请求访问失败的问题，错误信息：IllegalArgumentException: Invalid character found in the request target. The valid characters are defined in RFC 7230 and RFC 3986
 *  2、打包后，在tomcat中运行的时候，需要在tomcat中添加配置：relaxedPathChars 和 relaxedQueryChars
 *  如： <Connector port="8084" protocol="HTTP/1.1" relaxedPathChars="[]|" relaxedQueryChars="[]|{}^&#x5c;&#x60;&quot;&lt;&gt;" useBodyEncodingForURI="true" connectionTimeout="20000" redirectPort="8443" URIEncoding="UTF-8"/>
 * @ClassName: TopinfoTomcatConnectorCustomizer
 * @Description: TODO
 * @Author: Ypp
 * @Date: 2021/11/4 11:31
 * @Copyright: 2021 Hangzhou Jiuxi Inc. All rights reserved.
 */
public class TopinfoTomcatConnectorCustomizer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        factory.addConnectorCustomizers(new TomcatConnectorCustomizer() {
            @Override
            public void customize(Connector connector) {
                connector.setProperty("relaxedPathChars", "{}[]|");
                connector.setProperty("relaxedQueryChars", "{}[]|");
            }
        });
    }
}
