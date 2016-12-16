package rvale.notes.expensenote.widgetpac;

import rvale.notes.expensenote.R;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

public class MyWidget extends AppWidgetProvider{
	
	
	
	
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,int[] appWidgetIds) {
		      for(int i=0; i<appWidgetIds.length; i++){
		         int currentWidgetId = appWidgetIds[i];
		         String url = "http://www.google.com";
		         
		         Intent intent = new Intent(Intent.ACTION_VIEW);
		         intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		         intent.setData(Uri.parse(url));
		         
		         PendingIntent pending = PendingIntent.getActivity(context, 0,intent, 0);
		         RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.widget_preview);
		         
		        // views.setOnClickPendingIntent(R.id.button, pending);
		         appWidgetManager.updateAppWidget(currentWidgetId,views);
		         Toast.makeText(context, "widget added", Toast.LENGTH_SHORT).show();
		      }
		   }
		



public static PendingIntent buildButtonPendingIntent(Context context) {
	++MyWidgetIntentReceiver.clickCount;

	// initiate widget update request
	Intent intent = new Intent();
//	intent.setAction(WidgetUtils.WIDGET_UPDATE_ACTION);
	return PendingIntent.getBroadcast(context, 0, intent,
			PendingIntent.FLAG_UPDATE_CURRENT);
}

}