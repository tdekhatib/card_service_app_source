package com.ma.hololstore.Activity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dantsu.escposprinter.EscPosCharsetEncoding;
import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.hoin.btsdk.BluetoothService;
import com.hoin.btsdk.PrintPic;

import com.ma.hololstore.Classes.Config;
import com.ma.hololstore.Classes.PrinterCommands;
import com.ma.hololstore.R;
import com.ma.hololstore.Classes.serial_class;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;
public class PrintsActivity extends AppCompatActivity {
   // WebView wb = null;
    String url="";
    public static ArrayList<serial_class> cards=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prints);
       // final WebView wb = findViewById(R.id.wbview);

        display_cards();
        (findViewById(R.id.bt_print)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                layout= new LinearLayout(PrintsActivity.this);
                layout.setPadding(0,0,0,0);

               /* Bitmap bmp = GetForInv();
               File filename = new File( Config.GET_PATH(PrintsActivity.this)+"image.jpg");
                //  File filename = new File( "mnt/sdcard/image.jpg");
                FileOutputStream fOut = null;

                    fOut = new FileOutputStream(filename);
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                  //  printImage(filename.getAbsolutePath());
                    printTest(filename.getAbsolutePath());*/


                    String text = GetForInv();
                  //  Toast.makeText(PrintsActivity.this, text, Toast.LENGTH_SHORT).show();
                    printTest(text);

                } catch (Exception e) {
                    Toast.makeText(PrintsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.i(Config.TAG, "onClick: "+e.getMessage());
                    e.printStackTrace();
                }

            }
        });

      }

    private void printTest(String text) {
        try {
            EscPosPrinter printer = new EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(), 203, 48f, 32);
            printer.printFormattedText(text);
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    private void add_image_barcode(LinearLayout lay , String text )
    {
        try {
           ImageView img = get_image_barrcode(text);
            lay.addView(img);
        }catch (Exception ex){
            Log.i(Config.TAG, "add_image_barcode: "+ex.getMessage());
        }
    }

    private ImageView get_image_barrcode(String text) {
    try{
        ImageView valueTV = new ImageView(this);

        //    valueTV.setForegroundGravity(Gravity.CENTER);
        final float scale =this.getResources().getDisplayMetrics().density;
        int pixels = (int) (140 * scale + 0.5f);

        valueTV.setLayoutParams(new LinearLayout.LayoutParams(pixels, pixels));
        valueTV.setScaleType(ImageView.ScaleType.FIT_XY);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) valueTV.getLayoutParams();
        params.gravity=Gravity.CENTER_HORIZONTAL;

        params.setMargins(0, 5, 0, 0); //substitute parameters for left, top, right, bottom
        valueTV.setLayoutParams(params);
        try {
            Bitmap bitmap = encodeAsBitmap(text);
            valueTV.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return valueTV;
    }catch (Exception ex){}
    return null;
    }

    private void add_text_view(LinearLayout lay , String text , int i)
    {
      try {
          TextView valueTV = new TextView(this);
          Typeface tf = Typeface.createFromAsset(this.getAssets(), "fonts/font2.ttf");
          valueTV.setTypeface(tf, Typeface.NORMAL);
          if (i==0) {
              valueTV.setText(text);
              valueTV.setTextColor(BLACK);
              valueTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
              valueTV.setGravity(Gravity.CENTER);
              valueTV.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
              LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) valueTV.getLayoutParams();
              params.setMargins(0, 5, 0, 0); //substitute parameters for left, top, right, bottom
              valueTV.setLayoutParams(params);

          }else
          {
               valueTV.setTextColor(BLACK);
              valueTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
              valueTV.setGravity(Gravity.CENTER);
              valueTV.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
              LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) valueTV.getLayoutParams();
              params.setMargins(0, 5, 0, 0); //substitute parameters for left, top, right, bottom
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                  valueTV.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT));
              } else {
                  valueTV.setText(Html.fromHtml(text));
              }
              valueTV.setLayoutParams(params);
          }
          lay.addView(valueTV);
      }catch (Exception ex){
          Log.i(Config.TAG, "add_text_view: "+ex.getMessage());
      }
      }
    private void display_cards() {

        try {
            Log.i(Config.TAG, "display_cards: 1111");
            LinearLayout lay = findViewById(R.id.lay_print);
            // add_text_view(lay , Config.GET_DATE(Long.valueOf( cards.get(0).serial_date)),0);
            // add_text_view(lay , "***********************",0);
            add_text_view(lay, (ViewCardDetialActivity.card_name), 0);
            for (int i = 0; i < cards.size(); i++) {
                serial_class c = cards.get(i);
                add_text_view(lay, "***********************", 0);
                add_text_view(lay, "رقم البطاقة", 0);

                add_text_view(lay, c.serial_value, 0);
                add_image_barcode(lay, c.serial_company);
                add_text_view(lay, "***********************", 0);
                add_text_view(lay, "رقم العملية", 0);
                add_text_view(lay, c.serial_id, 0);
            }
            add_text_view(lay, "***********************", 0);
            add_text_view(lay, "شكرا لاستخدام خدمات حلول ستور ", 1);
            add_text_view(lay, "***********************", 0);
        }catch (Exception ex){}

        }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        cards=null;

