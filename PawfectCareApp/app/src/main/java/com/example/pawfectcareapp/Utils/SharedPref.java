package com.example.pawfectcareapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SharedPref {

    public static final String SECURED_PREFS_KEY = "com.example.pawfectcareapp.secured_prefs";
    public static final String PREFS_KEY = "com.example.pawfectcareapp.pref_key";
    public static final String DEVICE_FIREBASE_TOKEN  = "com.example.pawfectcareapp.firebase_token";
    public static final String USERNAME  = "com.example.pawfectcareapp.user_name";
    public static final String PASSWORD  = "com.example.pawfectcareapp.password";
    public static final String USER_ID = "com.example.pawfectcareapp.user_id";
    public static final String FULLNAME = "com.example.pawfectcareapp.fullname";
    public static final String GENDER = "com.example.pawfectcareapp.gender";
    public static final boolean IS_ACTIVE = false;
    public static final boolean IS_READ_TC = false;
    public static final String EMAIL  = "com.example.pawfectcareapp.email";
    public static final String ROLE_ID  = "com.example.pawfectcareapp.role_id";
    public static final String TOKEN  = "com.example.pawfectcareapp.token";
    public static final String TOKEN_API  = "com.example.pawfectcareapp.token_api";
//    public static final String FREIGHT_TYPE  = "com.example.pawfectcareapp.token"; //1-AF, 2-SF



    public void writePrefString(Context context, String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPref.edit();
        edit.putString(key, value);
        edit.apply();
    }
    public void secureWritePrefBoolean(Context context, String key, Boolean value) throws GeneralSecurityException, IOException {
        getEncryptedPrefs(context).edit().putBoolean(key, value).apply();
    }

    public String readPrefString(Context context, String key){
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
        return sharedPref.getString(key, null);
    }

//    public boolean readPrefBoolean(Context context, String key) {
//        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
//        return sharedPref.getBoolean(key, false); // default is false if key doesn't exist
//    }

    public int readPrefInt(Context context, int key){
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
        return sharedPref.getInt(String.valueOf(key), -1);
    }

    public void removePrefs(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPref.edit();
        edit.remove(key).apply();
    }



    public void secureWritePrefString(Context context,String key , String value) throws GeneralSecurityException, IOException {
        getEncryptedPrefs(context).edit().putString(key, value).apply();
    }



    public String secureReadPrefString(Context context,String key) throws GeneralSecurityException, IOException {
        return getEncryptedPrefs(context).getString(key, null);
    }

    public void secureWritePrefInt(Context context,String key , int value) throws GeneralSecurityException, IOException {
        getEncryptedPrefs(context).edit().putInt(key, value).apply();
    }

    public int secureReadPrefInt(Context context,String key) throws GeneralSecurityException, IOException {
        return getEncryptedPrefs(context).getInt(key, -1);
    }


    public boolean secureReadPrefBoolean(Context context,String key) throws GeneralSecurityException, IOException {
        return getEncryptedPrefs(context).getBoolean(key, false);
    }

    public void secureRemovePrefs(Context context,String key) throws GeneralSecurityException, IOException {
        getEncryptedPrefs(context).edit().remove(key).apply();
    }


    public void  writePrefInt(Context context,String key, int value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPref.edit();
        edit.putInt(key, value);
        edit.apply();
    }

    public SharedPreferences getEncryptedPrefs(Context context) {
        MasterKey masterKey;
        try {
            masterKey = new MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException("Failed to get or create MasterKey", e);
        }

        try {
            return EncryptedSharedPreferences.create(
                    context,
                    SECURED_PREFS_KEY,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            // 🧹 Key is likely corrupted → delete prefs and retry
            context.deleteSharedPreferences(SECURED_PREFS_KEY);
            try {
                return EncryptedSharedPreferences.create(
                        context,
                        SECURED_PREFS_KEY,
                        masterKey,
                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                );
            } catch (GeneralSecurityException | IOException ex) {
                throw new RuntimeException("Re-creating EncryptedSharedPreferences failed after cleanup", ex);
            }
        }
    }


    Bitmap generateQRCode(String text) throws WriterException {
        int width = 500;
        int height = 500;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        MultiFormatWriter codeWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = codeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
            int n = 0;
            while (n < width) {
                int x = n++;
                int n2 = 0;
                while (n2 < height) {
                    int y = 0;
                    bitmap.setPixel(x, y, bitMatrix.get(x, y = n2++) ? -16777216 : -1);
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }
}
