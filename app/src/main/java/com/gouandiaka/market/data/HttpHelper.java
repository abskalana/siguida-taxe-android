package com.gouandiaka.market.data;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gouandiaka.market.entity.Entity;
import com.gouandiaka.market.entity.Localdata;
import com.gouandiaka.market.entity.Paiement;
import com.gouandiaka.market.utils.PrefUtils;
import com.gouandiaka.market.utils.RequestListener;
import com.gouandiaka.market.utils.Utils;
import com.gouandiaka.market.utils.Validator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HttpHelper {


    public static final String REQUEST_POST = "https://siguidataxe.com/api/entity/create/";
    public static final String REQUEST_POST_DATA = "https://siguidataxe.com/api/localdata/";
    public static final String REQUEST_PAIEMENT = "https://siguidataxe.com/api/paiement/create/";
    public static String LOGIN_URL = "https://siguidataxe.com/api/mobileauth/";


    public static boolean postEntityForSuccess(String urlString, String jsonBody) {
        int maxRetries = 3;
        int retryDelay = 3000;
        int attempt = 0;
        while (attempt < maxRetries) {
            attempt++;

            String content = postContent(urlString, jsonBody,null);

            if ("true".equalsIgnoreCase(content)) {
                return true;
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

        return false;
    }


    public static Entity postEntity(String urlString, String jsonBody, String pk) {
        int maxRetries = 3;
        int retryDelay = 3000;
        int attempt = 0;
        while (attempt < maxRetries) {
          attempt++;

            String content = postContent(urlString, jsonBody,pk);

            if (!Utils.isEmpty(content)) {
                Entity  entity = Entity.parseList(content);
                if (entity!=null) {
                    LocalDatabase.instance().addRemoteEntity(entity);
                    return entity;
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

        return null;
    }


    public static String postContent(String urlString, String jsonBody, String pk) {
        HttpURLConnection connection = null;
        try {


            if(!Utils.isEmpty(pk)){
                if (!urlString.endsWith("/")) {
                    urlString += "/";
                }
                URL url = new URL(urlString + pk+ "/");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
            }else{
                URL url = new URL(urlString);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
            }

            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);


            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
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
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)
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
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
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
       new Thread(() -> {
            Gson gson = new Gson();
            String content = gson.toJson(entity);
            Entity b = HttpHelper.postEntity(HttpHelper.REQUEST_POST, content,null);
            new Handler(Looper.getMainLooper()).post(() -> {
                if(requestListener!=null) requestListener.onSuccess(Validator.isValidRemote(b),b);
            });
        }).start();
    }



    public static void sendPaiement(Paiement paiement, RequestListener requestListener) {
       new Thread(() -> {
            Gson gson = new Gson();
            String content = gson.toJson(paiement);
            Entity b  = HttpHelper.postEntity(HttpHelper.REQUEST_PAIEMENT,content, paiement.getId());
            new Handler(Looper.getMainLooper()).post(() -> {
                if(requestListener!=null) requestListener.onSuccess(Validator.isValidRemote(b),b);
            });
        }).start();
    }



    private static void loadEntity(String annee, String mois,String property, String locality,RequestListener requestListener) {
        new Thread(() -> {
            List<Entity> response = HttpHelper.getEntities(HttpHelper.REQUEST_POST,annee,mois,property,locality);
            new Handler(Looper.getMainLooper()).post(() -> {
                if(Utils.isNotEmptyList(response)){
                    LocalDatabase.instance().saveRemoteEntity(response);
                    requestListener.onSuccess(true,null);
                }else{
                    requestListener.onSuccess(false, null);
                }
            });

        }).start();
    }


    public static void reloadEntity(Context context, RequestListener requestListener){
        int annee = PrefUtils.getAnnee();
        String mois = PrefUtils.getString("mois");
        if(annee < 2025 || Utils.isSelectOrEmpty(mois)) {
            Toast.makeText(context,"Specifier l'année et le mois Config",Toast.LENGTH_SHORT).show();
            requestListener.onSuccess(true, null);
            return;
        }

        String property = PrefUtils.getString("espace", "PRIVEE");
        String locality = PrefUtils.getString("place");
        if(Utils.isSelectOrEmpty(property) || Utils.isSelectOrEmpty(locality)) {
            Toast.makeText(context,"Specifier le type propriete et le quartier Config",Toast.LENGTH_SHORT).show();
            requestListener.onSuccess(true,null);
            return;
        }

        HttpHelper.loadEntity(String.valueOf(annee), mois,property, locality,requestListener);

    }

    public static void sendAll(RequestListener requestListener) {
        List<Entity> entities = LocalDatabase.instance().getModel();
        List<Paiement> paiements = LocalDatabase.instance().getPaiement();
        if((entities == null || entities.isEmpty()) && (paiements == null || paiements.isEmpty())){
            requestListener.onSuccess(true, null);
        }
        Localdata localdata = new Localdata(entities,paiements);
        new Thread(() -> {
            Gson gson = new Gson();
            String content = gson.toJson(localdata);
            boolean b = HttpHelper.postEntityForSuccess(HttpHelper.REQUEST_POST_DATA, content);
            new Handler(Looper.getMainLooper()).post(() -> {
                if(requestListener!=null) requestListener.onSuccess(b,null);
            });
        }).start();
    }

}
