package controllers;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static controllers.CommonClass.HOST;

public class AdminActionsController {

    RestTemplate restTemplate = new RestTemplate();

    /**
     * Method for execution of Create account request and receive response
     * @param token
     * @param username
     * @param password
     * @param email
     * @param name
     * @return
     */
    public ResponseEntity<String> createUserAccount(String token, String username, String password, String email, String name){
        // request url
        String URL = HOST + "admin/create";

        // set "x-access-token" header
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-access-token", token);

        // request body parameters
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        map.put("email", email);
        map.put("name", name);

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

    /**
     * Method for execution of Delete account request and receive response
     * @param token
     * @param username
     * @return
     */
    public ResponseEntity<String> deleteUserAccount(String token, String username){
        // request url
        String URL = HOST + "admin/delete";

        // set "x-access-token" header
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-access-token", token);

        // request body parameters
        Map<String, String> map = new HashMap<>();
        map.put("username", username);

        // build the request
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(map, headers);

        // send POST request and return
        try{
            return restTemplate.exchange(URL, HttpMethod.DELETE, entity, String.class);
        }
        catch (HttpClientErrorException e){
            return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Method for execution of Update account request and receive response
     * @param token
     * @param oldUsername
     * @param newUsername
     * @param newPassword
     * @param newEmail
     * @param newName
     * @return
     */
    public ResponseEntity<String> updateUserAccount(String token, String oldUsername, String newUsername, String newPassword, String newEmail, String newName){
        // request url
        String URL = HOST + "admin/update";

        // set "x-access-token" header
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-access-token", token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // request body parameters
        ObjectNode requestBody = JsonNodeFactory.instance.objectNode();
        requestBody.put("oldUsername", oldUsername);
        ObjectNode updatedUser = requestBody.with("updatedUser");
        updatedUser.put("username", newUsername);
        updatedUser.put("password", newPassword);
        updatedUser.put("email", newEmail);
        updatedUser.put("name", newName);

        // build the request
        HttpEntity<String> entity = new HttpEntity<String>(requestBody.toString(), headers);

        // send PUT request and return
        try{
            return restTemplate.exchange(URL, HttpMethod.PUT, entity, String.class);
        }
        catch (HttpClientErrorException e){
            return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Method for execution of Self update of admin account request and receive response
     * @param token
     * @param newAdminname
     * @param newPassword
     * @return
     */
    public ResponseEntity<String> selfUpdateOfAdminUser(String token, String newAdminname, String newPassword){
        // request url
        String URL = HOST + "admin/self";

        // set "x-access-token" header
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-access-token", token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // request body parameters
        ObjectNode requestBody = JsonNodeFactory.instance.objectNode();
        requestBody.put("username", newAdminname);
        requestBody.put("password", newPassword);

        // build the request
        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

        // send PUT request and return
        try{
            return restTemplate.exchange(URL, HttpMethod.PUT, entity, String.class);
        }
        catch (HttpClientErrorException e){
            return new ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }
}
