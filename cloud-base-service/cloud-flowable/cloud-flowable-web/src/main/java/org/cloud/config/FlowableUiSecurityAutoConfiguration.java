package org.cloud.config;

import org.flowable.spring.boot.FlowableSecurityAutoConfiguration;
import org.flowable.spring.boot.idm.IdmEngineServicesAutoConfiguration;
import org.flowable.ui.common.model.UserRepresentation;
import org.flowable.ui.common.rest.idm.CurrentUserProvider;
import org.flowable.ui.common.security.ApiHttpSecurityCustomizer;
import org.flowable.ui.common.security.DefaultPrivileges;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;

import java.util.Arrays;

@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter({
        IdmEngineServicesAutoConfiguration.class,
})
@AutoConfigureBefore({
        FlowableSecurityAutoConfiguration.class,
        OAuth2ClientAutoConfiguration.class,
})
public class FlowableUiSecurityAutoConfiguration {

    @Bean
    public ApiHttpSecurityCustomizer apiHttpSecurityCustomizer() {
        return new ApiHttpSecurityCustomizer() {
            @Override
            public void customize(HttpSecurity http) throws Exception {
            }
        };
    }

    @Bean
    public CurrentUserProvider currentUserProvider() {
        return new CurrentUserProvider() {
            @Override
            public UserRepresentation getCurrentUser(Authentication authentication) {
                UserRepresentation userRepresentation = new UserRepresentation();
                userRepresentation.setId("0");
                userRepresentation.setFirstName("driftwood");
                userRepresentation.setLastName("driftwood");
                userRepresentation.setPrivileges(
                        Arrays.asList(
                                DefaultPrivileges.ACCESS_IDM,
                                DefaultPrivileges.ACCESS_MODELER,
                                DefaultPrivileges.ACCESS_ADMIN,
                                DefaultPrivileges.ACCESS_TASK,
                                DefaultPrivileges.ACCESS_REST_API
                        )
                );
                return userRepresentation;
            }

            @Override
            public boolean supports(Authentication authentication) {
                return true;
            }
        };
    }
}