package com.ma.hololstore.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ma.hololstore.Adapters.MyNotificationAdapter;
import com.ma.hololstore.Adapters.TransAdapter;
import com.ma.hololstore.Adapters.card_adapter;
import com.ma.hololstore.Classes.Config;
import com.ma.hololstore.Classes.card_class;
import com.ma.hololstore.Classes.trans_class;
import com.ma.hololstore.R;
import com.ma.hololstore.Server.SERVER_INFO2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class RassedTransActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rassed_trans);
        load_all_trans();
    }

    private void load_all_trans() {
        if (Config.internetAvailable(this))
        {
            new AsyncTask<Void,Void,Void>(){
                @Override
                protected Void doInBackground(Void... voids) {
                    res = SERVER_INFO2.LOAD_ALL_RASSED_TRANS(ctx);
                    return null;
                }

                Context ctx;
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    ctx= RassedTransActivity.this;
                    progressDialog = new ProgressDialog(ctx);
                    progressDialog.setMessage("جاري جلب تحويلات الرصيد ");
                    progressDialog.show();
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    progressDialog.dismiss();
                    display_data(res);
                }

                String res="";
                ProgressDialog progressDialog;
            }.execute();
        }
        else
            Toast.makeText(this, "لا يوجد اتصال انترنت", Toast.LENGTH_SHORT).show();

    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
    private void display_data(String res) {
        Log.i(Config.TAG, "display_data: "+res);
        ArrayList<trans_class> all_data = new ArrayList<>();

        try {
            JSONArray arr = new JSONArray(res);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject object = arr.getJSONObject(i);
                trans_class c = new trans_class();
                c.shop_money = object.getString("trans_money");
                c.shop_to = object.getString("shop_to1");
                c.shop_from = object.getString("shop_from1");
                c.shop_date = object.getString("trans_date");
                all_data.add(c);
            }


            RecyclerView RCV = findViewById(R.id.recyle_data);
            RCV.setLayoutManager(new GridLayoutManager(this, 1));
            RCV.setNestedScrollingEnabled(false);
            RCV.setAdapter(new TransAdapter(this, all_data));

        } catch (Exception ex) {
            Toast.makeText(this, "فشلت عملية التحميل للبيانات", Toast.LENGTH_SHORT).show();
        }

    }
}
