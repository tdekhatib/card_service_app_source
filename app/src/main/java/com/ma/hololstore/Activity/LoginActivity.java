package com.ma.hololstore.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ma.hololstore.Classes.Config;
import com.ma.hololstore.R;
import com.ma.hololstore.Server.SERVER_INFO2;

import org.json.JSONArray;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bind_layout_to_code();


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Config.GET_KEY(this,"remember").equals("1"))
        {
            txt_login_password.setText(Config.GET_KEY(this,"shop_password"));
            txt_login_username.setText(Config.GET_KEY(this,"shop_username"));
            chk.setChecked(true);

        }
        else
            chk.setChecked(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();



    }

    CheckBox chk;
    EditText txt_login_username
           , txt_login_password
   ;
    TextView txt_signup
           , txt_forget_password
  ;
    Button bt_login_accept;


    private  void bind_layout_to_code()

    {
        chk = findViewById(R.id.chk_remeber);

        txt_login_username = findViewById(R.id.txt_login_username);
                txt_login_password= findViewById(R.id.txt_login_password);
        txt_signup= findViewById(R.id.txt_signup);
         bt_login_accept= findViewById(R.id.bt_login_accept);
        txt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
        bt_login_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_login_password.getText().length()>0 && txt_login_username.getText().length()>0)
                {
                    Config.SaveKEY(LoginActivity.this,"shop_password" , txt_login_password.getText().toString());

                    if (chk.isChecked())
                    {
                        Config.SaveKEY(LoginActivity.this,"shop_username" , txt_login_username.getText().toString());
                        Config.SaveKEY(LoginActivity.this,"remember","1");
                    }
                    else
                    {
                        Config.SaveKEY(LoginActivity.this,"shop_username" ,"");
                       // Config.SaveKEY(LoginActivity.this,"shop_password" , "");
                        Config.SaveKEY(LoginActivity.this,"remember","0");
                    }

                    if (Config.internetAvailable(LoginActivity.this)) {
                        try {
                            new AsyncTask<Void, Void, Void>() {

                                ProgressDialog progressDialog;
String username,password,serial;
                                @Override
                                protected void onPreExecute() {
                                    super.onPreExecute();
                                    username=txt_login_username.getText().toString();
                                    password=txt_login_password.getText().toString();
                                    serial = Config.GET_IMEI(LoginActivity.this);
                                    progressDialog = new ProgressDialog(LoginActivity.this);
                                    progressDialog.setMessage("جاري تسجيل الدخول ....");
                                    ctx=LoginActivity.this;
                                    progressDialog.show();
                                }
                                String res="";
                                @Override
                                protected void onPostExecute(Void aVoid) {
                                    super.onPostExecute(aVoid);
                                    progressDialog.dismiss();
                                    try{
                                        Log.i(Config.TAG, "onPostExecute: "+res);
                                        JSONArray arr = new JSONArray(res);
                                        if (arr.length()>0)
                                        {
                                            JSONObject obj =(arr.getJSONObject(0));
                                            Config.shop_id=obj.getString("shop_id");
                                            Config.shop_name=obj.getString("shop_name");
                                            Config.shop_mobile=obj.getString("shop_mobile");
                                            Config.shop_account=obj.getInt("shop_account");
                                            Config.shop_price=obj.getInt("shop_price");
                                            Config.SaveKEY(ctx,"shop_name",obj.getString("shop_name"));
                                            Config.SaveKEY(ctx,"shop_id",obj.getString("shop_id"));
                                            Config.SaveKEY(ctx,"shop_mobile",obj.getString("shop_mobile"));
                                            Config.SaveKEY(ctx,"shop_username",obj.getString("shop_username"));
                                            Config.SaveKEY(ctx,"shop_address",obj.getString("shop_address"));
                                         //   Config.SaveKEY(ctx,"shop_password",obj.getString("shop_password"));
                                            startActivity(new Intent(ctx,MainActivity.class));
                                            LoginActivity.this.finish();

                                         }
                                        else
                                            Toast.makeText(ctx, "اسم المستخدم او كلمة المرور غير صحيحين ", Toast.LENGTH_SHORT).show();

                                    }
                                    catch (Exception ex)
                                    {
                                        Toast.makeText(ctx, "فشل تسجيل الدخول للنظام", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                Context ctx;
                                @Override
                                protected Void doInBackground(Void... voids) {
                                    res = SERVER_INFO2.LOGIN_SHOP(username,password,serial,ctx);
                                    return null;
                                }
                            }.execute();

                        } catch (Exception ex) {
                        }
                    }
                    else
                        Toast.makeText(LoginActivity.this, "تأكد من اتصال الانترنت", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(LoginActivity.this, "أدخل اسم المستخدم وكلمة المرور ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
}
