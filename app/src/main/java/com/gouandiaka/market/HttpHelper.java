package com.gouandiaka.market;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gouandiaka.market.entity.Entity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class HttpHelper {

    public static final String REQUEST_POST = "https://siguidataxe.com/api/entity/create/";
    public static final String REQUEST_PAIEMENT = "https://siguidataxe.com/api/paiement/create/";
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

    // --- GET pour récupérer la liste des entités ---
    public static List<Entity> getEntities(String urlString) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            // Vérifier le code de réponse
            int status = connection.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                return null; // ou gérer l'erreur
            }

            // Lire la réponse
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line.trim());
            }
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Entity>>() {}.getType();

            List<Entity> entityList = gson.fromJson(response.toString(), listType);
            return entityList;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (Exception ignored) {}
            if (connection != null) connection.disconnect();
        }
    }

    public static   String   makeRequest(){
        String content = LocalDatabase.instance().getModel();
        boolean b1 = true;
        if(!Utils.isEmpty(content)){
            b1 = HttpHelper.postEntity(HttpHelper.REQUEST_POST,content);
            if(b1){
                LocalDatabase.instance().clearLocaleTraffic();
            }
        }
        if(!b1){
            return "Echec enregistrement";
        }

        String result = LocalDatabase.instance().getPaiement();
        if(!Utils.isEmpty(result)){
            b1 = HttpHelper.postEntity(HttpHelper.REQUEST_PAIEMENT,result);
            if(b1){
                LocalDatabase.instance().clearPaiement();
            }
        }
        if(!b1) {
            return "Echec  Paiement";
        }

        return null;

    }

    public static void syncEntity(Activity context, View view){
        view.setVisibility(View.VISIBLE);
        new Thread(() -> {
            String content = LocalDatabase.instance().getModel();
            if(!Utils.isEmpty(content)){
                HttpHelper.postEntity(HttpHelper.REQUEST_POST,content);
            }
            List<Entity> response = HttpHelper.getEntities(HttpHelper.REQUEST_POST);

            if (response == null || response.isEmpty()) {
                context.runOnUiThread(() -> {
                    view.setVisibility(View.GONE);
                    Toast.makeText(context, "Erreur ", Toast.LENGTH_SHORT).show();
                });
            } else {
                context.runOnUiThread(() -> {
                    view.setVisibility(View.GONE);
                    LocalDatabase.instance().addRemoveEntity(response);
                    Toast.makeText(context, "SUCCESS", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

}
