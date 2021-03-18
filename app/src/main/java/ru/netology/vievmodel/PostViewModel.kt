package ru.netology.vievmodel

import android.app.Application
import androidx.lifecycle.*
import ru.netology.db.AppDb
import ru.netology.dto.Post
import ru.netology.repository.PostRepository

import ru.netology.repository.PostRepositorySQLiteImpl

private val empty = Post(
        id = 0,
        author = "",
        content = "",
        published = "",
        liked  = false,
        likes = 0,
        toSend = false,
        toSends = 0,
        viewing = false,
        viewings = 0,
//        video = " "
)

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository:PostRepository = PostRepositorySQLiteImpl(
            AppDb.getInstance(application).postDao
    )
    val data = repository.getAll()
    val edited = MutableLiveData(empty)


    fun likeById(id: Long) = repository.likeById(id)
    fun toSendsById(id: Long) = repository.toSendsById(id)
    fun viewingById(id: Long) = repository.viewingById(id)

    fun removeById(id: Long) = repository.removeById(id)

    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {

            return
        }
        edited.value = edited.value?.copy(content = text)
    }
}