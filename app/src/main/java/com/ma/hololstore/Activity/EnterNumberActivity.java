package com.ma.hololstore.Activity;

import androidx.appcompat.app.AppCompatActivity;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ma.hololstore.Classes.Config;
import com.ma.hololstore.R;
import com.ma.hololstore.Server.SERVER_INFO2;

import org.json.JSONArray;
import org.json.JSONObject;

public class EnterNumberActivity extends AppCompatActivity {

    EditText txt_phone_number;
    Button bt_search_client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_number);


        txt_phone_number  = findViewById(R.id.txt_phone_number);
        bt_search_client  = findViewById(R.id.bt_search);


        bt_search_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_phone_number.getText().toString().length()>5)
                    search_to_clients();
                else
                    Toast.makeText(EnterNumberActivity.this, "من فضلك ادخل رقم الهاتف من اجل تحويل الرصيد له  ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void search_to_clients() {

        try{
            if (Config.internetAvailable(this)) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        ctx = EnterNumberActivity.this;
                        progressDialog  = new ProgressDialog(ctx);
                        progressDialog.setMessage("جاري البحث عن الزبون");
                        phone_number = txt_phone_number.getText().toString();
                        progressDialog.show();

                    }
                    String phone_number ="";
                    Context ctx;
                    String res = "";
                    ProgressDialog progressDialog;
                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        progressDialog.dismiss();
Display_data(res);

                    }


                    @Override
                    protected Void doInBackground(Void... voids) {
                        res  = SERVER_INFO2.SEARCH_FOR_CLIENT(ctx,phone_number);
                        return null;
                    }
                }.execute();
            }
            else
                Toast.makeText(this, "لا يوجد اتصال بالانترنت ", Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex)
        {}
    }

    private void Display_data(String res) {
        Log.i(Config.TAG, "Display_data: "+res);
       try {
           JSONArray arr = new JSONArray(res);
           if (arr.length()>0)
           {
               JSONObject obj = arr.getJSONObject(0);
               ConvertMoneyActivity.shop_id = obj.getString("shop_id");
               ConvertMoneyActivity.shop_mobile = obj.getString("shop_mobile");
               ConvertMoneyActivity.shop_name = obj.getString("shop_name");
startActivity(new Intent(this,ConvertMoneyActivity.class));
           }
           else
               Config.show_msg(this,"الحساب الذي تبحث عنه غير موجود ");

       }catch (Exception ex){}
       }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
}
