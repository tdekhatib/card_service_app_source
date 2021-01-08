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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import com.ma.hololstore.Classes.Config;
import com.ma.hololstore.R;
import com.ma.hololstore.Server.SERVER_INFO2;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
public class SignUpActivity extends AppCompatActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
    private static final String TAG = Config.TAG ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        bind_layout_to_code();
    }
    Spinner spinner_type;
    Button bt_select_location;
    ArrayList<String> all_type;
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
            bt_select_location = findViewById(R.id.bt_select_location);
            bt_select_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    select_location();
                }
            });
/*spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 1)
        {
            bt_select_location.setVisibility(View.VISIBLE);
        }
        else
        {
            bt_select_location.setVisibility(View.GONE);
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
});*/
            txt_signup_username = findViewById(R.id.txt_signup_username);
            txt_signup_password = findViewById(R.id.txt_signup_password);
            txt_signup_mobile = findViewById(R.id.txt_signup_mobile);
            txt_signup_address = findViewById(R.id.txt_signup_address);

            bt_reqister_save = findViewById(R.id.bt_reqister_save);
            bt_reqister_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (txt_signup_mobile.getText().length()>6)
                    {
                        if (txt_signup_password.getText().length()>0 && txt_signup_username.getText().length()>0)
                        {
                            if (Config.internetAvailable(SignUpActivity.this))
                            {
                                new AsyncTask<Void,Void,Void>(){
                                    @Override
                                    protected Void doInBackground(Void... voids) {
                                        res = SERVER_INFO2.SIGN_UP(username,password,mobile,address,name ,type , serial,ctx);
                                        return null;
                                    }
                                    String res , serial  , name, username , password ,mobile ,address, type;
                                    Context ctx;

                                    @Override
                                    protected void onPostExecute(Void aVoid) {
                                        super.onPostExecute(aVoid);

                                        Log.i(TAG, "onPostExecute: "+res);
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
                                                Config.SaveKEY(SignUpActivity.this,"shop_id",obj.getString("shop_id"));
                                                Config.SaveKEY(SignUpActivity.this,"shop_name",obj.getString("shop_name"));
                                                Config.SaveKEY(SignUpActivity.this,"shop_username",obj.getString("shop_username"));
                                                Config.SaveKEY(SignUpActivity.this,"shop_mobile",obj.getString("shop_mobile"));
                                                Config.SaveKEY(SignUpActivity.this,"shop_account",obj.getString("shop_account"));
                                                Config.SaveKEY(SignUpActivity.this,"shop_type",obj.getString("shop_type"));
                                                Config.SaveKEY(SignUpActivity.this,"shop_password",password);
                                                startActivity(new Intent(ctx, MainActivity.class));
                                                SignUpActivity.this.finish();
                                            }
                                            else
                                                Toast.makeText(ctx, "فشل تسجيل الحساب الجديد . حاول مرة أخرى ", Toast.LENGTH_SHORT).show();
                                        }
                                        catch (Exception ex)
                                        {
                                            Toast.makeText(ctx, "فشل تسجيل الحساب الجديد . حاول مرة أخرى ", Toast.LENGTH_SHORT).show();

                                        }
                                    }

                                    ProgressDialog progressDialog;
                                    @Override
                                    protected void onPreExecute() {
                                        super.onPreExecute();
                                        ctx = SignUpActivity.this;
                                        username = txt_signup_username.getText().toString();
                                        password=txt_signup_password.getText().toString();
                                        mobile=txt_signup_mobile.getText().toString();
                                        address = txt_signup_address.getText().toString();
                                        type=spinner_type.getSelectedItemPosition()+"";
                                        serial = Config.GET_IMEI(ctx);
                                        //     coinid = ((coins_class) spiner_coins.getSelectedItem()).coin_id;
                                        //   countryid = ((country_class) spiner_country.getSelectedItem()).country_id;

                                        name = txt_signup_name.getText().toString();

                                        progressDialog = new ProgressDialog(ctx);
                                        progressDialog.setMessage("جاري تسجيل حساب جديد");
                                        progressDialog.show();
                                    }
                                }.execute();
                            }
                            else
                                Toast.makeText(SignUpActivity.this, "لا يوجد اتصال انترنت", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(SignUpActivity.this, "أدخل اسم للمستخدم وكلمة المرور ", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(SignUpActivity.this, "أدخل رقم موبايل صحيح للاكمال", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception ex)
        {}
    }

    private void select_location() {
        startActivity(new Intent(this,MapLocationActivity.class));
    }

    EditText txt_signup_username;
    EditText txt_signup_name
            ,txt_signup_password
            ,txt_signup_mobile,txt_signup_address
            ;
    Button bt_reqister_save;


}
