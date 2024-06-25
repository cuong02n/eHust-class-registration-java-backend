package vn.edu.hust.ehustclassregistrationjavabackend.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;
import vn.edu.hust.ehustclassregistrationjavabackend.utils.BaseResponse;
import vn.edu.hust.ehustclassregistrationjavabackend.utils.GsonUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class CustomExceptionResolver extends DefaultHandlerExceptionResolver {
    Logger logger = LoggerFactory.getLogger(CustomExceptionResolver.class);
    @Override
    protected ModelAndView doResolveException(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, Object handler, Exception ex) {
        logger.error("{}: {}",request.getRequestURI(), ex.getMessage());
        try {
            if (ex instanceof ErrorResponse errorResponse) {
                return this.handleErrorResponse(errorResponse, request, response, handler);
            }

            sendServerError(ex, request, response);
        } catch (Exception handlerEx) {
            if (logger.isWarnEnabled()) {
                logger.warn("Failure while trying to resolve exception [" + ex.getClass().getName() + "]", handlerEx);
            }
        }

        return new ModelAndView();
    }

    @Override
    protected void sendServerError(@NonNull Exception ex, @NonNull HttpServletRequest request, @NonNull HttpServletResponse response) throws IOException {

        if (response.isCommitted()) {
            return;
        }
        String message = null;
        int statusCode = 400;
        if (ex instanceof ErrorResponse errorResponse) {
            message = errorResponse.getDetailMessageCode();
        } else if (ex instanceof DataIntegrityViolationException) {
            message = "Cannot process your requests, maybe there is one of your requests not exist! ";
        } else if (ex instanceof MessageException me) {
            message = me.getMessage();
            statusCode = me.httpStatus.value();
        } else if (ex instanceof AccessDeniedException) {
            message = "You do not have permission to access this resource";
            statusCode = 403;
        }else if(ex instanceof NullPointerException n){
            message = "Yêu cầu không hợp lệ: "+n.getMessage();
            statusCode = 400;
        }else if(ex instanceof HttpMessageNotReadableException hmne){
            message = hmne.getMessage();
            statusCode = 400;
        }


        if (statusCode == 400 && message == null) {
            statusCode = 500;
            message = ex.getMessage();
        }

        response.addHeader("content-type", "application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new String(GsonUtil.gsonExpose.toJsonTree(new BaseResponse.ErrorResponse(statusCode, message)).toString().getBytes(StandardCharsets.UTF_8)));
        response.setStatus(statusCode);
    }

    @Override
    protected @NonNull ModelAndView handleErrorResponse(ErrorResponse errorResponse, @NonNull HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        response.setStatus(errorResponse.getStatusCode().value());
        response.addHeader("content-type", "application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(GsonUtil.gsonExpose.toJson(new BaseResponse.ErrorResponse(errorResponse.getStatusCode().value(), errorResponse.getBody().getDetail())));
        return new ModelAndView();
    }
}