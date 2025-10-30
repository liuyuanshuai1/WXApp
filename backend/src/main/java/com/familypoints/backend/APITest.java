package com.familypoints.backend;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

public class APITest {
    private static final String BASE_URL = "http://localhost:8081";
    private static final HttpClient client = HttpClient.newHttpClient();
    
    public static void main(String[] args) {
        System.out.println("Starting API endpoint tests...");
        
        // 1. Test public endpoints
        testPublicEndpoints();
        
        // 2. Test authenticated endpoints (need to get token first)
        // testAuthenticatedEndpoints();
        
        System.out.println("API testing completed.");
    }
    
    private static void testPublicEndpoints() {
        System.out.println("\n=== Testing Public Endpoints ===");
        
        // Test /api/user/test
        try {
            String testUrl = BASE_URL + "/api/user/test";
            System.out.println("1. Testing /api/user/test endpoint:");
            System.out.println("   URL: " + testUrl);
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(testUrl))
                    .GET()
                    .build();
            
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("   Status code: " + response.statusCode());
            System.out.println("   Response: " + response.body());
            
            if (response.statusCode() == 200) {
                System.out.println("   [PASS] Test endpoint is working properly");
            } else {
                System.out.println("   [FAIL] Test endpoint has issues");
            }
        } catch (Exception e) {
            System.out.println("   [ERROR] Test endpoint request failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Send GET request
    private static HttpResponse<String> sendGetRequest(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
    
    // Send POST request
    private static HttpResponse<String> sendPostRequest(String url, String jsonBody) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
    
    // Send GET request with authorization header
    private static HttpResponse<String> sendGetRequestWithAuth(String url, String token) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
    
    // Send POST request with authorization header
    private static HttpResponse<String> sendPostRequestWithAuth(String url, String jsonBody, String token) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}