package com.ma.hololstore.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ma.hololstore.Adapters.service_adapter;
import com.ma.hololstore.Classes.Config;
import com.ma.hololstore.Classes.category_class;
import com.ma.hololstore.R;
import com.ma.hololstore.Server.SERVER_INFO2;
import com.ma.hololstore.Classes.service_class;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ServiceActivity extends AppCompatActivity {
public static category_class category = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        ((TextView) findViewById(R.id.item_category_name)).setText(category.category_name);
        ((TextView) findViewById(R.id.txt_category_desc)).setText(category.category_desc);
        ((TextView) findViewById(R.id.txt_shop_name)).setText(" أهلا بك ...  " + Config.shop_name) ;

        Picasso.get().load( SERVER_INFO2.URL2+"files/" +category.category_image)
                .into(((ImageView) findViewById(R.id.item_category_image)));

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    private void display_all_service(String res) {

        try {
            JSONArray arr = new JSONArray(res);
            ArrayList<service_class> all_data = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject object = arr.getJSONObject(i);
                service_class c = new service_class();
                c.service_active = object.getString("service_active");
                c.service_desc = object.getString("service_desc");
                c.service_id = object.getString("service_id");
                c.service_from = object.getString("service_from");
                c.service_name = object.getString("service_name");
                c.service_price = object.getDouble("service_price");
                c.service_seller_price = object.getInt("service_seller_price");
                c.service_to = object.getString("service_to");
                c.service_type = object.getString("service_type");
                all_data.add(c);
            }
            service_adapter adp = new service_adapter(this, R.layout.item_service, all_data);
            ListView lst_data = (ListView) findViewById(R.id.lst_data);
            lst_data.setAdapter(adp);
            lst_data.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    RequestServiceActivity.service = (service_class) parent.getItemAtPosition(position);
                    startActivity(new Intent(ServiceActivity.this, RequestServiceActivity.class));
                }
            });
        } catch (Exception ex) {
            Toast.makeText(this, "فشلت عملية التحميل للخدمات", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle(" الرصيد  " + Config.shop_account + " " + Config.coin_name);

        load_all_my_service();
    }

    private void load_all_my_service() {

        try {
            if (Config.internetAvailable(this)) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        ctx = ServiceActivity.this;
                        progressDialog = new ProgressDialog(ctx);
                        progressDialog.setMessage("جاري تحميل كل الخدمات");
                        progressDialog.show();

                    }

                    Context ctx;
                    String res = "";
                    ProgressDialog progressDialog;

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        display_all_service(res);
                        progressDialog.dismiss();
                    }


                    @Override
                    protected Void doInBackground(Void... voids) {
                        res = SERVER_INFO2.GET_ALL_SERVICE2(category.category_id, ctx);

                        return null;
                    }
                }.execute();
            } else
                Toast.makeText(this, "لا يوجد اتصال بالانترنت ", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
        }

    }

}
