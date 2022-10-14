package icu.harx

import io.vertx.core.AbstractVerticle
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.StaticHandler
import io.vertx.ext.web.handler.sockjs.SockJSBridgeOptions
import io.vertx.ext.web.handler.sockjs.SockJSHandler
import io.vertx.ext.bridge.PermittedOptions

object Server extends AbstractVerticle:

  override def start(): Unit =
    val server = vertx.createHttpServer()

    val router = Router.router(vertx);

    val permitted = new PermittedOptions()
    permitted.setAddress("chatroom")

    val sockJSHandler = SockJSHandler.create(vertx)
    val options = new SockJSBridgeOptions()
    options.addInboundPermitted(permitted)
    options.addOutboundPermitted(permitted)

    router
      .route("/eventbus/*")
      .subRouter(sockJSHandler.bridge(options))

    router
      .route()
      .handler(StaticHandler.create())

    server
      .requestHandler(router)
      .listen(8080)

  def main(args: Array[String]): Unit =
    Vertx
      .vertx()
      .deployVerticle(Server)
