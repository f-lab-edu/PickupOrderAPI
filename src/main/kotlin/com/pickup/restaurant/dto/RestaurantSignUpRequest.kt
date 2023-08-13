package com.pickup.restaurant.dto

import com.pickup.menuItem.entity.MenuItem
import com.pickup.restaurant.entity.BusinessHour
import com.pickup.restaurant.entity.Location
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

data class RestaurantSignUpRequest(
    @field:NotBlank(message = "상점명은 필수 입력 항목입니다.")
    @field:Size(min = 2, max = 20)
    val name: String,

    @field:NotBlank(message = "사업자번호는 필수 입력 항목입니다.")
    val businessNumber: String,

    @field:Email(message = "유효한 이메일 주소를 입력하세요.")
    @field:NotBlank(message = "이메일 주소는 필수 입력 항목입니다.")
    val email: String,

    @field:NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @field:Size(min = 8, max = 20)
    val password: String,

    @field:NotBlank(message = "위치는 필수 입력 항목입니다.")
    val location: Location,

    @field:Pattern(regexp = "(^$|[0-9]{10})", message = "유효한 전화번호를 입력하세요.")
    @field:NotBlank(message = "매장 전화번호는 필수 입력 항목입니다.")
    val tel: String,

    val businessHours: List<BusinessHour>,

    val menuItems: MutableList<MenuItem>
)
