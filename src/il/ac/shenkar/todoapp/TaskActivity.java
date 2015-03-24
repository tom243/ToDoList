package il.ac.shenkar.todoapp;

import il.ac.shenkar.todoapp.common.AppConsts;

import java.util.Calendar;

import com.example.todoapp.R;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

public class TaskActivity extends Activity implements View.OnClickListener {

	private EditText descriptionEt;
	private Button btnCalendar, btnTimePicker;
	private EditText txtDate, txtTime;
	
	private int mYear, mMonth, mDay, mHour, mMinute;
	private Calendar c = Calendar.getInstance();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_task);
		descriptionEt  = (EditText) findViewById(R.id.task_description);
		btnCalendar = (Button) findViewById(R.id.btnCalendar);
        btnTimePicker = (Button) findViewById(R.id.btnTimePicker);
        txtDate = (EditText) findViewById(R.id.txtDate);
        txtTime = (EditText) findViewById(R.id.txtTime);
        
		Bundle extras = getIntent().getExtras(); 

		if (extras != null) { // We are in edit mode
			setTitle("Edit Task");
			Button bt=(Button) findViewById(R.id.create_activity_button);
			bt.setText("Edit");
			mYear= extras.getInt(AppConsts.EXTRA_MESSAGE_YEAR);
			mMonth= extras.getInt(AppConsts.EXTRA_MESSAGE_MONTH);
			mDay=extras.getInt(AppConsts.EXTRA_MESSAGE_DAY);
			mHour= extras.getInt(AppConsts.EXTRA_MESSAGE_HOUR);
			mMinute=extras.getInt(AppConsts.EXTRA_MESSAGE_MINUTE);
			descriptionEt.setText(extras.getString(AppConsts.EXTRA_MESSAGE_DESC));
			txtDate.setText(mDay + "-" +  mMonth + "-" + mYear);
			txtTime.setText(mHour + ":" + mMinute );
		}
        
        btnCalendar.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

        Log.i("AddTask","onCreate - end");
        
        
/*        
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("My notification")
                .setContentText("Hello World!");
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(2,mBuilder.build());
*/        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_task, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void createTask(View view) {
		Log.i("AddTask","create task" );
		Intent intent = new Intent(this,MainActivity.class);
    	String descriptionTaskToPass = descriptionEt.getText().toString();
    	// check if chosen any date+time (not empty)
    	System.out.println("year: "+mYear+" month: "+mMonth+" day: "+mDay+" hour: "+mHour+" minutes: "+mMinute);
    	
    	intent.putExtra(AppConsts.EXTRA_MESSAGE_DESC, descriptionTaskToPass);
    	intent.putExtra(AppConsts.EXTRA_MESSAGE_YEAR, mYear);
    	intent.putExtra(AppConsts.EXTRA_MESSAGE_MONTH, mMonth); 
    	intent.putExtra(AppConsts.EXTRA_MESSAGE_DAY, mDay);
    	intent.putExtra(AppConsts.EXTRA_MESSAGE_HOUR, mHour);
    	intent.putExtra(AppConsts.EXTRA_MESSAGE_MINUTE, mMinute); 
  	
    	Log.i("AddTask","create task - before parent" );
    	if (getParent() == null) {
    		setResult(Activity.RESULT_OK,intent);
    	} else {
    		getParent().setResult(Activity.RESULT_OK,intent);
    	}
    	finish();
	}

	/*
	 * date picker or time picker button was clicked
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {

		if (v == btnCalendar) {
			 
            // Process to get Current Date
			// final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
 
            // Launch Date Picker Dialog
            DatePickerDialog dpd = new DatePickerDialog(this,
            		new DatePickerDialog.OnDateSetListener() {
 
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                int monthOfYear, int dayOfMonth) {
                            // save Selected date 
                        	mYear = year;
                            mMonth = monthOfYear+1;
                            mDay = dayOfMonth;
                            // Display Selected date in text box
                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                    }, mYear, mMonth, mDay);
            dpd.show();
        }
        if (v == btnTimePicker) {
 
            // Process to get Current Time
        	// final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
 
            // Launch Time Picker Dialog
            TimePickerDialog tpd = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
 
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        	// save Selected time 
                        	mHour = hourOfDay;
                        	mMinute = minute;
                        	// Display Selected time in text box
                            txtTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            tpd.show();
        }
		
		
	}

	
}
