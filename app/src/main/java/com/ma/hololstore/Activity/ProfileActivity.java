package com.ma.hololstore.Activity;

import androidx.appcompat.app.AppCompatActivity;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ma.hololstore.Classes.Config;
import com.ma.hololstore.R;
import com.ma.hololstore.Server.SERVER_INFO2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    Spinner spinner_type;
     ArrayList<String> all_type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bind_layout_to_code();


    }


    private  void bind_layout_to_code()
    {
        try {
                all_type  = new ArrayList<>();
                all_type.add("شخصي");
                all_type.add("متجر");
                txt_signup_name = findViewById(R.id.txt_signup_name);
                spinner_type = findViewById(R.id.spiner_type);
                ArrayAdapter<String> adp = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,all_type);
                spinner_type.setAdapter(adp);
            txt_signup_username = findViewById(R.id.txt_signup_username);
             txt_signup_mobile = findViewById(R.id.txt_signup_mobile);
            txt_signup_address = findViewById(R.id.txt_signup_address);
            txt_signup_name.setText(  Config.GET_KEY(ProfileActivity.this,"shop_name" ));
            txt_signup_username.setText(  Config.GET_KEY(ProfileActivity.this,"shop_username" ));
            txt_signup_mobile.setText(  Config.GET_KEY(ProfileActivity.this,"shop_mobile" ));
            txt_signup_address.setText(  Config.GET_KEY(ProfileActivity.this,"shop_address" ));
            spinner_type.setSelection( Integer.valueOf(Config.GET_KEY(ProfileActivity.this,"shop_type" )));
            bt_reqister_save = findViewById(R.id.bt_reqister_save);
            bt_reqister_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (txt_signup_mobile.getText().length()>6)
                    {
                        if ( txt_signup_username.getText().length()>0)
                        {
                            if (Config.internetAvailable(ProfileActivity.this))
                            {
                                new AsyncTask<Void,Void,Void>(){
                                    @Override
                                    protected Void doInBackground(Void... voids) {
                                        res = SERVER_INFO2.UPDATE_PROFILE(username,mobile,address,name ,type ,ctx);
                                        return null;
                                    }
                                    String res  , name, username   ,mobile ,address,type;
                                    Context ctx;

                                    @Override
                                    protected void onPostExecute(Void aVoid) {
                                        super.onPostExecute(aVoid);

                                        progressDialog.dismiss();
                                        try{
                                            JSONArray arr = new JSONArray(res);
                                            if (arr.length()>0)
                                            {
                                                JSONObject obj = arr.getJSONObject(0);
                                                Config.shop_id=obj.getString("shop_id");
                                                Config.shop_name=obj.getString("shop_name");
                                                Config.shop_username=obj.getString("shop_username");
                                                Config.shop_mobile=obj.getString("shop_mobile");
                                                Config.shop_account=obj.getInt("shop_account");
                                                Config.SaveKEY(ProfileActivity.this,"shop_id",obj.getString("shop_id"));
                                                Config.SaveKEY(ProfileActivity.this,"shop_name",obj.getString("shop_name"));
                                                Config.SaveKEY(ProfileActivity.this,"shop_username",obj.getString("shop_username"));
                                                Config.SaveKEY(ProfileActivity.this,"shop_mobile",obj.getString("shop_mobile"));
                                                Config.SaveKEY(ProfileActivity.this,"shop_account",obj.getString("shop_account"));
                                                Config.SaveKEY(ProfileActivity.this,"shop_type",obj.getString("shop_type"));


                                      Config.show_msg(ctx,"تم تعديل بيانات الحساب بنجاح");
                                            }
                                            else
                                                Toast.makeText(ctx, "فشلت عملية تعديل البروفايل ", Toast.LENGTH_SHORT).show();
                                        }
                                        catch (Exception ex)
                                        {
                                            Toast.makeText(ctx, "فشلت عملية تعديل البروفايل ", Toast.LENGTH_SHORT).show();

                                        }
                                    }

                                    ProgressDialog progressDialog;
                                    @Override
                                    protected void onPreExecute() {
                                        super.onPreExecute();
                                        ctx = ProfileActivity.this;
                                        username = txt_signup_username.getText().toString();
                                        mobile=txt_signup_mobile.getText().toString();
                                        address = txt_signup_address.getText().toString();
                                        name = txt_signup_name.getText().toString();
                                        type = spinner_type.getSelectedItemPosition()+"";
                                        progressDialog = new ProgressDialog(ctx);
                                        progressDialog.setMessage("جاري حفظ التعديلات");
                                        progressDialog.show();
                                    }
                                }.execute();
                            }
                            else
                                Toast.makeText(ProfileActivity.this, "لا يوجد اتصال انترنت", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(ProfileActivity.this, "أدخل اسم للمستخدم   ", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(ProfileActivity.this, "أدخل رقم موبايل صحيح للاكمال", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception ex)
        {}
    }

    EditText txt_signup_username;
    EditText txt_signup_name
             ,txt_signup_mobile,txt_signup_address
            ;
    Button bt_reqister_save;


}
