import controllers.AdminActionsController;
import controllers.CommonClass;
import controllers.LoginController;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class TestDeleteUser {
    private LoginController loginController;
    private CommonClass commonClass;
    private AdminActionsController adminActionsController;

    @Before
    public void setUp(){
        loginController = new LoginController();
        commonClass = new CommonClass();
        adminActionsController = new AdminActionsController();
    }

    /**
     * Test delete user request with invalid username
     * DELETE /admin/delete
     */
    @Test
    public void deleteNonexistingUser(){
        System.out.println("Running: adminCreateUserParametersTest");
        ResponseEntity<String> adminLoginResponse = loginController.loginAndReturnResponse("admin", "admin", "admin");
        commonClass.checkResponseCodeAndMessage(adminLoginResponse, HttpStatus.OK);
        String adminToken = commonClass.getAuthorizationToken(adminLoginResponse);
        ResponseEntity<String> deleteUserResponse = adminActionsController.deleteUserAccount(adminToken, "nonexisting_user");
        commonClass.checkResponseCode(deleteUserResponse, HttpStatus.UNAUTHORIZED);
    }
}
