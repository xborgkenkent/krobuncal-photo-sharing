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
class HomeController @Inject()(val postRepo: PostRepo, val imageRepo: ImageRepo, val commentRepo: CommentRepo, val controllerComponents: ControllerComponents)(implicit ec:ExecutionContext) extends BaseController with I18nSupport{

    val formPost = Form(
    mapping(
        "id" -> default(uuid, UUID.randomUUID),
        "photoID" -> default(uuid,UUID.randomUUID),
        "description" -> optional(text),
    )(Post.apply)(Post.unapply))

    val commentForm = Form("comments" -> text)

    def index() = Action.async { implicit request => 
        for {
            _ <- postRepo.createTable
            _ <- imageRepo.createTable
            _ <- commentRepo.createTable
        } yield(Redirect(routes.HomeController.home()))
    }

    def home() = Action.async { implicit request =>
        for{
            posts <- postRepo.get()
            comments <- commentRepo.get()
        } yield(Ok(views.html.index(posts.toList, formPost, commentForm, comments.toList)))        
    }

}
