package dev.jhonatan
package service.httpclient.service

import model.exception.{
  ApplicationException,
  GatewayInternalException,
  GatewayUnableToIdentify,
  GatewayUnableToProcessMessage,
  GatewayUnableToRetrieveMessage,
  GatewayUnknownException
}
import service.httpclient.dto.{AccessToken, Error, GetUserSavedTracks}

import zio.{Config, Console, IO, ZIO, ZLayer}
import zio.http.{Body, Client, Header, Request, URL}
import zio.config.magnolia.deriveConfig
import zio.json.*
import zio.http.Header.Authorization.Bearer
import zio.schema.codec.JsonCodec.schemaBasedBinaryCodec

import scala.util.Try

trait SpotifyService(
    client: Client,
    config: SpotifyService.SpotifyConfig
) {
  def getUserSavedTracks(
      next: Option[String] = None
  ): IO[ApplicationException[_], GetUserSavedTracks]
}

object SpotifyService {
  final case class SpotifyConfig(
      getTokenEndpoint: String,
      getUserSavedTracksEndpoint: String,
      token: String
  )

  private lazy val config = deriveConfig[SpotifyConfig]
    .nested("spotify")

  lazy val layer: ZLayer[Client, Config.Error, SpotifyServiceImpl] = ZLayer {
    for {
      client <- ZIO.service[Client]
      config <- ZIO.config[SpotifyConfig](config)
    } yield SpotifyServiceImpl(
      client,
      config
    )
  }
}

case class SpotifyServiceImpl(
    client: Client,
    config: SpotifyService.SpotifyConfig
) extends SpotifyService(client, config) {
  override def getUserSavedTracks(
      next: Option[String] = None
  ): IO[ApplicationException[_], GetUserSavedTracks] =
    for {
      token <- getToken
      res <- client
        .url(
          URL
            .decode(next.getOrElse(config.getUserSavedTracksEndpoint))
            .toOption
            .get
        )
        .batched(
          Request
            .get("/")
            .addHeader(
              Bearer(token.accessToken)
            )
        )
        .catchAll { exception =>
          ZIO.fail(GatewayUnableToRetrieveMessage(exception))
        }
      data <-
        if (
          res.status.isError && res.status.code >= 400 && res.status.code <= 499
        )
          ZIO.fail(GatewayUnableToIdentify())
        else if (res.status.isError)
          res.body
            .to[Error]
            .map(error =>
              GatewayInternalException(error.error, error.errorDescription)
            )
            .flatMap(ZIO.fail)
            .catchAll { exception =>
              ZIO.fail(GatewayUnableToProcessMessage(exception))
            }
        else
          res.body
            .to[GetUserSavedTracks]
            .catchAll { exception =>
              ZIO.fail(GatewayUnableToProcessMessage(exception))
            }
    } yield data

  private def getToken: IO[Nothing, AccessToken] = ZIO
    .succeed(
      Right(
        AccessToken(
          config.token,
          "Bearer",
          0
        )
      )
    )
    .flatMap(ZIO.fromEither)
}
