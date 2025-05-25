package com.samcrm.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.StorageScopes;
import com.google.api.services.storage.model.StorageObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


public class CloudStorage {

    static Storage storage=null;
    public static void uploadFile(String bucketName, String filePath, String fileName)
    {
        try {
            Storage storage = getStorage();
            StorageObject object = new StorageObject();
            object.setBucket(bucketName);
            //File sdcard = Environment.getExternalStorageDirectory();
            File file = new File(filePath);

            InputStream stream = new FileInputStream(file);

            try {
                Log.d("GCS","Test");
                String contentType = URLConnection.guessContentTypeFromStream(stream);
                InputStreamContent content = new InputStreamContent(contentType, stream);

                Storage.Objects.Insert insert = storage.objects().insert(bucketName, null, content);
                insert.setName(fileName);
                insert.execute();

            } finally {
                stream.close();
            }
        }catch(Exception e)
        {
            class Local {}; Log.d("GCS","Sub: "+Local.class.getEnclosingMethod().getName()+" Error code: "+e.getMessage());

            e.printStackTrace();
        }
    }

    public static Bitmap downloadFile(String bucketName, String fileName, String destinationDirectory) throws Exception {

        InputStream stream = null;
        Bitmap image = null;
//        File directory = new File(destinationDirectory);
//        if (!directory.isDirectory()) {
//            throw new Exception("Provided destinationDirectory path is not a directory");
//        }
//        File file = new File(directory.getAbsolutePath() + "/" + fileName);

        Storage storage = getStorage();

        Storage.Objects.Get get = storage.objects().get(bucketName, fileName);
        //FileOutputStream stream = new FileOutputStream(file);
        try {
            HttpResponse response = get.executeMedia();
            if(response != null) {
                stream = response.getContent();
                image = BitmapFactory.decodeStream(stream);
            }
        } catch(Exception e) {
            Log.d("GCS downloadFile: ","image not available.");
        }
        return image;
    }

    private static Storage getStorage() {

        try {

            if (storage == null)
            {
                HttpTransport httpTransport = new NetHttpTransport();
                com.google.api.client.json.JsonFactory jsonFactory = new JacksonFactory();
                List<String> scopes = new ArrayList<>();
                scopes.add(StorageScopes.DEVSTORAGE_FULL_CONTROL);

                Credential credential = new GoogleCredential.Builder()
                        .setTransport(httpTransport)
                        .setJsonFactory(jsonFactory)
                        .setServiceAccountId("raagatechmusic@appspot.gserviceaccount.com") //Email
                        .setServiceAccountPrivateKeyFromP12File(getTempPkc12File())
                        .setServiceAccountScopes(scopes).build();

                storage = new Storage.Builder(httpTransport, jsonFactory,
                        credential).setApplicationName("raagatechmusic")
                        .build();
            }

            return storage;
        }catch(Exception e)
        {
            class Local {}; Log.d("GCS","Sub: "+Local.class.getEnclosingMethod().getName()+" Error code: "+e.getMessage());

        }
        Log.d("GCS","Storage object is null ");
        return null;
    }

    private static File getTempPkc12File() {
        try {
            // xxx.p12 export from google API console
            InputStream pkc12Stream = AppController.getInstance().getResources().getAssets().open("raagatechmusic-923963dffddc.p12");
            File tempPkc12File = File.createTempFile("temp_pkc12_file", "p12");
            OutputStream tempFileStream = new FileOutputStream(tempPkc12File);
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = pkc12Stream.read(bytes)) != -1) {
                tempFileStream.write(bytes, 0, read);
            }
            return tempPkc12File;
        }catch(Exception e)
        {
            class Local {}; Log.d("GCS","Sub: "+Local.class.getEnclosingMethod().getName()+" Error code: "+e.getMessage());

        }
        Log.d("GCS"," getTempPkc12File is null");
        return null;
    }
}
