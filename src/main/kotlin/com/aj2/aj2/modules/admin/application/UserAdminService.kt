package com.aj2.aj2.modules.admin.application

import com.aj2.aj2.modules.identity.application.dto.CreateUserAdminRequest
import com.aj2.aj2.modules.identity.application.dto.UserDto
import com.aj2.aj2.modules.identity.application.dto.toDto
import com.aj2.aj2.modules.identity.domain.User
import com.aj2.aj2.modules.identity.domain.UserRepository
import com.aj2.aj2.shared.exceptions.BadRequestException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.format.DateTimeFormatter

private val DEFAULT_PASSWORD_FORMAT = DateTimeFormatter.ofPattern("ddMMyyyy")

@Service
class UserAdminService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    @Transactional
    fun create(request: CreateUserAdminRequest): UserDto {
        if (userRepository.findByEmail(request.email) != null) {
            throw BadRequestException("Email ${request.email} is already registered")
        }

        val providedPassword = request.password
        val rawPassword: String = if (providedPassword != null) {
            providedPassword
        } else {
            val dateOfBirth = request.dateOfBirth
                ?: throw BadRequestException("dateOfBirth is required when password is not provided")
            DEFAULT_PASSWORD_FORMAT.format(dateOfBirth)
        }

        val user = userRepository.save(
            User(
                email = request.email,
                passwordHash = passwordEncoder.encode(rawPassword)!!,
                role = request.role,
                firstName = request.firstName,
                lastName = request.lastName,
                gender = request.gender,
                phoneNumber = request.phoneNumber,
                dateOfBirth = request.dateOfBirth,
            ),
        )

        return user.toDto()
    }
}
