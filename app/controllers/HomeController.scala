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

import models.Image
import models.Post
import models.Post._
import scala.collection.mutable.ListBuffer
import play.api.i18n.I18nSupport

@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents) extends BaseController with I18nSupport{

    //val pictureBuffer = collection.mutable.Map.empty[String, Array[Byte]]
    val posts: ListBuffer[Post] = ListBuffer.empty
    val images: ListBuffer[Image] = ListBuffer.empty

    def uploadPhoto() = Action(parse.multipartFormData) { implicit request =>
       
        try {
            val id = UUID.randomUUID

            formPost.bindFromRequest().fold(
                formWithErrors => BadRequest("formWithErrors"),
                data => {posts += Post(photoID = id, description = data.description, comments = List(""))
                Redirect(routes.HomeController.index())}
            )
            val temporaryFile = request.body.file("picture").get
            

            val extension = temporaryFile.filename.split("\\.+").toList.last
            val fileBytes = Files.readAllBytes(Paths.get(temporaryFile.ref.file.getAbsolutePath))

            //pictureBuffer(id.toString) = fileBytes
            val imageInfo = Image(id, fileBytes, extension)
            
            images += Image(id, fileBytes, extension)

            println(posts, images)
            Redirect(routes.HomeController.index())
        }catch {
            case _ => BadRequest
        }

  }

    def getPhoto(id: String) = Action { request =>
        val image = images.find(img => (img.id.toString==id))
        image match {
            case Some(value) => Ok(value.imageByte).as(s"image/${value.extension}")
            case None => Ok("not found")
        }
    }

    def addComment(id: String) = Action { implicit request =>
        formPost.bindFromRequest.fold(
            formWithErrors => BadRequest("failed"),
            data => {
                val post = posts.find(_.id.toString==id).get
                println(data.comments, data.description, data.id, data.photoID)
                val comment = post.comments.concat(request.body.asFormUrlEncoded.get("comments"))
                posts.update(posts.indexOf(post), Post(id = data.id, photoID = data.photoID, description = data.description ,comments = comment.toList))
                Redirect(routes.HomeController.index())
            }
        )
    }




  def index() = Action { implicit request =>
    Ok(views.html.index(posts.toList, formPost))
  }
}