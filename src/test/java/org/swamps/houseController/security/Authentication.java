package org.swamps.houseController.security;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HouseControllerApplication.class)
@WebAppConfiguration
@IntegrationTest({"server.port=8082",
        "security.basic.enabled=false",
        "server.ssl.key-store = /home/paul/repository/Spring-Base/keystore.jks",
        "server.ssl.key-store-password = changeit",
        "server.ssl.key-password = changeit"})
public class Authentication {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    public static final String USERS_URL = "https://localhost:8082/users";

    private RestTemplate restTemplate  = new RestClientBuilder().withUsername("admin").withPassword("admin").build();

    private RestTemplate restUserTemplate;


    @Before
    @After
    public void cleanupUserAccounts() {
        RestTemplate usableRestTemplate = restUserTemplate != null ? restUserTemplate : restTemplate;
        UserAccountDto[] userAccounts = usableRestTemplate.getForObject(USERS_URL, UserAccountDto[].class);
        for (UserAccountDto account : Arrays.asList(userAccounts)) {
            usableRestTemplate.delete(USERS_URL + "/" + account.getUserId());
        }
    }


    @Test
    public void ensureDefaultAdminAvailableWhenNoOtherAdminUserIsThere() {
        UserAccountDto[] users = restTemplate.getForObject(USERS_URL, UserAccountDto[].class);
        assertThat(Arrays.asList(users).size(), equalTo(0));
    }

    @Test
    public void ensureDefaultAdminNotAvailableIfAnotherAdminUserIsThere() {
        exception.expect(HttpClientErrorException.class);
        exception.expectMessage(CoreMatchers.containsString("401 Unauthorized"));
        createAccount("newUser", "New User", "new.user@gmail.com", "password", "ROLE_ADMIN");
        restTemplate  = new RestClientBuilder().withUsername("admin").withPassword("admin").build();
        restUserTemplate = new RestClientBuilder().withUsername("newUser").withPassword("password").build();
        UserAccountDto[] users = restTemplate.getForObject(USERS_URL, UserAccountDto[].class);
        assertThat(Arrays.asList(users).size(), equalTo(1));
    }

    @Test
    public void ensureDefaultAdminAvailableIfNoOtherAdminUserIsThere() {
        createAccount("newUser", "New User", "new.user@gmail.com", "password");
        UserAccountDto[] forNow = restTemplate.getForObject(USERS_URL, UserAccountDto[].class);
        assertThat(Arrays.asList(forNow).size(), equalTo(1));
    }

    @Test
    public void ensureNewUserCanBeLoggedInto() {
        createAccount("newUser", "New User", "new.user@gmail.com", "password");
        RestTemplate newUserConnection = new RestClientBuilder().withUsername("newUser").withPassword("password").build();
        UserAccountDto newUser = newUserConnection.getForObject(USERS_URL+"/newUser", UserAccountDto.class);
        assertThat(newUser.getUserId(), containsString("newUser"));
    }

    private UserAccountDto createAccount(String accountName, String accountFullName, String emailAddress, String password, String... roles) {
        ResponseEntity<UserAccountDto> response = submitNewUserAccount(accountName, accountFullName, emailAddress, password, roles);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        UserAccountDto accountCreated = response.getBody();
        return accountCreated;
    }

    private ResponseEntity<UserAccountDto> submitNewUserAccount(String accountName, String accountFullName, String emailAddress, String password, String... roles) {
        UserAccountDtoBuilder builder  = UserAccountDtoBuilder.emptyUser()
                .withUserId(accountName)
                .withFirstName(accountFullName)
                .withLastName("LastName")
                .withEmailAddress(emailAddress)
                .withPassword(password);

        for (String role : roles) {
            builder.withRole(role);
        }
        return restTemplate.postForEntity(USERS_URL, builder.createUserAccountDto(), UserAccountDto.class, Collections.EMPTY_MAP);
    }
}
