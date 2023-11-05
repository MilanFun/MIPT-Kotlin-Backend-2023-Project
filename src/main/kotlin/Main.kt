import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.*
import service.ServicePosts
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import service.JSONConv

fun Application.module() {
    install(ContentNegotiation) {

    }

    val blogPostRepository = ServicePosts()

    routing {
        route("/api/posts") {
            get("/") {
                val blogPosts = blogPostRepository.getAllPosts()
                call.respond(JSONConv.deconvert(blogPosts))
            }

            get("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: throw NotFoundException()
                val blogPost = blogPostRepository.getPostById(id) ?: throw NotFoundException()
                println(blogPost)
                call.respond(JSONConv.deobject(blogPost))
            }

            put("/") {
                val p = call.receiveText()
                println(JSONConv.convert(p))
                blogPostRepository.save(JSONConv.convert(p).get("post"))
                call.respond(HttpStatusCode.Created)
            }

            patch("/{id}") {
                val body = call.receiveText()
                val id = call.parameters["id"]?.toIntOrNull() ?: throw NotFoundException()
                val data: Map<String, String> = JSONConv.convert(body)
                println(data)
                blogPostRepository.update(id, data.get("post"))
                call.respond(HttpStatusCode.Accepted)
            }

//            delete ("/id") {
//                val id = call.parameters["id"]?.toIntOrNull() ?: throw NotFoundException()
//                blogPostRepository.delete(id)
//                call.respond(HttpStatusCode.OK)
//            }
        }
    }
}

fun main() {
    embeddedServer(Netty, port = 9099, module = Application::module).start(wait = true)
}