package com.ma.hololstore.Activity;

import androidx.appcompat.app.AppCompatActivity;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.ma.hololstore.Classes.Config;
import com.ma.hololstore.R;
import com.ma.hololstore.Server.SERVER_INFO2;
public class ChangePasswordActivity extends AppCompatActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        txt_old_password = findViewById(R.id.txt_old_password);
        txt_confirm_password = findViewById(R.id.txt_confirm_password);
        txt_new_password = findViewById(R.id.txt_new_password);
        bt_change_password = findViewById(R.id.bt_change_password);

        bt_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_password();
            }
        });
    }

    private void change_password() {
        if (data_complete()) {
            send_new_password();

        }
    }

    private void send_new_password() {

        if (Config.internetAvailable(this)) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    res = SERVER_INFO2.CHANGE_PASSWORD(old_password, new_password, ctx);
                    return null;
                }

                Context ctx;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressDialog = new ProgressDialog(ChangePasswordActivity.this);

                    old_password = txt_old_password.getText().toString();
                    new_password = txt_new_password.getText().toString();
                    ctx = ChangePasswordActivity.this;
                    progressDialog.setMessage("جاري تغير كلمة المرور ");
                    progressDialog.show();
                }

                ProgressDialog progressDialog;
                String res = "";
                String old_password;
                String new_password;

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);

                    progressDialog.dismiss();
                    display_result(res);
                }


            }.execute();
        } else
            Config.show_msg(this, "لايوجد اتصال انترنت ");
    }

    private void display_result(String res) {
        if (res.contains("OK")) {
            Config.SaveKEY(this, "shop_password", txt_new_password.getText().toString());
            txt_new_password.setText("");
            txt_confirm_password.setText("");
            txt_new_password.setText("");
            Config.show_msg(this, "تم تعديل كلمة المرور بنجاح ");
        } else
            Config.show_msg(this, "فشلت عملية تعديل كلمة المرور ");
    }

    private boolean data_complete() {

        if (!txt_old_password.getText().toString().equals(Config.GET_KEY(this, "shop_password"))) {
            Config.show_msg(this, "كلمة المرور القديمة غير صحيحة " );
            return false;
        }

        if (!txt_confirm_password.getText().toString().equals(txt_new_password.getText().toString())) {
            Config.show_msg(this, "كلمتا المرور الجديدتين غير متطابقتين  ");
            return false;
        }

        if (txt_new_password.getText().length() <=0) {
            Config.show_msg(this, "ادخل كلمة المرور الجديدة للمتابعة ");
            return false;
        }

        return true;
    }

    Button bt_change_password;
    EditText txt_old_password, txt_new_password,txt_confirm_password;
}
