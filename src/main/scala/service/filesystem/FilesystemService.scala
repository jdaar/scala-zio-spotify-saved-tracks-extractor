package dev.jhonatan
package service.filesystem

import model.exception.{ApplicationException, GatewayUnknownException}
import model.Track

import zio.{Console, IO, ZIO, ZLayer}
import zio.Config
import zio.config.magnolia.deriveConfig
import zio.json.{DeriveJsonCodec, JsonCodec}
import zio.schema.{DeriveSchema, Schema}
import zio.json.EncoderOps

import java.io.File

trait FilesystemService(config: FilesystemService.FilesystemConfig) {
  def appendStdout(tracks: List[Track]): IO[ApplicationException[_], Unit]
}

object FilesystemService {
  final case class FilesystemConfig(separator: String)
  private lazy val config: Config[FilesystemConfig] = deriveConfig

  lazy val layer: ZLayer[Any, Config.Error, FilesystemService] = ZLayer {
    for {
      config <- ZIO.config[FilesystemConfig](config)
    } yield FilesystemServiceImpl(config)
  }
}

class FilesystemServiceImpl(config: FilesystemService.FilesystemConfig)
    extends FilesystemService(config) {
  lazy implicit val schema: Schema[Track] = DeriveSchema.gen[Track]
  lazy implicit val codec: JsonCodec[Track] = DeriveJsonCodec.gen

  override def appendStdout(
      tracks: List[Track]
  ): IO[ApplicationException[_], Unit] =
    Console
      .printLine(tracks.map(_.toJson).reduce(_ + config.separator + _))
      .catchAll { exception => ZIO.fail(GatewayUnknownException(exception)) }
}
