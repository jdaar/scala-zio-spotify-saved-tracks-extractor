package dev.jhonatan
package usecase

import model.exception.{
  ApplicationException,
  GatewayBadResponse,
  GatewayInternalException,
  GatewayTimeoutException,
  GatewayUnableToIdentify,
  GatewayUnableToProcessMessage,
  GatewayUnableToRetrieveMessage,
  GatewayUnknownException,
  NumericExceptionId
}
import util.ExceptionCodec
import service.httpclient.service.SpotifyService

import service.filesystem.FilesystemService
import zio.{ZIO, ZLayer}
import zio.http.Client

object UseCaseInterpreter {
  private def catchException[T](
      applicationException: ApplicationException[T]
  )(using
      handler: ExceptionCodec[T]
  ) =
    lazy val identity = handler
      .identity(applicationException)
    ZIO.fail(
      RuntimeException(
        "Error ".concat(identity._1.toString).concat(": ").concat(identity._2)
      )
    )

  private def matchException(applicationException: ApplicationException[_]) =
    applicationException match
      case ex @ GatewayTimeoutException()         => catchException(ex)
      case ex @ GatewayBadResponse()              => catchException(ex)
      case ex @ GatewayUnknownException(original) => catchException(ex)
      case ex @ GatewayUnableToRetrieveMessage(_) => catchException(ex)
      case ex @ GatewayUnableToProcessMessage(_)  => catchException(ex)
      case ex @ GatewayInternalException(_, _)    => catchException(ex)
      case ex @ GatewayUnableToIdentify()         => catchException(ex)

  def interpret[A](
      useCase: ZIO[SpotifyService & FilesystemService, ApplicationException[
        _
      ], A]
  ): ZIO[
    Any,
    RuntimeException,
    A
  ] =
    useCase
      .provide(
        Client.default,
        SpotifyService.layer,
        FilesystemService.layer
      )
      .catchAll({
        case exception: ApplicationException[_] =>
          matchException(exception)
        case exception =>
          ZIO
            .fail(
              RuntimeException(
                "Error "
                  .concat(NumericExceptionId(0L).toString)
                  .concat(": ")
                  .concat(exception.getMessage)
              )
            )
      })
}
