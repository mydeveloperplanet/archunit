package com.mydeveloperplanet.archunit.taikai.webconfig.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
@EnableWebSecurity
public class WebConfig {

    private final CsrfTokenRequestAttributeHandler handler = new CsrfTokenRequestAttributeHandler();

    /**
     * Configure the security filter chain for the Spring security.
     *
     * @param http The http security for configuring the web based security
     * @return the security filter chain for the Spring security
     * @throws Exception The chaining methods may throw any exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // By default, the lookup of the CsrfToken will be deferred until it is needed. By setting the csrfRequestAttributeName to null, the CsrfToken must
        // first be loaded to determine what attribute name to use. This causes the CsrfToken to be loaded on every request.
        handler.setCsrfRequestAttributeName(null);

        http
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        return http.build();
    }
}
