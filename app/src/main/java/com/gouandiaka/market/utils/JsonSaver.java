package com.gouandiaka.market.utils;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Environment;

import com.google.gson.Gson;
import com.gouandiaka.market.LocalDatabase;
import com.gouandiaka.market.entity.Entity;
import com.gouandiaka.market.entity.Paiement;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class JsonSaver {


    private static File saveJsonToDocuments(Context context, String jsonData, String fileName) {
        try {
            // Créer le dossier "Documents/Siguiataxe" si nécessaire
            File dir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Siguiataxe");
            if (!dir.exists()) dir.mkdirs();

            // Créer le fichier
            File file = new File(dir, fileName);
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(jsonData.getBytes());
                fos.flush();
            }

            // Scanner le fichier pour qu'il apparaisse dans les pickers et explorateurs
            MediaScannerConnection.scanFile(context,
                    new String[]{file.getAbsolutePath()},
                    new String[]{"application/json"},
                    null);

            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Dump les données Entity et Paiement en JSON
     * dans des fichiers prêts à partager.
     */
    public static void dumpFile(Context context) {
        List<Entity> entities = LocalDatabase.instance().getBackupModel();
        List<Paiement> paiements = LocalDatabase.instance().getBackupPaiement();

        if (entities.isEmpty() && paiements.isEmpty()) return;

        Gson gson = new Gson();

        if (!entities.isEmpty()) {
            saveJsonToDocuments(context, gson.toJson(entities), "entity.json");
        }

        if (!paiements.isEmpty()) {
            saveJsonToDocuments(context, gson.toJson(paiements), "paiement.json");
        }
    }
}
