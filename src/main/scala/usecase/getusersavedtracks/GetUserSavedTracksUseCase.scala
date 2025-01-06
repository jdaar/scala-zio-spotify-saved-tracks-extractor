package dev.jhonatan
package usecase.getusersavedtracks

import model.exception.{ApplicationException, GatewayUnknownException}
import service.httpclient.service.SpotifyService
import service.httpclient.dto.GetUserSavedTracks
import model.Track

import service.filesystem.FilesystemService
import zio.{Console, IO, ZIO}

object GetUserSavedTracksUseCase {
  def apply: ZIO[SpotifyService & FilesystemService, ApplicationException[
    _
  ], List[Track]] =
    for {
      spotifyService <- ZIO.service[SpotifyService]
      filesystemService <- ZIO.service[FilesystemService]
      result <- iterateWhileAppendingTracksToOutputFile(
        spotifyService,
        filesystemService
      )
    } yield result

  private def iterateWhileAppendingTracksToOutputFile(
      spotifyService: SpotifyService,
      filesystemService: FilesystemService,
      accumulated: List[Track] = List(),
      next: Option[String] = None
  ): IO[ApplicationException[_], List[Track]] = spotifyService
    .getUserSavedTracks(next)
    .flatMap(result =>
      ZIO
        .succeed(result)
        .map(v => v.items.map(dtoToModel))
        .tap(v => filesystemService.appendStdout(v))
        .flatMap(v =>
          result.next match
            case Some(value) =>
              iterateWhileAppendingTracksToOutputFile(
                spotifyService,
                filesystemService,
                accumulated ++ v,
                result.next
              )
            case None => ZIO.succeed(accumulated ++ v)
        )
    )

  private def dtoToModel(userSavedTrack: GetUserSavedTracks.UserSavedTrack) =
    Track(
      userSavedTrack.track.album.name,
      userSavedTrack.track.artists.map(_.name),
      userSavedTrack.track.diskNumber,
      userSavedTrack.track.explicit,
      userSavedTrack.track.durationMs,
      userSavedTrack.track.name,
      userSavedTrack.track.popularity,
      userSavedTrack.track.trackNumber,
      userSavedTrack.track.spotifyUrl
    )
}
