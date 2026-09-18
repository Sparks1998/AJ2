package com.aj2.aj2.modules.identity.application.dto

import com.aj2.aj2.modules.identity.domain.User

/**
 * Internal result of a login, used only by the API layer to set the
 * Authorization response header. The token itself never appears in the
 * JSON body - clients read it from the header, same as every subsequent
 * rotated token. expiresAt isn't carried here either, since it's already
 * in the token's own "exp" claim.
 */
data class LoginResult(
    val accessToken: String,
    val user: User,
)
