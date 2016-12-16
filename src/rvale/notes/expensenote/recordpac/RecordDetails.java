package rvale.notes.expensenote.recordpac;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.pdf.PdfDocument.Page;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import rvale.notes.expensenote.MainActivity;
import rvale.notes.expensenote.R;

public class RecordDetails extends Activity implements OnClickListener
{

	Button pdf,csv,completereport;
	Spinner days,months,year;
	String arrdays[]={"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
	
	//String arrdays[]={"1 days","7 days","15 days"};
	
	String mon[]={"1","2","3","4","5","6","7","8","9","10","11","12"};//{"Jan","Feb","Mar","Apr","May","Jun","July","Aug","Sep","Oct","Nov","Dec"};
	ArrayList<String> yea=new ArrayList<String>();

	Spinner sday,smonth,syear;
	SQLiteDatabase db;
	TableLayout tablelayout;
	boolean isneededtoadd=true;
	
	String sortdate,sortmonth,sortyear;
	
Calendar c;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.recordsdetails);
		pdf=(Button) findViewById(R.id.pdf);
		csv=(Button) findViewById(R.id.csv);
		completereport=(Button) findViewById(R.id.detailreport);
		
		sday=(Spinner) findViewById(R.id.days);
		smonth=(Spinner) findViewById(R.id.months);
		syear=(Spinner) findViewById(R.id.year);
		
		c=Calendar.getInstance();
		
		tablelayout=(TableLayout) findViewById(R.id.table);
		
		pdf.setOnClickListener(RecordDetails.this);
		csv.setOnClickListener(RecordDetails.this);
		completereport.setOnClickListener(RecordDetails.this);
		
