package il.ac.shenkar.todoapp;

import il.ac.shenkar.todoapp.common.Alarm;
import il.ac.shenkar.todoapp.common.AppConsts;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class AlarmHelper {
	
	
	public void setAlarm(Context c, Alarm alarm) {
		if (c == null || alarm == null)
			return;
		// create the intent, with the receiver that should handle the alarm.
		Intent activityIntent = new Intent(c.getApplicationContext(), alarm.getReciever());
		// set the action.
		activityIntent.setAction(alarm.getAction());
		// set the extras.
		activityIntent.putExtras(alarm.getExtras());
		PendingIntent pendingInent = PendingIntent.getBroadcast(c,
				alarm.getId(), activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) c
				.getSystemService(Context.ALARM_SERVICE);
		if (alarm.getIntervalMillis() > 0)
			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
					alarm.getTriggerAtMillis(), alarm.getIntervalMillis(),
					pendingInent);
		else {
			alarmManager.set(AlarmManager.RTC_WAKEUP,
					alarm.getTriggerAtMillis(), pendingInent);
		}
	}

	public void cancelAlarm(Context c,String message) {
		//Now you will have to deal with it.
		Log.i("ALARM-HELPER","notification canceled.");
		// get the notification manager service.
		NotificationManager nofiManager = (NotificationManager) c
				.getSystemService(Context.NOTIFICATION_SERVICE);
	//	nofiManager.cancel(message,0);
	//	nofiManager.cancelAll();
		
		
	}

}
