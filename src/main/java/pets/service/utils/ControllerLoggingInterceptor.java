package pets.service.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.util.StreamUtils.copyToByteArray;
import static pets.service.utils.LogMasker.maskDetails;

public class ControllerLoggingInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(ControllerLoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        request.setAttribute("startTime", System.currentTimeMillis());
        String maskedRequestUrl = maskDetails(request.getRequestURI());
        logger.info("Receiving [{}] URL [{}]", request.getMethod(), maskedRequestUrl);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        //String requestBodyAsString = getRequestBodyAsString(request);     //NOSONAR
        //String maskedRequestUrl = maskDetails(request.getRequestURI());   //NOSONAR
        //logger.info("Received Request for Method {} for URL {} with {}", request.getMethod(), maskedRequestUrl, requestBodyAsString); //NOSONAR
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) throws Exception {
        String maskedRequestUrl = maskDetails(request.getRequestURI());
        long duration = System.currentTimeMillis() - (Long) request.getAttribute("startTime");
        logger.info("Returning [{}] Status Code [{}] URL [{}] AFTER [{}ms]",
                request.getMethod(), response.getStatus(), maskedRequestUrl, duration);
    }

    /**
     * For POST/PUT/DELETE etc, this always returns 'Error Reading Request Body' because
     * the InputStream in request has already been used/closed
     * Fot GET, this returns 'No Request Body'
     *
     * @param request httpservletrequest
     * @return request body as String
     */
    private String getRequestBodyAsString(HttpServletRequest request) { //NOSONAR
        try {
            HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(request);
            ServletInputStream inputStream = requestWrapper.getInputStream();
            byte[] content = copyToByteArray(inputStream);
            String requestBody = new String(content);
            return requestBody.isEmpty() ? "No Request Body" : "Data: " + maskDetails(requestBody);
        } catch (NullPointerException | IOException ex) {
            return "Error Reading Request Body";
        }
    }
}
