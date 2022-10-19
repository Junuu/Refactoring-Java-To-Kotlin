package anthill.Anthill.domain.member.entity

import anthill.Anthill.domain.board.entity.Board
import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.*

@Table(
    name = "member", indexes = [
        Index(name = "unique_idx_user_id", columnList = "user_id", unique = true),
        Index(name = "unique_idx_nickname", columnList = "nickname", unique = true),
        Index(name = "unique_idx_phone_number", columnList = "phone_number", unique = true)
    ]
)
@Entity
@DynamicUpdate
data class Member(
    @Column(name = "user_id", length = 20) val userId: String,
    @Column(length = 255) val password: String,
    @Column(name = "nickname", length = 20) var nickName: String,
    @Column(length = 40) val name: String,
    @Column(name = "phone_number", length = 40) val phoneNumber: String,
    @Embedded var address: Address,

    @JsonIgnore
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    val boards: List<Board> = ArrayList()
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    val id: Long = 0L

    fun changeNickName(nickName: String) {
        this.nickName = nickName
    }
}