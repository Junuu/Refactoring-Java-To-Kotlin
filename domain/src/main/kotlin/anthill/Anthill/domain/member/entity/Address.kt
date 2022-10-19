package anthill.Anthill.domain.member.entity

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class Address(
    @Column val zipCode : String,
    val streetNameAddress : String,
    val detailAddress : String,
)