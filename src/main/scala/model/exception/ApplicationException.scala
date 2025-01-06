package dev.jhonatan
package model.exception

import java.io.{ByteArrayOutputStream, PrintStream}
import java.nio.charset.StandardCharsets

sealed trait ApplicationException[E] extends Exception {
  def complement: Option[String] = Option.empty

  protected def complementThrowable(original: Throwable): Option[String] =
    val stream = ByteArrayOutputStream()
    val printStream = PrintStream(stream)
    original.printStackTrace(printStream)
    val stackTrace = String(stream.toByteArray, StandardCharsets.UTF_8)
    Some(
      original.getClass.getName
        .concat(": ")
        .concat(original.getMessage)
        .concat("\n = = = STACK TRACE\n")
        .concat(stackTrace)
    )
}

case class GatewayTimeoutException()
    extends ApplicationException[GatewayTimeoutException]

case class GatewayBadResponse() extends ApplicationException[GatewayBadResponse]

case class GatewayUnknownException(original: Throwable)
    extends ApplicationException[GatewayUnknownException] {
  override def complement: Option[String] = complementThrowable(original)
}

case class GatewayUnableToRetrieveMessage(original: Throwable)
    extends ApplicationException[GatewayUnknownException] {
  override def complement: Option[String] = complementThrowable(original)
}

case class GatewayUnableToProcessMessage(original: Throwable)
    extends ApplicationException[GatewayUnknownException] {
  override def complement: Option[String] = complementThrowable(original)
}

case class GatewayInternalException(code: String, description: String)
    extends ApplicationException[GatewayUnknownException] {
  override def complement: Option[String] = Some(
    code.concat(": ").concat(description)
  )
}

case class GatewayUnableToIdentify()
    extends ApplicationException[GatewayUnableToIdentify]
