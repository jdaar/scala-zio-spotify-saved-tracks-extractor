package dev.jhonatan
package service.httpclient.dto

import zio.schema.annotation.fieldName
import zio.schema.codec.BinaryCodec
import zio.schema.codec.JsonCodec.schemaBasedBinaryCodec
import zio.schema.{DeriveSchema, Schema}

case class Error(
    @fieldName("error") error: String,
    @fieldName("error_description") errorDescription: String
)

object Error {
  implicit val schema: Schema[Error] = DeriveSchema.gen
  implicit val codec: BinaryCodec[Error] = schemaBasedBinaryCodec
}
