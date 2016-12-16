package rvale.notes.expensenote.dialogpac;

import rvale.notes.expensenote.R;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MyDialog extends Dialog 
{
Context c;
	public MyDialog(Context context) {
		super(context);
		c=context;
	}

	
	Button rate,can;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mydialog_righty);
		//setContentView(R.layout.mydialog_lefty);
		
		
		 rate=(Button)findViewById(R.id.hrate);
		rate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				try{
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=rvale.notes.expensenote"));
				   c.startActivity(intent);
				}catch(Exception e)
				{
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.store.com?id=rvale.notes.expensenote"));
					   c.startActivity(intent);
				}
				

				dismiss();
			}
		});
		 can=(Button)findViewById(R.id.hcan);
		
		can.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				dismiss();
			}
		});
		
		
	}
	
}
