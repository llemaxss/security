package com.gmail.llemaxiss.security.config.ACBAMWCPOTAF;

import com.gmail.llemaxiss.security.component.AuthServerProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.client.RestTemplate;

/**
 * AppConfigByAuthManagerWithCustomProvidersOtpTokenAndFilters
 */

/**
 * 1) Сначала требуется создать бин RestTemplate
 * 2) Далее создаются компонент {@link AuthServerProxy}, так как он зависит от RestTemplate
 * 3) Далее создаются провайдеры в {@link ACBAMWCPOTAF_2}, так как они зависият от {@link AuthServerProxy}
 * 4) Далее создаются фильтры в {@link ACBAMWCPOTAF_3}, так как они зависият от AuthenticationManager,
 * который создается в {@link ACBAMWCPOTAF_2#authenticationManager(HttpSecurity)}
 */
//@EnableWebSecurity(debug = true)
//@Configuration
public class ACBAMWCPOTAF {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
