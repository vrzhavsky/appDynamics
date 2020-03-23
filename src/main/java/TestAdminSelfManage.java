import controllers.AdminActionsController;
import controllers.CommonClass;
import controllers.LoginController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class TestAdminSelfManage {
    private AdminActionsController adminActionsController;
    private LoginController loginController;
    private CommonClass commonClass;
    private String adminToken;

    @BeforeEach
    public void setUp(){
        adminActionsController = new AdminActionsController();
        loginController = new LoginController();
        commonClass = new CommonClass();
    }
    /**
     * Test for self update of admin user.
     * PUT /admin/self
     */
    @Test
    public void selfUpdateOfAdminUserTest() {
        System.out.println("Running: selfUpdateOfAdminUser");
        ResponseEntity<String> adminLoginResponse = loginController.loginAndReturnResponse("admin", "admin", "admin");
        adminToken = commonClass.getAuthorizationToken(adminLoginResponse);

        ResponseEntity<String> selfUpdateOfAdminUserEmptyPasswordResponse = adminActionsController.selfUpdateOfAdminUser(adminToken, "admin", "");
        commonClass.checkResponseCode(selfUpdateOfAdminUserEmptyPasswordResponse, HttpStatus.UNAUTHORIZED);

        ResponseEntity<String> selfUpdateOfAdminUserEmptyUserResponse = adminActionsController.selfUpdateOfAdminUser(adminToken, "", "admin");
        commonClass.checkResponseCode(selfUpdateOfAdminUserEmptyUserResponse, HttpStatus.UNAUTHORIZED);

    }
}
