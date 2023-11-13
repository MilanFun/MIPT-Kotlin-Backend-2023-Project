package entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import util.LocalDateSerializer
import java.time.LocalDateTime

@Serializable
data class Posts(@SerialName("id") var id: Int? = null,
                 @SerialName("post") var post: String,
                 @Serializable(with = LocalDateSerializer::class) var created: LocalDateTime? = null,
                 @Serializable(with = LocalDateSerializer::class) var updated: LocalDateTime? = null
)