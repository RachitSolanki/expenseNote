package rvale.notes.expensenote;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class StartActivity extends Activity implements OnClickListener
{

	SharedPreferences share;
	Button lefty,righty;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.start_activity_layout);
		
		
 		lefty=(Button) findViewById(R.id.lefty);
     	righty=(Button) findViewById(R.id.righty);
     	
 		share=getSharedPreferences("NoteMode", 0);
	
 		if(share.getString("mode", "").equalsIgnoreCase("lefty"))
 		{
 			startActivity(new Intent(StartActivity.this,MainActivity.class));
 			finish();
 			
 		}
 		else
 		{
 			if(share.getString("mode", "").equalsIgnoreCase("righty"))
 	 		{
 				startActivity(new Intent(StartActivity.this,MainActivity.class));
 		 		finish();
 	 		}
 			
 		}
 		
 		
 		lefty.setOnClickListener(StartActivity.this);
 		righty.setOnClickListener(StartActivity.this);
 		
 		
		
	}


	@Override
	public void onClick(View v)
	{
		if(v.equals(lefty))
		{
			Editor edit=share.edit();	
			edit.putString("mode","lefty");
			edit.commit();
			startActivity(new Intent(StartActivity.this,MainActivity.class));
			finish();
		}
		
		if(v.equals(righty))
		{
			

			Editor edit=share.edit();	
			edit.putString("mode","righty");
			edit.commit();
			startActivity(new Intent(StartActivity.this,MainActivity.class));
			finish();

		}
		
	}
	
	
}
