package com.ma.hololstore.Activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ma.hololstore.R;
import com.ma.hololstore.Classes.serial_class;

import java.util.ArrayList;

public class ViewCardDetialActivity extends AppCompatActivity {
    public static ArrayList<serial_class> data = new ArrayList<>();

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

   /* public static String serial_id ="";
    public static String serial_value = "";
    public static String serial_company = "";*/
    public static String card_name = "";
    public static String card_price = "";
    String res = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_card_detial);

        ((TextView) findViewById(R.id.item_card_name)).setText(card_name);
       // ((TextView) findViewById(R.id.item_card_buyer_price)).setText("مبلغ العملية للزبون : "+card_price);

        for (int i=0;i<data.size();i++){
            res+="رقم السيريال : " +data.get(i).serial_company + "\n";
            res+="رقم البطاقة : " +data.get(i).serial_value + "\n";
            res+="رقم العملية : " +data.get(i).serial_id + "\n";
            res+="*************************"  +"\n";

        }
   //     ((TextView) findViewById(R.id.item_card_company_serial)).setText("رقم السيريال : " +serial_company);
       // ((TextView) findViewById(R.id.item_card_my_serial)).setText("رقم العملية : " + serial_id);
        ((TextView) findViewById(R.id.item_card_serial)).setText(res);
       // ((EditText) findViewById(R.id.txt_client_mobile)).setText(phone);

        ((Button) findViewById(R.id.bt_end_seller)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent m = new Intent(ViewCardDetialActivity.this,PrintsActivity.class);
                    PrintsActivity.cards=ViewCardDetialActivity.data;
                    startActivity(m);
                } catch (Exception e) {
                     e.printStackTrace();
                }
            }
        });
        ((Button) findViewById(R.id.bt_copy)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copy_text();
            }
        });

        ((Button) findViewById(R.id.bt_share)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share_text();
            }
        });

    }
    private void share_text() {
          String data = get_data();
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString( R.string.app_name));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, data);
        startActivity(Intent.createChooser(sharingIntent, "مشاركة..."));
    }




    private String get_data() {
try{
    String data = getString(R.string.app_name) + "\n"+card_name+"\n"+ res;
    return  data;

}catch (Exception ex){}
    return  "";
    }

    private void copy_text() {
        String s = data.get(0).serial_value;
         ClipboardManager clipboard = (ClipboardManager)  getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(s,s);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "تم نسخ البطاقة ", Toast.LENGTH_SHORT).show();

    }
}
