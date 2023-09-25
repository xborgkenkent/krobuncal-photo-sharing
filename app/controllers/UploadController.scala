package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import play.i18n._
import models.repo.PostRepo
import models.repo.ImageRepo
import models.repo.CommentRepo
import play.api.i18n.I18nSupport
import play.api.data.Forms._
import play.api.data.Form
import java.util.UUID
import models.domain.Post

@Singleton
class UploadController @Inject()(val postRepo: PostRepo, val imageRepo: ImageRepo, val commentRepo: CommentRepo, val controllerComponents: ControllerComponents)(implicit ec:ExecutionContext) extends BaseController with I18nSupport{

    val formPost = Form(
    mapping(
        "id" -> default(uuid, UUID.randomUUID),
        "photoID" -> default(uuid,UUID.randomUUID),
        "description" -> optional(text),
    )(Post.apply)(Post.unapply))

    def uploadPhoto() = Action(parse.multipartFormData) { implicit request =>
        try {
            formPost.bindFromRequest().fold(
                formWithErrors => BadRequest("formWithErrors"),
                data => {
                    val id = UUID.randomUUID
                    val post = Post(id = id, photoID = id, description = data.description)
                    request.body.file("picture") match {
                        case Some(value) => {
                            for{
                                _ <- postRepo.insert(post)
                                _ <- imageRepo.tempFile(id, value)
                            } yield(Ok)
                        }
                        case None => None
                    }
                }
            )
            Redirect(routes.HomeController.home())
        }catch {
            case _ => BadRequest
        }

  }

}

