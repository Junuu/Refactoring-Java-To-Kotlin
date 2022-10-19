package anthill.Anthill.domain.member.dto

import anthill.Anthill.domain.member.entity.Address

data class MemberResponseDTO(
   val userId : String,
   val nickName : String,
   val name : String,
   val phoneNumber : String,
   val address : Address,
)
