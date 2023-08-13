package com.pickup.restaurant.entity

import com.pickup.exception.CustomException
import com.pickup.exception.ErrorCode
import com.pickup.exception.ErrorMessage
import com.pickup.menuItem.entity.MenuItem
import com.pickup.restaurant.dto.ApprovalStatus
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class Restaurant(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "email", unique = true, nullable = false)
    var email: String,

    @Column(name = "password", nullable = false)
    var password: String,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "business_number", nullable = false)
    val businessNumber: String,

    @ManyToOne(fetch = FetchType.LAZY, cascade = arrayOf(CascadeType.ALL))
    @JoinColumn(name = "location_id")
    val location: Location,

    val tel: String,

    @Enumerated(EnumType.STRING)
    var approvalStatus: ApprovalStatus = ApprovalStatus.PENDING,

    @OneToMany(fetch = FetchType.LAZY, cascade = arrayOf(CascadeType.ALL))
    @JoinColumn(name = "restaurant_id")
    val businessHours: List<BusinessHour>,

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant", cascade = [CascadeType.ALL], orphanRemoval = true)
    val menuItems: MutableList<MenuItem> = mutableListOf(),

    @CreatedDate
    @Column(nullable = false)
    val joinedAt: LocalDateTime = LocalDateTime.now(),

    @LastModifiedDate
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    // 승인 상태 유효성 검사
    fun isValidStatusChange(newStatus: ApprovalStatus): Boolean {
        return when (approvalStatus) {
            ApprovalStatus.PENDING -> newStatus in listOf(ApprovalStatus.APPROVED, ApprovalStatus.REJECTED)
            ApprovalStatus.REJECTED -> newStatus == ApprovalStatus.APPROVED
            ApprovalStatus.APPROVED -> newStatus == ApprovalStatus.REJECTED

        }
    }

    // 승인 상태 업데이트
    fun updateApprovalStatus(newStatus: ApprovalStatus) {
        if (!isValidStatusChange(newStatus)) {
            throw CustomException(ErrorCode.NOT_CHANGED, ErrorMessage.INVALID_STATUS_CHANGE)
        }
        this.approvalStatus = newStatus
    }
}