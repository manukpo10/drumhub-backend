package com.drumhub.user.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class SupabaseStorageService {

    private final String supabaseUrl;
    private final String serviceRoleKey;
    private final HttpClient httpClient;

    public SupabaseStorageService(
            @Value("${app.supabase.url}") String supabaseUrl,
            @Value("${app.supabase.service-role-key}") String serviceRoleKey
    ) {
        this.supabaseUrl = supabaseUrl;
        this.serviceRoleKey = serviceRoleKey;
        this.httpClient = HttpClient.newHttpClient();
    }

    /**
     * Uploads an avatar image to the Supabase Storage "avatars" bucket.
     *
     * @param username    used as the object key inside the bucket
     * @param imageBytes  raw file bytes
     * @param contentType MIME type (e.g. "image/jpeg")
     * @return public URL of the uploaded object
     */
    public String uploadAvatar(String username, byte[] imageBytes, String contentType) {
        String uploadUrl = supabaseUrl + "/storage/v1/object/avatars/" + username;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uploadUrl))
                .header("Authorization", "Bearer " + serviceRoleKey)
                .header("Content-Type", contentType)
                .header("x-upsert", "true")
                .PUT(HttpRequest.BodyPublishers.ofByteArray(imageBytes))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new RuntimeException(
                        "Supabase Storage upload failed with status " + response.statusCode() + ": " + response.body()
                );
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload avatar to Supabase Storage", e);
        }

        return supabaseUrl + "/storage/v1/object/public/avatars/" + username;
    }
}
