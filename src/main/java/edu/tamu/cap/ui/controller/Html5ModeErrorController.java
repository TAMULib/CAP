package edu.tamu.cap.ui.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

// TODO: this most likely can move into the framework
@RestController
public class Html5ModeErrorController implements ErrorController {

    private static final String PATH = "/error";

    @RequestMapping(value = PATH)
    public ModelAndView error(HttpServletRequest request, HttpServletResponse response) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == HttpServletResponse.SC_BAD_REQUEST || statusCode == HttpServletResponse.SC_FORBIDDEN) {
            response.setStatus(statusCode);
        } else if (request.getHeader("X-Requested-With") == null) {
            response.setStatus(HttpServletResponse.SC_OK);
        }
        return ViewController.index(request);
    }

}
