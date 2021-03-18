package ru.netology.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var liked: Boolean ,
    var likes: Int = 0,
    var toSend: Boolean ,
    var toSends: Int = 0,
    var viewing: Boolean ,
    var viewings: Int = 0,
//        var video: String,
) {
    fun toDto() = Post(id, author, content, published, liked, likes, toSend, toSends, viewing, viewings)

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(dto.id, dto.author, dto.content, dto.published, dto.liked, dto.likes, dto.toSend, dto.toSends, dto.viewing, dto.viewings)

    }
}