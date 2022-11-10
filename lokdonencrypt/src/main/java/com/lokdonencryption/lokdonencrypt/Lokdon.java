package com.lokdonencryption.lokdonencrypt;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.codec.binary.Base64;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Lokdon {
    private static CipherControl instance=null;
    private String uniqueId;
    private String API_KEY;
    public static int TYPE_GENERIC_STRING;
    public static int TYPE_PASSWORD;
    public static int TYPE_FILE;

    /**
     * this handles file encryption
     * @param context
     * @param apiKey
     * @return
     */
    public Lokdon(Context context, String apiKey) throws Exception{
        String uniqueId= Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        if(instance==null){
            instance=new CipherControl();
        }
        if(uniqueId==null){
            uniqueId=instance.generatePassword("19347s9234751636366LOK",2);
        }
        verifyAPIKeyCall(context,apiKey,uniqueId);
        //SharedPreferences pref=context.getSharedPreferences("lokdon",Context.MODE_PRIVATE);
        //pref.edit().putString("API_STATE","APPROVED").apply();

    }


    private void verifyAPIKeyCall(Context context, String apiKey, String androidId){
        String endpoint="https://activation.lokdon.com/activation/api/api_verify.php";
        MultipartBody.Builder builder=new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        builder.addFormDataPart("api_key",apiKey);
        builder.addFormDataPart("androidId",androidId);
        builder.addFormDataPart("API_SECRET",instance.encryptGenericData(androidId));
        RequestBody body=builder.build();
        Request request=new Request.Builder()
                .url(endpoint)
                .post(body)
                .build();

        OkHttpClient client=new OkHttpClient();
        Call callback=client.newCall(request);
        callback.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

                try {
                    verifyAPIkey(context, apiKey);
                }catch(Exception ex){
                    Log.e("lokdon","Your api verification failed",ex);
                }
                //SharedPreferences pref=context.getSharedPreferences("lokdon",Context.MODE_PRIVATE);
               // pref.edit().putString("API_STATE","FAILED").apply();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    JSONParser jp = new JSONParser();
                    String body=response.body().string();
                    Log.d("lokdon_test","api response: "+body);
                    JSONObject jo = (JSONObject) jp.parse(body);
                    if (jo != null) {
                        String status = jo.get("status").toString();
                        if (status.equalsIgnoreCase("success")) {
                            SharedPreferences pref = context.getSharedPreferences("lokdon", Context.MODE_PRIVATE);
                            pref.edit().putString("API_STATE", "APPROVED").apply();
                        } else {
                            SharedPreferences pref = context.getSharedPreferences("lokdon", Context.MODE_PRIVATE);
                            pref.edit().putString("API_STATE", "EXPIRED").apply();
                            Toast.makeText(context, "LokDon SDK, " + jo.get("message").toString(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        SharedPreferences pref = context.getSharedPreferences("lokdon", Context.MODE_PRIVATE);
                        pref.edit().putString("API_STATE", "INVALID").apply();
                        Toast.makeText(context, "LokDon SDK, " + jo.get("message").toString(), Toast.LENGTH_LONG).show();
                    }

                } catch (Exception ex) {
                    Log.e("lokdon", "error", ex);
                }
            }
        });
    }
    private void verifyAPIkey(Context context,String apiKey) throws Exception {
        String status=getSavedKey(context);
        if(status.equalsIgnoreCase("NONE")) {
            if (apiKey.length() > 0 && apiKey.contains("##")) {
                String[] parts = apiKey.split("##");
                String keyPart = parts[0];
                String idPart = parts[1];
                String plan = parts[2];
                String key = "LOK9294ASD@#$RSFALJ@#$LAJSDFJ@#$%^@#$ASFD*@$&)&$%&*)@#$";
                if (!instance.verifyKey(key, keyPart))
                    throw new Exception("Invalid or Expired API Key");
            } else {
                instance = null;
                throw new Exception("Invalid API Key");
            }
        }

    }
    private String getSavedKey(Context context){
        SharedPreferences pref=context.getSharedPreferences("lokdon",Context.MODE_PRIVATE);
        return pref.getString("API_STATE","NONE");
    }

    public String encryptPayload(String data){
        String encoded= Base64.encodeBase64String(data.getBytes(StandardCharsets.UTF_8));
        return CipherControl.getInstance().encryptData(encoded);
    }
    public String decryptPayload(String cipherText){
        String encoded=CipherControl.getInstance().decryptData(cipherText);
        return new String(Base64.decodeBase64(encoded),StandardCharsets.UTF_8);
    }


    /**
     * this handles string encryption
     * @param plainText
     * @return encryptedText
     * @throws Exception
     */
    public String encryptString(Context context,String plainText) throws Exception{
        if(!isVerified(context))
            throw new Exception("API Key invalid or quota exceeded for LokDon sdk, please visit https://activation.lokdon.com");
        if(plainText!=null){
            instance= CipherControl.getInstance();
            return instance.encryptGenericData(plainText);
        }
        return null;
    }

    /**
     * this handles string decryption
     * @param encrypted
     * @return plainText
     * @throws Exception
     */
    public String decryptString(Context context,String encrypted) throws Exception{
        if(!isVerified(context))
            throw new Exception("API Key invalid or quota exceeded for LokDon sdk, please visit https://activation.lokdon.com");
        if(encrypted!=null){
            instance= CipherControl.getInstance();
            return instance.decryptGenericData(encrypted);
        }
        return null;
    }

    /**
     * this handles file encryption
     * @param fis
     * @param key
     * @param outputFilePath
     * @return encryptedText
     * @throws Exception
     */
    public File encryptFile(Context context, FileInputStream fis, String key, String outputFilePath) throws Exception{
        if(!isVerified(context))
            throw new Exception("API Key invalid or quota exceeded for LokDon sdk, please visit https://activation.lokdon.com");
        if(fis!=null){
            instance= CipherControl.getInstance();
            return instance.encryptFile(fis,key,outputFilePath);
        }
        return null;
    }

    /**
     * this handles file encryption
     * @param fis
     * @return encryptedText
     * @throws Exception
     */
    public File decryptFile(Context context, FileInputStream fis, String key, String outputFilePath) throws Exception{
        if(!isVerified(context))
            throw new Exception("API Key invalid or quota exceeded for LokDon sdk, please visit https://activation.lokdon.com");
        if(fis!=null){
            instance= CipherControl.getInstance();
            return instance.decryptFile(fis,key,outputFilePath);
        }
        return null;
    }

    private boolean isVerified(Context context) {
        String status=getSavedKey(context);
        if(status.equalsIgnoreCase("APPROVED")){
            return true;
        }
        return false;
    }
    private String encryptPassword(Context context, String plainText, int SALT) throws Exception{
        if(!isVerified(context))
            throw new Exception("API Key invalid or quota exceeded for LokDon sdk, please visit https://activation.lokdon.com");
        if(plainText!=null){
            instance= CipherControl.getInstance();
            return instance.generatePassword(plainText,SALT);
        }
        return null;
    }
    private String decryptPassword(Context context, String encrypted) throws Exception{
        if(!isVerified(context))
            throw new Exception("API Key invalid or quota exceeded for LokDon sdk, please visit https://activation.lokdon.com");
        if(encrypted!=null){
            instance= CipherControl.getInstance();
            return instance.decryptPassword(encrypted);
        }
        return null;
    }


}
