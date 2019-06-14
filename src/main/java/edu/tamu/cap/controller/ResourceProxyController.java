package edu.tamu.cap.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.cap.model.repo.RepositoryViewRepo;

@RestController
@RequestMapping("/resource-proxy")
public class ResourceProxyController {
    @Autowired
    RepositoryViewRepo repositoryViewRepo;

    /**
     * Streaming proxy for resources contained in RepositoryViews associated with a Cap instance Serves PDFs inline instead of as an attachment, acts as a pass-through for other types
     *
     * @param String uri
     *
     */
    @RequestMapping(method = RequestMethod.GET)
    public void proxyResource(HttpServletResponse response, @RequestParam String uri) {
        URL sourceURL;
        try {
            sourceURL = new URL(uri);

            // only proxy for domains associated with existing RepositoryViews
            if (repositoryViewRepo.findByRootUriContainingIgnoreCase(sourceURL.getHost()).size() > 0) {
                byte[] sourceBytes = new byte[1024];
                int sourceLength;
                try {

                    URLConnection connection = sourceURL.openConnection();

                    response.setContentType(connection.getContentType());

                    // we want to display pdfs inline, not download
                    if (connection.getContentType().equalsIgnoreCase("application/pdf")) {
                        response.setHeader("Content-Disposition", connection.getHeaderField("Content-Disposition").replace("attachment", "inline"));
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
            } else {
                try {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Unknown Repository");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        } catch (MalformedURLException e2) {
            try {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad URI");
                e2.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
