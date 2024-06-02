package vn.edu.hust.ehustclassregistrationjavabackend.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(RateLimitInterceptor.class);
    final ConcurrentHashMap<String, Queue<Long>> requestSaved = new ConcurrentHashMap<>();
    int MAX_REQUEST;
    long intervalTime;

    public RateLimitInterceptor(@Value("${max_request_per_interval}") int maxRequest, @Value("${interval_millis}") long intervalTime) {
        MAX_REQUEST = maxRequest;
        this.intervalTime = intervalTime;
    }

    private String getClientIpAddress(HttpServletRequest request) {
//        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        System.out.println(request.getRemotePort());
        System.out.println(request.getRemoteHost());
        return request.getRemoteHost() + ":" + request.getRemotePort();
//        if (xForwardedForHeader != null) {
//            return xForwardedForHeader.split(",")[0].trim();
//        }
//        return request.getRemoteAddr();
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        long currentTime = System.currentTimeMillis();
        String ip = getClientIpAddress(request);
//        if (ip.startsWith("0:0:0:0:0:0:0:1")) return true;
        if (request.getRequestURI().startsWith("/swagger")) return true;

        synchronized (requestSaved) {
            Queue<Long> requests = requestSaved.get(ip);
            if (requests != null) {
                while (!requests.isEmpty()) {
                    Long time = requests.peek();
                    if (time + intervalTime > currentTime) {
                        break;
                    }
                    requests.poll();
                }
                if (requests.size() >= MAX_REQUEST) {
//                    response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());

                    @SuppressWarnings("DataFlowIssue")
                    String retry = String.valueOf((requests.peek() + intervalTime - currentTime) / 1000 + 1);
                    response.addHeader("Retry-After", retry);
                    log.info("{} was blocked due to many request", ip);

//                    response.getWriter().println("TOO MANY REQUEST, PLEASE TRY AGAIN IN " + retry + " SECOND(S)");
                    throw new MessageException("Từ từ thôi, hãy thử lại trong "+retry+" s",HttpStatus.TOO_MANY_REQUESTS);
                }
                requests.add(currentTime);
            } else {
                ConcurrentLinkedQueue<Long> newQueue = new ConcurrentLinkedQueue<>();
                newQueue.add(currentTime);

                requestSaved.put(ip, newQueue);
            }
            return true;
        }
    }
}
