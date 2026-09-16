package com.aj2.aj2.infrastructure.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
    private val authTokenFilter: AuthTokenFilter,
    private val apiAccessDeniedHandler: ApiAccessDeniedHandler,
    private val apiAuthenticationEntryPoint: ApiAuthenticationEntryPoint,
) {
    @Bean
    @Order(1)
    fun adminFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.securityMatcher("/admin/**")
        http.csrf(Customizer { obj: CsrfConfigurer<HttpSecurity?>? -> obj!!.disable() })
            .formLogin(Customizer { obj: FormLoginConfigurer<HttpSecurity?>? -> obj!!.disable() })
            .httpBasic(Customizer { obj: HttpBasicConfigurer<HttpSecurity?>? -> obj!!.disable() })
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .cors(Customizer.withDefaults())
            .oauth2ResourceServer { oauth2 -> oauth2.jwt(Customizer.withDefaults()) }
            .exceptionHandling {
                it.accessDeniedHandler(apiAccessDeniedHandler)
                it.authenticationEntryPoint(apiAuthenticationEntryPoint)
            }
            .authorizeHttpRequests { authorizer ->
                authorizer
                    .requestMatchers(HttpMethod.OPTIONS, "/admin/**").permitAll()
                    .anyRequest().hasRole("ADMIN")
            }
            .addFilterAfter(authTokenFilter, BearerTokenAuthenticationFilter::class.java)

        return http.build()
    }

    @Bean
    @Order(2)
    fun publicFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.securityMatcher("/**")
        http.csrf(Customizer { obj: CsrfConfigurer<HttpSecurity?>? -> obj!!.disable() })
            .formLogin(Customizer { obj: FormLoginConfigurer<HttpSecurity?>? -> obj!!.disable() })
            .httpBasic(Customizer { obj: HttpBasicConfigurer<HttpSecurity?>? -> obj!!.disable() })
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .cors(Customizer.withDefaults())
            .oauth2ResourceServer { oauth2 -> oauth2.jwt(Customizer.withDefaults()) }
            .exceptionHandling {
                it.accessDeniedHandler(apiAccessDeniedHandler)
                it.authenticationEntryPoint(apiAuthenticationEntryPoint)
            }
            .authorizeHttpRequests { it.anyRequest().permitAll() }
            .addFilterAfter(authTokenFilter, BearerTokenAuthenticationFilter::class.java)

        return http.build()
    }
}