close_bluetooth_connection();

    }
    private void drwa_img( ImageView img_bacrcode)
    {
      //  img_bacrcode.invalidate();
   try{     ImageView img = new ImageView(this);
        img.setImageDrawable(img_bacrcode.getDrawable());/*
img.getLayoutParams().height=150;
img.getLayoutParams().width=150;
img.setScaleType(ImageView.ScaleType.FIT_XY);*/
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(img);}catch (Exception ex){}
    }

    private void drawCenter2( String text , int c ) {
      try { // layout.setGravity(Gravity.CENTER_HORIZONTAL);
          layout.setOrientation(LinearLayout.VERTICAL);
          TextView textView = new TextView(this);
          textView.setWidth(px);
          textView.setPadding(0, -7, 0, -7);
          textView.setGravity(Gravity.CENTER_HORIZONTAL);
          textView.setTextColor(Color.argb(255, 30, 30, 30));
          textView.setVisibility(View.VISIBLE);
          if (c == 1)
              textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, Integer.valueOf(Config.GET_KEY(this, "font")) - 2);
          else
              textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, Integer.valueOf(Config.GET_KEY(this, "font")));

          Typeface tf = Typeface.createFromAsset(this.getAssets(), "fonts/font2.ttf");
          textView.setTypeface(tf, Typeface.NORMAL);
          if (c == 1) {
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                  textView.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT));
              } else {
                  textView.setText(Html.fromHtml(text));
              }
          } else
              textView.setText(text);
          layout.addView(textView);
      /*  layout.measure(canvas.getWidth(), canvas.getHeight());
        layout.layout(0, 0, canvas.getWidth(), canvas.getHeight());
        layout.draw(canvas);

*/
      }catch (Exception ex){}
    }
    private void drawCenter3(  String text ) {
    try {
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.setOrientation(LinearLayout.VERTICAL);
        TextView textView = new TextView(PrintsActivity.this);
        textView.setWidth(px);
        textView.setPadding(0, -7, 0, -7);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setTextColor(BLACK);
        textView.setVisibility(View.VISIBLE);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, Integer.valueOf(Config.GET_KEY(this, "font")) + 2);
        Typeface tf = Typeface.createFromAsset(this.getAssets(), "fonts/font2.ttf");
        textView.setTypeface(tf, Typeface.NORMAL);
        textView.setText(text);
        layout.addView(textView);
      /*  layout.measure(canvas.getWidth(), canvas.getHeight());
        layout.layout(0, 0, canvas.getWidth(), canvas.getHeight());
        ayout.draw(canvas);

*/
    }catch (Exception ex){}
    }
    int px=400;
    private String GetForInv( )
    {
        try {

            String text="";
            for(int i=0;i<cards.size();i++) {
                drawCenter2(  "الفاتورة",0);
          //  drwa_img(canvas,img_company);
                drawCenter2(    Config.GET_DATE(Long.valueOf(cards.get(i).serial_date) ) ,0);
                drawCenter2(   "***********************",0);

                text += generate_text_for_print();


             //   drawCenter2(canvas, paint,    SelectCardsActivity.category.category_name ,0);
                drawCenter2(     ViewCardDetialActivity.card_name ,0);
                drawCenter2( "رقم البطاقة", 0);
                drawCenter2(     cards.get(i).serial_value ,0);
                text += generate_text_for_print();


                drawCenter2(     cards.get(i).serial_company ,0);
                drawCenter2(  "***********************", 0);
                drawCenter2( "رقم العملية", 0);

                drawCenter3(  cards.get(i).serial_id);
                text += generate_text_for_print();


                drwa_img(   get_image_barrcode(cards.get(i).serial_value));
                text += generate_text_for_print();


                //   paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
             drawCenter2(   "***********************",0);
            drawCenter2(  "شكرا لاستخدام خدمات حلول ستور",1   );
            drawCenter2(   "***********************",0);
                text += generate_text_for_print();


                drawCenter2(   Config.GET_KEY(this,"shop_name"),0);
             drawCenter2(   "  ",0);
                text += generate_text_for_print();


            }
            return  text;

        }catch (Exception ex)
        {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            Log.i(Config.TAG, "GetForInv: "+ex.getMessage());
            return "";
        }

    }

    private String generate_text_for_print() {

        try {
            Bitmap bitmap = Bitmap.createBitmap(px, 255, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(WHITE);
            Paint paint = new Paint();
            paint.setTextAlign(Paint.Align.LEFT);
            paint.setTextSize(30);

            layout.measure(canvas.getWidth(), canvas.getHeight());
            layout.layout(0, 0, canvas.getWidth(), canvas.getHeight());
            final Bitmap bitmap2 = Bitmap.createBitmap(px, layout.getMeasuredHeight() + 80, Bitmap.Config.ARGB_8888);
            Canvas canvas2 = new Canvas(bitmap2);
            canvas2.drawColor(WHITE);
            paint = new Paint();
            paint.setTextAlign(Paint.Align.LEFT);
            paint.setTextSize(30);
            layout.draw(canvas2);
            Drawable d = new BitmapDrawable(getResources(), bitmap2);
            EscPosPrinter printer = new EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(), 203, 48f, 32);
            String text=   "[L]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, d) + "</img>\n";

            layout= new LinearLayout(PrintsActivity.this);
            layout.setPadding(0,0,0,0);
            Log.i(Config.TAG, "generate_text_for_print: "+text);
            return text;
        }catch (Exception ex
        ){
            Log.i(Config.TAG, "generate_text_for_print: "+ex.getMessage());
        }
return  "";
    }

    LinearLayout layout =null;
   /* @Override
    public void onPause() {
        super.onPause();
    try {

            if (mService != null)
                mService.stop();
        mService = null;
        }catch (Exception ex){
            Toast.makeText(PrintsActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
     }*/
     private   final int REQUEST_ENABLE_BT = 2;
    private static    BluetoothService mService = null;
    private  static  BluetoothDevice con_dev = null;
    private static final int REQUEST_CONNECT_DEVICE = 1;
   @Override
    public void onResume() {
        super.onResume();
       try {
        if (mService ==null) {
                mService = new BluetoothService(PrintsActivity.this,mHandler);
                if (mService.isAvailable() == false) {
                    Toast.makeText(PrintsActivity.this, "لا يوجد اتصال بالطابعة", Toast.LENGTH_LONG).show();
                } else {
                   con_dev = mService.getDevByMac(Config.GET_KEY(PrintsActivity.this, "address_mac"));
                    //      con_dev = mService.getDevByMac("66:12:97:4A:D3:00");
                    mService.connect(con_dev);
                }
        }
   } catch (Exception ex) {
        Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();

    }

       try {
           {
               if (mService.isBTopen() == false) {
                   Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                   startActivityForResult(enableIntent, REQUEST_ENABLE_BT);

                   return;
               }
           }
       }catch (Exception ex){
           Toast.makeText(PrintsActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
       }

    }



int test=0;
    private void printImage(String path) {
        try {
            if (mService.isBTopen()) {

                Toast.makeText(this,"جاري الطباعة", Toast.LENGTH_SHORT).show();
                byte[] sendData = null;
                PrintPic pg = new PrintPic();
                 pg.initCanvas(570);
                pg.initPaint();
                pg.drawImage(0, 0, path);
                sendData = pg.printDraw();
                mService.write(PrinterCommands.SELECT_BIT_IMAGE_MODE);

                Thread.sleep(50);
                mService.write(sendData);
                Thread.sleep(50);//��ӡbyte������

            } else
                Toast.makeText(this, "لا يوجد اتصال بلوتوث", Toast.LENGTH_SHORT).show();
        }catch (Exception ex){

            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


  private final  Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case BluetoothService.MESSAGE_STATE_CHANGE:
                        switch (msg.arg1) {
                            case BluetoothService.STATE_CONNECTED:   //������
                                Toast.makeText(PrintsActivity.this,"تم الاتصال بنجاح بالطابعة",
                                        Toast.LENGTH_SHORT).show();

                                break;
                            case BluetoothService.STATE_CONNECTING:  //��������
                                break;
                            case BluetoothService.STATE_LISTEN:     //�������ӵĵ���
                            case BluetoothService.STATE_NONE:
                                break;
                        }
                        break;
                    case BluetoothService.MESSAGE_CONNECTION_LOST:    //�����ѶϿ�����
                        Toast.makeText(PrintsActivity.this, "انقطاع الاتصال ",
                                Toast.LENGTH_SHORT).show();
                      //  close_bluetooth_connection();

                        break;
                    case BluetoothService.MESSAGE_UNABLE_CONNECT:     //�޷������豸
                        Toast.makeText(PrintsActivity.this, "انقطاع الاتصال ",
                                Toast.LENGTH_SHORT).show();
                       // close_bluetooth_connection();
                        break;
                }
            }catch (Exception ex){
                Toast.makeText(PrintsActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    };

    private void close_bluetooth_connection() {
        try {
            if (mService != null)
            { mService.stop();}
        }catch (Exception ex) {
            Toast.makeText(PrintsActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        mService = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    try {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ENABLE_BT:      //���������
                if (resultCode == Activity.RESULT_OK) {   //�����Ѿ���
                    Toast.makeText(PrintsActivity.this,"تشغيل البلوتوث", Toast.LENGTH_LONG).show();
                }
                break;
            case REQUEST_CONNECT_DEVICE:     //��������ĳһ�����豸
                if (resultCode == Activity.RESULT_OK) {   //�ѵ�������б��е�ĳ���豸��
                    String address = data.getExtras()
                            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);  //��ȡ�б������豸��mac��ַ

                }
                break;
        }
    }catch (Exception ex){
        Toast.makeText(PrintsActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();

    }
    }



    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, 160, 160, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? Color.argb(230,25,25,25) : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, 160, 0, 0, w, h);
        return bitmap;
    }
    ImageView img_bacrcode;
    ImageView img_company;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_change_font_size)
        {
            show_dilaog_for_change_font();
        }
        return super.onOptionsItemSelected(item);
    }
    private void show_dilaog_for_change_font() {
        try{
            final Dialog d = new Dialog(this);
            d.setTitle("قم بتحديد حجم الخط");
            d.setContentView(R.layout.dialog_change_font);
            final TextView txt_font_size = d.findViewById(R.id.txt_font_size);
            final TextView txt_font_size_amount = d.findViewById(R.id.txt_font_size_amount);
            txt_font_size_amount.setText(Config.GET_KEY(this,"font"));
            txt_font_size.setTextSize(TypedValue.COMPLEX_UNIT_DIP,Integer.valueOf(Config.GET_KEY(this,"font")));
            (d.findViewById(R.id.image_font_add)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int c = Integer.valueOf(Config.GET_KEY(PrintsActivity.this,"font"));
                    c++;
                    txt_font_size.setTextSize(TypedValue.COMPLEX_UNIT_DIP,c);
                    txt_font_size_amount.setText(c+"");
                    Config.SaveKEY(PrintsActivity.this,"font",c+"");
                }
            });
            (d.findViewById(R.id.image_font_minus)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int c = Integer.valueOf(Config.GET_KEY(PrintsActivity.this,"font"));
                    c--;
                    txt_font_size.setTextSize(TypedValue.COMPLEX_UNIT_DIP,c);
                    txt_font_size_amount.setText(c+"");
                    Config.SaveKEY(PrintsActivity.this,"font",c+"");
                }
            });
            (d.findViewById(R.id.bt_close_font_size_diloag)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    d.dismiss();
                }
            });
            d.show();
        }catch (Exception ex){}
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.changefont, menu);

        return super.onCreateOptionsMenu(menu);
    }



