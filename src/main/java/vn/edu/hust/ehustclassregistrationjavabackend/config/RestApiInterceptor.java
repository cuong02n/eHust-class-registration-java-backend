package vn.edu.hust.ehustclassregistrationjavabackend.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.*;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class RestApiInterceptor implements HandlerInterceptor {
    String debugFilePath;
    PrintStream printStream;
    int requestStt = 0;

    RestApiInterceptor(@Value("${debug.file.path}") String debugFilePath) {
        this.debugFilePath = debugFilePath;
        new File(debugFilePath);
        try {
            new PrintWriter(debugFilePath).close();

            printStream = new PrintStream(new FileOutputStream(debugFilePath, true));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        request.setAttribute("xxx-start-time-xxx", System.currentTimeMillis());
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    String[] contentTypeAllowedForLogging = {"application/json", "application/x-www-form-urlencoded", "text/plain"};

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex) throws Exception {
        printStream.println("---------------------------------------------------------");
        printStream.println("The request #" + ++requestStt);
        printStream.println("\tIp: " + request.getRemoteAddr() + ", " + request.getHeader("X-Forwarded-For"));
        printStream.println("\tUrl: " + request.getMethod() + " " + request.getRequestURI());
        String contentType = request.getHeader("Content-Type");
//        if (contentType != null && Arrays.asList(contentTypeAllowedForLogging).contains(contentType)) {
//            printStream.println("\tContent Length: " + request.getContentLength() + "; " + request.getContentLengthLong());
//            printStream.println("\tContent:\n" + request.getReader().lines().collect(Collectors.joining(System.lineSeparator())));
//        }else{
//            printStream.println("\tRequest Content wasn't displayed because not readable");
//        }

        printStream.println("Response: " + response.getStatus());
        printStream.println("\tThis request took " + ((System.currentTimeMillis() - (long) request.getAttribute("xxx-start-time-xxx")) / 1000.0) + " to complete");
    }
}
