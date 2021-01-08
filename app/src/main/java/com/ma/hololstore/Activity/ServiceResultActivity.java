package com.ma.hololstore.Activity;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ma.hololstore.R;

public class ServiceResultActivity extends AppCompatActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    public static String mobile="";
    public static String eshtrak="";

    public static String service_name="";
    public static String service_price="";
    public static String request_id="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_result);
        ((EditText) findViewById(R.id.txt_client_mobile)).setText(mobile);
        ((EditText) findViewById(R.id.txt_client_eshtrak)).setText(eshtrak);
        ((TextView) findViewById(R.id.item_service_name)).setText(service_name);
        ((TextView) findViewById(R.id.item_request_serial)).setText("رقم العملية : " + request_id);
        ((TextView) findViewById(R.id.item_card_buyer_price)).setText("مبلغ العملية: " + service_price);
        ((Button) findViewById(R.id.bt_end_seller)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ServiceResultActivity.this, MainActivity.class));
                ServiceResultActivity.this.finish();
            }
        });
    }
}
