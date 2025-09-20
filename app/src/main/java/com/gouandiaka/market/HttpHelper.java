package com.gouandiaka.market;
import android.util.Log;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class HttpHelper {

    public static final String REQUEST_POST = "https://siguidataxe.com/api/entity/create/";
    public static String LOGIN_URL = "https://siguidataxe.com/api/mobileauth/";

    // --- POST avec JSON (pour créer ou modifier une entité) ---
    public static boolean postEntity(String urlString, String jsonBody) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);


            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line.trim());
            }
            in.close();
            String clean = response.toString().replace("\"", "");
            return "true".equalsIgnoreCase(response.toString())  || "true".equalsIgnoreCase(clean);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (connection != null) connection.disconnect();
        }
    }

    public static int login(String urlString, String username, String password) {
        HttpURLConnection connection = null;
        try {
            // Construire l’URL avec paramètres
            String query = String.format("username=%s&password=%s",
                    URLEncoder.encode(username, "UTF-8"),
                    URLEncoder.encode(password, "UTF-8"));

            URL url = new URL(urlString + "?" + query);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            // Lire la réponse
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "UTF-8")
            );
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line.trim());
            }
            in.close();

            return Integer.parseInt(response.toString());

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            if (connection != null) connection.disconnect();
        }
    }
}
