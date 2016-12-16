package rvale.notes.expensenote.dialogpac;


import rvale.notes.expensenote.MainActivity;
import rvale.notes.expensenote.R;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

public class CalendarDialog extends Dialog implements android.view.View.OnClickListener
{

	DatePicker datepicker;
	Button ok;
	
	public CalendarDialog(Context context) 
	{
		super(context);

	
	}

	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setTitle("Select Date");
		setContentView(R.layout.calendardialogselection);
	
		
		datepicker=(DatePicker) findViewById(R.id.datePicker1);
		ok=(Button) findViewById(R.id.ok);
		
		
		ok.setOnClickListener(this);
	}




	@Override
	public void onClick(View v) {

		Log.e("date ",datepicker.getDayOfMonth()+"/"+datepicker.getMonth()+1+"/"+datepicker.getYear());
		MainActivity.tvdate.setText(datepicker.getDayOfMonth()+"/"+(datepicker.getMonth()+1)+"/"+datepicker.getYear());
		dismiss();
	}




	
		
		
	




	
	
}
