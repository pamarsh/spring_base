package org.swamps.houseController.users;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.swamps.houseController.HouseControllerApplication;
import org.swamps.houseController.dto.UserAccountDto;
import org.swamps.houseController.dto.UserAccountDtoBuilder;
import org.swamps.houseController.restClient.RestClientBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HouseControllerApplication.class)
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@IntegrationTest({"server.port=8084",
        "security.basic.enabled=false",
        "server.ssl.key-store = /home/paul/repository/Spring-Base/keystore.jks",
        "server.ssl.key-store-password = changeit",
        "server.ssl.key-password = changeit"})
public class UserAccountControllerTest {

    public static final String USER_NAME = "myUserName";
    public static final String USER_FULL_NAME = "Fred Basset";
    public static final String EMAIL_ADDRESS = "fred.basset@disney.com";
    public static final String USERS_URL = "https://localhost:8084/users";
    public static final String PASSWORD = "password";

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Value("${server.port}")
    private int port;

    private RestTemplate restTemplate  = new RestClientBuilder().withUsername("admin").withPassword("admin").build();

    @After
    @Before
    public void cleanupUserAccounts() {
        UserAccountDto[] userAccounts = restTemplate.getForObject(USERS_URL, UserAccountDto[].class);
        for (UserAccountDto account : Arrays.asList(userAccounts)) {
            deleteAccount(account.getUserId());
        }
    }

    @Test
    public void ensureSuccessfulInjectionOfUserAccount() {
        createAccount(USER_NAME, USER_FULL_NAME,  EMAIL_ADDRESS, "password");
        UserAccountDto[] forNow = restTemplate.getForObject(USERS_URL, UserAccountDto[].class);
        assertThat(Arrays.asList(forNow).size(), equalTo(1));
    }

    @Test
    public void newlyCreatedAccountHasUserRole() {
        createAccount(USER_NAME, USER_FULL_NAME, EMAIL_ADDRESS, "password");
        UserAccountDto userAccount = restTemplate.getForObject(USERS_URL+ "/" + USER_NAME, UserAccountDto.class);
        validateInList(userAccount.getRoles(), "USER");
    }

    @Test
    public void ensureThatWeCanSuccessfullyRetrieveAllStoredUserAccounts() {
        createAccount(USER_NAME, USER_FULL_NAME, EMAIL_ADDRESS, "password");
        createAccount(USER_NAME + "1", USER_FULL_NAME + "1", EMAIL_ADDRESS+"1", "password");
        UserAccountDto[] users = restTemplate.getForObject(USERS_URL, UserAccountDto[].class);
        assertThat(Arrays.asList(users).size(), equalTo(2));
    }

    @Test
    public void ensureThatWeCanChangeValuesInAUserAccount() {
        createAccount(USER_NAME, USER_FULL_NAME, EMAIL_ADDRESS, "password");
        changeAccount(USER_NAME, "Changed Full Name", "changedEmail@gmail.com", restTemplate);
    }

    @Test
    public void adminIsAbleToAmendAccountUserRoles() {
        createAccount(USER_NAME, USER_FULL_NAME, EMAIL_ADDRESS, PASSWORD);
        changeAccountRoles(USER_NAME, restTemplate, "ADMIN","USER","AUDIO","GUEST");
    }

    @Test
    public void createdUserCanAccessItsOwnDetails() {
        createAccount(USER_NAME, USER_FULL_NAME, EMAIL_ADDRESS, "password");
        RestTemplate restTemplate1 = new RestClientBuilder().withUsername(USER_NAME).withPassword("password").build();
        UserAccountDto user = restTemplate1.getForObject(USERS_URL + "/" + USER_NAME, UserAccountDto.class);
    }

    @Test
    public void createdUserCannotAccessAnotherUsersDetails() {
        exception.expect(HttpClientErrorException.class);
        exception.expectMessage(CoreMatchers.containsString("401 Unauthorized"));
        createAccount(USER_NAME, USER_FULL_NAME, EMAIL_ADDRESS, "password");
        createAccount(USER_NAME + "1", USER_FULL_NAME, EMAIL_ADDRESS + "1", "password");
        RestTemplate restTemplate1 = new RestClientBuilder().withUsername(USER_NAME).withPassword("password").build();

            UserAccountDto user = restTemplate1.getForObject(USERS_URL + "/" + USER_NAME + "1", UserAccountDto.class);
            fail("use able to access another users details");
    }

