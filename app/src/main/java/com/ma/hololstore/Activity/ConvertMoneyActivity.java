package com.ma.hololstore.Activity;

import androidx.appcompat.app.AppCompatActivity;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ma.hololstore.Classes.Config;
import com.ma.hololstore.R;
import com.ma.hololstore.Server.SERVER_INFO2;

public class ConvertMoneyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert_money);
        txt_shop_name = findViewById(R.id.txt_shop_name);
        txt_shop_mobile = findViewById(R.id.txt_shop_mobile);
        txt_rassed_convert = findViewById(R.id.txt_rassed_convert);
        txt_password = findViewById(R.id.txt_password);
        txt_shop_name.setText(shop_name);
        txt_shop_mobile.setText(shop_mobile);
        findViewById(R.id.bt_send_money).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_money_now();
            }
        });

    }

  static   int atemp=0;
    private void send_money_now() {
        try {

        if (Config.GET_KEY(this, "shop_password").equals(txt_password.getText().toString())) {
            if (txt_rassed_convert.getText().length() > 0) {
if (Integer.valueOf(txt_rassed_convert.getText().toString()) <= Config.shop_account) {
    if (Config.internetAvailable(this)) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                ctx = ConvertMoneyActivity.this;
                progressDialog = new ProgressDialog(ctx);
                progressDialog.setMessage("جاري تحويل الرصيد");
                rassed = txt_rassed_convert.getText().toString();
                progressDialog.show();

            }

            String rassed = "";
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
                res = SERVER_INFO2.SEND_MONEY_FROM_RASSED_TO_ANTHOR(ctx, Config.shop_id, ConvertMoneyActivity.shop_id, rassed);
                return null;
            }
        }.execute();
    } else
        Toast.makeText(this, "لا يوجد اتصال بالانترنت ", Toast.LENGTH_SHORT).show();
}else
    Config.show_msg(this,"الرصيد المطلوب تحويله غير متوفر بحسابك ");
}
         else
                Toast.makeText(this, "ادخل الرصيد المطلوب تحويله ", Toast.LENGTH_SHORT).show();
            } else {
                atemp++;
                if (atemp > 3)
                    finish();
                else
                    Toast.makeText(this, "كلمة المرور الخاصة بحسابك غير صحيحة ", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception ex){}
    }

    private void Display_data(String res) {

    try{
        if (res.contains("OK"))
        {
            Config.shop_account = Integer.valueOf(res.split(",")[1]);
            Config.SaveKEY(this,"shop_account",Config.shop_account+"");
            Config.show_msg(this,"تمت عملية تحويل الرصيد بنجاح ");
            txt_rassed_convert.setText("");
            txt_password.setText("");
         }
        else
            if (res.contains("nomoney"))
                Config.show_msg(this,"المبلغ المطلوب تحويله غير متوفر برصيدك  ");
            else
            if (res.contains("ERROR"))
                Config.show_msg(this,"فشلت العملية حاول مرة اخرى   ");


    }catch (Exception ex){
        Config.show_msg(this,"فشلت العملية حاول مرة اخرى   ");

    }
    }

    EditText txt_shop_name,txt_shop_mobile,txt_rassed_convert,txt_password;
    public static String shop_id="",shop_name = "" , shop_mobile="";
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

}
