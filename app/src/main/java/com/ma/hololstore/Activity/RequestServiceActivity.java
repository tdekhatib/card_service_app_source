package com.ma.hololstore.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ma.hololstore.Classes.Config;
import com.ma.hololstore.R;
import com.ma.hololstore.Server.SERVER_INFO2;
import com.ma.hololstore.Classes.service_class;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class RequestServiceActivity extends AppCompatActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    public static service_class service = null;
EditText txt_client_mobile , txt_client_eshtrak  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_service);

        ((TextView) findViewById(R.id.item_service_name)).setText(service.service_name);
        ((TextView) findViewById(R.id.item_service_price)).setText("السعر للزبون :"  + service.service_price);

        txt_client_mobile = findViewById(R.id.txt_client_mobile);
        txt_client_eshtrak = findViewById(R.id.txt_client_eshtrak);

        ((Button) findViewById(R.id.bt_buy_service)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_request();
            }
        });
        ((Button) findViewById(R.id.bt_cancel_buy_service)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void save_request() {
    if (txt_client_eshtrak.getText().length()>0 && txt_client_mobile.getText().length()>0)
    {
         if (Config.shop_account >= ( service.service_price)) {
            try {
                if (Config.internetAvailable(this)) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            ctx = RequestServiceActivity.this;
                            progressDialog = new ProgressDialog(ctx);
                            progressDialog.setMessage("جاري طلب الخدمة");
                            money = txt_client_eshtrak.getText().toString() + "";
                             phone = txt_client_mobile.getText().toString();
                            progressDialog.show();

                        }

                        Context ctx;
                        String res = "";

                        ProgressDialog progressDialog;

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            res = res.toLowerCase();
                            if (res.contains("nomoney"))
                                Toast.makeText(ctx, "لا يوجد رصيد كافي لديك لاتمام العملية ", Toast.LENGTH_SHORT).show();
                            else if (res.contains("error"))
                                Toast.makeText(ctx, "فشلت العملية ", Toast.LENGTH_SHORT).show();
                            else if (res.contains("ok")) {

                                Config.shop_account = Integer.valueOf(res.split(",")[1]);
                                  Toast.makeText(ctx, "تم الطلب بنجاح", Toast.LENGTH_SHORT).show();
                                   ServiceResultActivity.request_id = res.split(",")[2];

                                ServiceResultActivity.eshtrak=txt_client_eshtrak.getText().toString();
                                ServiceResultActivity.service_name = service.service_name;
                                ServiceResultActivity.service_price  = ""+service.service_seller_price;
                                ServiceResultActivity.mobile = txt_client_mobile.getText().toString();
                                startActivity(new Intent(RequestServiceActivity.this,ServiceResultActivity.class));

                            }

                            progressDialog.dismiss();

                        }

                        String money , phone;

                        @Override
                        protected Void doInBackground(Void... voids) {
                            res = SERVER_INFO2.SAVE_REQUEST_SERVICE(ctx, service.service_id, money, phone);

                            return null;
                        }
                    }.execute();
                } else
                    Toast.makeText(this, "لا يوجد اتصال بالانترنت ", Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {
            }
        }else
            Toast.makeText(this, "رصيدك لا يكفي لاتمام العملية", Toast.LENGTH_SHORT).show();
    }
    else
        Toast.makeText(this, "أدخل مبلغ للتحويل على الخدمة", Toast.LENGTH_SHORT).show();

    }
}
