package dev.jhonatan
package model.exception

sealed trait ExceptionId

case class NumericExceptionId(id: Long) extends ExceptionId
