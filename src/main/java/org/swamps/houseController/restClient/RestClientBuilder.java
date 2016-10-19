package org.swamps.houseController.restClient;

public class RestClientBuilder {
    private String username;
    private String password;
    private String host = null;
    private int port = -1;

    public RestClientBuilder emptyClient() { return  new RestClientBuilder(); }  ;

    public RestClientBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public RestClientBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public RestClientBuilder withHost(String host) {
        this.host = host;
        return this;
    }

    public RestClientBuilder withPort(int port) {
        this.port = port;
        return this;
    }

    public RestClient build() {
        return new RestClient(username, password, host, port);
    }
}