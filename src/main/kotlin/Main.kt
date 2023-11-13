import entity.Posts
import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.*
import service.ServicePosts
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable

@Serializable
data class Tmp(var post: String = "")
fun Application.module() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            ignoreUnknownKeys = true
            isLenient = true
        })
    }

    val blogPostRepository = ServicePosts()

    routing {
        route("/api/posts") {
            get("/") {
                val blogPosts = blogPostRepository.getAllPosts()
                call.respond(blogPosts)
            }

            get("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: throw NotFoundException()
                val blogPost = blogPostRepository.getPostById(id) ?: throw NotFoundException()
                println(blogPost)
                call.respond(blogPost)
            }

            put("/") {
                val p = call.receive<Posts>()
                println(p)
                blogPostRepository.save(p)
                call.respond(HttpStatusCode.Created)
            }

            patch("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: throw NotFoundException()
                val blogPost = call.receive<Posts>()
                blogPostRepository.update(blogPost.post, id)
                call.respond(HttpStatusCode.Accepted)
            }

            delete ("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: throw NotFoundException()
                blogPostRepository.delete(id)
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}

fun main() {
    embeddedServer(Netty, port = 9099, module = Application::module).start(wait = true)
}