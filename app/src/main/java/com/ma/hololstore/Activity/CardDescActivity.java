package com.ma.hololstore.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ma.hololstore.Classes.Config;
import com.ma.hololstore.R;
import com.ma.hololstore.Server.SERVER_INFO2;
import com.ma.hololstore.Classes.card_class;
import com.ma.hololstore.Classes.serial_class;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CardDescActivity extends AppCompatActivity {
	public static card_class card = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_desc);
		Picasso.get().load(SERVER_INFO2.URL2 + "files/" + card.card_image)
				.into((ImageView) findViewById(R.id.item_card_image));
		((TextView) findViewById(R.id.item_card_name)).setText(card.card_name);
		// ((TextView)findViewById(R.id.item_card_desc)).setText(card.card_desc);
		// ((TextView) findViewById(R.id.item_card_seller_price)).setText(("السعر للمشتري " + card.card_seller_price + " " + Config.coin_name));

		image_amount_add = findViewById(R.id.image_amount_add);
		image_amount_minus = findViewById(R.id.image_amount_minus);
		txt_amount_selected = findViewById(R.id.txt_amount_selected);
		txt_amount_money = findViewById(R.id.txt_amount_money);

		if (Config.shop_price == 0)
			((TextView) findViewById(R.id.item_card_buyer_price)).setText("السعر  " + card.card_seller_price + " دينار " );

		else        if (Config.shop_price == 1)

			((TextView) findViewById(R.id.item_card_buyer_price)).setText("السعر  " + card.card_buyer_price + " دينار " );
		((Button) findViewById(R.id.bt_cancel_buy_card)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		((Button) findViewById(R.id.bt_buy_card)).setOnClickListener(new View.OnClickListener() {
																		 @Override
																		 public void onClick(View v) {
																			 show_confirm_To_buy(); }
																	 }
		);
		image_amount_add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				add();
			}
		});
		image_amount_minus.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				minus();
			}
		});

		minus();


	}
	ImageView image_card,image_amount_add,image_amount_minus;
	TextView card_name,txt_amount_selected,txt_amount_money;
	public int amount=1;

	private void minus() {
		if (amount>1)
		{
			amount--;

		}
		txt_amount_money.setText((amount*get_price())+ " دينار ");

		txt_amount_selected.setText(amount+"");

	}

	private double get_price() {
		if (Config.shop_price == 0)
			return  card.card_seller_price;
		else
		if (Config.shop_price == 1)
			return  card.card_buyer_price;
		else
			return 99999999;
	}

	private void add() {
		if (amount<10)
		{
			amount++;
		}
		txt_amount_money.setText((amount*get_price())+" دينار ");
		txt_amount_selected.setText(amount+"");

	}

	private void show_confirm_To_buy() {

    /*    AlertDialog.Builder builder5 = new AlertDialog.Builder(this);

        builder5.setTitle("تأكيد");
        builder5.setMessage("هل انت متأكد من شراء  " + card.card_name + " ?");

        builder5.setPositiveButton("شراء",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
*/
		if (Config.shop_account >= card.card_seller_price) {
			buy_card_now(card,amount);
		} else
			Toast.makeText(CardDescActivity.this, "لا يوجد رصيد كافي لاتمام العملية ", Toast.LENGTH_SHORT).show();
                     /*   dialog.dismiss();


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
*/
	}


	private void buy_card_now(final card_class itemAtPosition , final int amount) {

		try {
			if (Config.internetAvailable(this)) {
				new AsyncTask<Void, Void, Void>() {
					@Override
					protected void onPreExecute() {
						super.onPreExecute();
						ctx = CardDescActivity.this;
						progressDialog = new ProgressDialog(ctx);
						progressDialog.setMessage("جاري عملية شراء البطاقة");
						progressDialog.show();

					}

					Context ctx;
					String res = "";
					ProgressDialog progressDialog;

					@Override
					protected void onPostExecute(Void aVoid) {
						super.onPostExecute(aVoid);
						display_res_buy(res , itemAtPosition.card_name);
						progressDialog.dismiss();
					}


					@Override
					protected Void doInBackground(Void... voids) {
						res = SERVER_INFO2.BUY_CARD_NOW(ctx, itemAtPosition.card_id, Config.shop_id , amount);
						return null;
					}
				}.execute();
			} else
				Toast.makeText(this, "لا يوجد اتصال بالانترنت ", Toast.LENGTH_SHORT).show();
		} catch (Exception ex) {
		}
	}
	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
	}
	private void display_res_buy(String res,String card_name) {
		try {
			// res = res.toLowerCase();
			Log.i(Config.TAG, "display_res_buy: "+res);
			if (res.contains("nomoney"))
				Toast.makeText(this, "رصيدك غير كافي لشراء البطاقات", Toast.LENGTH_SHORT).show();
			else
			if (res.contains("max_limit"))
				Toast.makeText(this, "لقد تجاوزت الحد الاعظمي لشراء البطاقات خلال يوم واحد", Toast.LENGTH_SHORT).show();
			else
			if (res.contains("nocard"))
				Toast.makeText(this, "لا يوجد بطاقات كافية من هذا الصنف . سيتم اضافة بطاقات باسرع وقت ممكن", Toast.LENGTH_SHORT).show();
			else if (res.contains("error"))
				Toast.makeText(this, "حدث خطأ ما ولم تتم العملية ", Toast.LENGTH_SHORT).show();
			else if (res.contains("OK")) {
				String dd = res.split("===")[0];
				Log.i(Config.TAG, "display_res_buy: "+dd);
				dd=dd.split(",")[1];
				Log.i(Config.TAG, "display_res_buy: "+dd);
				Config.shop_account = Double.valueOf(dd);
				ViewCardDetialActivity.card_name = card_name;
				Log.i(Config.TAG, "display_res_buy: "+res);
				String card_serials = res.split("===")[1];
				ArrayList<serial_class> all_data  = new ArrayList<>();
				try{
					JSONArray arr = new JSONArray(card_serials);
					for(int i=0;i<arr.length();i++)
					{
						JSONObject object = arr.getJSONObject(i);
						serial_class c = new serial_class();
						c.serial_company = object.getString("serial_company");
						c.serial_date = object.getString("serial_date");
						c.serial_id = object.getString("serial_id");
						c.serial_type = object.getString("serial_type");
						c.serial_value = object.getString("serial_value");
						//c.shop_id = object.getString("shop_id");

						all_data.add(c);
					}

				}catch (Exception ex){
					Log.i(Config.TAG, "display_res_buy: "+ex.getMessage());
				}
				ViewCardDetialActivity.data = all_data;
				// ViewCardDetialActivity.serial_value = data[3];
				// ViewCardDetialActivity.serial_company = data[4];
				startActivity(new Intent(CardDescActivity.this, ViewCardDetialActivity.class));
			}
		} catch (Exception ex) {
			Log.i(Config.TAG, "display_res_buy: "+ex.getMessage());
		}

	}
}
