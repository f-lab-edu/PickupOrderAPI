package com.pickup.exception

enum class ErrorMessage(
    val message: String
) {
    INVALID_PARAMETER("잘못된 요청 파라미터입니다."),

    USER_NOT_FOUND("회원 정보를 찾을 수 없습니다."),
    RESTAURANT_NOT_FOUND("음식점 정보를 찾을 수 없습니다."),
    MENU_NOT_FOUND("해당 메뉴를 찾을 수 없습니다."),
    ORDER_NOT_FOUND("해당 주문를 찾을 수 없습니다."),
    INVALID_STATUS_CHANGE("상태 변경이 불가능합니다. 상태를 확인해주세요"),

    DUPLICATE_EMAIL("이미 등록된 이메일입니다."),
    DUPLICATE_PHONE_NUMBER("이미 등록된 홴드폰 번호 입니다."),
    DUPLICATE_TEL("이미 등록된 홴드폰 번호 입니다."),
    DUPLICATE_BUSINESS_NUMBER("이미 등록된 사업자 등록 번호 입니다."),

    INTERNAL_SERVER_ERROR("서버 내부 오류입니다."),

    TOKEN_EXPIRED("토큰이 만료되었습니다."),
    TOKEN_MALFORMED("잘못된 토큰입니다."),
    TOKEN_INVALID("인증 토큰이 유효하지 않습니다");


}
