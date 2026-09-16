package com.aj2.aj2.infrastructure.security

import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.MessageDigest
import java.security.cert.X509Certificate
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.Base64

@Configuration
class JwtSupport {
    private val log = LoggerFactory.getLogger(JwtSupport::class.java)

    @Bean
    fun jwkSource(
        @Value($$"${security.jwt.keystore.location:file:./aj2-jwt.p12}") keyStoreLocation: Resource,
        @Value($$"${security.jwt.keystore.password:}") keyStorePassword: String,
        @Value($$"${security.jwt.keystore.alias:aj2-jwt}") keyAlias: String,
        @Value($$"${security.jwt.keystore.key-password:}") keyPassword: String,
    ): JWKSource<SecurityContext> {
        // Generates ephemeral RSA key for JWT signing
        fun generated(reason: String): JWKSource<SecurityContext> {
            val keyPair = generateRsaKey()
            val publicKey = keyPair.public as RSAPublicKey
            val privateKey = keyPair.private as RSAPrivateKey

            val kid = keyIdFromPublicKey(publicKey)
            log.warn(
                "JWT signing key: using GENERATED ephemeral RSA key ({}), kid={}",
                reason,
                kid,
            )

            val rsaKey = RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(kid)
                .build()

            val jwkSet = JWKSet(rsaKey)
            return JWKSource { jwkSelector, _ -> jwkSelector.select(jwkSet) }
        }

        if (!keyStoreLocation.exists() || !keyStoreLocation.isReadable) {
            return generated("keystore not found/readable at '$keyStoreLocation'")
        }

        if (keyStorePassword.isBlank()) {
            return generated("keystore password is missing (set security.jwt.keystore.password)")
        }

        return try {
            val keyStore = KeyStore.getInstance("PKCS12")
            keyStoreLocation.inputStream.use { input ->
                keyStore.load(input, keyStorePassword.toCharArray())
            }

            val effectiveKeyPassword = keyPassword.ifBlank { keyStorePassword }
            val privateKey = keyStore.getKey(keyAlias, effectiveKeyPassword.toCharArray()) as RSAPrivateKey
            val certificate = keyStore.getCertificate(keyAlias) as? X509Certificate
                ?: throw IllegalStateException("Certificate not found for alias '$keyAlias'")
            val publicKey = certificate.publicKey as RSAPublicKey

            val kid = keyIdFromPublicKey(publicKey)
            log.info(
                "JWT signing key: loaded RSA key from PKCS12 keystore '{}', alias='{}', kid={}",
                keyStoreLocation,
                keyAlias,
                kid,
            )

            val rsaKey = RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(kid)
                .build()

            val jwkSet = JWKSet(rsaKey)
            JWKSource { jwkSelector, _ -> jwkSelector.select(jwkSet) }
        } catch (e: Exception) {
            log.warn(
                "JWT signing key: failed to load PKCS12 keystore '{}' (alias='{}'): {}. Falling back to generated key.",
                keyStoreLocation,
                keyAlias,
                e.message,
            )
            generated("keystore load failure")
        }
    }

    @Bean
    fun jwtDecoder(jwkSource: JWKSource<SecurityContext>): JwtDecoder =
        OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource)

    @Bean
    fun jwtEncoder(jwkSource: JWKSource<SecurityContext>): JwtEncoder = NimbusJwtEncoder(jwkSource)

    private fun keyIdFromPublicKey(publicKey: RSAPublicKey): String {
        val digest = MessageDigest.getInstance("SHA-256").digest(publicKey.encoded)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest)
    }

    private fun generateRsaKey(): KeyPair {
        val generator = KeyPairGenerator.getInstance("RSA")
        generator.initialize(2048)
        return generator.generateKeyPair()
    }
}
