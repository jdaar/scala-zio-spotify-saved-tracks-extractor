package dev.jhonatan
package service.httpclient.dto

import zio.json.{
  JsonCodec,
  SnakeCase,
  jsonField,
  jsonHintNames,
  jsonMemberNames
}
import zio.schema.annotation.fieldName
import zio.schema.codec.BinaryCodec
import zio.schema.codec.JsonCodec.schemaBasedBinaryCodec
import zio.schema.{DeriveSchema, Schema}

case class GetUserSavedTracks(
    @fieldName("href") href: String,
    @jsonField("items") items: List[GetUserSavedTracks.UserSavedTrack],
    @fieldName("limit") limit: Long,
    @fieldName("next") next: Option[String],
    @fieldName("offset") offset: Long,
    @fieldName("previous") previous: Option[String],
    @fieldName("total") total: Long
)

object GetUserSavedTracks {
  implicit val schema: Schema[GetUserSavedTracks] = DeriveSchema.gen
  implicit val codec: BinaryCodec[GetUserSavedTracks] = schemaBasedBinaryCodec

  case class UserSavedTrack(
      @fieldName("added_at") addedAt: String,
      @fieldName("track") track: Track
  )

  case class Track(
      @fieldName("album") album: Album,
      @fieldName("artists") artists: List[Artist],
      @fieldName("disc_number") diskNumber: Double,
      @fieldName("explicit") explicit: Boolean,
      @fieldName("duration_ms") durationMs: Double,
      @fieldName("name") name: String,
      @fieldName("popularity") popularity: Double,
      @fieldName("track_number") trackNumber: Double,
      @fieldName("href") spotifyUrl: String
  )

  case class Album(
      @fieldName("album_type") albumType: String,
      @fieldName("total_tracks") totalTracks: Double,
      @fieldName("name") name: String,
      @fieldName("release_date") releaseDate: String,
      @fieldName("release_date_precision") releaseDatePrecision: String
  )

  case class Artist(
      @fieldName("name") name: String,
      @fieldName("href") spotifyUrl: String
  )
}
