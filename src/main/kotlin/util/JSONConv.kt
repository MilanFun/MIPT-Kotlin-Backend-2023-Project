//package util
//
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
//import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
//import com.fasterxml.jackson.module.kotlin.readValue
//import com.fasterxml.jackson.module.kotlin.registerKotlinModule
//import entity.Posts
//
//class JSONConv {
//    companion object {
//        fun convert(text: String) : Map<String, String> {
//            val objectMapper = jacksonObjectMapper()
//            val data: Map<String, String> = objectMapper.readValue(text)
//            return data
//        }
//
//        fun deconvert(posts: List<Posts>) : String {
//            val objectMapper = jacksonObjectMapper().registerKotlinModule().registerModule(JavaTimeModule())
//            val jsonArray = objectMapper.writeValueAsString(posts)
//
//            return jsonArray
//        }
//
//        fun deobject(post: Posts) : String {
//            val objectMapper = jacksonObjectMapper().registerKotlinModule().registerModule(JavaTimeModule())
//            val json = objectMapper.writeValueAsString(post)
//
//            return json
//        }
//    }
//}