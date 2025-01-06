package dev.jhonatan
package service.httpclient.dto

import zio.schema.annotation.fieldName
import zio.schema.codec.BinaryCodec
import zio.schema.codec.JsonCodec.schemaBasedBinaryCodec
import zio.schema.{DeriveSchema, Schema}

case class AccessToken(
    @fieldName("access_token") accessToken: String,
    @fieldName("token_type") tokenType: String,
    @fieldName("expires_in") expiresIn: Long
)

object AccessToken {
  implicit val schema: Schema[AccessToken] = DeriveSchema.gen[AccessToken]
  implicit val codec: BinaryCodec[AccessToken] = schemaBasedBinaryCodec
}
