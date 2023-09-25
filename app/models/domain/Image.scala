package models.domain

import java.util.UUID

case class Image(id: UUID, imageByte: Array[Byte], extension: String)
