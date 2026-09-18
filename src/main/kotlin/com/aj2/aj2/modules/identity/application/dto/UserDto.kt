package com.aj2.aj2.modules.identity.application.dto

import com.aj2.aj2.modules.identity.domain.Gender
import com.aj2.aj2.modules.identity.domain.User
import com.aj2.aj2.modules.identity.domain.UserRole
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

data class UserDto(
    val id: UUID,
    val email: String,
    val role: UserRole,
    val firstName: String?,
    val lastName: String?,
    val gender: Gender?,
    val phoneNumber: String?,
    val dateOfBirth: LocalDate?,
    val xpTotal: Int,
    val level: Int,
    val currentStreak: Int,
    val lastActivityAt: Instant?,
    val createdAt: Instant,
)

fun User.toDto() = UserDto(
    id = id!!,
    email = email,
    role = role,
    firstName = firstName,
    lastName = lastName,
    gender = gender,
    phoneNumber = phoneNumber,
    dateOfBirth = dateOfBirth,
    xpTotal = xpTotal,
    level = level,
    currentStreak = currentStreak,
    lastActivityAt = lastActivityAt,
    createdAt = createdAt!!,
)
