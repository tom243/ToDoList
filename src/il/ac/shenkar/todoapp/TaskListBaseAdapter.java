package il.ac.shenkar.todoapp;

import il.ac.shenkar.todoapp.common.AppConsts;
import il.ac.shenkar.todoapp.common.Task;

import java.util.ArrayList;

import com.example.todoapp.R;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender.SendIntentException;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class TaskListBaseAdapter extends BaseAdapter{

	private ArrayList<Task> tasksList ;
	private Context context;
	
	static class ViewHolder {
		TextView tvDesc;
		Button doneButton;
		TextView tvDate,tvTime;
	}

	public TaskListBaseAdapter(Context context,ArrayList<Task> tasksList) {
		this.tasksList = tasksList;
		this.context = context;
	}
	
	public void setTasksList(ArrayList<Task> tasksList) {
		this.tasksList = tasksList;
	}

	@Override
	public int getCount() {
		return tasksList.size();
	}

	@Override
	public Object getItem(int position) {
		if (this.tasksList != null && this.tasksList.size() > position)  {
			return this.tasksList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void UpdateDataSource(ArrayList<Task> tasksList)
	{
		if(tasksList == null) 
			return; //TODO Decide how to deal with it (Maybe an exception??)
		this.tasksList = tasksList;
	}

	private final OnClickListener doneButtonOnClickListener = new OnClickListener(){     
		@Override     
		public void onClick(View view) {         
				Log.i("adapter","done button clicked");
				int position = (Integer) view.getTag();  
				tasksList.remove(getItem(position));     
				notifyDataSetChanged();
				Log.i("adapter","notification finished");
				
				sendingIdForRemovingTask(position);	// broadcast-receiver
			}
	};
	
	
	public void sendingIdForRemovingTask(int pos) {
		Intent intent = new Intent(AppConsts.REMOVING_TASK_INTENT);	
		// add data
		String strPos = Integer.toString(pos);
		intent.putExtra(AppConsts.EXTRA_MSG_ID_TASK, strPos);
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.task_row, null);
			holder = new ViewHolder();
			holder.tvDesc = (TextView) convertView.findViewById(R.id.existing_task_description);
			holder.tvDate = (TextView) convertView.findViewById(R.id.date);
			holder.tvTime = (TextView) convertView.findViewById(R.id.time);
			
			holder.doneButton = (Button) convertView.findViewById(R.id.done_button);
			holder.doneButton.setOnClickListener(doneButtonOnClickListener);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvDesc.setText(tasksList.get(position).getItemDescription());
		holder.tvDate.setText(tasksList.get(position).getDay()+"/"+tasksList.get(position).getMonth()+"/"+tasksList.get(position).getYear());
		holder.tvTime.setText(tasksList.get(position).getHourOfDay()+":"+tasksList.get(position).getMinute());
		
		// adding TAG to done_button
		holder.doneButton.setTag(Integer.valueOf(position));
		return convertView;
	}

}

