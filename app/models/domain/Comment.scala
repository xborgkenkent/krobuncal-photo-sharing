package models.domain

import java.util.UUID

case class Comment(id: UUID, postId: UUID, comment: String)
