package com.aj2.aj2.modules.job.infrastructure

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "france-travail-auth", url = $$"${france-travail.token-url}")
interface FranceTravailAuthFeignClient {
    @PostMapping(consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun getToken(@RequestBody form: MultiValueMap<String, String>): TokenResponse

    data class TokenResponse(
        @JsonProperty("access_token") val accessToken: String,
        @JsonProperty("expires_in") val expiresIn: Int,
    )
}
