package com.ma.hololstore.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;


import com.ma.hololstore.Adapters.MyNotificationAdapter;
import com.ma.hololstore.Classes.Config;
import com.ma.hololstore.R;
import com.ma.hololstore.Classes.notification_class;

import java.util.ArrayList;

public class NotifyActivity extends AppCompatActivity {


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
        load_data();
    }

    private void load_data() {
        try{
            SQLiteDatabase db = openOrCreateDatabase(Config.dbName  , MODE_PRIVATE , null);
            Cursor cursor = db.rawQuery("select * from messages order by ID desc limit 100" , null);
            if (cursor.getCount()>0)
            {
                cursor.moveToFirst() ;
                for(int i=0;i<cursor.getCount();i++)
                {
notification_class c = new notification_class();
c.Body = cursor.getString(cursor.getColumnIndex("Body"));
c.Date1 = cursor.getString(cursor.getColumnIndex("Date1"));
c.Title = cursor.getString(cursor.getColumnIndex("Title"));
all_data.add( c);
                    cursor.moveToNext();
                }

            }

            db.close();


            RecyclerView RCV = findViewById(R.id.recyle_data);
            RCV.setLayoutManager(new GridLayoutManager(this, 1));
            RCV.setNestedScrollingEnabled(false);
            RCV.setAdapter(new MyNotificationAdapter(this, all_data));
        }catch (Exception ex){}

    }

    ArrayList<notification_class> all_data = new ArrayList<notification_class>();

}
