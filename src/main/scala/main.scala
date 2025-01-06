package dev.jhonatan

import usecase.UseCaseInterpreter
import usecase.getusersavedtracks.GetUserSavedTracksUseCase

import zio.config.typesafe.TypesafeConfigProvider
import zio.{Console, ExitCode, Runtime, URIO, ZIOAppArgs, ZIOAppDefault, ZLayer}

object App extends ZIOAppDefault {
  override val bootstrap: ZLayer[ZIOAppArgs, Any, Any] =
    Runtime.setConfigProvider(
      TypesafeConfigProvider
        .fromResourcePath()
    )

  override val run: URIO[Any, ExitCode] = (for {
    data <- UseCaseInterpreter
      .interpret(
        GetUserSavedTracksUseCase.apply
      )
      .catchAll(exception => Console.printError(exception))
  } yield ()).exitCode
}
