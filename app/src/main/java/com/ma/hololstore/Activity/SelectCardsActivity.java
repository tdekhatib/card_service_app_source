package com.ma.hololstore.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ma.hololstore.Adapters.card_adapter;
import com.ma.hololstore.Classes.Config;
import com.ma.hololstore.Classes.card_class;
import com.ma.hololstore.Classes.category_class;
import com.ma.hololstore.R;
import com.ma.hololstore.Server.SERVER_INFO2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SelectCardsActivity extends AppCompatActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    public  static category_class category=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_cards);
        setTitle(category.category_name);

    }

    @Override
    protected void onResume() {
        super.onResume();
        load_all_cards();
        try{

            c.findItem(R.id.action_settings).setTitle(" الرصيد  " + Config.shop_account);

        }catch (Exception ex){}

    }
    Menu c = null;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        try {
            getMenuInflater().inflate(R.menu.main, menu);
c=menu;
            c.findItem(R.id.action_settings).setTitle(" الرصيد  " + Config.shop_account);

        }catch (Exception ex){

        }
        return true;

    }


    private void load_all_cards() {
    try {
        if (Config.internetAvailable(this)) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    ctx = SelectCardsActivity.this;
                    progressDialog = new ProgressDialog(ctx);
                    progressDialog.setMessage("جاري تحميل البطاقات");
                    progressDialog.show();

                }

                Context ctx;
                String res = "";
                ProgressDialog progressDialog;

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    display_cards(res);
                    progressDialog.dismiss();
                }


                @Override
                protected Void doInBackground(Void... voids) {
                    res = SERVER_INFO2.GET_ALL_CARDS(category.category_id, ctx);
                    return null;
                }
            }.execute();
        } else
            Toast.makeText(this, "لا يوجد اتصال بالانترنت ", Toast.LENGTH_SHORT).show();
    } catch (Exception ex) {
    }
}
    private void display_cards(String res) {
        try {
            JSONArray arr = new JSONArray(res);
            ArrayList<card_class> all_data = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject object = arr.getJSONObject(i);
                card_class c = new card_class();
                c.category_id = object.getString("category_id");
                c.card_seller_price = object.getDouble("card_seller_price");
                c.card_image = object.getString("card_image");
                c.card_active = object.getString("card_active");
                c.card_buyer_price = object.getDouble("card_buyer_price");
                c.card_desc = object.getString("card_desc");
                c.card_id = object.getString("card_id");
                c.card_name = object.getString("card_name");
                c.card_price = object.getDouble("card_price");
                all_data.add(c);
            }
            ListView lst_data = findViewById(R.id.lst_data);
            card_adapter adp = new card_adapter(this, R.layout.item_cards, all_data);
            lst_data.setAdapter(adp);
            lst_data.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                  //  show_confirm_To_buy(((card_class) parent.getItemAtPosition(position)));

                    CardDescActivity.card = (card_class) parent.getItemAtPosition(position);
                    startActivity(new Intent(SelectCardsActivity.this,CardDescActivity.class));
                }
            });
        } catch (Exception ex) {
            Toast.makeText(this, "فشلت عملية التحميل للبيانات", Toast.LENGTH_SHORT).show();
        }

    }


}
