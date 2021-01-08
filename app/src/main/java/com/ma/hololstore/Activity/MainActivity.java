package com.ma.hololstore.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ma.hololstore.Classes.Config;
import com.ma.hololstore.R;
import com.ma.hololstore.Server.SERVER_INFO2;
import com.ma.hololstore.Adapters.SliderAdapter;
import com.ma.hololstore.Classes.card_class;
import com.ma.hololstore.Adapters.category_adapter;
import com.ma.hololstore.Classes.category_class;
import com.ma.hololstore.Adapters.my_seller_adapter;
import com.ma.hololstore.Adapters.request_adapter;
import com.ma.hololstore.Classes.request_class;
import com.ma.hololstore.Classes.serial_class;
import com.ma.hololstore.Classes.slider_class;
import com.ma.hololstore.Adapters.support_adapter;
import com.ma.hololstore.Classes.support_class;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
    Toolbar toolbar=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar  = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        lst_data = findViewById(R.id.lst_data);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        FirebaseApp.initializeApp(this);
        init_firebase_token();
        load_slider_from_internet();
        diaplay_data_now_slider("" , 1);

        ress();

        check_for_permision();
    }

    private void check_for_permision() {

        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyhavePermission()) {
                requestForSpecificPermission();
            }
        }
    }

    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN }, 101);
    }

    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }


    private void load_slider_from_internet()
    {
        final Context ctx = MainActivity.this;
        if (Config.internetAvailable(this))
        {
            new AsyncTask<Void,Void,Void>(){
                String res="";
                @Override
                protected Void doInBackground(Void... voids) {
                    res = SERVER_INFO2.GET_ALL_SLIDERS(ctx);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    diaplay_data_now_slider(res, 0);
                }
            }.execute();

        }

    }



    public void diaplay_data_now_slider(String result , int load) {
        try{
            if (load == 0) {
                if (Config.GET_KEY(this, "slider").equals(result))
                    return;
                Config.SaveKEY(this, "slider", result);
            }
            else
                result =Config.GET_KEY(this, "slider");
            JSONArray arr = new JSONArray(result);
            all_slider = new ArrayList<>();
            for (int i=0;i<arr.length();i++)
            {
                JSONObject obj = arr.getJSONObject(i);
                slider_class c = new slider_class();
                c.slider_id = obj.getString("slider_id");
                c.slider_image = obj.getString("slider_image");
                c.slider_url = obj.getString("slider_url");
                all_slider.add(c);
            }

            SliderView sliderView = findViewById(R.id.imageSlider);
            sliderView.setSliderAdapter(new SliderAdapter(this , all_slider));
            sliderView.startAutoCycle();
            sliderView.setIndicatorAnimation(IndicatorAnimations.WORM);
            sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);


        }catch (Exception ex){
            Log.i(Config.TAG, "diaplay_data_now_slider: "+ex.getMessage());
        }

    }
    ArrayList<slider_class> all_slider = new ArrayList<slider_class>();





    private void init_firebase_token() {

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.i(Config.TAG, " MyFirebaseMessagingService getInstanceId failed", task.getException());
                            return;
                        }
                        FirebaseMessaging.getInstance().subscribeToTopic("all");
                        send_token_to_server(task.getResult().getToken());
                    }
                });
    }

    private void send_token_to_server(String token) {
        try{
            Log.i(Config.TAG, "send_token_to_server: "+token);
            SERVER_INFO2.SEND_TOKEN_TO_SERVER(this , token , Config.GET_IMEI(this));

        } catch (Exception ex){}
    }
    ListView lst_data;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (idres>0){
                idres=0;
                ress();
            }
            else
                super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //  setTitle(" الرصيد  " + Config.shop_account );
        // getSupportActionBar().setTitle(" أهلا: " + Config.shop_name); ;

        try{

            c.findItem(R.id.action_settings).setTitle(" الرصيد  " + Config.shop_account);

        }catch (Exception ex){}
        ((TextView) ((NavigationView)
                findViewById(R.id.nav_view)).getHeaderView(0).findViewById(R.id.txt_shop_name)).setText(  " أهلا: " + Config.shop_name );



    }

    Menu c = null;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        try {
            getMenuInflater().inflate(R.menu.main, menu);
            c = menu;
            c.findItem(R.id.action_settings).setTitle(" الرصيد  " + Config.shop_account);

        }catch (Exception ex){

        }
        return true;

    }



    private void ress()
    {
        if ( idres==0) {
            load_all_categorys();

        } else if (idres==1) {
            load_all_my_sellers();
            idres=1;
        } else if (idres==2) {
            load_all_support();
            idres=2;
        } else if (idres==3) {
            load_all_my_service();
            idres=3;
        } else   if (idres==4) {
            load_all_requests();
            idres=4;
        }
    }


    int idres = 0;

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id  = item.getItemId();
        if (id == R.id.nav_request_rassed_list) {
            startActivity(new Intent(MainActivity.this, RassedTransActivity.class));
        }else   if (id == R.id.nav_request_rassed) {
            startActivity(new Intent(MainActivity.this, RequestMoneyActivity.class));
        }else
        if (id == R.id.nav_search) {
            startActivity(new Intent(MainActivity.this, SearchActivity.class));
        }else
        if (id == R.id.nav_whatsapp) {
            String url = "https://wa.me/218913929135";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }else
        if (id == R.id.nav_password) {
            startActivity(new Intent(MainActivity.this,ChangePasswordActivity.class));
        }else
        if (id == R.id.nav_profile) {
            startActivity(new Intent(MainActivity.this,ProfileActivity.class));
        }else
        if (id == R.id.nav_send_money) {
            startActivity(new Intent(MainActivity.this,EnterNumberActivity.class));
        }else

        if (id == R.id.nav_print) {
            startActivity(new Intent(MainActivity.this,DeviceListActivity.class));
        }else
        if (id == R.id.nav_notifiation) {
            startActivity(new Intent(MainActivity.this, NotifyActivity.class));
        }else

        if (id == R.id.nav_buy_cards) {
            load_all_categorys();
            idres=0;
        } else if (id == R.id.nav_my_sellers) {
            load_all_my_sellers();
            idres=1;
        } else if (id == R.id.nav_supports) {
            load_all_support();
            idres=2;
        }  else if (id == R.id.nav_add_support) {
            startActivity(new Intent(MainActivity.this, AddSupportActivity.class));
        }
        else if (id == R.id.nav_logout) {
            logout();
        } else if (id == R.id.nav_call) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel","+218913929135", null));
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            String data = getString(R.string.app_name) + "\n"+"https://play.google.com/store/apps/details?id="+this.getPackageName();
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString( R.string.app_name));
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, data);
            startActivity(Intent.createChooser(sharingIntent, "مشاركة..."));

        }
        else if (id == R.id.nav_send)
        { show_send_dialog();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void logout() {
        new AlertDialog.Builder(this).setMessage("هل تريد بالتاكيد تسجيل الخروج من البرنامج .  ").setPositiveButton("تسجيل خروج ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                log_out_now();
            }
        }).setNegativeButton("الغاء " , null ).show();
    }
    private void log_out_now() {
        new AsyncTask<Void,Void,Void>(){
            ProgressDialog progressDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("جاري تسجيل الخروج ");
                progressDialog.show();
                ctx = MainActivity.this;
            }
            Context ctx;
            @Override
            protected Void doInBackground(Void... voids) {
                res = SERVER_INFO2.LOGOUT(ctx);
                return null;
            }
            String res="";
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progressDialog.dismiss();
                if (res.contains("OK"))
                {
                    Config.shop_id="-2";
                    Config.shop_account=0;
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    MainActivity.this.finish();
                }
                else
                    Config.show_msg(MainActivity.this,"فشلت عملية تسجيل الخروج ");
            }
        }.execute();
    }
    private void show_send_dialog() {
        try{
            final Dialog d = new Dialog(this);
            d.setTitle("راسلنا");
            d.setContentView(R.layout.dialog_contact);
            final  EditText txt = d.findViewById(R.id.txt_contact_us);
            Button bt = d.findViewById(R.id.bt_send_message);
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (txt.getText().length()>0)
                    {
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString( R.string.app_name));
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, txt.getText().toString());
                        startActivity(Intent.createChooser(sharingIntent, "راسلنا..."));
                        d.dismiss();
                    }
                    else
                        Toast.makeText(MainActivity.this, "أدخل نص للرسالة", Toast.LENGTH_SHORT).show();
                }
            });
            d.show();

        }catch (Exception ex){}

    }

    private void load_all_requests() {

        try {
            if (Config.internetAvailable(this)) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        ctx = MainActivity.this;
                        progressDialog = new ProgressDialog(ctx);
                        progressDialog.setMessage("جاري تحميل كل طلبات الخدمات");
                        progressDialog.show();

                    }

                    Context ctx;
                    String res = "";
                    ProgressDialog progressDialog;

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        display_all_request(res);
                        progressDialog.dismiss();
                    }


                    @Override
                    protected Void doInBackground(Void... voids) {
                        res = SERVER_INFO2.GET_ALL_MY_REQUESTS(ctx);

                        return null;
                    }
                }.execute();
            } else
                Toast.makeText(this, "لا يوجد اتصال بالانترنت ", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
        }
    }

    private void display_all_request(final String res) {
        try {
            JSONArray arr = new JSONArray(res);
            ArrayList<request_class> all_data = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject object = arr.getJSONObject(i);
                request_class c = new request_class();
                c.request_type = object.getString("request_type");
                c.service_name = object.getString("service_name");
                c.request_date = object.getString("request_date");
                c.service_id = object.getString("service_id");
                c.request_eshtrak = object.getString("request_eshtrak");
                c.request_mobile = object.getString("request_mobile");
                c.shop_id = object.getString("shop_id");
                c.request_id = object.getString("request_id");

                all_data.add(c);
            }
            request_adapter adp = new request_adapter(this, R.layout.item_request, all_data);
            lst_data.setAdapter(adp);
            lst_data.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    request_class    request = (request_class) parent.getItemAtPosition(position);
                    ServiceResultActivity.mobile=request.request_mobile;
                    ServiceResultActivity.eshtrak=request.request_eshtrak;
                    ServiceResultActivity.request_id=request.request_id;
                    ServiceResultActivity.service_price=request.service_price;
                    ServiceResultActivity.service_name=request.service_name;

                    startActivity(new Intent(MainActivity.this, ServiceResultActivity.class));
                }
            });
        } catch (Exception ex) {
            Toast.makeText(this, "فشلت عملية التحميل للخدمات", Toast.LENGTH_SHORT).show();
        }

    }

    private void load_all_my_service() {

        try {
            if (Config.internetAvailable(this)) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        ctx = MainActivity.this;
                        progressDialog = new ProgressDialog(ctx);
                        progressDialog.setMessage("جاري تحميل كل الخدمات");
                        progressDialog.show();

                    }

                    Context ctx;
                    String res = "";
                    ProgressDialog progressDialog;

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        display_all_services(res);
                        progressDialog.dismiss();
                    }


                    @Override
                    protected Void doInBackground(Void... voids) {
                        res = SERVER_INFO2.GET_ALL_SERVICE(Config.country_id, ctx);

                        return null;
                    }
                }.execute();
            } else
                Toast.makeText(this, "لا يوجد اتصال بالانترنت ", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
        }

    }
    private void display_all_services(String res) {
        try {
            JSONArray arr = new JSONArray(res);
            ArrayList<category_class> all_data = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject object = arr.getJSONObject(i);
                category_class c = new category_class();
                c.category_id = object.getString("category_id");
                c.category_image = object.getString("category_image");
                c.category_name = object.getString("category_name");
                c.category_desc = object.getString("category_desc");
                all_data.add(c);
            }
            category_adapter adp = new category_adapter(this, R.layout.item_category, all_data);
            lst_data.setAdapter(adp);
            lst_data.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    category_class id2 = ((category_class) parent.getItemAtPosition(position));
                    load_service_and_display(id2);
                    // idres=6;
                }
            });
        } catch (Exception ex) {
            Toast.makeText(this, "فشلت عملية التحميل للبيانات", Toast.LENGTH_SHORT).show();
        }
    }

    private void load_service_and_display(category_class id2) {
        ServiceActivity.category = id2;
        startActivity(new Intent(MainActivity.this,ServiceActivity.class));
    }

    /*


     */
    private void load_all_support() {

        try {
            if (Config.internetAvailable(this)) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        ctx = MainActivity.this;
                        progressDialog = new ProgressDialog(ctx);
                        progressDialog.setMessage("جاري تحميل تذاكر الدعم الفني");
                        progressDialog.show();

                    }

                    Context ctx;
                    String res = "";
                    ProgressDialog progressDialog;

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        display_all_tickets(res);
                        progressDialog.dismiss();
                    }


                    @Override
                    protected Void doInBackground(Void... voids) {
                        res = SERVER_INFO2.GET_SUPPORTS(ctx);
                        return null;
                    }
                }.execute();
            } else
                Toast.makeText(this, "لا يوجد اتصال بالانترنت ", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
        }

    }

    private void display_all_tickets(String res) {
        try {
            JSONArray arr = new JSONArray(res);
            ArrayList<support_class> all_data = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject object = arr.getJSONObject(i);
                support_class c = new support_class();
                c.support_type = object.getString("support_type");
                c.shop_id = object.getString("shop_id");
                c.support_date = object.getString("support_date");
                c.support_desc = object.getString("support_desc");
                c.support_name = object.getString("support_name");
                c.support_id = object.getString("support_id");
                all_data.add(c);
            }
            support_adapter adp = new support_adapter(this, R.layout.item_supports, all_data);
            lst_data.setAdapter(adp);
            lst_data.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CommentsActivity.support = ((support_class) parent.getItemAtPosition(position));
                    startActivity(new Intent(MainActivity.this, CommentsActivity.class));
                }
            });
        } catch (Exception ex) {
            Toast.makeText(this, "فشلت عملية التحميل للبيانات", Toast.LENGTH_SHORT).show();
        }

    }

    private void load_all_my_sellers() {
        try {
            if (Config.internetAvailable(this)) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        ctx = MainActivity.this;
                        progressDialog = new ProgressDialog(ctx);
                        progressDialog.setMessage("جاري تحميل مشترياتي");
                        progressDialog.show();

                    }

                    Context ctx;
                    String res = "";
                    ProgressDialog progressDialog;

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        display_my_seller(res);
                        progressDialog.dismiss();
                    }


                    @Override
                    protected Void doInBackground(Void... voids) {
                        res = SERVER_INFO2.GET_MY_SELLER(ctx);
                        return null;
                    }
                }.execute();
            } else
                Toast.makeText(this, "لا يوجد اتصال بالانترنت ", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
        }
    }

    private void display_my_seller(String res) {
        try {
            JSONArray arr = new JSONArray(res);
            ArrayList<serial_class> all_data = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject object = arr.getJSONObject(i);
                serial_class c = new serial_class();
                c.card_id = object.getString("card_id");
                c.card_name = object.getString("card_name");
                c.serial_company = object.getString("serial_company");
                c.serial_date = object.getString("serial_date");
                c.serial_id = object.getString("serial_id");
                c.serial_type = object.getString("serial_type");
                c.serial_value = object.getString("serial_value");
                c.shop_id = object.getString("shop_id");

                all_data.add(c);
            }
            my_seller_adapter adp = new my_seller_adapter(this, R.layout.item_serials, all_data);
            lst_data.setAdapter(adp);
            lst_data.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    serial_class s = (serial_class) parent.getItemAtPosition(position);
                    ArrayList<serial_class> c  = new ArrayList<>();
                    c.add(s);

                    ViewCardDetialActivity.data=c;
                    ViewCardDetialActivity.card_name = s.card_name;
                    startActivity(new Intent(MainActivity.this, ViewCardDetialActivity.class));
                }
            });
        } catch (Exception ex) {
            Toast.makeText(this, "فشلت عملية التحميل للبيانات", Toast.LENGTH_SHORT).show();
        }
    }

    private void load_all_categorys() {
        try {
            if (Config.internetAvailable(this)) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        ctx = MainActivity.this;
                        progressDialog = new ProgressDialog(ctx);
                        progressDialog.setMessage("جاري تحميل التصنيفات");
                        progressDialog.show();

                    }

                    Context ctx;
                    String res = "";
                    ProgressDialog progressDialog;

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        display_category(res);
                        progressDialog.dismiss();
                    }


                    @Override
                    protected Void doInBackground(Void... voids) {
                        res = SERVER_INFO2.GET_CATEGORY(ctx);
                        return null;
                    }
                }.execute();
            } else
                Toast.makeText(this, "لا يوجد اتصال بالانترنت ", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
        }

    }


    private void display_category(String res) {
        try {
            JSONArray arr = new JSONArray(res);
            ArrayList<category_class> all_data = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject object = arr.getJSONObject(i);
                category_class c = new category_class();
                c.category_id = object.getString("category_id");
                c.category_image = object.getString("category_image");
                c.category_name = object.getString("category_name");
                all_data.add(c);
            }
            category_adapter adp = new category_adapter(this, R.layout.item_category, all_data);
            lst_data.setAdapter(adp);
            lst_data.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    category_class id2 = ((category_class) parent.getItemAtPosition(position));
                    load_card_and_display(id2);
                    // idres=6;
                }
            });
        } catch (Exception ex) {
            Toast.makeText(this, "فشلت عملية التحميل للبيانات", Toast.LENGTH_SHORT).show();
        }
    }




    private void load_card_and_display( category_class id2) {
        SelectCardsActivity.category = id2;
        startActivity(new Intent(MainActivity.this, SelectCardsActivity.class));
    }


    private void show_confirm_To_buy(final card_class itemAtPosition) {

        AlertDialog.Builder builder5 = new AlertDialog.Builder(this);

        builder5.setTitle("تأكيد");
        builder5.setMessage("هل انت متأكد من شراء  " + itemAtPosition.card_name + " ?");

        builder5.setPositiveButton("شراء",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (Config.shop_account >= itemAtPosition.card_seller_price) {
                            // buy_card_now(itemAtPosition);
                        } else
                            Toast.makeText(MainActivity.this, "لا يوجد رصيد كافي لاتمام العملية ", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();


                    }
                });
        builder5.setNegativeButton("الغاء", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                arg0.dismiss();
            }
        });
        AlertDialog alert = builder5.create();
        alert.show();

    }

}