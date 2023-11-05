package service

import entity.Posts
import java.time.LocalDateTime

class ServicePosts {
    private var posts: List<Posts> = ArrayList()
    private var idCounter: Int = 0

    fun getAllPosts(): List<Posts> {
        return posts
    }

    fun getPostById(id: Int): Posts? {
        for (i in posts) {
            if (i.id == id) {
                return i
            }
        }
        return null
    }

    fun save(post: String?) {
        val currentTimestamp = LocalDateTime.now()
        val post: Posts = Posts(idCounter, post, currentTimestamp, currentTimestamp)
        posts += post
        idCounter++
    }

    fun update(id: Int, post: String?) {
        val currentTimestamp = LocalDateTime.now()
        var it: Posts? = getPostById(id)
        if (it != null) {
            if (post == null) {
                it.post = ""
            } else {
                it.post = post
            }
            it.updated = currentTimestamp
        }
    }

    fun delete(id: Int) {
        val post: Posts? = getPostById(id)
        if (post != null) {
            posts -= post
        }
    }
}