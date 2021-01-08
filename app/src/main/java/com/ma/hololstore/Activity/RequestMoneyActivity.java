package com.ma.hololstore.Activity;

import androidx.appcompat.app.AppCompatActivity;
import in.mayanknagwanshi.imagepicker.ImageSelectActivity;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ma.hololstore.Classes.Config;
import com.ma.hololstore.R;
import com.ma.hololstore.Server.SERVER_INFO2;

public class RequestMoneyActivity extends AppCompatActivity {


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_money);
        txt_send_name = findViewById(R.id.txt_send_name);
        txt_request_money = findViewById(R.id.txt_request_money);
        txt_eshaar = findViewById(R.id.txt_eshaar);
        txt_note = findViewById(R.id.txt_note);
        txt_bank = findViewById(R.id.txt_bank);
        img_get_picture = findViewById(R.id.img_get_picture);
        bt_request_money = findViewById(R.id.bt_request_money);
        img_get_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_image();
            }
        });
        bt_request_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_request_image();
            }
        });
    }

    boolean is_image =false;
    private void save_request_image() {

if (is_image && txt_request_money.getText().length()>0 && txt_send_name.getText().length()>0) {
    if (Config.internetAvailable(RequestMoneyActivity.this)) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                res = SERVER_INFO2.SEND_REQUEST_MONEY(name, money, eshare, note, imag ,bank , ctx);
                return null;
            }

            String res, name, money, eshare, note , imag , bank;
            Context ctx;

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                progressDialog.dismiss();
                try {
                    if (res.contains("OK"))
                    {   Config.show_msg(ctx, "تمت عملية طلب الرصيد بنجاح . سيصلك اشعار عند الاضافة");
                    txt_eshaar.setText("");
                    txt_note.setText("");
                    txt_request_money.setText("");
                    txt_send_name.setText("");
                        txt_bank.setText("");
                    is_image=false;

                    }
                    else
                        Config.show_msg(ctx, "فشل تسجيل الحساب الجديد . حاول مرة أخرى ");
                } catch (Exception ex) {
                    Config.show_msg(ctx, "فشل تسجيل الحساب الجديد . حاول مرة أخرى ");

                }
            }

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                ctx = RequestMoneyActivity.this;
                progressDialog = new ProgressDialog(ctx);
                eshare = txt_eshaar.getText().toString();
                name=txt_send_name.getText().toString();
                note=txt_note.getText().toString();
                bank=txt_bank.getText().toString();
                money=txt_request_money.getText().toString();
                imag=Config.IMAGE_TO_BASE64(img_get_picture);
                progressDialog.setMessage("جاري طلب الرصيد");
                progressDialog.show();
            }
        }.execute();
    } else
        Toast.makeText(RequestMoneyActivity.this, "لا يوجد اتصال انترنت", Toast.LENGTH_SHORT).show();

}else

    Config.show_msg(this,"ادخل كل البيانات للمتابعة ففي عملية طلب الرصيد ");

    }

    private void select_image() {
    try {
        Intent intent = new Intent(this, ImageSelectActivity.class);
        intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, true);//default is true
        intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
        intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
        intent.putExtra(ImageSelectActivity.FLAG_CROP, false);//default is false
        startActivityForResult(intent, 1213);
    }catch (Exception ex){}
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1213 && resultCode == Activity.RESULT_OK) {
            String filePath = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
            Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
            img_get_picture.setImageBitmap(selectedImage);
            is_image=true;
        }
    }

    EditText txt_send_name;
    EditText     txt_request_money;
    EditText  txt_eshaar;
    EditText    txt_note;
    EditText    txt_bank;
  ImageView img_get_picture;
           Button bt_request_money;


}
