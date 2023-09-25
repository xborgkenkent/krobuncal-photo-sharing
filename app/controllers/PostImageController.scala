package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.json.Json
import play.api.libs.functional.syntax._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import java.util.UUID

import play.i18n._

import java.nio.file.{Files, Paths}
import java.io.File
import java.io.FileOutputStream
import models.domain.Image
import models.domain.Post
import play.api.data.Forms._
import play.api.data.Form
import scala.collection.mutable.ListBuffer
import play.api.i18n.I18nSupport
import models.repo.PostRepo
import models.repo.ImageRepo
import models.repo.CommentRepo
import models.domain.Comment

@Singleton
class PostImageController @Inject()(
    val postRepo: PostRepo,
    val imageRepo: ImageRepo,
    val commentRepo: CommentRepo,
    val controllerComponents: ControllerComponents)(implicit ec:ExecutionContext) extends BaseController with I18nSupport{

    val formPost = Form(
        mapping(
            "id" -> default(uuid, UUID.randomUUID),
            "photoID" -> default(uuid,UUID.randomUUID),
            "description" -> optional(text),
        )(Post.apply)(Post.unapply))

    val commentForm = Form("comments" -> text)
    


//     def uploadPhoto() = Action(parse.multipartFormData) { implicit request =>
//         try {
//             formPost.bindFromRequest().fold(
//                 formWithErrors => BadRequest("formWithErrors"),
//                 data => {
//                     val id = UUID.randomUUID
//                     val post = Post(id = id, photoID = id, description = data.description)
//                     request.body.file("picture") match {
//                         case Some(value) => {
//                             for{
//                                 _ <- postRepo.insert(post)
//                                 _ <- imageRepo.tempFile(id, value)
//                             } yield(Ok)
//                         }
//                         case None => None
//                     }
//                 }
//             )
            
//             Redirect(routes.PostImageController.home())
//         }catch {
//             case _ => BadRequest
//         }

//   }

    def getPhoto(id: String) = Action.async { request =>
        imageRepo.get(UUID.fromString(id)).map{im => 
            Ok(im.imageByte).as(s"image/${im.extension}")
        }
    }

    // def addComment(postid: String) = Action.async { implicit request =>
    //     commentForm.bindFromRequest().fold(
    //         formWithErrors => Future(BadRequest),
    //         comment => {
    //             commentRepo.insert(Comment(id = UUID.randomUUID(), postId = UUID.fromString(postid), comment)).map(_ => 
    //                 Redirect(routes.PostImageController.home()))
    //         }
    //     )
    // }

    // def index() = Action.async { implicit request => 
    //     for {
    //         _ <- postRepo.createTable
    //         _ <- imageRepo.createTable
    //         _ <- commentRepo.createTable
    //     } yield(Redirect(routes.PostImageController.home()))
    // }

    // def home() = Action.async { implicit request =>
    //     for{
    //         posts <- postRepo.get()
    //         comments <- commentRepo.get()
    //     } yield(Ok(views.html.index(posts.toList, formPost, commentForm, comments.toList)))        
    // }
}