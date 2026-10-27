package com.vinstore.topup;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SecurityRulesTest {

    @Value("${local.server.port}")
    private int port;

    private final HttpClient client = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NEVER)
            .build();

    private URI uri(String path) {
        return URI.create("http://localhost:" + port + path);
    }

    @Test
    void halamanAdminTanpaLoginDialihkanKeLogin() throws Exception {
        HttpResponse<String> response = client.send(
                HttpRequest.newBuilder(uri("/admin")).build(),
                HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(302);
        assertThat(response.headers().firstValue("Location"))
                .hasValueSatisfying(location -> assertThat(location).contains("/login"));
    }

    @Test
    void halamanPublikBisaDibukaTanpaLogin() throws Exception {
        HttpResponse<String> response = client.send(
                HttpRequest.newBuilder(uri("/")).build(),
                HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    void webhookDenganTandaTanganPalsuDitolakDenganKode401() throws Exception {
        String json = "{\"gatewayRef\":\"PAY-X\",\"status\":\"PAID\",\"amount\":10000.00,\"signature\":\"palsu\"}";

        HttpResponse<String> response = client.send(
                HttpRequest.newBuilder(uri("/api/webhooks/payment"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json))
                        .build(),
                HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(401);
    }
}