package database

//import entity.Posts
//import org.ktorm.database.Database
//import org.ktorm.database.SqlDialect
//import org.ktorm.schema.Table
//import org.ktorm.schema.int
//import org.ktorm.schema.timestamp
//import org.ktorm.schema.varchar
//import org.ktorm.support.mysql.MySqlDialect
//import javax.swing.text.html.parser.Entity
//
//object DatabaseConfig {
//    private const val jdbcUrl = "jdbc:mysql://localhost:3306/posts"
//    private const val dbUser = "root"
//    private const val dbPassword = "password"
//
//    val database = Database.connect(url = jdbcUrl,
//                                    password = dbPassword,
//                                    user = dbUser,
//                                    driver = "com.mysql.jdbc.Driver")
//}
//
//object BlogPost : Table<Posts>(tableName = "posts") {
//    val id = int("id").primaryKey().bindTo { it.id }
//    val post = varchar("post").bindTo { it.post }
//    val created = timestamp("created").bindTo { it.created }
//    val updated = timestamp("updated").bindTo { it.updated }
//}