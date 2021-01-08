package com.ma.hololstore.Server;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.ma.hololstore.Activity.SplashActivity;
import com.ma.hololstore.Classes.Config;
import com.ma.hololstore.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class SERVER_INFO2 {
    private static final String GET_ALL_CATEGORY ="GET_ALL_CATEGORY" ;
    private static final String LOGIN_TO_SHOP = "LOGIN_TO_SHOP";
    private static final String GET_ALL_COINS = "GET_ALL_COINS";
    private static final String GET_ALL_COUNTERIES = "GET_ALL_COUNTERIES";
    private static final String SINGUP_FOR_SHOP ="SINGUP_FOR_SHOP" ;
    private static final String GET_ALL_CARD = "GET_ALL_CARD";
    private static final String GET_ALL_SUPPORT = "GET_ALL_SUPPORT";
    public static final String SEARCH = "SEARCH";
    private static final String GET_ALL_COMMENT = "GET_ALL_COMMENT";
    private static final String SAVE_NEW_COMMETS = "SAVE_NEW_COMMETS";
    private static final String GET_ALL_SERVICES = "GET_ALL_SERVICES";
    private static final String REQUEST_NEW_SERVICE = "REQUEST_NEW_SERVICE";
    private static final String SAVE_NEW_SUPPORTS = "SAVE_NEW_SUPPORTS";
    private static final String GET_ALL_MY_REQUEST = "GET_ALL_MY_REQUEST";
    private static final String DELETE_MY_REQUEST = "DELETE_MY_REQUEST";
    private static final String GET_MY_SELLERS ="GET_MY_SELLERS" ;
    private static final String BUY_NEWS_CARDS ="BUY_NEWS_CARDS" ;
    private static final String GET_ALL_SERVICE_FROM_CATEGORY = "GET_ALL_SERVICE_FROM_CATEGORY" ;
    private static final String GET_CLIENTS ="GET_CLIENTS" ;
    private static final String SEND_MONEY_FROM_RASSED_TO_ANTHOR = "SEND_MONEY_FROM_RASSED_TO_ANTHOR";
    private static final String UPDATE_TOKEN = "UPDATE_TOKEN";
    private static final String REQUEST_MONEY = "SAVE_NEW_MONEY_REQUEST";
    private static final String LOGOUT ="LOGOUT" ;
    private static final String GET_ALL_SLIDER = "GET_ALL_SLIDER";
    private static final String CHANGE_PASSWORD ="CHANGE_PASSWORD" ;
    //   public static final String URL2= "http://192.168.43.30/CARDSERVICE/";
     public static final String URL2= "https://hololstore.com/";
    public static final String IMAGE_SERVER = URL2+"files/" ;
    private static final String UPDATE_PROFILE = "UPDATE_PROFILE" ;
    private static final String LOAD_ALL_RASSED_TRANS ="LOAD_ALL_RASSED_TRANS" ;
    public static String WEB_SERVCE_URL=URL2+"WebService.asmx";
    public  static String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";
    private static String UPLOAD_TO_SERVER(String method,String id ,String data,Context ctx)
    {

        try {
            id = Config.Encrypt(id);
            data = Config.Encrypt(data);
            SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, method);
            PropertyInfo pi;
            pi = new PropertyInfo();
            pi.setName("id");
            pi.setValue(id);
            pi.setType(String.class);
            request.addProperty(pi);
            pi = new PropertyInfo();
            pi.setName("data");
            pi.setValue(data);
            pi.setType(String.class);
            request.addProperty(pi);
            pi = new PropertyInfo();
            pi.setName("token");
            pi.setValue(Config.GET_IMEI(ctx));
            pi.setType(String.class);
            request.addProperty(pi);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;

            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransport = new HttpTransportSE(WEB_SERVCE_URL);
            Object response = null;
            try {

                httpTransport.call(WSDL_TARGET_NAMESPACE + method, envelope);
                response = envelope.getResponse();
            } catch (Exception exception) {
                response = "Error:" + exception.toString();
                Log.i(TAG, "UPLOAD_TO_SERVER:8 "+response);
                return response.toString();
            }
            String result = response.toString();
            result = Config.Decrypt(result);
            Log.i(TAG, "UPLOAD_TO_SERVER:8 "+result);
            return result;
        }catch (Exception ex)
        {
            return "error";
        }
    }
/*

    public static void GET_ALL_CATEGORY_DATA(Context ctx)
{
    try{
        String res = UPLOAD_TO_SERVER(GET_ALL_CATEGORY,"","");
        save_result_category(ctx,res);
      //  Log.i(TAG, "GET_ALL_CATEGORY_DATA: "+res);
    }catch (Exception ex)
    {}

}*/


