package com.lm2a.tacoonline.web;

import jakarta.servlet.http.HttpServletRequest;
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

@Slf4j
@Controller
@RequiredArgsConstructor
public class MyErrorController implements ErrorController {

    private final ErrorAttributes errorAttributes;
    private final WebRequest webRequest;

    @RequestMapping("/no")
    public String handleError(WebRequest request, Model model) {
        log.error("Fallo de la app");
        Map<String, Object> errorAttributesMap = errorAttributes.getErrorAttributes(webRequest, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.STACK_TRACE, ErrorAttributeOptions.Include.PATH, ErrorAttributeOptions.Include.ERROR));
        model.addAttribute("msg", errorAttributesMap.get("error"));
        model.addAttribute("url", errorAttributesMap.get("path"));
        return "error";
    }
}
