package com.gouandiaka.market;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gouandiaka.market.entity.Entity;
import com.gouandiaka.market.entity.Paiement;
import com.gouandiaka.market.utils.PrefUtils;
import com.gouandiaka.market.utils.RequestListener;
import com.gouandiaka.market.utils.Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class HttpHelper {


    public static final String REQUEST_POST = "https://siguidataxe.com/api/entity/create/";
    public static final String REQUEST_PAIEMENT = "https://siguidataxe.com/api/paiement/create/";
    public static String LOGIN_URL = "https://siguidataxe.com/api/mobileauth/";
    public static String BACKUP_URL = "https://siguidataxe.com/api/backup/";

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
            return "true".equalsIgnoreCase(response.toString()) || "true".equalsIgnoreCase(clean);

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
            Type listType = new TypeToken<List<Entity>>() {
            }.getType();

            List<Entity> entityList = gson.fromJson(response.toString(), listType);
            return entityList;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (Exception ignored) {
            }
            if (connection != null) connection.disconnect();
        }
    }

    public static void sendEnRegistrement(Entity entity, RequestListener requestListener) {
        List<Entity> entities = new ArrayList<>();
        entities.add(entity);
        new Thread(() -> {
            Gson gson = new Gson();
            String content = gson.toJson(entities);
            boolean b = HttpHelper.postEntity(HttpHelper.REQUEST_POST, content);
            new Handler(Looper.getMainLooper()).post(() -> {
                requestListener.onSuccess(b);
            });
        }).start();
    }



    public static void sendPaiement(Paiement entity, RequestListener requestListener) {
        List<Paiement> entities = new ArrayList<>();
        entities.add(entity);
        new Thread(() -> {
            Gson gson = new Gson();
            String content = gson.toJson(entities);
            boolean b = HttpHelper.postEntity(HttpHelper.REQUEST_PAIEMENT, content);
            new Handler(Looper.getMainLooper()).post(() -> {
                requestListener.onSuccess(b);
            });
        }).start();
    }

    public static void sendAll(Context context, RequestListener requestListener) {
        List<Entity> entities = LocalDatabase.instance().getModel();
        List<Paiement> paiements = LocalDatabase.instance().getPaiement();
        if(entities.isEmpty() && paiements.isEmpty()){
            Toast.makeText(context, "Vide",Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            Gson gson = new Gson();
            boolean status = false;
            if(!entities.isEmpty()){
                String content = gson.toJson(entities);
                status= HttpHelper.postEntity(HttpHelper.REQUEST_POST, content);
                if(status) LocalDatabase.instance().clearLocaleTraffic();
            }

            if(!paiements.isEmpty()){
                String content = gson.toJson(paiements);
                status = status && HttpHelper.postEntity(HttpHelper.REQUEST_PAIEMENT, content);
                if(status) LocalDatabase.instance().clearPaiement();
            }

            boolean finalStatus = status;
            new Handler(Looper.getMainLooper()).post(() -> {
                requestListener.onSuccess(finalStatus);
            });
        }).start();
    }

    public static void loadEntity(Activity context, View view, TextView countView) {
        view.setVisibility(View.VISIBLE);
        new Thread(() -> {

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

    public static void postBackup(Activity context, View view) {

    }

}
