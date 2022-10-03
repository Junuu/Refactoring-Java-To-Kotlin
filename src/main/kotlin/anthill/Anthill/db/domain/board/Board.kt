package anthill.Anthill.db.domain.board

import anthill.Anthill.api.dto.board.BoardResponseDTO
import anthill.Anthill.db.domain.member.Member
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.*


@Table(name = "board")
@Entity
@DynamicUpdate
data class Board(
    var title: String,
    var content: String,
    @Column(length = 20) val writer: String,
    var hits: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "member_id")
    var member: Member,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    val id: Long = 0L

    fun changeInfo(changedTitle: String, changedContent: String) {
        title = changedTitle
        content = changedContent
    }

    fun toBoardResponseDTO(): BoardResponseDTO {
        return BoardResponseDTO(
            id = this.id,
            title = this.title,
            content = this.content,
            writer = this.writer,
            hits = this.hits,
        )
    }
}