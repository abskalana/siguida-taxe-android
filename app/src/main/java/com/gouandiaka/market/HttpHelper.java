package com.gouandiaka.market;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
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
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

public class HttpHelper {


    public static final String TYPE_ENTITY = "entity";
    public static final String TYPE_PAYMENT = "paiement";
    public static final String REQUEST_POST = "https://siguidataxe.com/api/entity/create/";
    public static final String REQUEST_PAIEMENT = "https://siguidataxe.com/api/paiement/create/";
    public static String LOGIN_URL = "https://siguidataxe.com/api/mobileauth/";

    public static List<Entity> postEntity(String urlString, String jsonBody, String type) {
        int maxRetries = 3;
        int retryDelay = 3000;
        int attempt = 0;
        List<Entity> results = null;

        while (!Utils.isNotEmptyList(results) && attempt < maxRetries) {
            attempt++;

            String content = postContent(urlString, jsonBody);

            if (!Utils.isEmpty(content)) {
                results = Entity.parseList(content);
                if (Utils.isNotEmptyList(results)) {
                    LocalDatabase.instance().addRemoteEntity(results);
                    return results;
                }
            }

            if (attempt < maxRetries) {
                try {
                    Thread.sleep(retryDelay);
                } catch (Exception ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        return results;
    }

    public static String postContent(String urlString, String jsonBody) {
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

            return response.toString();


        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
    public static List<Entity> getEntities(String urlString, String annee, String mois, String property, String locality) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {

            String query = String.format("annee=%s&mois=%s&prop=%s&loc=%s",
                    URLEncoder.encode(annee, "UTF-8"),
                    URLEncoder.encode(mois, "UTF-8"),
                    URLEncoder.encode(property, "UTF-8"),
                    URLEncoder.encode(locality, "UTF-8")
            );

            URL url = new URL(urlString + "?" + query);

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
            List<Entity> b = HttpHelper.postEntity(HttpHelper.REQUEST_POST, content,TYPE_ENTITY);
            new Handler(Looper.getMainLooper()).post(() -> {
                requestListener.onSuccess(Utils.isNotEmptyList(b),b);
            });
        }).start();
    }



    public static void sendPaiement(Paiement paiement, RequestListener requestListener) {
        List<Paiement> paiments = new ArrayList<>();
        paiments.add(paiement);
        new Thread(() -> {
            Gson gson = new Gson();
            String content = gson.toJson(paiments);
            List<Entity> b  = HttpHelper.postEntity(HttpHelper.REQUEST_PAIEMENT,content, TYPE_PAYMENT);
            new Handler(Looper.getMainLooper()).post(() -> {
                requestListener.onSuccess(Utils.isNotEmptyList(b),b);
            });
        }).start();
    }

    public static void sendAll(Context context, RequestListener requestListener) {
        List<Entity> entities = LocalDatabase.instance().getModel();
        List<Paiement> paiements = LocalDatabase.instance().getPaiement();
        if(entities.isEmpty() && paiements.isEmpty()){
            Toast.makeText(context, "Vide",Toast.LENGTH_SHORT).show();
            requestListener.onSuccess(true, new ArrayList<>());
            return;
        }


        new Thread(() -> {
            Gson gson = new Gson();
            List<Entity> result = null;
            boolean success = false;
            if(Utils.isNotEmptyList(entities)){
                String content = gson.toJson(entities);
                result= HttpHelper.postEntity(HttpHelper.REQUEST_POST, content,TYPE_ENTITY);
                if(Utils.isNotEmptyList(result)) {
                    LocalDatabase.instance().clearLocaleTraffic();
                    success = true;
                }
            }

            if(Utils.isNotEmptyListPaiement(paiements)) {
                String content = gson.toJson(paiements);
                result = HttpHelper.postEntity(HttpHelper.REQUEST_PAIEMENT, content, TYPE_PAYMENT);
                if (Utils.isNotEmptyList(result)) {
                    success = Utils.isNotEmptyList(result);
                    LocalDatabase.instance().clearPaiement();
                }
            }

            boolean finalStatus = success;
            List<Entity> finalResult = result;
            new Handler(Looper.getMainLooper()).post(() -> {
                requestListener.onSuccess(finalStatus, finalResult);
            });
        }).start();
    }

    private static void loadEntity(String annee, String mois,String property, String locality,RequestListener requestListener) {
        new Thread(() -> {
            List<Entity> response = HttpHelper.getEntities(HttpHelper.REQUEST_POST,annee,mois,property,locality);
            new Handler(Looper.getMainLooper()).post(() -> {
                if(Utils.isNotEmptyList(response)){
                    LocalDatabase.instance().saveRemoteEntity(response);
                    requestListener.onSuccess(true,response);
                }else{
                    requestListener.onSuccess(false, null);
                }
            });

        }).start();
    }


    public static void reloadEntity(Context context, RequestListener requestListener){
        int defaultYear = Calendar.getInstance().get(Calendar.YEAR);
        String annee = PrefUtils.getString("annee", String.valueOf(defaultYear));
        String mois = PrefUtils.getString("mois");
        if(Utils.isEmpty(annee) || Utils.isSelectOrEmpty(mois)) {
            Toast.makeText(context,"Specifier l'année et le mois Config",Toast.LENGTH_SHORT).show();
            requestListener.onSuccess(true, new ArrayList<>());
            return;
        }

        String property = PrefUtils.getString("espace", "PRIVEE");
        String locality = PrefUtils.getString("place");
        if(Utils.isSelectOrEmpty(property) || Utils.isSelectOrEmpty(locality)) {
            Toast.makeText(context,"Specifier le type propriete et le quartier Config",Toast.LENGTH_SHORT).show();
            requestListener.onSuccess(true,new ArrayList<>());
            return;
        }

        HttpHelper.loadEntity(annee, mois,property, locality,requestListener);

    }

}
