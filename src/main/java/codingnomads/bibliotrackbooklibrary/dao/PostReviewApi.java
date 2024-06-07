package codingnomads.bibliotrackbooklibrary.dao;

import codingnomads.bibliotrackbooklibrary.model.forms.ReviewForm;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class PostReviewApi implements IPostReviewApi{

    @Autowired
    RestTemplate restTemplate;

    @Override
    public void postReview(ReviewForm reviewForm) {
        String reviewFormAsJson = jsonifyForm(reviewForm);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            HttpEntity<String> entity = new HttpEntity<>(reviewFormAsJson, headers);

            String postReviewEndpoint = "https://m09d3.wiremockapi.cloud/";
            ResponseEntity<String> response = restTemplate.exchange(postReviewEndpoint, HttpMethod.POST, entity, String.class);
            HttpStatusCode statusCode = response.getStatusCode();
            HttpStatus httpStatus = HttpStatus.valueOf(statusCode.value());
            System.out.println("Response Status Code: " + httpStatus);
        } catch (RestClientException e) {
            throw new RuntimeException("Error while posting review: " + e.getMessage(), e);
        }
    }

    private String jsonifyForm(Object form) {
        Gson gson = new Gson();
        return gson.toJson(form);
    }
}
