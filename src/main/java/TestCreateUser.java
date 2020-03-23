import controllers.AdminActionsController;
import controllers.CommonClass;
import controllers.LoginController;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class TestCreateUser {
    private LoginController loginController;
    private AdminActionsController adminActionsController;
    private CommonClass commonClass;
    private String new_user_username;
    private String adminToken;

    @Before
    public void setUp(){
        System.out.println("Running: setUp");
        loginController = new LoginController();
        adminActionsController = new AdminActionsController();
        commonClass = new CommonClass();
    }

    /**
     * Test to check that username and password parameters are mandatory and email and name are optional.
     * POST /admin/create
     */
    @Test
    public void adminCreateUserParametersTest(){
        System.out.println("Running: adminCreateUserParametersTest");
        ResponseEntity<String> adminLoginResponse = loginController.loginAndReturnResponse("admin", "admin", "admin");
        commonClass.checkResponseCodeAndMessage(adminLoginResponse, HttpStatus.OK);
        adminToken = commonClass.getAuthorizationToken(adminLoginResponse);

        new_user_username = RandomStringUtils.randomAlphanumeric(5);
        ResponseEntity<String> createUserResponseEmptyUsername = adminActionsController.createUserAccount(adminToken, "", "password", "email@mail.com", "name");
        commonClass.checkResponseCode(createUserResponseEmptyUsername, HttpStatus.UNAUTHORIZED);
        ResponseEntity<String> createUserResponseEmptyPassword = adminActionsController.createUserAccount(adminToken, new_user_username, "", "email@mail.com", "name");
        commonClass.checkResponseCode(createUserResponseEmptyPassword, HttpStatus.UNAUTHORIZED);
        ResponseEntity<String> createUserResponseEmptyEmail = adminActionsController.createUserAccount(adminToken, new_user_username, "password", "", "name");
        commonClass.checkResponseCodeAndMessage(createUserResponseEmptyEmail, HttpStatus.OK);
        ResponseEntity<String> createUserResponseEmptyName = adminActionsController.createUserAccount(adminToken, new_user_username + "1", "password", "email@mail.com", "");
        commonClass.checkResponseCodeAndMessage(createUserResponseEmptyName, HttpStatus.OK);
    }

    /**
     * Tear down method with deleting of created user.
     * DELETE /admin/delete
     */
    @After
    public void tearDown(){
        System.out.println("Running: tearDown");
        if(new_user_username != null) {
            adminActionsController.deleteUserAccount(adminToken, new_user_username);
            adminActionsController.deleteUserAccount(adminToken, new_user_username + "1");
            System.out.println("User " + new_user_username + " and " + new_user_username + "1" + " are deleted");
        }
    }

}
