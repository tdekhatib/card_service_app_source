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
import android.widget.ListView;
import android.widget.Toast;

import com.ma.hololstore.Classes.Config;
import com.ma.hololstore.R;
import com.ma.hololstore.Server.SERVER_INFO2;
import com.ma.hololstore.Adapters.comments_adapter;
import com.ma.hololstore.Classes.comments_class;
import com.ma.hololstore.Classes.support_class;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CommentsActivity extends AppCompatActivity {

    public static support_class support = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        setTitle(support.support_name);
        load_all_commects();
        ((Button) findViewById(R.id.bt_send_commet)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_commets();
            }
        });


    }

    private void save_commets() {
      final  EditText txt = findViewById(R.id.txt_commet);
        if (txt.getText().length()>0)
        {
            try{
                if (Config.internetAvailable(this)) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            ctx = CommentsActivity.this;
                            progressDialog  = new ProgressDialog(ctx);
                            progressDialog.setMessage("جاري حفظ التعليق");
commet = txt.getText().toString();
progressDialog.show();

                        }
                        String commet ="";
                        Context ctx;
                        String res = "";
                        ProgressDialog progressDialog;
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            progressDialog.dismiss();
                            if (res.toLowerCase().contains("ok"))
                            {
                                txt.setText("");
                                load_all_commects();
                            }
                            else
                                Toast.makeText(ctx, "فشلت عملية التعليق", Toast.LENGTH_SHORT).show();
                        }


                        @Override
                        protected Void doInBackground(Void... voids) {
                            res  = SERVER_INFO2.SAVE_COMMETES(ctx,support.support_id,commet,Config.shop_id);
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
        else
            Toast.makeText(this, "ادخل نص للتعليق", Toast.LENGTH_SHORT).show();

    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
    private void load_all_commects() {
        try{
            if (Config.internetAvailable(this)) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        ctx = CommentsActivity.this;
                        progressDialog  = new ProgressDialog(ctx);
                        progressDialog.setMessage("جاري تحميل التعليقات");
                        progressDialog.show();

                    }
                    Context ctx;
                    String res = "";
                    ProgressDialog progressDialog;
                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        dispay_data(res);
                        progressDialog.dismiss();

                    }


                    @Override
                    protected Void doInBackground(Void... voids) {
                        res = SERVER_INFO2.GET_ALL_COMMENTS(support.support_id,ctx);
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

    private void dispay_data(String res) {

        try{
            JSONArray arr = new JSONArray(res);
            ArrayList<comments_class> all_data = new ArrayList<>();
            for (int i=0 ;i<arr.length();i++)
            {
                JSONObject object   = arr.getJSONObject(i);
                comments_class c = new comments_class();
                c.commet_desc = object.getString("commet_desc");
                c.shop_id = object.getString("shop_id");
                c.commet_date = object.getString("commet_date");
                c.commet_id = object.getString("commet_id");
                c.shop_name = object.getString("shop_name");
                c.support_id = object.getString("support_id");
                all_data.add(c);
            }
            comments_adapter adp = new comments_adapter(this,R.layout.item_comments,all_data);
            ListView lst_data = findViewById(R.id.lst_data);
            lst_data.setAdapter(adp);

        }catch (Exception ex)
        {
            Toast.makeText(this, "فشلت عملية التحميل للبيانات", Toast.LENGTH_SHORT).show();
        }

    }
}
