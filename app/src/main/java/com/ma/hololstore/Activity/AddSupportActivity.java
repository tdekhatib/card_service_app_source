package com.ma.hololstore.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
 import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.ma.hololstore.R;
import com.ma.hololstore.Classes.Config;
 import com.ma.hololstore.Server.SERVER_INFO2;

import androidx.appcompat.app.AppCompatActivity;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class AddSupportActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_support);
    txt_support_desc=findViewById(R.id.txt_support_desc);
    txt_support_name = findViewById(R.id.txt_support_name);
        ((Button) findViewById(R.id.bt_request_add_supports)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_new_support();
            }
        });

    }

    private void save_new_support() {
        try {
            if (Config.internetAvailable(this)) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        ctx = AddSupportActivity.this;
                        progressDialog = new ProgressDialog(ctx);
                        progressDialog.setMessage("جاري حفظ تذكرة الدعم الفني");

                        desc = txt_support_desc.getText().toString();
                        name = txt_support_name.getText().toString();
                        progressDialog.show();

                    }

                    Context ctx;
                    String res = "";

                    ProgressDialog progressDialog;

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        res = res.toLowerCase();

                      if (res.contains("ok")) {

                             Toast.makeText(ctx, "تم حفظ التذكرة بنجاح", Toast.LENGTH_SHORT).show();
                            AddSupportActivity.this.finish();
                        }   else
                          Toast.makeText(ctx, "فشلت العملية ", Toast.LENGTH_SHORT).show();

                        progressDialog.dismiss();

                    }

                    String  desc, name;

                    @Override
                    protected Void doInBackground(Void... voids) {
                        res = SERVER_INFO2.SAVE_NEW_SUPPORT(ctx,  name, desc);

                        return null;
                    }
                }.execute();
            } else
                Toast.makeText(this, "لا يوجد اتصال بالانترنت ", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
        }

    }

    EditText txt_support_name , txt_support_desc ;
}
