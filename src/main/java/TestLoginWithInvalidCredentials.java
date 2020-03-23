import controllers.CommonClass;
import controllers.LoginController;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class TestLoginWithInvalidCredentials {
    private static LoginController loginController;
    private static CommonClass commonClass;

    @BeforeClass
    public static void setUp(){
        System.out.println("Running: setUp");
        loginController = new LoginController();
        commonClass = new CommonClass();
    }

    /**
     * Test login authorization functionality for simple user with invalid credentials.
     * POST /user/login
     */
    @Test
    public void userInvalidCredentialsLoginTest(){
        System.out.println("Running: userInvalidCredentialsLoginTest");
        ResponseEntity<String> userLoginResponse1 = loginController.loginAndReturnResponse("user", "???", "???");
        commonClass.checkResponseCode(userLoginResponse1, HttpStatus.UNAUTHORIZED);
        ResponseEntity<String> userLoginResponse2 = loginController.loginAndReturnResponse("user", "", "");
        commonClass.checkResponseCode(userLoginResponse2, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Test login authorization functionality for admin user with invalid credentials.
     * POST /admin/login
     */
    @Test
    public void adminInvalidCredentialsLoginTest(){
        System.out.println("Running: adminInvalidCredentialsLoginTest");
        ResponseEntity<String> adminLoginResponse1 = loginController.loginAndReturnResponse("admin", "???", "???");
        commonClass.checkResponseCode(adminLoginResponse1, HttpStatus.UNAUTHORIZED);
        ResponseEntity<String> adminLoginResponse2 = loginController.loginAndReturnResponse("admin", "", "");
        commonClass.checkResponseCode(adminLoginResponse2, HttpStatus.UNAUTHORIZED);
    }
}