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

    fun save(post: Posts) {
        val currentTimestamp = LocalDateTime.now()
        post.id = idCounter
        post.updated = currentTimestamp
        post.created = currentTimestamp
        posts += post
        idCounter++
    }

    fun update(post: String, id: Int) : Boolean {
        val currentTimestamp = LocalDateTime.now()
        val blogPost = getPostById(id)
        if (blogPost != null) {
            blogPost.post = post
            blogPost.updated = currentTimestamp
            return true
        }
        return false
    }

    fun delete(id: Int) {
        val post: Posts? = getPostById(id)
        if (post != null) {
            posts -= post
        }
    }

    private fun exist(post: Posts) : Boolean {
        return posts.contains(post)
    }
}