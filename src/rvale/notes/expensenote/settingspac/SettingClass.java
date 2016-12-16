package rvale.notes.expensenote.settingspac;

import rvale.notes.expensenote.MainActivity;
import rvale.notes.expensenote.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingClass extends Activity
{

	Spinner spinner;
	String moneySymbols[]={"$","€","£","¥","Rs."};
	
	RadioGroup easemode;
	EditText expensselimit;
	SharedPreferences sharee;
	Button saveandedit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.setting_layout);
		sharee=getSharedPreferences("NoteMode", 0);
		spinner =(Spinner) findViewById(R.id.moneysign);
		easemode=(RadioGroup) findViewById(R.id.radioGroup1);
		expensselimit=(EditText) findViewById(R.id.expensselimit);
		
		
		easemode.clearCheck();
		
		saveandedit=(Button) findViewById(R.id.saveandedit);
		
		saveandedit.setText(SettingClass.this.getResources().getString(R.string.edit));
		expensselimit.setEnabled(false);
		expensselimit.setText(String.valueOf(sharee.getFloat("EXPENSE_BUDGET", 0.0F)));
		
		saveandedit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(expensselimit.isEnabled())
				{
					saveandedit.setText(SettingClass.this.getResources().getString(R.string.edit));
					expensselimit.setEnabled(false);
try{
					Editor e=sharee.edit();
					e.putFloat("EXPENSE_BUDGET",Float.parseFloat(expensselimit.getText().toString()));
					e.commit();
}catch(NumberFormatException e)
{
	Toast.makeText(SettingClass.this, "Budget is Very Large Exception",Toast.LENGTH_SHORT).show();
	}
					
catch(Exception e)
{
	Toast.makeText(SettingClass.this, "Exception Occured Please Try Again",Toast.LENGTH_SHORT).show();
	}
					
				}
				else
				{
					
					saveandedit.setText(SettingClass.this.getResources().getString(R.string.save));
					expensselimit.setEnabled(true);
				}
				
			}
		});
		
		easemode.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId)
			{
				Editor e =sharee.edit();
			 switch(checkedId)
			 {
			 case R.id.radio0 : e.putString("mode","lefty");
				 				
				 				break;
			 
			 case R.id.radio1 : e.putString("mode","righty");		
				 				break;
			 }			
			 
			 e.commit();
			 	
			}
		});
		
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(SettingClass.this,android.R.layout.simple_spinner_item,moneySymbols);
		spinner.setAdapter(adapter);
		
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				
				Editor e=sharee.edit();
				e.putString("SettingsCurrency",moneySymbols[arg2]);
				e.commit();
				
				MainActivity.currencyshow.setText(moneySymbols[arg2]);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			
				
			}
		});
		
		
		
		
		if(sharee.getString("mode", "").equalsIgnoreCase("lefty"))
		{
			
		((RadioButton)easemode.getChildAt(0)).setChecked(true);
		
	
			
		 }else
		{
			 ((RadioButton)easemode.getChildAt(1)).setChecked(true);
						
		}
		
		
		for(int i=0;i<moneySymbols.length;i++)
		{
			if(moneySymbols[i].equalsIgnoreCase(sharee.getString("SettingsCurrency",moneySymbols[0])))
			spinner.setSelection(i);
		}
	}
	
	@SuppressLint("InlinedApi")
	@Override
	public void onBackPressed() {
		
		Intent intent=new Intent(SettingClass.this,MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK); 
		startActivity(intent);
	
		super.onBackPressed();

	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	
	}
	
}
