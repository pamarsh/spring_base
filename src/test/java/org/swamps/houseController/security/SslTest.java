package org.swamps.houseController.security;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.swamps.houseController.HouseControllerApplication;
import org.swamps.houseController.dto.UserAccountDto;
import org.swamps.houseController.restClient.RestClientBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HouseControllerApplication.class)
@WebAppConfiguration
@IntegrationTest({"server.port=8083",
        "security.basic.enabled=false",
        "server.ssl.key-store = keystore.jks",
        "server.ssl.key-store-password = changeit",
        "server.ssl.key-password = changeit"})
public class SslTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();



    public final String USERS_URL_SSL = "https://localhost:8083/users";
    public final String USERS_URL = "http://localhost:8083/users";


    private RestTemplate restTemplate  = new RestClientBuilder().withUsername("admin").withPassword("admin").build();

    @Test
    public void ensureSslConnectionAllowedWithSslServerPort() {
       UserAccountDto[] accounts =  restTemplate.getForObject(USERS_URL_SSL, UserAccountDto[].class);
    }

    @Test
    public void ensureNonSslRequestFailsOnSslPort() {
        exception.expect(ResourceAccessException.class);
        exception.expectMessage(CoreMatchers.containsString("failed to respond"));
        UserAccountDto[] accounts =  restTemplate.getForObject(USERS_URL, UserAccountDto[].class);
    }


}