package com.ma.hololstore.Activity;

import androidx.appcompat.app.AppCompatActivity;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ma.hololstore.Adapters.search_adapter;
import com.ma.hololstore.Classes.Config;
import com.ma.hololstore.Classes.shop_class;
import com.ma.hololstore.R;
import com.ma.hololstore.Server.SERVER_INFO2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        txt_search = findViewById(R.id.txt_search);

        txt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (txt_search.getText().length()>0)
                        Search(txt_search.getText().toString());
                    else
                        Config.show_msg(SearchActivity.this, "ادخل اسم المتجر للبحث عنه " );
                    return true;
                }
                return false;
            }
        });
        findViewById(R.id.bt_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_search.getText().length()>0)
                    Search(txt_search.getText().toString());
                else
                    Config.show_msg(SearchActivity.this, "ادخل اسم المتجر للبحث عنه " ) ;
            }
        });
        lst_ads = (ListView) findViewById(R.id.lst_ads);


    }

    private void Search( final String name) {

        final Context ctx = this;
        if (Config.internetAvailable(this))
        {
            new AsyncTask<Void,Void,Void>(){
                @Override
                protected Void doInBackground(Void... voids) {
                res = SERVER_INFO2.SEARCH(ctx, name);
                    return null;
                }
String res = "";

                ProgressDialog progressDialog;
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progressDialog = new ProgressDialog(ctx);
                    progressDialog.setMessage("جاري البحث");
                progressDialog.show();
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                progressDialog.dismiss();
                dispaly_res(res);
                }


            }.execute();


        }
        else
            Config.show_msg(SearchActivity.this, "لا يتوفر اتصال انترنت  " );

    }

    ListView lst_ads;
    EditText txt_search;



    private void dispaly_res(String res) {
        try{
            TextView txt_no_result = findViewById(R.id.txt_no_result);
            Log.i(Config.TAG, "dispaly_res: "+res);
            JSONArray arr = new JSONArray(res);
            if (arr.length()>0) {
                ArrayList<shop_class> data = new ArrayList<>();
                txt_no_result.setVisibility(View.INVISIBLE);
                for (int i=0;i<arr.length();i++){
                    JSONObject obj = arr.getJSONObject(i);
                    shop_class c = new shop_class();
                    c.shop_mobile = obj.getString("shop_mobile");
                    c.shop_address = obj.getString("shop_address");
                    c.shop_id = obj.getString("shop_id");
                    c.shop_name = obj.getString("shop_name");
                data.add(c);
                }
                search_adapter adp = new search_adapter(this,R.layout.item_search,data);
                lst_ads = findViewById(R.id.lst_ads);
                lst_ads.setAdapter(adp);
            }
            else
            txt_no_result.setVisibility(View.VISIBLE);
        }catch (Exception ex){}

    }


    private boolean isLoading=false;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

}
