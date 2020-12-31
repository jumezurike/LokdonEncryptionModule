package com.lokdonencryption.lokdonencryptionmodule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import com.lokdonencryption.lokdonencrypt.Lokdon;

import java.io.File;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    Lokdon instance;
    Context c = MainActivity.this;
    private String raw_val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            instance = new Lokdon(this, "API_KEY");
            String encrypted=instance.encryptString(this,"hello world");
            Log.e("lokdon_test","encrypted: "+encrypted);
        }catch(Exception ex){
            Log.e("lokdon_test","Error: ",ex);
        }
    }

    private TextInputEditText getEdt(int id){
        return findViewById(id);
    }

    /**
     * this returns the users input
     * @param id
     * @return
     */
    private String returnUserEntry(int id){
        if(!getEdt(R.id.tinp).getText().toString().trim().equals("") && !TextUtils.isEmpty(getEdt(R.id.tinp).getText().toString().trim())){
            raw_val = getEdt(R.id.tinp).getText().toString();
        }
        return raw_val;
    }

    /**
     * this handles the demo of the data encryption
     * @param dataType
     * @param cipher
     * @param raw_val
     * @param ios
     * @param m3pin
     * @param sess_id
     * @throws Exception
     */
    /*
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

    }*/

    /**
     * this handles text encryption
     * @param raw_val
     * @param cipher
     * @return
     */
    /*
    private String encryptText(String raw_val, CipherControl cipher){
        getTv(R.id.tv_disp_encrypt).setText(cipher.encryptGenericData(raw_val));
        return cipher.encryptGenericData(raw_val);
    }*/

    /**
     * this handles password encryption
     * @param raw_pass
     * @param cipher
     * @return
     */
    /*
    private String encryptPass(String raw_pass, CipherControl cipher){
        getTv(R.id.tv_disp_encrypt).setText(cipher.generatePassword(raw_pass,5));
        return cipher.generatePassword(raw_pass,5);
    }*/

    /**
     * this handles sms encryption
     * @param raw_text
     * @param cipher
     * @param m3Pin
     * @param session_id
     * @return
     */
    /*
    private String encryptSms(String raw_text, CipherControl cipher, String m3Pin, String session_id){
        getTv(R.id.tv_disp_encrypt).setText(cipher.encryptNew(raw_text,m3Pin,session_id));
        return cipher.encryptNew(raw_text,m3Pin,session_id);
    }*/

    /**
     * this handles file encryption
     * @param inputStream
     * @param raw_file_path
     * @param cipher
     * @param pin
     * @return
     * @throws Exception
     */
    /*
    private File encryptFiles(InputStream inputStream, String raw_file_path, CipherControl cipher, String pin) throws Exception {
        return cipher.encryptFile(inputStream, raw_file_path, pin);
    }*/

    /**
     * this returns a textview object
     * @param id
     * @return
     */
    private TextView getTv(int id){
        return findViewById(id);
    }
}