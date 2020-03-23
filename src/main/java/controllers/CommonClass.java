package controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.Assert.*;

/**
 * Class with common methods and variables
 */
public class CommonClass {
    /**
     * URL
     */
    public static final String HOST = "http://localhost:8080/";

    /**
     * Check that success:true message is in response.
     * @param responseEntity
     * @return
     */
    public Boolean isSuccessMessageInResponse(ResponseEntity<String> responseEntity){
        return responseEntity.getBody().contains("\"success\"" + ":" + "true");
    }

    /**
     * Get authorization token from response headers.
     * @param responseEntity
     * @return
     */
    public String getAuthorizationToken(ResponseEntity<String> responseEntity){
        String tokenWithExtraSymbols = Objects.requireNonNull(responseEntity.getHeaders().get("x-access-token")).toString();
        return tokenWithExtraSymbols.substring(1, tokenWithExtraSymbols.length() - 1);
    }

    /**
     * Check response code and message methods
     * @param responseEntity
     * @param httpStatus
     */
    public void checkResponseCodeAndMessage(ResponseEntity<String> responseEntity, HttpStatus httpStatus){
        try{assertEquals(responseEntity.getStatusCode(), httpStatus);
        assertTrue(this.isSuccessMessageInResponse(responseEntity));}
        catch (AssertionError e) {
            System.out.println(responseEntity.getBody() + responseEntity.getStatusCode());
            fail();
        }
    }

    /**
     * Check response code methods
     * @param responseEntity
     * @param httpStatus
     */
    public void checkResponseCode(ResponseEntity<String> responseEntity, HttpStatus httpStatus){
        try{assertEquals(responseEntity.getStatusCode(), httpStatus);
        }
        catch (AssertionError e) {
            System.out.println(responseEntity.getBody() + responseEntity.getStatusCode());
            fail();
        }
    }
}
