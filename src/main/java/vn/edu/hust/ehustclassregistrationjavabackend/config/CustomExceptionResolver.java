package vn.edu.hust.ehustclassregistrationjavabackend.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.response.BaseResponse;
import vn.edu.hust.ehustclassregistrationjavabackend.utils.GsonUtil;

import java.io.IOException;

@Component
//@Order
public class CustomExceptionResolver extends DefaultHandlerExceptionResolver {

//    @Override
//    protected ModelAndView doResolveException(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, Object handler, @NonNull Exception ex) {
//        super.doResolveException(request,response,handler,ex);
//        try {
//            if (ex instanceof ErrorResponse errorResponse) {
////                HttpHeaders headers = errorResponse.getHeaders();
////                headers.forEach((name, values) -> values.forEach(value -> response.addHeader(name, value)));
//                System.out.println(errorResponse.getDetailMessageCode());
//            }
//        } catch (Exception e) {
//        }
//
//        return new ModelAndView();
//    }


    @Override
    protected ModelAndView doResolveException(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, Object handler, Exception ex) {
        try {
            // ErrorResponse exceptions that expose HTTP response details
            if (ex instanceof ErrorResponse errorResponse) {
//                ModelAndView mav = null;
//                if (ex instanceof HttpRequestMethodNotSupportedException theEx) {
//                    mav = handleHttpRequestMethodNotSupported(theEx, request, response, handler);
//                } else if (ex instanceof HttpMediaTypeNotSupportedException theEx) {
//                    mav = handleHttpMediaTypeNotSupported(theEx, request, response, handler);
//                } else if (ex instanceof HttpMediaTypeNotAcceptableException theEx) {
//                    mav = handleHttpMediaTypeNotAcceptable(theEx, request, response, handler);
//                } else if (ex instanceof MissingPathVariableException theEx) {
//                    mav = handleMissingPathVariable(theEx, request, response, handler);
//                } else if (ex instanceof MissingServletRequestParameterException theEx) {
//                    mav = handleMissingServletRequestParameter(theEx, request, response, handler);
//                } else if (ex instanceof MissingServletRequestPartException theEx) {
//                    mav = handleMissingServletRequestPartException(theEx, request, response, handler);
//                } else if (ex instanceof ServletRequestBindingException theEx) {
//                    mav = handleServletRequestBindingException(theEx, request, response, handler);
//                } else if (ex instanceof MethodArgumentNotValidException theEx) {
//                    mav = handleMethodArgumentNotValidException(theEx, request, response, handler);
//                } else if (ex instanceof HandlerMethodValidationException theEx) {
//                    mav = handleHandlerMethodValidationException(theEx, request, response, handler);
//                } else if (ex instanceof NoHandlerFoundException theEx) {
//                    mav = handleNoHandlerFoundException(theEx, request, response, handler);
//                } else if (ex instanceof NoResourceFoundException theEx) {
//                    mav = handleNoResourceFoundException(theEx, request, response, handler);
//                } else if (ex instanceof AsyncRequestTimeoutException theEx) {
//                    mav = handleAsyncRequestTimeoutException(theEx, request, response, handler);
//                }
                System.out.println("Error Response");
                return this.handleErrorResponse(errorResponse, request, response, handler);
            }

            // Other, lower level exceptions
//            System.out.println(ex.getMessage());
//            if (ex instanceof ConversionNotSupportedException theEx) {
//                 handleConversionNotSupported(theEx, request, response, handler);
//            } else if (ex instanceof TypeMismatchException theEx) {
//                return handleTypeMismatch(theEx, request, response, handler);
//            } else if (ex instanceof HttpMessageNotReadableException theEx) {
//                return handleHttpMessageNotReadable(theEx, request, response, handler);
//            } else if (ex instanceof HttpMessageNotWritableException theEx) {
//                return handleHttpMessageNotWritable(theEx, request, response, handler);
//            } else if (ex instanceof MethodValidationException theEx) {
//                return handleMethodValidationException(theEx, request, response, handler);
//            } else if (ex instanceof BindException theEx) {
//                return handleBindException(theEx, request, response, handler);
//            } else if (ex instanceof AsyncRequestNotUsableException) {
//                return handleAsyncRequestNotUsableException(
//                        (AsyncRequestNotUsableException) ex, request, response, handler);
//            }
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
        String message = ex.getMessage();
        if (ex instanceof ErrorResponse errorResponse) {
            message = errorResponse.getDetailMessageCode();
        } else if (ex instanceof DataIntegrityViolationException d) {
            message = "Cannot process your requests, maybe there is one of your requests not exist! "+d.getLocalizedMessage();
        }

        int statusCode = (message.equals(ex.getMessage())) ? 500 : 400;

        response.addHeader("content-type", "application/json");
        response.getWriter().write(GsonUtil.gsonExpose.toJsonTree(new BaseResponse.ErrorResponse(statusCode, message)).toString());
        response.setStatus(statusCode);
    }

    @Override
    protected @NonNull ModelAndView handleErrorResponse(ErrorResponse errorResponse, @NonNull HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        response.setStatus(errorResponse.getStatusCode().value());
        response.addHeader("content-type", "application/json");
        response.getWriter().println(GsonUtil.gsonExpose.toJson(new BaseResponse.ErrorResponse(errorResponse.getStatusCode().value(), errorResponse.getBody().getDetail())));
        return new ModelAndView();
    }
}