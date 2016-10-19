package org.swamps.houseController.http;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;

/**
 * Created by paul on 5/01/15.
 */

public class SslContainereCustomiser {



    public EmbeddedServletContainerCustomizer containerCustomizer(@Value("${keystore.file}") String keystoreFile,
                                                                  @Value("${keystore.password}") String keystorePassword,
                                                                  @Value("${keystore.type}") String keystoreType,
                                                                  @Value("${keystore.alias}") String keystoreAlias) throws FileNotFoundException
    {
        final String absoluteKeystoreFile = ResourceUtils.getFile(keystoreFile).getAbsolutePath();

        return factory -> {
            TomcatEmbeddedServletContainerFactory containerFactory = (TomcatEmbeddedServletContainerFactory) factory;
            containerFactory.addConnectorCustomizers((TomcatConnectorCustomizer) (Connector connector) -> {
                connector.setSecure(true);
                connector.setScheme("https");
                connector.setAttribute("keystoreFile", keystoreFile);
                connector.setAttribute("keystorePass", keystorePassword);
                connector.setAttribute("keystoreType", keystoreType);
                connector.setAttribute("keyAlias", keystoreAlias);
                connector.setAttribute("clientAuth", "false");
                connector.setAttribute("sslProtocol", "TLS");
                connector.setAttribute("SSLEnabled", true);
            });
        };
    }

}



