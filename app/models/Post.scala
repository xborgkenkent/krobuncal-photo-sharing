package models

import java.util.UUID

import play.api.data.Forms._
import play.api.data.Form
import scala.collection.mutable.ListBuffer

case class Post(id: UUID = UUID.randomUUID, photoID: UUID = UUID.randomUUID, description: Option[String] = None, var comments: List[String])

object Post {
    val formPost = Form(
        mapping(
            "id" -> default(uuid, UUID.randomUUID),
            "photoID" -> default(uuid,UUID.randomUUID),
            "description" -> optional(text),
            "comments" -> default(list(text),List(""))
        )(Post.apply)(Post.unapply))

    
}
