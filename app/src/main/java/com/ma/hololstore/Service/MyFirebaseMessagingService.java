package com.ma.hololstore.Service;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ma.hololstore.Classes.Config;

import java.util.Map;
import androidx.annotation.NonNull;



public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
       // Config.SEND_TO_SERVER(s);
    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

         if (remoteMessage.getData().size() > 0) {
            Map<String, String> obj = remoteMessage.getData();
            if (obj.get("type").equals("content")) {
                String title = obj.get("title");
                String body = obj.get("body");

                Config.ExecuteNonQuery(this,"insert into messages(Title , Body , Date1) values('"+title+"','"+body+"','"+Config.GET_DATE()+"')");
                Config.sendMyNotification(MyFirebaseMessagingService.this, title, body);
            }
        }
    }
}