package com.lokdonencryption.lokdonencryptionmodule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.lokdonencryption.lokdonencrypt.CipherControl;

import java.io.File;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    CipherControl cipher;
    Context c = MainActivity.this;
    private String raw_val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cipher  = new CipherControl();

        getBut(R.id.btn_encrypt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(getEdt(R.id.tinp_entry).getText().toString().trim())){
                   String encrypt =  cipher.encryptGenericData(getEdt(R.id.tinp_entry).getText().toString().trim());
                    getEdt(R.id.tinp_entry).setText(encrypt);
                }
                else{
                    Toast.makeText(c, " Oh you didnt add a text ", Toast.LENGTH_LONG).show();
                }
            }
        });

        getBut(R.id.btn_decrypt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(getEdt(R.id.tinp_entry).getText().toString().trim())){
                  String decrypt =   cipher.decryptGenericData(getEdt(R.id.tinp_entry).getText().toString().trim());
                    getEdt(R.id.tinp_entry).setText(decrypt);
                }
                else{
                    Toast.makeText(c, " Oh you didnt add a text ", Toast.LENGTH_LONG).show();
                }
            }
        });




    }

    private TextInputEditText getEdt(int id){
        return findViewById(id);
    }

    private Button getBut(int id){
        return findViewById(id);
    }

/*    *//**
     *this returns the android unique id
     * @param resolver
     * @return
     *//*
    private String getAndroidUniqueId(ContentResolver resolver){
        String unique_id = android.provider.Settings.Secure.getString(resolver, android.provider.Settings.Secure.ANDROID_ID);
        return unique_id;
    }


    private TextInputEditText getEdt(int id){
        return findViewById(id);
    }

    *//**
     * this returns the users input
     * @param id
     * @return
     *//*
    private String returnUserEntry(int id){
        if(!getEdt(R.id.tinp).getText().toString().trim().equals("") && !TextUtils.isEmpty(getEdt(R.id.tinp).getText().toString().trim())){
            raw_val = getEdt(R.id.tinp).getText().toString();
        }
        return raw_val;
    }

    *//**
     * this handles the demo of the data encryption
     * @param dataType
     * @param cipher
     * @param raw_val
     * @param ios
     * @param m3pin
     * @param sess_id
     * @throws Exception
     *//*
    private void handleEncryption(String dataType, CipherControl cipher, String raw_val, InputStream ios, String m3pin, String sess_id) throws Exception {
        switch (dataType){
            case "Text":
                encryptText(raw_val,cipher);
                break;
            case "Password":
                encryptPass(raw_val,cipher);
                break;
            case "SMS":
                encryptSms(raw_val,cipher,m3pin,sess_id);
                break;
            case "File":
                encryptFiles(ios,raw_val,cipher,m3pin);
                break;
            default:
                Toast.makeText(c , getString(R.string.no_val), Toast.LENGTH_LONG).show();
                break;
        }

    }

    *//**
     * this handles text encryption
     * @param raw_val
     * @param cipher
     * @return
     *//*
    private String encryptText(String raw_val, CipherControl cipher){
        getTv(R.id.tv_disp_encrypt).setText(cipher.encryptGenericData(raw_val));
        return cipher.encryptGenericData(raw_val);
    }

    *//**
     * this handles password encryption
     * @param raw_pass
     * @param cipher
     * @return
     *//*
    private String encryptPass(String raw_pass, CipherControl cipher){
        getTv(R.id.tv_disp_encrypt).setText(cipher.generatePassword(raw_pass,5));
        return cipher.generatePassword(raw_pass,5);
    }

    *//**
     * this handles sms encryption
     * @param raw_text
     * @param cipher
     * @param m3Pin
     * @param session_id
     * @return
     *//*
    private String encryptSms(String raw_text, CipherControl cipher, String m3Pin, String session_id){
        getTv(R.id.tv_disp_encrypt).setText(cipher.encryptNew(raw_text,m3Pin,session_id));
        return cipher.encryptNew(raw_text,m3Pin,session_id);
    }

    *//**
     * this handles file encryption
     * @param inputStream
     * @param raw_file_path
     * @param cipher
     * @param pin
     * @return
     * @throws Exception
     *//*
    private File encryptFiles(InputStream inputStream, String raw_file_path, CipherControl cipher, String pin) throws Exception {
        return cipher.encryptFile(inputStream, raw_file_path, pin);
    }

    *//**
     * this returns a textview object
     * @param id
     * @return
     *//*
    private TextView getTv(int id){
        return findViewById(id);
    }*/
}