static String notification="";
    private static void save_result_news(Context ctx, String res) {
        try {
            JSONArray arr = new JSONArray(res);

            SQLiteDatabase db = ctx.openOrCreateDatabase(Config.dbName, Context.MODE_PRIVATE, null);
          //  db.execSQL("delete from news;");

            for (int i=0;i<arr.length();i++)
            {
                JSONObject obj = arr.getJSONObject(i);
                db.execSQL("insert into news values('"+obj.getString("news_id")
                        +"','"+obj.getString("news_title")
                        +"','"+obj.getString("news_url")
                        +"','"+obj.getString("news_date")
                        +"','"+obj.getString("news_author")
                        +"','"+obj.getString("source_id")
                        +"','"+ Config.String_Base64( obj.getString("news_desc"))
                        +"','"+obj.getString("news_image")
                        +"','"+"0"+"') ;");
                if (Config.GET_KEY(ctx,"notify").equals("1"))
                {
                    if (Config.ExecuteScalar(ctx,"select follow from sources where source_id="+obj.getString("source_id")).toString().equals("1"))
                           notification +=obj.getString("news_title")+"\n";
                }
            }
db.close();
           if (Config.GET_KEY(ctx,"notify").equals("1"))
            {
              if (notification.length()>5)
                  show_notification(ctx,notification);
            }
        }catch (Exception ex){
            Log.i(TAG, "save_result_news: "+ex.getMessage());
        }
    }

    private static void show_notification(Context ctx, String notification) {

        try {
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int notifyID = 1;
                String CHANNEL_ID = "my_channel_01";// The id of the channel.
                CharSequence name = ctx.getString(R.string.app_name);// The user-visible name of the channel.
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
// Create a notification and set the notification channel.
                Intent notificationIntent = new Intent(ctx, SplashActivity.class);

                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent intent = PendingIntent.getActivity(ctx, 0,
                        notificationIntent, 0);
                Notification notification2 = null;
                if (Config.GET_KEY(ctx, "notify_sound").equals("1")) {
                    notification2 = new Notification.Builder(ctx)
                            .setContentTitle(ctx.getString(R.string.app_name))
                            .setContentText(notification)
                            .setSmallIcon(R.drawable.app_icon)
                            .setChannelId(CHANNEL_ID)
                            .setContentIntent(intent)
                            .setSound(alarmSound)
                            .build();
                } else

                {
                    notification2 = new Notification.Builder(ctx)
                            .setContentTitle(ctx.getString(R.string.app_name))
                            .setContentText(notification)
                            .setSmallIcon(R.drawable.app_icon)
                            .setChannelId(CHANNEL_ID)
                            .setContentIntent(intent)

                            .build();

                }
                notification2.flags |= Notification.FLAG_AUTO_CANCEL;
                NotificationManager mNotificationManager =
                        (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

                mNotificationManager.createNotificationChannel(mChannel);
                mNotificationManager.notify(notifyID, notification2);

            } else {

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx)
                        .setSmallIcon(R.drawable.app_icon) // notification icon
                        .setContentTitle(ctx.getString(R.string.app_name)) // title for notification

                        .setContentText(notification) // message for notification
                        .setAutoCancel(true); // clear notification after click
                if (Config.GET_KEY(ctx, "notify_sound").equals("1"))
                    mBuilder.setSound(alarmSound);
                Intent intent = new Intent(ctx, SplashActivity.class);
                @SuppressLint("WrongConstant") PendingIntent pi = PendingIntent.getActivity(ctx, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
                mBuilder.setContentIntent(pi);

                NotificationManager mNotificationManager =
                        (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(0, mBuilder.build());
            }
            notification = "";

        }catch (Exception ex){
            Log.i(TAG, "show_notification: "+ex.getMessage());
        }
        }
        String CHANNEL_ID = "my_channel_01";



    private static void save_result_source(Context ctx, String res) {
        try {
            JSONArray arr = new JSONArray(res);

            SQLiteDatabase db = ctx.openOrCreateDatabase(Config.dbName, Context.MODE_PRIVATE, null);
            ArrayList tmp = new ArrayList();
            Cursor cursor = db.rawQuery("select * from sources ", null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    tmp.add(cursor.getString(cursor.getColumnIndex("source_id")) + "," + cursor.getString(cursor.getColumnIndex("follow")) + "," + cursor.getString(cursor.getColumnIndex("notify")));
                    cursor.moveToNext();
                }

            }

            db.execSQL("delete from sources;");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                db.execSQL("insert into sources values('" + obj.getString("source_id")
                        + "','" + obj.getString("source_name")
                        + "','" + obj.getString("source_url")
                        + "','" + obj.getString("category_id")
                        + "','" + obj.getString("source_image")
                        + "'," + "1"
                        + "," + "1" + ") ;");
            }
            for (int i = 0; i < tmp.size(); i++) {
                String[] ss = tmp.get(i).toString().split(",");
                db.execSQL("update sources set follow=" + ss[1] + " ,notify=" + ss[2] + "   where source_id=" + ss[0]);
            }
        } catch (Exception ex) {
        }
    }


    private static void save_result_category(Context ctx, String res) {
        try {
            JSONArray arr = new JSONArray(res);

            SQLiteDatabase db = ctx.openOrCreateDatabase(Config.dbName, Context.MODE_PRIVATE, null);
            db.execSQL("delete from categories");
        for (int i=0;i<arr.length();i++)
        {
            JSONObject obj = arr.getJSONObject(i);
            db.execSQL("insert into categories values('"+obj.getString("category_id")+"','"+obj.getString("category_name")+"','"+obj.getString("category_image")+"') ;");
        }

        }catch (Exception ex){}
        }


    public static String LOGIN_SHOP(String username, String password,String shop_serial, Context ctx) {
        String data   = "{\"shop_serial\":\""+shop_serial+"\",\"shop_password\":\""+password+"\"}";

        String res = UPLOAD_TO_SERVER(LOGIN_TO_SHOP, username,data,ctx);
    return  res;
    }

    public static String GET_COINS(Context ctx) {
        String res = UPLOAD_TO_SERVER(GET_ALL_COINS, "","",ctx);
        return  res;
    }

    public static String GET_COUNTRIES(Context ctx) {
        String res = UPLOAD_TO_SERVER(GET_ALL_COUNTERIES, "","",ctx);
        return  res;
    }

    public static String SIGN_UP(String shop_username, String shop_password, String shop_mobile, String shop_address, String shop_name, String shop_type,String shop_serial, Context ctx) {
      String data   = "{\"shop_name\":\""+shop_name+"\",\"shop_password\":\""+
              shop_password+
              "\",\"shop_mobile\":\"" + shop_mobile +
              "\",\"shop_serial\":\"" + shop_serial +
              "\",\"shop_type\":\"" + shop_type +
              "\",\"shop_address\":\"" + shop_address +
              "\",\"shop_username\":\"" + shop_username + "\"}";

        String res = UPLOAD_TO_SERVER(SINGUP_FOR_SHOP ,"",data ,ctx);
        return  res;
    }


    public static String GET_CATEGORY(Context ctx) {

        String res = UPLOAD_TO_SERVER(GET_ALL_CATEGORY, Config.country_id+"",Config.shop_id,ctx);
        return  res;
    }

    public static String GET_ALL_CARDS(String id2,Context ctx) {
        String res = UPLOAD_TO_SERVER(GET_ALL_CARD, "",id2,ctx);
        return  res;

    }

    public static String GET_SUPPORTS(Context ctx) {
        String res = UPLOAD_TO_SERVER(GET_ALL_SUPPORT, Config.shop_id+"","",ctx);
        return  res;


    }

    public static String GET_ALL_COMMENTS(String support_id,Context ctx) {

        String res = UPLOAD_TO_SERVER(GET_ALL_COMMENT, Config.shop_id,support_id,ctx);
        return  res;
    }

    public static String SAVE_COMMETES(Context ctx, String support_id, String commet, String shop_id) {
        String data   = "{\"support_id\":\""+support_id+"\",\"shop_id\":\""+shop_id+"\"}";

        String res = UPLOAD_TO_SERVER(SAVE_NEW_COMMETS ,commet,data ,ctx);
        return  res;
    }

    public static String GET_ALL_SERVICE(int country_id, Context ctx) {
        String res = UPLOAD_TO_SERVER(GET_ALL_SERVICES, country_id+"","",ctx);
        return  res;
    }

    public static String SAVE_REQUEST_SERVICE(Context ctx, String service_id, String request_eshtrak, String request_mobile) {

        String data   = "{\"shop_id\":\""+Config.shop_id+"\",\"request_eshtrak\":\""+request_eshtrak+"\",\"request_mobile\":\"" + request_mobile + "\",\"service_id\":\"" + service_id + "\"}";

        String res = UPLOAD_TO_SERVER(REQUEST_NEW_SERVICE,"",data ,ctx);
        return  res;
    }

    public static String SAVE_NEW_SUPPORT(Context ctx, String support_name, String support_desc) {
        String data   = "{\"support_name\":\""+support_name+"\",\"support_desc\":\""+support_desc+"\"}";

        String res = UPLOAD_TO_SERVER(SAVE_NEW_SUPPORTS ,Config.shop_id,data ,ctx);
        return  res;
    }

    public static String GET_ALL_MY_REQUESTS(Context ctx) {
        String res = UPLOAD_TO_SERVER(GET_ALL_MY_REQUEST ,Config.shop_id,"" ,ctx);
        return  res;
    }

    public static String DELETE_REQUEST_SERVICE(Context ctx, String money, String request_id) {

        String res = UPLOAD_TO_SERVER(DELETE_MY_REQUEST ,request_id,Config.shop_id ,ctx);
        return  res;
    }

    public static String GET_MY_SELLER(Context ctx) {
        String res = UPLOAD_TO_SERVER(GET_MY_SELLERS ,Config.shop_id,Config.shop_id ,ctx);
        return  res;

    }

    public static String BUY_CARD_NOW(Context ctx, String card_id, String shop_id , int amount) {
        String res = UPLOAD_TO_SERVER(BUY_NEWS_CARDS ,shop_id,card_id+"-"+amount ,ctx);
        return  res;

    }

    public static String GET_ALL_SERVICE2(String category_id, Context ctx) {
        String res = UPLOAD_TO_SERVER(GET_ALL_SERVICE_FROM_CATEGORY ,category_id,"" ,ctx);
        return  res;

    }

    public static String SEARCH_FOR_CLIENT(Context ctx, String phone_number) {
        String res = UPLOAD_TO_SERVER(GET_CLIENTS ,Config.shop_id,phone_number,ctx);
        return  res;

    }

    public static String SEND_MONEY_FROM_RASSED_TO_ANTHOR(Context ctx, String shop_id, String shop_id2, String rassed) {
        String res = UPLOAD_TO_SERVER(SEND_MONEY_FROM_RASSED_TO_ANTHOR ,shop_id,shop_id2+"-"+rassed,ctx);
        return  res;

    }
    public static void SEND_TOKEN_TO_SERVER(final Context  mainActivity, final String token , final String serial) {

    new AsyncTask<Void,Void,Void>()
    {
        @Override
        protected Void doInBackground(Void... voids) {
            UPLOAD_TO_SERVER(UPDATE_TOKEN ,Config.shop_id+"-"+serial,token,mainActivity);
            return null;
        }
    }.execute();
    }
    public static String SEND_REQUEST_MONEY(String rm_name, String rm_money, String rm_note, String rm_eshaar, String rm_image , String rm_bank, Context ctx) {
        String data   = "{\"rm_name\":\""+rm_name+
                "\",\"rm_money\":\""+rm_money+
                "\",\"rm_note\":\"" + rm_note +
                "\",\"rm_image\":\"" + rm_image +
                "\",\"rm_eshaar\":\"" + rm_eshaar +
                "\",\"rm_bank\":\"" + rm_bank +
                "\"}";
        String res = UPLOAD_TO_SERVER(REQUEST_MONEY ,Config.shop_id,data,ctx);
        return  res;
    }
    public static String LOGOUT(Context ctx) {
        String res = UPLOAD_TO_SERVER(LOGOUT ,Config.shop_id,Config.GET_IMEI(ctx),ctx);
        return  res;
    }
    public static String GET_ALL_SLIDERS(Context ctx) {
        String res = UPLOAD_TO_SERVER(GET_ALL_SLIDER ,"0","0",ctx);
        return  res;
    }
    public static String CHANGE_PASSWORD(String old_password, String new_password, Context ctx) {
        String data   = "{\"old_password\":\""+old_password+
                "\",\"new_password\":\""+new_password+
                "\"}";
        String res = UPLOAD_TO_SERVER(CHANGE_PASSWORD ,Config.shop_id,data,ctx);
        return  res;
    }
    public static String UPDATE_PROFILE(String username, String mobile, String address, String name,String shop_type, Context ctx) {
        String data   = "{\"shop_name\":\""+name+

                "\",\"shop_mobile\":\"" + mobile +
                "\",\"shop_type\":\"" + shop_type +
                "\",\"shop_address\":\"" + address +
                "\",\"shop_username\":\"" + username +
                "\"}";

        String res = UPLOAD_TO_SERVER(UPDATE_PROFILE ,Config.shop_id,data ,ctx);
        return  res;


    }

    public static String SEARCH(Context ctx, String name) {
        String res = UPLOAD_TO_SERVER(SEARCH ,Config.shop_id,name ,ctx);
        return  res;

    }

    public static String LOAD_ALL_RASSED_TRANS(Context ctx) {
        String res = UPLOAD_TO_SERVER(LOAD_ALL_RASSED_TRANS ,Config.shop_id,"" ,ctx);
        return  res;

    }
}
