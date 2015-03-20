package il.ac.shenkar.todoapp;

import com.example.todoapp.R;
import il.ac.shenkar.todoapp.common.AppConsts;
import il.ac.shenkar.todoapp.common.OnDataSourceChangeListener;
import il.ac.shenkar.todoapp.common.Task;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.VelocityTrackerCompat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class MainActivity extends Activity implements OnDataSourceChangeListener{

	private MainController controller;
	private TaskListBaseAdapter adapter;
	private long currentTaskPosition;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Create the controller
        controller = new MainController(this);
        // register for OnDataSourceChangedListener event.
     	controller.registerOnDataSourceChanged(this);
     	
        ListView lv = (ListView) findViewById(R.id.listView1);
        if (lv != null) {
        	adapter = new TaskListBaseAdapter(this, controller.getAllTasks());
        	lv.setAdapter(adapter);
        }
        registerForContextMenu(lv);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(updatingTaskWasRemovedReceiver, new IntentFilter(AppConsts.REMOVING_TASK_INTENT));
    }
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
    
    /*
     * button adding new task clicked, starting intent for add task.
     */
    public void newTaskClicked(View view) {
    	Log.i("addNewTask", "first add-new-task");
    	Intent intent = new Intent(this, AddTask.class);
    	startActivityForResult(intent, AppConsts.GET_TASK_REQUEST);
    	Log.i("addNewTask", "Last add-new-task");
    }

    /*
     * after chosen all fields for creating new task in addTask activity
     * create the task and save it in controller.
     * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
     */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("main","On Result before if" );
		if (requestCode == AppConsts.GET_TASK_REQUEST && resultCode == RESULT_OK) {
			Log.i("main","On Result if" );
			String descPass= data.getStringExtra(AppConsts.EXTRA_MESSAGE_DESC);
			int yearPass = data.getIntExtra(AppConsts.EXTRA_MESSAGE_YEAR, 0);
			int monPass = data.getIntExtra(AppConsts.EXTRA_MESSAGE_MONTH, 0);
			int dayPass = data.getIntExtra(AppConsts.EXTRA_MESSAGE_DAY, 0);
			int hourPass = data.getIntExtra(AppConsts.EXTRA_MESSAGE_HOUR, 0);
			int minPass = data.getIntExtra(AppConsts.EXTRA_MESSAGE_MINUTE, 0);
			
			Task t = new Task(descPass,yearPass,monPass,dayPass,hourPass,minPass);
			controller.addTask(t);
			adapter.setTasksList(controller.getAllTasks());
		}
		else{
			if (requestCode == AppConsts.GET_TASK_EDIT && resultCode == RESULT_OK) {
				String descPass = data.getStringExtra(AppConsts.EXTRA_MESSAGE_DESC);
				int yearPass = data.getIntExtra(AppConsts.EXTRA_MESSAGE_YEAR, 0);
				int monPass = data.getIntExtra(AppConsts.EXTRA_MESSAGE_MONTH, 0);
				int dayPass = data.getIntExtra(AppConsts.EXTRA_MESSAGE_DAY, 0);
				int hourPass = data.getIntExtra(AppConsts.EXTRA_MESSAGE_HOUR, 0);
				int minPass = data.getIntExtra(AppConsts.EXTRA_MESSAGE_MINUTE, 0);
				
				Task task = controller.getAllTasks().get((int) currentTaskPosition);
				task.setItemDescription(descPass);
				task.setYear(yearPass);
				task.setMonth(monPass);
				task.setDay(dayPass);
				task.setHourOfDay(hourPass);
				task.setMinute(minPass);
				
				controller.updateTask(task);
                Toast.makeText(this, "Edited" , Toast.LENGTH_SHORT).show();
			}
			
		}
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public void DataSourceChanged() {
		if (adapter != null) {
			adapter.UpdateDataSource(controller.getAllTasks());
			adapter.notifyDataSetChanged();
		}
	}
	
	private BroadcastReceiver updatingTaskWasRemovedReceiver = new BroadcastReceiver() {
		  @Override
		  
		  public void onReceive(Context context, Intent intent) {
			Log.i("main- reciever","onRecieved");
		    // Extract data included in the Intent
		    String message = intent.getStringExtra(AppConsts.EXTRA_MSG_ID_TASK);
		    Log.d("main- reciever", "Got message: " + message);
		    // delete the task
		    Toast toast = Toast.makeText(context,"Task number "+message+" was removed.", 2);
		    toast.show();
		    int id = Integer.parseInt(message);
		    System.out.println("deleted: "+id);
		    controller.doneTask(controller.getAllTasks().get(id));
		    controller.invokeDataSourceChanged();	// update the adapter and call refreshing
		  }
		};
		
	/*
	 * A try for touching the task	
	 */

		
		
	/** This will be invoked when an item in the listview is long pressed */
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) 
	{
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.actions, menu);
	}	
	
	/** This will be invoked when a menu item is selected */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
 
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        Log.i("debug",Long.toString(info.id));
        currentTaskPosition=info.id;
        switch(item.getItemId()){
            case R.id.cnt_mnu_edit:
        		Intent intent = new Intent(this, AddTask.class);
        		startActivityForResult(intent, AppConsts.GET_TASK_EDIT);
                break;
            case R.id.cnt_mnu_delete:
            	Task task = controller.getAllTasks().get((int) currentTaskPosition);
            	controller.removeTask(task);
                Toast.makeText(this, "Deleted"   , Toast.LENGTH_SHORT).show();
                break;
            case R.id.cnt_mnu_share:
                Toast.makeText(this, "Shared"   , Toast.LENGTH_SHORT).show();
                break;
	        default:
	            return super.onContextItemSelected(item);
 
        }
        return true;
    }
	
    @Override
    protected void onPause() {
      // Unregister since the activity is about to be closed.
    	super.onPause();
    	LocalBroadcastManager.getInstance(this).unregisterReceiver(updatingTaskWasRemovedReceiver);
    }
		
}
