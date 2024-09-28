//package notiflow.server.Middleware;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import notiflow.server.Services.ToxicDetectionService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//import org.springframework.web.util.ContentCachingRequestWrapper;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import java.io.IOException;
//import java.util.List;
//
//@Component
//public class ToxicFilter extends OncePerRequestFilter {
//
//    private final ToxicDetectionService toxicDetectionService;
//
//    @Autowired
//    public ToxicFilter(ToxicDetectionService toxicDetectionService) {
//        this.toxicDetectionService = toxicDetectionService;
//    }
//
//    private final List<String> monitoredEndpoints = List.of(
//            "/api/email/sendMail",
//            "/api/email/sendTemplateMail",
//            "/api/email/sendAttachmentMail",
//            "/api/email/sendAttachmentMailWithTemplate",
//            "/api/email/schedule/sendMail",
//            "/api/email/schedule/sendTemplateMail",
//            "/api/email/schedule/sendAttachmentMail",
//            "/api/email/schedule/sendAttachmentMailWithTemplate"
//    );
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        if (isMonitoredEndpoint(request.getRequestURI()) && request.getMethod().equals("POST")) {
//            ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
//
//            ObjectMapper objectMapper = new ObjectMapper();
//            JsonNode jsonNode = objectMapper.readTree(wrappedRequest.getContentAsByteArray());
//            String message = jsonNode.path("message").asText();
//
//            // Check for toxicity
//            ResponseEntity<String> toxicityResponse = toxicDetectionService.checkToxicity(message);
//            if (toxicityResponse != null) {
//                response.setStatus(toxicityResponse.getStatusCodeValue());
//                response.getWriter().write(toxicityResponse.getBody());
//                return;
//            }
//
//            // Continue the filter chain with the wrapped request
//            filterChain.doFilter(wrappedRequest, response);
//        } else {
//            filterChain.doFilter(request, response);
//        }
//    }
//
//    private boolean isMonitoredEndpoint(String requestURI) {
//        return monitoredEndpoints.stream().anyMatch(requestURI::equals);
//    }
//}
