package rvale.notes.expensenote;

import java.util.Calendar;

import rvale.notes.expensenote.dialogpac.CalendarDialog;
import rvale.notes.expensenote.dialogpac.MyDialog;
import rvale.notes.expensenote.graphpac.LineChart1;
import rvale.notes.expensenote.recordpac.RecordDetails;
import rvale.notes.expensenote.settingspac.SettingClass;
import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.filter.Approximator;
import com.github.mikephil.charting.data.filter.Approximator.ApproximatorType;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends Activity implements OnClickListener
{

	SQLiteDatabase db;
	SharedPreferences share;
	Button save,record,graph,settings;
	EditText etwhopay,etwhompay,etamount,etextranote;
	CheckBox cb1,cb2;
	String historywho,historywhom;
	public static TextView tvdate;
	public static TextView currencyshow;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//getWindow().requestFeature(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		share=getSharedPreferences("NoteMode", 0);
		
		
		
		
		if(share.getString("mode", "").equalsIgnoreCase("lefty"))
		{
		  setContentView(R.layout.activity_main_lefty);
		 }else
		{
				setContentView(R.layout.activity_main_righty);
	    }
		
		Calendar ccc=Calendar.getInstance();
		
		if(share.getString("help_first", "yes").equalsIgnoreCase("yes"))
		{
			//show dialog of help
		}
		
		
		
		if(share.getInt("nooftimes",0)==3)
		{
			MyDialog d=new MyDialog(MainActivity.this);
			d.show();

			int i=share.getInt("nooftimes",0);
			Editor eee=share.edit();
			eee.putInt("nooftimes",i+1);
			eee.commit();
		}
		else
		{
			
			int i=share.getInt("nooftimes",0);
		if(i<5)
		{
			Editor eee=share.edit();
			eee.putInt("nooftimes",i+1);
			eee.commit();
		}
		}	
		
		
		  save=(Button) findViewById(R.id.save);
			record=(Button) findViewById(R.id.viewdetails);
			graph=(Button) findViewById(R.id.graph);
			settings=(Button) findViewById(R.id.settings);
			save.setOnClickListener(this);
			record.setOnClickListener(this);
			graph.setOnClickListener(this);
			settings.setOnClickListener(this);
			
			etwhopay=(EditText)findViewById(R.id.editText1);
	        etwhompay=(EditText)findViewById(R.id.editText2);
	        etamount=(EditText)findViewById(R.id.editText3);
			etextranote=(EditText)findViewById(R.id.editText4);
			tvdate=(TextView) findViewById(R.id.datetextview);
			
			currencyshow=(TextView) findViewById(R.id.currencyshow);
			
			
			
			currencyshow.setText(share.getString("SettingsCurrency", "Rs."));
			
			tvdate.setOnClickListener(this);
			
			tvdate.setText(ccc.get(Calendar.DATE)+"/"+(ccc.get(Calendar.MONTH)+1)+"/"+ccc.get(Calendar.YEAR));
			cb1=(CheckBox) findViewById(R.id.checkBox1);
			cb2=(CheckBox) findViewById(R.id.checkBox2);
			
			cb1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					
					if(isChecked)
					{
						historywho=etwhopay.getText().toString();
								etwhopay.setText("Me");
					}
					else
					{
						
						etwhopay.setText(historywho);
								
					}
					
				}
			});
			
			cb2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					
					
					if(isChecked)
					{
						historywhom=etwhompay.getText().toString();
						etwhompay.setText("Me");
					}
					else
					{
						
						etwhompay.setText(historywhom);
								
					}
					
				}
			});
			
			
			
			
		   db=openOrCreateDatabase("EXPENSENOTES",MODE_PRIVATE, null);
			db.execSQL("CREATE TABLE IF NOT EXISTS EXPENSETABLE (DATEVALUE VARCHAR,WHOPAY VARCHAR,WHOMPAY VARCHAR,AMOUNT DOUBLE,EXTRANOTE VARCHAR);");
			//db.close();


			
			AdView mAdView = (AdView) findViewById(R.id.ad_view);

	        // Create an ad request. Check logcat output for the hashed device ID to
	        // get test ads on a physical device. e.g.
	        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
	        AdRequest adRequest = new AdRequest.Builder().build();
	                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
	                

	        // Start loading the ad in the background.
	        mAdView.loadAd(adRequest);
		

		
	
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
*/
	@Override
	public void onClick(View v) {
		
		if(v.equals(tvdate))
		{
			
			CalendarDialog cd=new CalendarDialog(MainActivity.this);
			cd.show();
			
		}
		
		
		if(v.equals(save))
		{
		
		//	db=openOrCreateDatabase("EXPENSENOTES",MODE_PRIVATE, null);
					boolean save=true;
		    	
					Vibrator vv = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
					 // Vibrate for 500 milliseconds
					 vv.vibrate(200);
					 
					
			if(etwhopay.getText().toString().equalsIgnoreCase(""))
			{
				//show message
				Log.e("",""+etwhopay.getText().toString().equalsIgnoreCase(""));
				etwhopay.setError("Field Reqired");
				save=false;
				
			}
			
			if(etwhompay.getText().toString().equalsIgnoreCase(""))
			{
				//show message
				
				etwhompay.setError("Field Reqired");
				save=false;
				
			}
			if(etamount.getText().toString().equalsIgnoreCase(""))
			{
				//show message
				
				etamount.setError("Field Reqired");
				save=false;
				
			}
			Double amo = 0.0;
			try
			{
		     amo=Double.parseDouble(etamount.getText().toString());
			}catch(NumberFormatException e)
			{
				//message
			}
			Calendar cc=Calendar.getInstance();
			String temp;
			if(cc.get(Calendar.AM_PM)==0)
				temp="AM";
			else
				temp="PM";
						
			if(save)
			{
			db.execSQL("INSERT INTO EXPENSETABLE VALUES ('"+tvdate.getText().toString()+"  at  "+cc.get(Calendar.HOUR)+" : "+cc.get(Calendar.MINUTE)+" "+ temp+"','"+etwhopay.getText().toString()+"','"+etwhompay.getText().toString()+"','"+amo+"','"+etextranote.getText().toString()+"');");
		   Toast.makeText(MainActivity.this,"Saved..",Toast.LENGTH_SHORT).show();
			}
			
		}
		
		if(v.equals(record))
		{
			startActivity(new Intent(MainActivity.this,RecordDetails.class));
		}
		
		if(v.equals(graph))
		{
			startActivity(new Intent(MainActivity.this,LineChart1.class));
			
		}
		
		if(v.equals(settings))
		{
			startActivity(new Intent(MainActivity.this,SettingClass.class));
			
		}
		
		
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	
		 db.close();
	}

}
