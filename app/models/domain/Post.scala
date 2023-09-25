package models.domain

import java.util.UUID

import play.api.data.Forms._
import play.api.data.Form
import scala.collection.mutable.ListBuffer

case class Post(id: UUID = UUID.randomUUID, photoID: UUID = UUID.randomUUID, description: Option[String])

