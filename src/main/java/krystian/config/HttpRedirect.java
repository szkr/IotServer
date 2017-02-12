package krystian.config;

import io.undertow.servlet.api.SecurityConstraint;
import io.undertow.servlet.api.SecurityInfo;
import io.undertow.servlet.api.TransportGuaranteeType;
import io.undertow.servlet.api.WebResourceCollection;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.undertow.UndertowEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 12/21/2016 11:41 PM
 */
@Configuration
public class HttpRedirect {
    @Bean
    public EmbeddedServletContainerFactory undertow() {
        UndertowEmbeddedServletContainerFactory undertow = new UndertowEmbeddedServletContainerFactory();
        undertow.addBuilderCustomizers(builder -> builder.addHttpListener(80, "0.0.0.0"));


        //Uncomment to redirect http to https
        undertow.addDeploymentInfoCustomizers(deploymentInfo -> deploymentInfo.addSecurityConstraint(new SecurityConstraint()
                .addWebResourceCollection(new WebResourceCollection()
                        .addUrlPattern("/*"))
                .setTransportGuaranteeType(TransportGuaranteeType.CONFIDENTIAL)
                .setEmptyRoleSemantic(SecurityInfo.EmptyRoleSemantic.PERMIT))
                .setConfidentialPortManager(httpServerExchange -> 443));


        return undertow;
    }
}