		db=openOrCreateDatabase("EXPENSENOTES",MODE_PRIVATE, null);
			
		
		sortdate=String.valueOf(this.c.get(Calendar.DATE));
		sortmonth=String.valueOf(this.c.get(Calendar.MONTH)+1);
		sortyear=String.valueOf(this.c.get(Calendar.YEAR));
		
		
			
		
		checkyearexists();

	
		DefaultView(sortdate+"/"+sortmonth+"/"+sortyear);
		spinnerController();
		
		
		
	}
	
	public void checkyearexists()
	{
		SharedPreferences yearjson=getSharedPreferences("getYearList", 0);

		try
		{
			
			JSONArray arr=new JSONArray("[{"+yearjson.getString("list","")+"}]");
			Log.e(" ",arr.toString());
			for(int i=0;i<arr.length();i++)
			{
				JSONObject obj=arr.getJSONObject(i);
				Log.e(" 1",obj.toString());
				//yea.add(obj.getString("year"));
				
				if(obj.getString("year").equalsIgnoreCase(sortyear));
				{
					//donnot add to list
					isneededtoadd=false;
				}
				
			
			}
		}catch(JSONException e)
		{
			Log.e("2 ",e.getMessage());
			
		}

		if(isneededtoadd)
		{
			
			//{year:84,iuhb:874}
			Editor re=yearjson.edit();
			String sss=yearjson.getString("list","");
			
			try
			{
				
				JSONArray arr=new JSONArray("[{"+sss+"}]");
				
				if(arr.length()==1)
					sss="year:"+sortyear;
				else
					sss=sss+",year:"+sortyear;
						
				re.putString("list", sss);
				re.commit();
				
				Log.e("3",sss);
				
			}catch(JSONException e)
			{
				Log.e("4",e.getMessage());
			}
			
			
		
			
		}
			
	        
		//now add to list
		
		try
		{
			
			JSONArray arr=new JSONArray("[{"+yearjson.getString("list","")+"}]");
			Log.e(" 6",arr.toString());
			for(int i=0;i<arr.length();i++)
			{
				JSONObject obj=arr.getJSONObject(i);
				Log.e("7 ",obj.toString());
				yea.add(obj.getString("year"));
				
				
			
			}
		}catch(JSONException e)
		{
			Log.e("8 ",e.getMessage());
			
		}
		
		
	}
	
	public void addnewyear()
	{
		
		
		
	}
	
	
	@Override
	protected void onDestroy() {

	super.onDestroy();

	db.close();
	}
	
	public void DefaultView(String date)
	{
		//SELECT * FROM Customers
		//WHERE City LIKE 's%';
		Cursor c=db.rawQuery("SELECT * FROM EXPENSETABLE WHERE DATEVALUE LIKE '%"+date+"%'",null);
		c.moveToFirst();
		
	//	Log.e("value of ",""+c.getCount());
		for(int i=0;i<c.getCount();i++)
		{
			
			addTableRow(c,i);
			c.moveToNext();
		}
		c.close();
		
	}
	
	
	public void addTableRow(Cursor cursor,int i)
	{
		
		TableRow tr=new TableRow(RecordDetails.this);
		View vhorizontal=new View(RecordDetails.this);
		vhorizontal.setLayoutParams(new LayoutParams(2, LayoutParams.MATCH_PARENT));
		vhorizontal.setBackgroundColor(0xff000000);
		
		View vvertical=new View(RecordDetails.this);
		vvertical.setLayoutParams(new LayoutParams( LayoutParams.MATCH_PARENT,2));
		vvertical.setBackgroundColor(0xff000000);
		
		
		int num=cursor.getColumnCount();
		
		for(int j=0;j<num;j++)
		{
			TextView tv=new TextView(RecordDetails.this);
			if(android.os.Build.VERSION.SDK_INT>11)
			{
				//change cell view
				tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_cell));
			}
			
			tv.setPadding(10,10, 10, 10);
			if(i==0)
			tv.setText(cursor.getColumnName(j));
			else
			{
				
				if(j==3)
					tv.setText(MainActivity.currencyshow.getText().toString()+" "+cursor.getString(j).toString());
				else
					tv.setText(cursor.getString(j).toString());
					
			}
			tr.addView(tv);
		
		}
			tr.addView(vhorizontal);
		tablelayout.addView(tr);

		
	}
	
	
	
	
	
	public void spinnerController()
	{
		
		
		ArrayAdapter<String> adapterday = new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_item, arrdays);
		adapterday.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sday.setAdapter(adapterday);
		sday.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) 
			{
				sortdate=arrdays[position];
				//tablelayout.invalidate();
				tablelayout.removeAllViews();
			Log.e("date ",sortdate+"/"+sortmonth+"/"+sortyear);
				DefaultView(sortdate+"/"+sortmonth+"/"+sortyear);
				
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) 
			{
				
				
			}
		});
		
		sday.setSelection(c.get(Calendar.DATE)-1);
		
	
		ArrayAdapter<String> adaptermonth = new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_item, mon);
		adaptermonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		smonth.setAdapter(adaptermonth);
		smonth.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) 
			{
			sortmonth=mon[position];	
			//tablelayout.invalidate();
			tablelayout.removeAllViews();
			
			DefaultView(sortdate+"/"+sortmonth+"/"+sortyear);
			
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) 
			{
				
				
			}
		});
		
		smonth.setSelection(c.get(Calendar.MONTH));

		
		//yea[0]=String.valueOf(c.get(Calendar.YEAR));
		
		ArrayAdapter<String> adapteryear = new ArrayAdapter<String>(this,  android.R.layout.simple_spinner_item, yea);
		adapteryear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		syear.setAdapter(adapteryear);
		syear.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) 
			{
				sortyear=yea.get(position);
				tablelayout.removeAllViews();
			//	tablelayout.invalidate();
				DefaultView(sortdate+"/"+sortmonth+"/"+sortyear);
				
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) 
			{
				
				
			}
		});

	//syear.setSelection(0);
		
		
	}
	
	
	

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v)
	{
	
	  if(v.equals(csv))
	  {
		  
		  
 
		  String string="";
		  
		  
		  //code to get current date value
		  
			Cursor c=db.rawQuery("SELECT * FROM EXPENSETABLE",null);
			c.moveToFirst();
		
			for(int i=0;i<c.getCount();i++)
			{
				string=string+c.getString(c.getColumnIndex("WHOPAY")).toString()+","+
					c.getString(c.getColumnIndex("WHOMPAY")).toString()+","+
					c.getString(c.getColumnIndex("AMOUNT")).toString()+","+
					c.getString(c.getColumnIndex("EXTRANOTE")).toString()+"\n";
					
				
				c.moveToNext();
			}
			
			Log.e("",string);
		c.close();
		  
		  
	
		  
		  
		Calendar calendar = Calendar.getInstance();
		  
		  
		  
		  try {
			    File newFolder = new File(Environment.getExternalStorageDirectory(), "Rvale");
			    if (!newFolder.exists()) {
			        newFolder.mkdir();
			        Log.e("",newFolder.isHidden()+"");
			    }
			    try {
			        File file = new File(newFolder, "Database"+calendar.get(Calendar.DATE)+"_"+calendar.get(Calendar.MONTH)+"_"+calendar.get(Calendar.YEAR)+calendar.get(Calendar.MINUTE)+"_"+calendar.get(Calendar.MINUTE)+".csv");
			        file.createNewFile();
			        FileOutputStream fos;
					 
			        byte[] data = string.getBytes();
			        fos = new FileOutputStream(file);
			        fos.write(data);
			        fos.flush();
			        fos.close();
			   	       
			    
			    
			    } catch (Exception ex) {
			        System.out.println("ex: " + ex);
			    	Toast.makeText(RecordDetails.this, "File Failed to Save", Toast.LENGTH_SHORT).show();
			    	return;
					
			    }
			} catch (Exception e) {
			    System.out.println("e: " + e);
		    	Toast.makeText(RecordDetails.this, "File Cannot Be Created", Toast.LENGTH_SHORT).show();
				return;
			}
		  
			Toast.makeText(RecordDetails.this, "File Saved", Toast.LENGTH_SHORT).show();
			
		
		  
		  
	  }
	
	  
	  if(v.equals(pdf))
	  {
		 
	  }
	  
	  
	  
	  if(v.equals(completereport))
	  {
		 
		  
		  
	  }
	  
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	@Override
	protected int computeVerticalScrollRange() {
	    final int count = getChildCount();
	    final int contentHeight = getHeight() - getPaddingBottom()
	        - getPaddingTop();
	    if (count == 0) {
	        return contentHeight;
	    }


	@Override
	protected int computeVerticalScrollOffset() {
	    int result = Math.max(0, super.computeVerticalScrollOffset());
	    return result - getScrollVerticalMin();
	}
	@Override
	protected int computeHorizontalScrollRange() {
	    final int count = getChildCount();
	    final int contentWidth = getWidth() - getPaddingLeft()
	        - getPaddingRight();
	    if (count == 0) {
	        return contentWidth;
	    }

	    int scrollRange = getChildAt(0).getRight();
	    final int scrollX = getScrollX();
	    final int overscrollRight = Math.max(0, scrollRange - contentWidth);
	    if (scrollX < 0) {
	        scrollRange -= scrollX;
	    } else if (scrollX > overscrollRight) {
	        scrollRange += scrollX - overscrollRight;
	    }
	    return (int) (scrollRange * mScaleFactor + getScrollHorizontalMin());
	}

	@Override
	protected int computeHorizontalScrollOffset() {
	    int result = Math.max(0, super.computeHorizontalScrollOffset());
	    return result - getScrollHorizontalMin();
	}
	@Override
	protected int computeVerticalScrollExtent() {
	    return getHeight() + getScrollVerticalMin();
	}

	@Override
	protected int computeHorizontalScrollExtent() {
	    return getWidth() + getScrollHorizontalMin();
	}
	private int getScrollHorizontalMin() {
	    if (this.getChildCount() < 1)
	        return 0;
	    View child = this.getChildAt(0);
	    return (int) ((1 - mScaleFactor) * child.getPivotX());
	}

	private int getScrollVerticalMin() {
	    if (this.getChildCount() < 1)
	        return 0;
	    View child = this.getChildAt(0);
	    return (int) ((1 - mScaleFactor) * child.getPivotY());
	}
	private int getScrollVerticalRange() {
	    int scrollRange = 0;
	    if (getChildCount() > 0) {
	        View child = getChildAt(0);
	        scrollRange = (int) Math.max(0, child.getHeight() * mScaleFactor
	                - (getHeight() - getPaddingBottom() - getPaddingTop())
	                + getScrollVerticalMin());
	    }
	return scrollRange;
	}

	private int getScrollHorizontalRange() {
	    int scrollRange = 0;
	    if (getChildCount() > 0) {
	        View child = getChildAt(0);
	        scrollRange = (int) Math.max(0, child.getWidth() * mScaleFactor
	                - (getWidth() - getPaddingRight() - getPaddingLeft())
	                + getScrollHorizontalMin());
	    }
	    return scrollRange;
	}
	
*/	
	
	
	
	
	
	
	
	
}
