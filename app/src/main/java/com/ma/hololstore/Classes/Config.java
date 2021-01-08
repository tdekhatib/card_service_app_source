package com.ma.hololstore.Classes;


import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.ma.hololstore.Activity.NotifyActivity;
import com.ma.hololstore.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import androidx.core.app.NotificationCompat;


/**
 * Created by TDE on 2018-01-05.
 */

public class Config {

    public static final String TAG = "CARD-TAG";
    public static String dbName = "card_db";
    public static String shop_username="";
    public static int shop_price=0;

    static String k = "4nod24age21a323s";
    static  String sa="nos3ecdgtasdcv31";
    public static String shop_id="";
    public static String shop_name="";
    public static String shop_mobile="";
    public static double shop_account=0;
    public static int shop_coin=0;
    public static int country_id=0;
    public static String coin_name="نقطة";


    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
    public static String EM(String message)
    {
        ENC encryption = ENC.getDefault(k, sa, new byte[16]);
        String encrypted = encryption.encryptOrNull(message);
        return encrypted;
     }
    public static String DM(String cipherText)
    {
        ENC encryption = ENC.getDefault(k, sa, new byte[16]);
        String decrypted = encryption.decryptOrNull(cipherText);
        return  decrypted;
    }
    private static String key= "kemjs55#%&*25366";
    public static String Decrypt(String text)  {
        try {
             Cipher cipher = Cipher.getInstance
                    ("AES/CBC/PKCS5Padding"); //this parameters should not be changed
            byte[] keyBytes = new byte[16];
            byte[] b = key.getBytes("UTF-8");
            int len = b.length;
            if (len > keyBytes.length)
                len = keyBytes.length;
            System.arraycopy(b, 0, keyBytes, 0, len);
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] results = new byte[text.length()];
            try {
                results = cipher.doFinal(Base64.decode(text, 0));
            } catch (Exception e) {
                Log.i("Erron in Decryption", e.toString());
            }
            String ss =new String(results, "UTF-8");
            return  ss;// it returns the result as a String
        }catch (Exception ex)
        {}
        return "-1";
        }
    public static String Encrypt(String text )  {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] keyBytes = new byte[16];
            byte[] b = key.getBytes("UTF-8");
            int len = b.length;
            if (len > keyBytes.length)
                len = keyBytes.length;
            System.arraycopy(b, 0, keyBytes, 0, len);
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

            byte[] results = cipher.doFinal(text.getBytes("UTF-8"));
            return Base64.encodeToString(results, 0); // it returns the result as a String
        }catch (Exception ex){

        }
        return "-1";
    }
    public static void CreateDB(Context ctx) {
        try {
            SQLiteDatabase db = ctx.openOrCreateDatabase(Config.dbName, ctx.MODE_PRIVATE, null);

            db.execSQL("CREATE TABLE IF NOT EXISTS [values] (\n" +
                    "  [key] [nvaRCHAR(50)], \n" +
                    "  [val] [nvaRCHAR(50)]);\n");

            db.execSQL("CREATE TABLE IF NOT EXISTS [messages] (\n" +
                    "  [ID] INTEGER PRIMARY KEY AUTOINCREMENT , \n" +
                    "  [Title] [NVARCHAR(500)] , " +
                    "  [Body] [NVARCHAR(1000)] , " +
                    "  [Date1] [NVARCHAR(30)] " +
                    " );");
            if (GET_KEY(ctx,"slider").equals("No KEY")) {
                SaveKEY(ctx, "slider", "[]");}
            if (GET_KEY(ctx,"first").equals("No KEY")) {
                SaveKEY(ctx, "first", "1");}
            if (GET_KEY(ctx,"shop_name").equals("No KEY")) {
                SaveKEY(ctx, "shop_name", "");}
            if (GET_KEY(ctx,"shop_password").equals("No KEY")) {
                SaveKEY(ctx, "shop_password", "");}
            if (GET_KEY(ctx,"shop_username").equals("No KEY")) {
                SaveKEY(ctx, "shop_username", "");}
            if (GET_KEY(ctx,"remember").equals("No KEY")) {
                SaveKEY(ctx, "remember", "0");}
            if (GET_KEY(ctx,"font").equals("No KEY")) {
                SaveKEY(ctx, "font", "16");}

            if (GET_KEY(ctx,"address_mac").equals("No KEY")) {
                SaveKEY(ctx, "address_mac", "");}

            db.close();
        } catch (Exception ex) {}
    }
    public static void SaveKEY(Context ctx ,String key, String val) {
        try{
            SQLiteDatabase db = ctx.openOrCreateDatabase(dbName,Context.MODE_PRIVATE,null);
            Cursor cursor =db.rawQuery("select count(*) from [values] where [key]='"+EM(key)+"' ;",null);
            cursor.moveToFirst();
            if (cursor.getInt(0) >0)
            {
                db.execSQL("update [values] set [val]='"+EM(val)+"' where [key]='"+EM(key)+"' ;");
            }
            else
            {
                db.execSQL("insert into [values]([key],[val]) values ('"+EM(key)+"' , '"+EM(val)+"') ;");
            }
            db.close();
        }
        catch (Exception ex){}
    }
    public static String GET_KEY(Context ctx ,String key) {
        try{
            SQLiteDatabase db = ctx.openOrCreateDatabase(dbName,Context.MODE_PRIVATE,null);
            Cursor cursor =db.rawQuery("select [val] from [values] where [key]='"+EM(key)+"' ;",null);
            cursor.moveToFirst();
            String s = cursor.getString(cursor.getColumnIndex("val"));
            s = DM(s);
            db.close();
            return s;
        }
        catch (Exception ex){
            return  "No KEY";
        }
    }


    public static String Base64_String( String base64)
    {
        try {
        byte[] data = Base64.decode(base64, Base64.DEFAULT);

            String text = new String(data, "UTF-8");
            return  text;
        } catch (UnsupportedEncodingException e) {

        }
        return "";
    }



    public static String String_Base64( String text)
    {
        try {
            byte[] data = text.getBytes("UTF-8");
            String base64 = Base64.encodeToString(data, Base64.DEFAULT);
            return  base64;
        }catch (Exception ex)
        {}
        return  "";
        }

    public static void ExecuteNonQuery(Context ctx, String q) {
        try {

            SQLiteDatabase db = ctx.openOrCreateDatabase(dbName, ctx.MODE_PRIVATE, null);
            db.execSQL(q);
            db.close();
        } catch (Exception ex) {
            Log.d(TAG, "ExecuteNonQuery: "+ex.getMessage());
        }
    }

    public static String GET_IMEI(Context ctx) {
        TelephonyManager tm =
                (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        String androidId = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
return  androidId;
    }

     public static String GET_PATH(Context ctx) {
        String path = ctx.getFilesDir().getAbsolutePath() + "/files/";
        File f = new File(path);
        if (!f.exists())
            f.mkdirs();
        return path;
    }

    public static String ExecuteScalar(Context ctx, String query) {
        try {
            SQLiteDatabase db = ctx.openOrCreateDatabase(dbName, ctx.MODE_PRIVATE, null);
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
String s = cursor.getString(0);
if (s == null)
    return  "0";

                return  s;
            }
            db.close();
        } catch (Exception ex) {
            Log.d(TAG, "ExecuteScalar: "+ex.getMessage());
        }
        return "0";
    }




    public static boolean internetAvailable(Context ctx) {

        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


    public static String GET_DATE(Long aLong) {
        try{
        Date c = new Date(aLong*1000);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);
        return formattedDate;
    }catch (Exception ex){
            return "";
        }
    }
    public static String GET_DATE( ) {
        try{
            Date c = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(c);
            return formattedDate;
        }catch (Exception ex){
            return "";
        }
    }
    public static void show_msg(Context ctx,String msg) {

    new AlertDialog.Builder(ctx).setMessage(msg).setPositiveButton("موافق " , null).show();
    }




    public static void sendMyNotification(Context ctx , String title , String body) {

        try {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            String name="HOLOL-Store";
            final String CHANNEL_ID = "ID97";
            Intent c = new Intent(ctx, NotifyActivity.class);
            PendingIntent intent = PendingIntent.getActivity(ctx, 0,
                    c, 0);

            Notification notification = new NotificationCompat.Builder(ctx, CHANNEL_ID)
                    .setSmallIcon(R.drawable.app_icon)
                    .setContentTitle(title)
                    .setContentText(body)

                    .setContentIntent(intent)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(body))
                    .build();
            NotificationManager mNotificationManager =
                    (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                mNotificationManager.createNotificationChannel(mChannel);
            }
// Issue the notification.
            Random c1 = new Random();
            mNotificationManager.notify(c1.nextInt(9999) , notification);
        }catch (Exception ex){}

    }


    public static String IMAGE_TO_BASE64(ImageView img_get_picture) {

try {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    BitmapDrawable drawable = (BitmapDrawable) img_get_picture.getDrawable();
    Bitmap bitmap = drawable.getBitmap();

    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
    byte[] imageBytes = baos.toByteArray();
    String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
    return imageString;

}catch (Exception ex){}
return "";
}
}
