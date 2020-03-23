package controllers;

import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static controllers.CommonClass.HOST;

public class LoginController {

    // create an instance of RestTemplate
    RestTemplate restTemplate = new RestTemplate();

    /**
     * Method to login and receive the response.
     * @param role
     * @param username
     * @param password
     * @return
     */
    public ResponseEntity<String> loginAndReturnResponse(String role, String username, String password){
        // request url
        String URL = HOST + role + "/login";
        // create headers
        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // request body parameters
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);

        // build the request
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(map, headers);

        // send POST request and return
        try{
            return restTemplate.postForEntity(URL, entity, String.class);
        }
        catch (HttpClientErrorException e){
            return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }
}
