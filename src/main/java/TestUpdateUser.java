import controllers.AdminActionsController;
import controllers.CommonClass;
import controllers.LoginController;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Tests update user request checks that oldUsername, username, password parameters are mandatory and email, name parameters are optional.
 * POST /admin/update
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestUpdateUser {
    private static LoginController loginController;
    private static CommonClass commonClass;
    private static AdminActionsController adminActionsController;
    private static String username;
    private static String adminToken;

    @BeforeAll
    public static void setUp(){
        System.out.println("Running: SetUp");
        loginController = new LoginController();
        commonClass = new CommonClass();
        adminActionsController = new AdminActionsController();
        ResponseEntity<String> adminLoginResponse = loginController.loginAndReturnResponse("admin", "admin", "admin");
        commonClass.checkResponseCodeAndMessage(adminLoginResponse, HttpStatus.OK);
        adminToken = commonClass.getAuthorizationToken(adminLoginResponse);

        username = RandomStringUtils.randomAlphanumeric(5);
        ResponseEntity<String> createUserResponse = adminActionsController.createUserAccount(adminToken, username, "password", "email@mail.com", "name");
        commonClass.checkResponseCodeAndMessage(createUserResponse, HttpStatus.OK);

    }

    @Test
    @Order(1)
    public void updateUserInvalidOldUsername(){
        System.out.println("Running: updateUserInvalidOldUsername");
        ResponseEntity<String> updateUserWithEmptyOldUsernameResponse = adminActionsController.updateUserAccount(adminToken, "", "new_username", "new_password", "new_email", "new_name");
        commonClass.checkResponseCode(updateUserWithEmptyOldUsernameResponse, HttpStatus.UNAUTHORIZED);
    }

    @Test
    @Order(2)
    public void updateUserWithEmptyUsername(){
        System.out.println("Running: updateUserWithEmptyUsername");
        ResponseEntity<String> updateUserWithEmptyUsernameResponse = adminActionsController.updateUserAccount(adminToken, username, "", "", "", "");
        commonClass.checkResponseCode(updateUserWithEmptyUsernameResponse, HttpStatus.UNAUTHORIZED);
    }

    @Test
    @Order(3)
    public void updateUserWithEmptyPassword(){
        System.out.println("Running: updateUserWithEmptyPassword");
        ResponseEntity<String> updateUserWithEmptyPasswordResponse = adminActionsController.updateUserAccount(adminToken, username, username + "new_username", "", "new_email", "new_name");
        commonClass.checkResponseCode(updateUserWithEmptyPasswordResponse, HttpStatus.UNAUTHORIZED);
    }

    @Test
    @Order(4)
    public void updateUserEmptyEmailAndName(){
        System.out.println("Running: updateUserEmptyEmailAndName");
        ResponseEntity<String> updateUserWithEmptyEmailResponse = adminActionsController.updateUserAccount(adminToken, username, username + username, "new_password", "", "new_name");
        commonClass.checkResponseCodeAndMessage(updateUserWithEmptyEmailResponse, HttpStatus.OK);

        ResponseEntity<String> updateUserWithEmptyNameResponse = adminActionsController.updateUserAccount(adminToken, username + username, username + username + username, "updated_password", "new_email", "");
        commonClass.checkResponseCodeAndMessage(updateUserWithEmptyNameResponse, HttpStatus.OK);
    }
}
