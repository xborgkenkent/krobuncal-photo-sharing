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
import models.domain.Comment

@Singleton
class CommentController @Inject()(val postRepo: PostRepo, val imageRepo: ImageRepo, val commentRepo: CommentRepo, val controllerComponents: ControllerComponents)(implicit ec:ExecutionContext) extends BaseController with I18nSupport{

    val commentForm = Form("comments" -> text)

    def addComment(postid: String) = Action.async { implicit request =>
        commentForm.bindFromRequest().fold(
            formWithErrors => Future(BadRequest),
            comment => {
                commentRepo.insert(Comment(id = UUID.randomUUID(), postId = UUID.fromString(postid), comment)).map(_ => 
                    Redirect(routes.HomeController.home()))
            }
        )
    }

}

