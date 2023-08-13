package com.pickup.menuItem.dto

import java.math.BigDecimal
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class MenuItemRequest(
    val restaurantId: Long,

    @field:NotBlank(message = "메뉴명은 필수 입력 항목입니다.")
    @field:Size(min = 2, max = 20)
    val name: String,

    @field:NotBlank(message = "가격은 필수 입력 항목입니다.")
    val price: BigDecimal,

    val description: String?,

    val isSoldOut: Boolean = false
)
