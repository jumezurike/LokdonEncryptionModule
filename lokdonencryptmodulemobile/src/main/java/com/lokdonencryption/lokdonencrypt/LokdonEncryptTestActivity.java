package com.lokdonencryption.lokdonencrypt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class LokdonEncryptTestActivity extends AppCompatActivity {
    CipherControl cipher;
    Context c = LokdonEncryptTestActivity.this;
    private String raw_val;
    private String dataType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lokdon_encrypt_test);
        //cipher  = new CipherControl();
        /*try {
            handleEncryption(returnDataType(), cipher, returnUserEntry(R.id.tinp), new InputStream() {
                @Override
                public int read() throws IOException {
                    return 0;
                }
            },"1713486","miasma2203CA?");
        } catch (Exception e) {
            Toast.makeText(c, " Error as a result of " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }*/
    }

    /**
     * This method is used for returning arrayAdapter
     *
     * @param arr_cont_id
     * @param lay_id
     * @param dropId
     * @return
     *//*
    private ArrayAdapter getArrayAdapter(Context c, int arr_cont_id, int lay_id, int dropId) {
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(c, arr_cont_id, lay_id);
        arrayAdapter.setDropDownViewResource(dropId);
        return arrayAdapter;
    }

    private void getSelecChurchBr() {

        ArrayAdapter arrAd = getArrayAdapter(this, R.array.data_type, android.R.layout.simple_spinner_item, android.R.layout.simple_dropdown_item_1line);
        Spinner churchspin = getLokdonSpin(R.id.lokd_encry_sel);
        churchspin.setAdapter(arrAd);
        churchspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dataType = (String) parent.getItemAtPosition(position);

                if (dataType.equalsIgnoreCase(getString(R.string.sel_data_type))) {
                    Toast.makeText(c, "Please select a valid data type ", Toast.LENGTH_LONG).show();
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

private String returnDataType(){
        return dataType;
}

    private Spinner getLokdonSpin(int id){
        return findViewById(id);
    }

    *//**
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