package dev.jhonatan
package util

import model.exception.{
  ApplicationException,
  ExceptionId,
  GatewayBadResponse,
  GatewayInternalException,
  GatewayTimeoutException,
  GatewayUnableToIdentify,
  GatewayUnableToProcessMessage,
  GatewayUnableToRetrieveMessage,
  GatewayUnknownException,
  NumericExceptionId
}

trait ExceptionCodec[E] {
  def identity(exception: ApplicationException[E]): (ExceptionId, String)
}

object ExceptionCodec {
  given ExceptionCodec[GatewayTimeoutException] with {
    override def identity(
        exception: ApplicationException[GatewayTimeoutException]
    ) = (
      NumericExceptionId(1L),
      "Service timed out"
    )
  }

  given ExceptionCodec[GatewayBadResponse] with {
    override def identity(
        exception: ApplicationException[GatewayBadResponse]
    ) = (
      NumericExceptionId(2L),
      "Service served a bad response"
    )
  }

  given ExceptionCodec[GatewayUnknownException] with {
    override def identity(
        exception: ApplicationException[GatewayUnknownException]
    ) = (
      NumericExceptionId(3L),
      "Service had an unknown exception { "
        .concat(exception.complement.get)
        .concat(" }")
    )
  }

  given ExceptionCodec[GatewayUnableToProcessMessage] with {
    override def identity(
        exception: ApplicationException[GatewayUnableToProcessMessage]
    ) = (
      NumericExceptionId(4L),
      "Service couldn't process message { "
        .concat(exception.complement.get)
        .concat(" }")
    )
  }

  given ExceptionCodec[GatewayUnableToRetrieveMessage] with {
    override def identity(
        exception: ApplicationException[GatewayUnableToRetrieveMessage]
    ) = (
      NumericExceptionId(5L),
      "Service couldn't retrieve message { "
        .concat(exception.complement.get)
        .concat(" }")
    )
  }

  given ExceptionCodec[GatewayInternalException] with {
    override def identity(
        exception: ApplicationException[GatewayInternalException]
    ) = (
      NumericExceptionId(6L),
      "Service had an internal error { "
        .concat(exception.complement.get)
        .concat(" }")
    )
  }

  given ExceptionCodec[GatewayUnableToIdentify] with {
    override def identity(
        exception: ApplicationException[GatewayUnableToIdentify]
    ) = (
      NumericExceptionId(7L),
      "Service couldn't identify itself"
    )
  }
}
