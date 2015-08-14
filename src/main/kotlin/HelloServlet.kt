package pmhsfelix.kotlinfirst

import java.io.OutputStream;
import java.nio.charset.Charset;
import org.joda.time.DateTime;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

val logger = LoggerFactory.getLogger("HelloServlet");

class HelloServlet : HttpServlet() {

    override fun doGet(req:HttpServletRequest, res:HttpServletResponse) {

        logger.info("'{}' on '{}' with accept:'{}'",
            req.getMethod(),
            req.getRequestURI(),
            req.getHeader("Accept")
            );

        val utf8 = Charset.forName("utf-8");
        res.setContentType("text/plain; charset=${utf8.name()}");
        
        val respBody = "Hello Web, from Kotlin (current date and time is ${DateTime()})\n";        
        val respBodyBytes = respBody.toByteArray(utf8); 
        res.setStatus(200);
        res.setContentLength(respBodyBytes.size());        
        res.getOutputStream().use {
            it.write(respBodyBytes);    
        }        
    }
}
