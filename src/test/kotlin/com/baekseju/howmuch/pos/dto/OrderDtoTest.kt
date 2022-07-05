package com.baekseju.howmuch.pos.dto

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

internal class OrderDtoTest {
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
    fun orderDto() {
        val orderDto = OrderDto(
            menuItems = listOf(OrderDto.MenuItem(id=1, quantity = 1)),
            totalPrice = 12900,
            payWith = "credit-card"
        )

        val violations = validator.validate(orderDto)

        assertThat(violations).isEmpty()
    }

    @Test
    fun orderDtoCheckNull() {
        val orderDto = OrderDto(
            menuItems = null,
            totalPrice = null,
            payWith = null
        )

        val violations = validator.validate(orderDto)


        assertThat(violations).isNotEmpty
        assertThat(violations.filter { it.constraintDescriptor.annotation is NotNull }.size).isEqualTo(2)
        assertThat(violations.filter { it.constraintDescriptor.annotation is NotBlank }.size).isEqualTo(1)
    }

    @Test
    fun orderDtoCheckEmpty() {
        val orderDto = OrderDto(
            menuItems = listOf(OrderDto.MenuItem(id=1, quantity = 1)),
            totalPrice = 5900,
            payWith = ""
        )

        val violations = validator.validate(orderDto)


        assertThat(violations).isNotEmpty
        assertThat(violations.filter { it.constraintDescriptor.annotation is NotBlank }.size).isEqualTo(1)
    }

    @Test
    fun orderDtoCheckBlank() {
        val orderDto = OrderDto(
            menuItems = listOf(OrderDto.MenuItem(id=1, quantity = 1)),
            totalPrice = 5900,
            payWith = " "
        )

        val violations = validator.validate(orderDto)


        assertThat(violations).isNotEmpty
        assertThat(violations.filter { it.constraintDescriptor.annotation is NotBlank }.size).isEqualTo(1)
    }

    @Test
    fun orderDtoCheckMin() {
        val orderDto = OrderDto(
            menuItems = listOf(OrderDto.MenuItem(id=1, quantity = 1)),
            totalPrice = -1,
            payWith = "credit-card"
        )

        val violations = validator.validate(orderDto)


        assertThat(violations).isNotEmpty
        assertThat(violations.filter { it.constraintDescriptor.annotation is Min }.size).isEqualTo(1)
    }
}
