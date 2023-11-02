package com.example.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.util.*

@SpringBootApplication
class DemoApplication

@RestController
@RequestMapping
class RestControllers(val service: BlogService) {
	@GetMapping("/")
	fun index(): List<BlogPost> {
		return service.findBlogs()
	}

	@GetMapping("/{id}")
	fun post(@PathVariable id: String): BlogPost {
		return service.findBlogById(id).get()
	}

	@PutMapping("/add")
	fun posts(@RequestBody blogpost: BlogPost) {
		val currentTimestamp = LocalDateTime.now()
		blogpost.created = currentTimestamp
		blogpost.updated = currentTimestamp
		service.save(blogpost)
	}

	@PatchMapping("/update/{id}")
	fun patch(@PathVariable id: String, @RequestBody blogpost: BlogPost) {
		service.update(id, blogpost.post)
	}

	@DeleteMapping("/delete/{id}")
	fun delete(@PathVariable id: String) {
		service.delete(id)
	}
}


interface BlogPostCrudRepository : CrudRepository<BlogPost, String>

@Table("BLOGPOST")
data class BlogPost(@Id var id: String?,
					var post: String,
					var created: LocalDateTime?,
					var updated: LocalDateTime?
)

data class Text(var text: String)

@Service
class BlogService(private val db: BlogPostCrudRepository) {
	private var idCounter: Int = 0

	fun findBlogs(): List<BlogPost> = db.findAll().toList()

	fun findBlogById(id: String): Optional<BlogPost> = db.findById(id)

	fun save(post: BlogPost) {
		db.save(post)
	}

	fun delete(id: String) {
		db.deleteById(id)
	}

	fun update(id: String, post: String) {
		var blogPost: BlogPost = findBlogById(id).get()
		val currentTimestamp = LocalDateTime.now()
		blogPost.updated = currentTimestamp
		blogPost.post = post
		db.save(blogPost)
	}
}

fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}