/*

    private void print_image(String file) {
        File fl = new File(file);
        if (fl.exists()) {
            Bitmap bmp = BitmapFactory.decodeFile(file);
            convertBitmap(bmp);
            mService.write(PrinterCommands.SET_LINE_SPACING_24);

            int offset = 0;
            while (offset < bmp.getHeight()) {
                mService.write(PrinterCommands.SELECT_BIT_IMAGE_MODE);
                for (int x = 0; x < bmp.getWidth(); ++x) {

                    for (int k = 0; k < 3; ++k) {

                        byte slice = 0;
                        for (int b = 0; b < 8; ++b) {
                            int y = (((offset / 8) + k) * 8) + b;
                            int i = (y * bmp.getWidth()) + x;
                            boolean v = false;
                            if (i < dots.length()) {
                                v = dots.get(i);
                            }
                            slice |= (byte) ((v ? 1 : 0) << (7 - b));
                        }
                        mService.write(slice);
                    }
                }
                offset += 24;
                mService.write(PrinterCommands.FEED_LINE);
                mService.write(PrinterCommands.FEED_LINE);
                mService.write(PrinterCommands.FEED_LINE);
                mService.write(PrinterCommands.FEED_LINE);
                mService.write(PrinterCommands.FEED_LINE);
                mService.write(PrinterCommands.FEED_LINE);
            }
            mService.write(PrinterCommands.SET_LINE_SPACING_30);


        } else {
            Toast.makeText(this, "file doesn't exists", Toast.LENGTH_SHORT)
                    .show();
        }
    }



    public String convertBitmap(Bitmap inputBitmap) {

       int mWidth = inputBitmap.getWidth();
       int mHeight = inputBitmap.getHeight();

        convertArgbToGrayscale(inputBitmap, mWidth, mHeight);
     String   mStatus = "ok";
        return mStatus;

    }

    private void convertArgbToGrayscale(Bitmap bmpOriginal, int width,
                                        int height) {
        int pixel;
        int k = 0;
        int B = 0, G = 0, R = 0;
        dots = new BitSet();
        try {

            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    // get one pixel color
                    pixel = bmpOriginal.getPixel(y, x);

                    // retrieve color of all channels
                    R = Color.red(pixel);
                    G = Color.green(pixel);
                    B = Color.blue(pixel);
                    // take conversion up to one single value by calculating
                    // pixel intensity.
                    R = G = B = (int) (0.299 * R + 0.587 * G + 0.114 * B);
                    // set bit into bitset, by calculating the pixel's luma
                    if (R < 55) {
                        dots.set(k);//this is the bitset that i'm printing
                    }
                    k++;

                }


            }


        } catch (Exception e) {
            // TODO: handle exception
            Log.e(TAG, e.toString());
        }
    }
*/



}
