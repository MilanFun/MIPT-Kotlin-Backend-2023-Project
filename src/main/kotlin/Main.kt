import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import entity.Posts
import entity.User
import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.routing.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.*
import service.ServicePosts
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import kotlinx.serialization.json.Json
import java.util.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import service.ServiceUser

fun Application.module() {
    val blogPostRepository = ServicePosts()
    val userRepository = ServiceUser()

    val secret = "secret"
    val issuer = "http://localhost:9099/"
    val audience = "http://localhost:9099/hello"
    val myrealm = "Access to 'hello'"

    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            ignoreUnknownKeys = true
            isLenient = true
        })
    }

    install(Authentication) {
        jwt("auth-jwt") {
            realm = myrealm
            verifier(JWT
                .require(Algorithm.HMAC256(secret))
                .withAudience(audience)
                .withIssuer(issuer)
                .build())
            validate { credential ->
                if (credential.payload.getClaim("username").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { _, _ ->
                call.respond(
                    HttpStatusCode.Unauthorized,
                    mapOf("error" to "invalid_token", "error_description" to "Token is not valid or has expired")
                )
            }
        }
    }

    routing {
        post("/login") {
            val user = call.receive<User>()
            if (!userRepository.exist(user)) {
                userRepository.save(user)
                println(userRepository.users)
                val token = JWT.create()
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .withClaim("username", user.username)
                    .withClaim("password", user.password)
                    .withExpiresAt(Date(System.currentTimeMillis() + 20 * 60 * 1000))
                    .sign(Algorithm.HMAC256(secret))
                call.respond(hashMapOf("token" to token))
            } else {
                call.respond(HttpStatusCode.Conflict)
            }
        }
        authenticate("auth-jwt") {
            route("/api/posts") {
                get("/") {
                    val blogPosts = blogPostRepository.getAllPosts()
                    val principal = call.principal<JWTPrincipal>()
                    val username = principal!!.payload.getClaim("username").asString()
                    val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                    if (username != null && expiresAt != null && expiresAt > 0) {
                        println(blogPosts.size)
                        call.respond(blogPosts)
                        call.respond(HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.Unauthorized)
                    }
                }

                get("/{id}") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw NotFoundException()
                    val principal = call.principal<JWTPrincipal>()
                    val username = principal!!.payload.getClaim("username").asString()
                    val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                    if (username != null && expiresAt != null && expiresAt > 0) {
                        val blogPost = blogPostRepository.getPostById(id) ?: throw NotFoundException()
                        println(blogPost)
                        call.respond(blogPost)
                        call.respond(HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.Unauthorized)
                    }
                }

                put("/") {
                    val p = call.receive<Posts>()
                    val principal = call.principal<JWTPrincipal>()
                    val username = principal!!.payload.getClaim("username").asString()
                    val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                    if (username != null && expiresAt != null && expiresAt > 0) {
                        println(p)
                        println(userRepository.getUserByUsername(username))
                        println(username)
                        println(userRepository.users)
                        blogPostRepository.save(p, userRepository.getUserByUsername(username) ?: throw NotFoundException())
                        call.respond(HttpStatusCode.Created)
                    } else {
                        call.respond(HttpStatusCode.Unauthorized)
                    }
                }

                patch("/{id}") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw NotFoundException()
                    val blogPost = call.receive<Posts>()
                    val principal = call.principal<JWTPrincipal>()
                    val username = principal!!.payload.getClaim("username").asString()
                    val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                    val post: Posts = blogPostRepository.getPostById(id) ?: throw NotFoundException()
                    if (username != null && expiresAt != null && expiresAt > 0) {
                        if (username == post.user?.username) {
                            blogPostRepository.update(blogPost.post, id)
                            call.respond(HttpStatusCode.Accepted)
                        } else {
                            call.respond(HttpStatusCode.Forbidden)
                        }
                    } else {
                        call.respond(HttpStatusCode.Unauthorized)
                    }
                }

                delete ("/{id}") {
                    val id = call.parameters["id"]?.toIntOrNull() ?: throw NotFoundException()
                    val principal = call.principal<JWTPrincipal>()
                    val username = principal!!.payload.getClaim("username").asString()
                    val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                    if (username != null && expiresAt != null && expiresAt > 0) {
                        if (username == blogPostRepository.getPostById(id)?.user?.username) {
                            blogPostRepository.delete(id)
                            call.respond(HttpStatusCode.Accepted)
                        } else {
                            call.respond(HttpStatusCode.Forbidden)
                        }
                    } else {
                        call.respond(HttpStatusCode.Unauthorized)
                    }
                }
            }
        }
    }
}

fun main() {
    embeddedServer(Netty, port = 9099, module = Application::module).start(wait = true)
}