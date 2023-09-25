package models.repo

import javax.inject._
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile
import models.domain.Image
import java.util.UUID
import play.api.mvc.MultipartFormData
import java.nio.file.{Files, Paths}
import java.io.File

@Singleton
class ImageRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {
 import slick.jdbc.PostgresProfile.api._

 class ImageTable(tag: Tag) extends Table[Image](tag, "IMAGES") {
    def id = column[UUID]("ID", O.PrimaryKey)
    def imageByte = column[Array[Byte]]("IMAGE_BYTE")
    def extension = column[String]("EXTENSION", O.Length(255, true))

    def * = (id, imageByte, extension).mapTo[Image]
 }

 val table = TableQuery[ImageTable]

 def createTable = db.run(table.schema.create)

 def tempFile(id: UUID, temporaryFile: MultipartFormData.FilePart[play.api.libs.Files.TemporaryFile]) = {
    val extension = temporaryFile.filename.split("\\.+").toList.last
    val fileBytes = Files.readAllBytes(Paths.get(temporaryFile.ref.file.getAbsolutePath))
    val image = Image(id, fileBytes, extension)

    insert(image)
 }

 def insert(image: Image) = db.run(table returning table.map(_.id) += image)

 def get(id: UUID) = db.run(table.filter(_.id===id).result.head)


}
