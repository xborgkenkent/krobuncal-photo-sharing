package models.repo

import javax.inject._
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile
import models.domain.Comment
import java.util.UUID

@Singleton
class CommentRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {
 import slick.jdbc.PostgresProfile.api._

 class CommentTable(tag: Tag) extends Table[Comment](tag, "COMMENTS") {
    def id = column[UUID]("ID", O.PrimaryKey)
    def postId = column[UUID]("POST_ID")
    def comment = column[String]("COMMENT", O.Length(255, true))

    def * = (id, postId, comment).mapTo[Comment]
 }

 val table = TableQuery[CommentTable]

 def createTable = db.run(table.schema.create)

 def insert(comment: Comment) = db.run(table returning table.map(_.id) += comment)

 def get() = db.run(table.result)
}
