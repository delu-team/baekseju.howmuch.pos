package com.baekseju.howmuch.pos.dto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

internal class UserDtoTest {
    private lateinit var validatorFactory: ValidatorFactory
    private lateinit var validator: Validator

    @BeforeEach
    fun setup() {
        validatorFactory = Validation.buildDefaultValidatorFactory()
        validator = validatorFactory.validator
    }

    @AfterEach
    fun close() {
        validatorFactory.close()
    }

    @Test
    fun userDto() {
        val userDto = UserDto(
            phoneNumber = "010-1111-2222"
        )

        val violations = validator.validate(userDto)

        assertThat(violations).isEmpty()
    }

    @Test
    fun userDtoCheckNull() {
        val userDto = UserDto(
            phoneNumber = null
        )

        val violations = validator.validate(userDto)

        assertThat(violations).isNotEmpty
        assertThat(violations.filter { it.constraintDescriptor.annotation is NotNull }.size).isEqualTo(1)
    }

    @Test
    fun userDtoCheckEmpty() {
        val userDto = UserDto(
            phoneNumber = ""
        )

        val violations = validator.validate(userDto)

        assertThat(violations).isNotEmpty
        assertThat(violations.filter { it.constraintDescriptor.annotation is Pattern }.size).isEqualTo(1)
    }

    @Test
    fun userDtoCheckPattern() {
        val userDto = UserDto(
            phoneNumber = "010-12312-13123"
        )

        val violations = validator.validate(userDto)

        assertThat(violations).isNotEmpty
        assertThat(violations.filter { it.constraintDescriptor.annotation is Pattern }.size).isEqualTo(1)
    }
}