package notiflow.server.Services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import org.json.JSONObject;

@Service
public class ToxicDetectionService {

    private RestTemplate restTemplate;

    @Autowired
    public ToxicDetectionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private JsonNode postAndGet(String inputString) {
        JSONObject json = new JSONObject();
        json.put("word", inputString);

        String postUrl = "http://127.0.0.1:5000/submit";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<String> requestEntity = new HttpEntity<>(json.toString(), headers);

        ResponseEntity<String> postResponse = restTemplate.exchange(postUrl, HttpMethod.POST, requestEntity, String.class);

        System.out.println("POST Response: " + postResponse.getBody());

        String getUrl = "http://127.0.0.1:5000/detect";

        ResponseEntity<String> getResponse = restTemplate.getForEntity(getUrl, String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = mapper.readTree(getResponse.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonNode;
    }

    public ResponseEntity<String> checkToxicity(String inputString) {
        JsonNode toxicResponse = postAndGet(inputString);

        if (toxicResponse != null && "false".equals(toxicResponse.path("proceed").asText())) {
            return ResponseEntity.badRequest().body("Toxic content detected in message");
        } else if (toxicResponse == null) {
            return ResponseEntity.badRequest().body("Error in toxic content detection service");
        }

        return null;
    }
}
