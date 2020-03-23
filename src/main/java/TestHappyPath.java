import controllers.AdminActionsController;
import controllers.CommonClass;
import controllers.LoginController;
import controllers.UserActionsController;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;

/**
 * Class with happy path test scenarios.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestHappyPath {
    private static LoginController loginController;
    private static AdminActionsController adminActionsController;
    private static CommonClass commonClass;
    private static UserActionsController userActionsController;
    private static String adminToken;
    private static String userToken;
    private static String new_user_username;

    @BeforeAll
    public static void setUp(){
        System.out.println("Running: setUp");
        loginController = new LoginController();
        adminActionsController = new AdminActionsController();
        commonClass = new CommonClass();
        userActionsController = new UserActionsController();
    }

    /**
     * Test login functionality for admin.
     * POST /admin/login/
     */
    @Test
    @Order(1)
    public void adminLoginTest(){
        System.out.println("Running: adminLoginTest");
        ResponseEntity<String> adminLoginResponse = loginController.loginAndReturnResponse("admin", "admin", "admin");
        commonClass.checkResponseCodeAndMessage(adminLoginResponse, HttpStatus.OK);
        adminToken = commonClass.getAuthorizationToken(adminLoginResponse);
    }

    /**
     * Test create user functionality with correct data in request body.
     * POST /admin/create
     */
    @Test
    @Order(2)
    public void createUserTest(){
        System.out.println("Running: createUserTest");
        if(adminToken == null) {
            adminLoginTest();
        }
        new_user_username = RandomStringUtils.randomAlphanumeric(20);
        ResponseEntity<String> createUserResponse = adminActionsController.createUserAccount(adminToken, new_user_username, "password", "email@mail.com", "name");
        commonClass.checkResponseCodeAndMessage(createUserResponse, HttpStatus.OK);
        System.out.println("User " + new_user_username + " is created");
    }

    /**
     * Test login authorization functionality for simple user.
     * POST /user/login
     */
    @Test
    @Order(3)
    public void userLoginTest(){
        System.out.println("Running: userLoginTest");
        if(new_user_username == null){
            createUserTest();
        }
        ResponseEntity<String> userLoginResponse = loginController.loginAndReturnResponse("user", new_user_username, "password");
        commonClass.checkResponseCodeAndMessage(userLoginResponse, HttpStatus.OK);
        userToken = commonClass.getAuthorizationToken(userLoginResponse);
    }

    /**
     * Test for get user data request
     * GET /user/getData
     */
    @Test
    @Order(4)
    public void getUserDataTest(){
        if(userToken == null) {
            userLoginTest();
        }
        System.out.println("Running: getUserDataTest");
        String getDataText = "Lorem ipsum dolor sit amet consectetur adipisicing elit. Incidunt voluptates rem, voluptas earum laborum aliquid ipsum atque culpa iusto! Dignissimos minus hic magni quis! Enim necessitatibus cupiditate excepturi suscipit quaerat.";
        ResponseEntity<String> userData = userActionsController.getData(userToken);
        assertEquals(userData.getStatusCode(), HttpStatus.OK);
        assertEquals(userData.getBody(),getDataText);
    }

    /**
     * Test for update simple User request.
     * PUT /admin/update
     */
    @Test
    @Order(5)
    public void updateUserAccountTest(){
        System.out.println("Running: updateUserAccount");
        if(adminToken == null){
            adminLoginTest();
        }
        if(new_user_username == null){
            createUserTest();
        }
        ResponseEntity<String> updateUserResponse = adminActionsController.updateUserAccount(adminToken, new_user_username, new_user_username + "_updated", "new_password", "updated_email", "updated_name");
        commonClass.checkResponseCodeAndMessage(updateUserResponse, HttpStatus.OK);
        if(commonClass.isSuccessMessageInResponse(updateUserResponse)){
            new_user_username += "_updated";
        }
        System.out.println("User " + new_user_username + " is updated");
    }

    /**
     * Test for self update of admin user.
     * PUT /admin/self
     */
    @Test
    @Order(6)
    public void selfUpdateOfAdminUserTest() {
        System.out.println("Running: selfUpdateOfAdminUser");
        if (adminToken == null) {
            adminLoginTest();
        }
        ResponseEntity<String> selfUpdateOfAdminUserResponse = adminActionsController.selfUpdateOfAdminUser(adminToken, "admin1", "admin1");
        commonClass.checkResponseCodeAndMessage(selfUpdateOfAdminUserResponse, HttpStatus.OK);

        //create new admin login session to rename admin account back to 'admin' name.
        ResponseEntity<String> adminLoginResponse = loginController.loginAndReturnResponse("admin", "admin1", "admin1");
        String new_adminToken = commonClass.getAuthorizationToken(adminLoginResponse);
        ResponseEntity<String> selfUpdateOfAdminUserResponseNew = adminActionsController.selfUpdateOfAdminUser(new_adminToken, "admin", "admin");
        commonClass.checkResponseCodeAndMessage(selfUpdateOfAdminUserResponseNew, HttpStatus.OK);
    }

    /**
     * Test for delete user request.
     * DELETE /admin/delete
     */
    @Test
    @Order(7)
    public void deleteUserTest(){
        if(new_user_username == null) {
            createUserTest();
        }
        ResponseEntity<String> deleteUserResponse = adminActionsController.deleteUserAccount(adminToken, new_user_username);
        commonClass.checkResponseCodeAndMessage(deleteUserResponse, HttpStatus.OK);
        if(commonClass.isSuccessMessageInResponse(deleteUserResponse)){
            System.out.println("User " + new_user_username + " is deleted");
            new_user_username = null;
        }
    }

    /**
     * Tear down method with deleting of created user.
     * DELETE /admin/delete
     */
    @AfterAll
    public static void tearDown(){
        System.out.println("Running: tearDown");
        if(new_user_username != null) {
            adminActionsController.deleteUserAccount(adminToken, new_user_username);
            System.out.println("User " + new_user_username + " is deleted");
        }
    }
}
