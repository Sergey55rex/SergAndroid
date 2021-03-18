package ru.netology.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import ru.netology.dao.PostDao
import ru.netology.dto.Post
import ru.netology.entity.PostEntity

class PostRepositoryImpl(
    private val dao: PostDao
) : PostRepository {
    override fun getAll() = Transformations.map(dao.getAll()) { list ->
        list.map {
            Post(it.id, it.author, it.content, it.published, it.liked, it.likes, it.toSend, it.toSends, it.viewing, it.viewings)
        }
    }

    override fun likeById(id: Long) {
        dao.likeById(id)
    }

    override fun save(post: Post) {
        dao.save(PostEntity.fromDto(post))
    }

    override fun toSendsById(id: Long) {
       dao.toSendsById(id)
    }

    override fun viewingById(id: Long) {
        dao.viewingById(id)
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
    }
}