    @Test
    public void nonAdminUserCanChangeItsPasswordAndFullName() {
        createAccount(USER_NAME, USER_FULL_NAME, EMAIL_ADDRESS, PASSWORD);
        RestTemplate userRestClient =  new RestClientBuilder().withUsername(USER_NAME).withPassword("password").build();
        changeAccount(USER_NAME, "Changed Full Name", "changedEmail@gmail.com", userRestClient);
    }

    @Test
    public void nonAdminUserCannotChangeItsRoles() {
        exception.expect(HttpClientErrorException.class);
        exception.expectMessage(CoreMatchers.containsString("403 Forbidden"));
        createAccount(USER_NAME, USER_FULL_NAME, EMAIL_ADDRESS, PASSWORD);
        RestTemplate userRestClient =  new RestClientBuilder().withUsername(USER_NAME).withPassword("password").build();
        changeAccountRoles(USER_NAME, userRestClient, "ADMIN", "USER", "AUDIO", "GUEST");
    }

    private void changeAccountRoles(String userName, RestTemplate restClient, String... roles) {
        List<String> newRoles = Arrays.asList(roles);
        UserAccountDto user = restClient.getForObject(USERS_URL + "/" + userName, UserAccountDto.class);
        final UserAccountDtoBuilder accountDtoBuilder = UserAccountDtoBuilder.emptyUser().withEmailAddress(user.getEmailAddress())
                .withPassword(user.getPassword())
                .withFirstName(user.getFirstName())
                .withLastName(user.getLastName())
                .withUserId(user.getUserId());

        for (String newRole : newRoles) {
            accountDtoBuilder.withRole(newRole);
        }
        restClient.put(USERS_URL + "/" + userName,accountDtoBuilder.createUserAccountDto());
         user = restClient.getForObject(USERS_URL + "/" + userName, UserAccountDto.class);
        //verifyRoles(user.getRoles(), newRoles);
    }

    private void verifyRoles(List<String> actual, List<String> expected) {
        for (String expectedRole : expected) {
            if (! actual.contains(expectedRole)) {
                fail( "Expected role " + expected + " not available in role list");
            }
        }
    }

    @Test
    public void accountIsRemovedWhenDeleted() {
        createAccount(USER_NAME, USER_FULL_NAME, EMAIL_ADDRESS, "password");
        createAccount(USER_NAME + "1", USER_FULL_NAME + "1", EMAIL_ADDRESS + "1", "password");
        UserAccountDto[] forNow1 = restTemplate.getForObject(USERS_URL, UserAccountDto[].class);
        List<UserAccountDto> list = Arrays.asList(forNow1);
        assertThat(list.size(), equalTo(2));
        deleteAccount(list.get(0).getUserId());
        UserAccountDto[] forNow = restTemplate.getForObject(USERS_URL, UserAccountDto[].class);
        assertThat(Arrays.asList(forNow).size(), equalTo(1));
    }

    private void validateInList(List<String> roles, String expectedRole) {
        for (String role : roles) {
            if ( role.equals(expectedRole)) {
                return;
            }
        }
        fail("Role " + expectedRole + " Not in role list");
    }

    private void changeAccount(String userName, String fullName, String email, RestTemplate restClient) {

        UserAccountDto newAccount  = UserAccountDtoBuilder.emptyUser()
                .withFirstName(fullName)
                .withLastName("")
                .withUserId(userName)
                .withEmailAddress(email)
                .withRole("USER")
                .createUserAccountDto();
        restClient.put(USERS_URL + "/" + userName,newAccount);
        UserAccountDto user = restTemplate.getForObject(USERS_URL + "/" + userName, UserAccountDto.class);
        assertThat(user.getEmailAddress(), equalTo(email));
        assertThat(user.getFirstName(), equalTo(fullName));
    }

    private UserAccountDto createAccount(String accountName, String accountFullName, String emailAddress, String password) {
        ResponseEntity<UserAccountDto> response = submitNewAccount(accountName, accountFullName, emailAddress, password);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        UserAccountDto accountCreated = response.getBody();
        return accountCreated;
    }

    private ResponseEntity<UserAccountDto> submitNewAccount(String accountName, String accountFullName, String emailAddress, String password) {
        UserAccountDto account = UserAccountDtoBuilder.emptyUser()
                .withUserId(accountName)
                .withFirstName(accountFullName)
                .withLastName("whatever")
                .withEmailAddress(emailAddress)
                .withPassword(password)
                .createUserAccountDto();
        return restTemplate.postForEntity(USERS_URL, account, UserAccountDto.class, Collections.EMPTY_MAP);
    }

    private void deleteAccount(String userId) {
        restTemplate.delete(USERS_URL + "/" + userId);
    }

}