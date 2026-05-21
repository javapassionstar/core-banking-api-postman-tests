package pl.malgorzata.galera;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.UUID;

import static pl.malgorzata.galera.BankDataGenerator.*;

public class ServerBuilder {

    private final com.sun.net.httpserver.HttpServer server;

    public ServerBuilder() throws IOException {
        this.server = com.sun.net.httpserver.HttpServer.create(new java.net.InetSocketAddress(8080), 0);
    }

    public void registerCustomerEndpoint() throws IOException {
        server.createContext("/api/v1/customers", httpExchange -> {
            // POPRAWKA: Jeśli metoda jest niepoprawna, natychmiast przerywamy (return)
            if (!isMethodValid(httpExchange, "POST")) return;

            String requestBody;
            try (InputStream is = httpExchange.getRequestBody();
                 Scanner scanner = new Scanner(is, StandardCharsets.UTF_8)) {
                requestBody = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
            }

            String generatedId = UUID.randomUUID().toString();
            String response = requestBody.replaceFirst("\\{", "{\n  \"id\": \"" + generatedId + "\",");

            httpExchange.getResponseHeaders().add("Content-Type", "application/json");
            httpExchange.sendResponseHeaders(201, response.getBytes(StandardCharsets.UTF_8).length);

            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes(StandardCharsets.UTF_8));
            }
        });
    }

    public void logInEndpoint() throws IOException {
        server.createContext("/api/v1/auth/login", httpExchange -> {
            // POPRAWKA: Przerywamy w przypadku złej metody HTTP
            if (!isMethodValid(httpExchange, "POST")) return;

            String response = "{\"token\": \"" + generateToken() + "\"}";
            httpExchange.getResponseHeaders().add("Content-Type", "application/json");
            httpExchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes(StandardCharsets.UTF_8));
            }
        });
    }

    public void openAccountAndGetBalanceEndpoints() {
        server.createContext("/api/v1/accounts", httpExchange -> {
            String path = httpExchange.getRequestURI().getPath();
            String method = httpExchange.getRequestMethod();

            // 1. OBSŁUGA GET (Pobranie salda) -> /api/v1/accounts/[numer]/balance
            if ("GET".equalsIgnoreCase(method) && path.endsWith("/balance")) {

                String authHeader = httpExchange.getRequestHeaders().getFirst("Authorization");
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    sendUnauthorizedResponse(httpExchange);
                    return;
                }

                String response = "{\n" +
                        "  \"balance\": " + generateRandomAccountBalance() + ",\n" +
                        "  \"currency\": \"PLN\",\n" +
                        "  \"status\": \"ACTIVE\"\n" +
                        "}";

                httpExchange.getResponseHeaders().add("Content-Type", "application/json");
                httpExchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes(StandardCharsets.UTF_8));
                }
            }
            // 2. OBSŁUGA POST (Tworzenie konta) -> /api/v1/accounts
            else if ("POST".equalsIgnoreCase(method) && path.equals("/api/v1/accounts")) {

                String authHeader = httpExchange.getRequestHeaders().getFirst("Authorization");
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    sendUnauthorizedResponse(httpExchange);
                    return;
                }

                String fakeIbanResponse = "{\n  \"IBAN\":\"" + generateIban() + "\"\n}";

                httpExchange.getResponseHeaders().add("Content-Type", "application/json");
                httpExchange.sendResponseHeaders(201, fakeIbanResponse.getBytes(StandardCharsets.UTF_8).length);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(fakeIbanResponse.getBytes(StandardCharsets.UTF_8));
                }
            }
            // Obsługa sytuacji, gdy ktoś wpisze zły adres lub metodę (np. DELETE /accounts)
            else {
                httpExchange.sendResponseHeaders(404, 0);
                httpExchange.getResponseBody().close();
            }
        });
    }

    public void runServer() {
        server.setExecutor(null);
        server.start();
        System.out.println("Mock server started on port 8080");
    }

    private boolean isMethodValid(HttpExchange httpExchange, String requiredMethod) throws IOException {
        String method = httpExchange.getRequestMethod();
        if (!requiredMethod.equalsIgnoreCase(method)) {
            httpExchange.sendResponseHeaders(405, 0);
            httpExchange.getResponseBody().close();
            return false;
        }
        return true;
    }

    //Wydzielenie powtarzającego się kodu dla błędu 401 (Zasada DRY - Don't Repeat Yourself)
    private void sendUnauthorizedResponse(HttpExchange httpExchange) throws IOException {
        String errorResponse = "{\n  \"error\": \"Unauthorized\",\n  \"message\": \"Brak autoryzacji. Wymagany poprawny token Bearer.\"\n}";
        httpExchange.getResponseHeaders().add("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(401, errorResponse.getBytes(StandardCharsets.UTF_8).length);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(errorResponse.getBytes(StandardCharsets.UTF_8));
        }
    }
}
