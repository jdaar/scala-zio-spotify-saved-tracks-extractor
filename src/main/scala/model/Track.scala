package dev.jhonatan
package model

import service.httpclient.dto.GetUserSavedTracks.Album

import zio.schema.annotation.fieldName

case class Track(
    albumName: String,
    artistNames: List[String],
    diskNumber: Double,
    explicit: Boolean,
    durationMs: Double,
    name: String,
    popularity: Double,
    trackNumber: Double,
    spotifyUrl: String
)
