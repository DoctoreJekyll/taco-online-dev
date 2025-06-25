package com.lm2a.tacoonline.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MyErrorController implements ErrorController {

    final ErrorAttributes errorAttributes;
    final WebRequest webRequest;

    @RequestMapping("/error")
    public String handleError(WebRequest request, Model model) {
        log.error("Fallo de la app");
        Map<String, Object> error = errorAttributes.getErrorAttributes(webRequest, ErrorAttributeOptions.of(ErrorAttributeOptions.defaults().getIncludes()));

        model.addAttribute("msg", error.get("error"));
        model.addAttribute("url", error.get("path"));

        return "error";
    }

}
