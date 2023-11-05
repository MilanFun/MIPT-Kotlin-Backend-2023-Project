package entity

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

data class Posts(var id: Int?,
                 var post: String?,
                 var created: LocalDateTime?,
                 var updated: LocalDateTime?
)