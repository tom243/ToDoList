package il.ac.shenkar.todoapp;

import java.util.Random;

import com.example.todoapp.R;

import il.ac.shenkar.todoapp.common.AppConsts;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class NotificationBroadCastReciever extends BroadcastReceiver {

	 Random random = new Random();
	 int m = random.nextInt(9999 - 1000) + 1000;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// Create the Notification.
		Bundle extras = intent.getExtras();
		// Fetch the message from the bundle.
		String message = extras.getString(AppConsts.Extra_Message);
		// crate the notification.
		createNotification(context, message);
	}

	/*
	 * Crate notification with a specific message.
	 */
	public void createNotification(Context context, String message) {

		// get the notification manager service.
		NotificationManager nofiManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// Prepare intent which is triggered if the
		// notification is selected
		Intent intent = new Intent(context, MainActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		// Build notification
		Notification noti = new Notification.Builder(context)
				.setContentTitle(message)
				.setContentText("Remind you to do this task!")
				.setSmallIcon(R.drawable.ic_launcher).setContentIntent(pIntent)
				.build();
		// hide the notification after its selected
		noti.flags |= Notification.FLAG_INSISTENT;
				//FLAG_AUTO_CANCEL;
		nofiManager.notify(m, noti);
	}
}
