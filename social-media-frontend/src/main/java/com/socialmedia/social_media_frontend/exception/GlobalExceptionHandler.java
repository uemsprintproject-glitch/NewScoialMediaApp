package com.socialmedia.social_media_frontend.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ControllerAdvice(annotations = Controller.class)
public class GlobalExceptionHandler {

    private static final Pattern MESSAGE_PATTERN = Pattern.compile("\\\"message\\\"\\s*:\\s*\\\"(.*?)\\\"");

    @ExceptionHandler(HttpStatusCodeException.class)
    public String handleHttpStatusCodeException(HttpStatusCodeException ex, HttpServletRequest request, Model model) {
        int statusCode = ex.getStatusCode().value();
        model.addAttribute("status", statusCode);
        model.addAttribute("errorMessage", extractBackendMessage(ex));
        model.addAttribute("path", request.getRequestURI());
        return "error";
    }

    @ExceptionHandler(ResponseStatusException.class)
    public String handleResponseStatusException(ResponseStatusException ex, HttpServletRequest request, Model model) {
        model.addAttribute("status", ex.getStatusCode().value());
        String reason = ex.getReason();
        model.addAttribute("errorMessage", reason == null || reason.isBlank() ? "Request failed" : reason);
        model.addAttribute("path", request.getRequestURI());
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleAnyException(Exception ex, HttpServletRequest request, Model model) {
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        model.addAttribute("errorMessage", ex.getMessage() == null ? "Unexpected error occurred" : ex.getMessage());
        model.addAttribute("path", request.getRequestURI());
        return "error";
    }

    private String extractBackendMessage(HttpStatusCodeException ex) {
        String responseBody = ex.getResponseBodyAsString();
        if (responseBody != null && !responseBody.isBlank()) {
            Matcher matcher = MESSAGE_PATTERN.matcher(responseBody);
            if (matcher.find()) {
                String parsedMessage = matcher.group(1).replace("\\\"", "\"");
                if (!parsedMessage.isBlank()) {
                    return parsedMessage;
                }
            }

            return responseBody;
        }

        String reason = ex.getStatusText();
        if (reason != null && !reason.isBlank()) {
            return reason;
        }

        return "Request failed";
    }
}
