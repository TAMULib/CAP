package edu.tamu.cap.controller;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resource-proxy")
public class ResourceProxyController {
    @RequestMapping(method = RequestMethod.GET)
    public void proxyResource(HttpServletResponse response, @RequestParam String uri) {
        URL sourceURL;
        try {
            sourceURL = new URL(uri);
            byte[] sourceBytes = new byte[1024];
            int sourceLength;
            try {

                URLConnection connection = sourceURL.openConnection();

                response.setContentType(connection.getContentType());

                //we want to display pdfs inline, not download
                if (connection.getContentType().equalsIgnoreCase("application/pdf")) {
                    response.setHeader("Content-Disposition", connection.getHeaderField("Content-Disposition").replace("attachment","inline"));
                } else {
                    response.setHeader("Content-Disposition", connection.getHeaderField("Content-Disposition"));
                }

                InputStream sourceData = sourceURL.openStream();

                while ((sourceLength = sourceData.read(sourceBytes)) != -1) {
                    response.getOutputStream().write(sourceBytes, 0, sourceLength);
                }
                sourceData.close();

                response.setContentLength(sourceBytes.length);

                response.getOutputStream().flush();
                response.getOutputStream().close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
}
