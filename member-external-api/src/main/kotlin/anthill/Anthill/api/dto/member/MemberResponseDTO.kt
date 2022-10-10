package anthill.Anthill.api.dto.member

import anthill.Anthill.db.domain.member.Address

data class MemberResponseDTO(
   val userId : String,
   val nickName : String,
   val name : String,
   val phoneNumber : String,
   val address : Address,
)
