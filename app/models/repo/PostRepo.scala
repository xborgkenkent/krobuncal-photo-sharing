package models.repo

import javax.inject._
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile
import models.domain.Post
import java.util.UUID

@Singleton
class PostRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {
 import slick.jdbc.PostgresProfile.api._

 class PostTable(tag: Tag) extends Table[Post](tag, "POSTS") {
    def id = column[UUID]("ID", O.PrimaryKey)
    def photoId = column[UUID]("PHOTO_ID")
    def description = column[Option[String]]("DESCRIPTION", O.Length(255, true))

    def * = (id, photoId, description).mapTo[Post]
 }

 val table = TableQuery[PostTable]

 def createTable = db.run(table.schema.create)

 def insert(post: Post) = db.run(table returning table.map(_.id) += post)

 def get() = db.run(table.result)
}
