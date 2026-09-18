package com.aj2.aj2.infrastructure.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
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

/**
 * oauth2ResourceServer { jwt(...) } does the automatic part: extracts the
 * Authorization header, verifies the JWT signature, checks exp. AuthTokenFilter
 * runs right after it for the part Spring Security can't know on its own:
 * cross-checking auth_tokens for revocation/rotation and re-resolving the
 * caller's current role from the DB. @PreAuthorize (method security) is only
 * ever used on top of that for role checks (e.g. hasRole('ADMIN')) - never
 * for token/authentication checks.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
    private val authTokenFilter: AuthTokenFilter,
    private val apiAccessDeniedHandler: ApiAccessDeniedHandler,
    private val apiAuthenticationEntryPoint: ApiAuthenticationEntryPoint,
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
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
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .requestMatchers(
                        "/auth/login",
                        "/actuator/**",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                    ).permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterAfter(authTokenFilter, BearerTokenAuthenticationFilter::class.java)

        return http.build()
    }
}
