package controllers;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static controllers.CommonClass.HOST;

public class UserActionsController {

    RestTemplate restTemplate = new RestTemplate();

    /**
     * Method for execution of Get data request and receive response.
     * @param token
     * @return
     */
    public ResponseEntity<String> getData(String token){
        // request url
        String URL = HOST + "user/getData";

        // set "x-access-token" header
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-access-token", token);

        // build the request
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // send GET request and return
        return restTemplate.exchange(URL, HttpMethod.GET, entity,
                String.class);

    }
}
