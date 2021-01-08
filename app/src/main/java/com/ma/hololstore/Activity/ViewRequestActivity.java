package com.ma.hololstore.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ma.hololstore.Classes.Config;
import com.ma.hololstore.R;
import com.ma.hololstore.Server.SERVER_INFO2;
import com.ma.hololstore.Classes.request_class;

public class ViewRequestActivity extends AppCompatActivity {
    public static request_class request = null;
    EditText txt_mobile , txt_money , txt_desc ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_request);

        ((TextView) findViewById(R.id.txt_service_name)).setText(request.service_name);
        txt_mobile = findViewById(R.id.txt_request_mobile);
        txt_money = findViewById(R.id.txt_request_money);
        txt_desc = findViewById(R.id.txt_request_desc);

        txt_mobile.setText(request.request_mobile);
       // txt_money.setText(request.request_money+"");
      //  txt_desc.setText(request.request_desc);

        ((Button) findViewById(R.id.bt_request_service)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_request();
            }
        });

    }

    private void save_request() {
        if (request.request_type.equals("0"))
        {
                try {
                    if (Config.internetAvailable(this)) {
                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                ctx = ViewRequestActivity.this;
                                progressDialog = new ProgressDialog(ctx);
                                progressDialog.setMessage("جاري الغاء الخدمة");
                                   progressDialog.show();

                            }

                            Context ctx;
                            String res = "";

                            ProgressDialog progressDialog;

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);
                                res = res.toLowerCase();
                               if (res.contains("error"))
                                    Toast.makeText(ctx, "فشلت العملية ", Toast.LENGTH_SHORT).show();
                                else if (res.contains("ok")) {

                                    Config.shop_account = Integer.valueOf(res.split(",")[1]);
                                    Toast.makeText(ctx, "تم الغاء الطلب بنجاح", Toast.LENGTH_SHORT).show();
                                    ViewRequestActivity.this.finish();
                                }

                                progressDialog.dismiss();

                            }

                            String money, desc, phone;

                            @Override
                            protected Void doInBackground(Void... voids) {
                                res = SERVER_INFO2.DELETE_REQUEST_SERVICE(ctx, money, request.request_id);

                                return null;
                            }
                        }.execute();
                    } else
                        Toast.makeText(this, "لا يوجد اتصال بالانترنت ", Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                }

        }
        else
            if (request.request_type.equals("1"))
            Toast.makeText(this, "لا يمكن الغاء طلب الخدمة لانه قد تم انجازه", Toast.LENGTH_SHORT).show();
            else
            if (request.request_type.equals("2"))
                Toast.makeText(this, "الخدمة ملغاة مسبقا", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

}
