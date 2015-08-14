package pmhsfelix.kotlinfirst

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

fun main(args:Array<String>) {
    val logger = LoggerFactory.getLogger("main");
    logger.info("main started");
    val portDef = System.getenv("PORT");
    val port = if (portDef != null) Integer.valueOf(portDef) else 8080;
    val server = Server(port);
    val handler = ServletHandler();
    server.setHandler(handler);
    handler.addServletWithMapping(ServletHolder(HelloServlet()), "/*");
    server.start();
    logger.info("server started");
}
