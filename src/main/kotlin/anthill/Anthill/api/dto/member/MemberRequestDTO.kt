package anthill.Anthill.api.dto.member

import anthill.Anthill.db.domain.member.Address
import anthill.Anthill.db.domain.member.Member
import org.mindrot.jbcrypt.BCrypt
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

data class MemberRequestDTO(
    @field: NotBlank(message = "아이디를 입력해주세요.")
    @field: Size(min = 5, max = 20, message = "아이디는 5자 이상 20자 이하로 입력해주세요.")
    val userId: String,

    @field: NotBlank(message = "비밀번호를 입력해주세요.")
    @field: Size(min = 8, message = "비밀번호를 8자 이상으로 입력해주세요.")
    var password: String,

    @field: NotBlank(message = "별명을 입력해주세요.")
    @field: Size(max = 20, message = "별명을 20자 이하로 입력해주세요.")
    val nickName: String,

    @field: NotBlank(message = "이름을 입력해주세요.")
    val name: String,

    @field: NotBlank(message = "휴대전화번호를 입력해주세요.")
    @field: Pattern(regexp = "(01[016789])(\\d{3,4})(\\d{4})", message = "올바른 휴대폰 번호를 입력해주세요.")
    val phoneNumber: String,

    val address: Address,
) {
    fun hashingPassword() {
        password = BCrypt.hashpw(password, BCrypt.gensalt())
    }

    fun toEntity(): Member {
        return Member(
            userId = this.userId,
            password = this.password,
            nickName = this.nickName,
            name = this.name,
            phoneNumber = this.phoneNumber,
            address = this.address
        )
    }
}
