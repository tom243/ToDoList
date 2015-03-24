package il.ac.shenkar.todoapp;

import il.ac.shenkar.todoapp.common.Alarm;
import il.ac.shenkar.todoapp.common.AppConsts;
import il.ac.shenkar.todoapp.common.OnDataSourceChangeListener;
import il.ac.shenkar.todoapp.common.Task;
import il.ac.shenkar.totoapp.data.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

public class MainController {
	// The data model, act as a cache 
	private ArrayList<Task> tasks;
	private Context context;	// maybe delete
	private IDataAccess dao;
	private AlarmHelper alarmHelper;
	private static int alarmIdCounter;
	
	//observers list.
	private List<OnDataSourceChangeListener> dataSourceChangedListenrs = 
			new ArrayList<OnDataSourceChangeListener>();


	public MainController(Context context) {
		this.context = context;
		dao = DAO.getInstance(context.getApplicationContext());
		alarmHelper = new AlarmHelper();
	}

	public ArrayList<Task> getAllTasks() {
		try {
			if (tasks != null)
				return tasks;
			dao.open();
			ArrayList<Task> tl = dao.getTaskList();
			dao.close();
			PopulateTasksCache(tl);
			return tl;
		} catch (Exception e) {
			// in case of error, return empty list.
			return new ArrayList<Task>();
		}
	}

	private void PopulateTasksCache(List<Task> tasksList )
	{	 // create HashMap and inserts all tasks 
		tasks =  new ArrayList<Task>();
		for (Task task : tasksList) {
			tasks.add(task);
		}
	}

	public void refreshData() {
		tasks = null;
		getAllTasks();
	}

	/*
	 * Add task to the data source.
	 */
	public void addTask(Task t) {
		try {
			Log.i("Controller","addTask start");
			//open the connection to the DAO
			dao.open();
			//add the task to the data base and use the returned task and add it to the local cache.
			//the task that returned from the DAO contain the id of the entity.
			Task retTask = dao.addTask(t);
			dao.close();
			if ( retTask == null) return;
			
			if(tasks.contains(retTask)) 
				return;	// checking task that is already exist in our list
			//TODO decide the logic, update? exception?
			// toast a message and ignore
			tasks.add(retTask);
			//update what ever it will be.
			invokeDataSourceChanged();
		} catch (Exception e) {
			Log.e("MainController",e.getMessage());
		}
	}

	/*
	 * remove task from the data source.
	 */
	public void removeTask(Task t) {
		
		//open the database connection
		dao.open();
		//remove the task from the database.
		dao.removeTask(t);
		//remove from the local cache.
		removeFromCache(t);
		//close the connection.
		dao.close();
		invokeDataSourceChanged();
		CancelAlarm(t.getItemDescription());
		
	}


	/*
	 * done task from the data source.
	 */
	public void doneTask(Task t) {
		
		//open the database connection
		dao.open();
		//remove the task from the database.
		dao.doneTask(t);
		//close the connection.
		dao.close();
		if (tasks.contains(t)) { 
			int i = tasks.indexOf(t);
			tasks.get(i).setStatus(t.getStatus());
		}
		invokeDataSourceChanged();
		CancelAlarm(t.getItemDescription());
	}
	
	
	public void updateTask(Task task){
		dao.open();
		//update the task in the database.
		dao.updateTask(task);
		//close the connection.
		dao.close();
		//update the local cache.
		if(tasks.contains(task)){
			int i = tasks.indexOf(task);
			tasks.get(i).setItemDescription(task.getItemDescription());
			tasks.get(i).setYear(task.getYear());
			tasks.get(i).setMonth(task.getMonth());
			tasks.get(i).setDay(task.getDay());
			tasks.get(i).setHourOfDay(task.getHourOfDay());
			tasks.get(i).setMinute(task.getMinute());
			tasks.get(i).setStatus(task.getStatus());
		}
		invokeDataSourceChanged();
	}
	
	
	public void removeFromCache(Task t)
	{
		if ( tasks.contains(t) ) 
			tasks.remove(t);
	}
	
	public void registerOnDataSourceChanged(OnDataSourceChangeListener listener)
	{
		if(listener!=null)
			dataSourceChangedListenrs.add(listener);
	}
	
	public void unRegisterOnDataSourceChanged(OnDataSourceChangeListener listener)
	{
		if(listener!=null)
			dataSourceChangedListenrs.remove(listener);
	}
	
	public void invokeDataSourceChanged()
	{			
		for (OnDataSourceChangeListener listener : dataSourceChangedListenrs) {
			listener.DataSourceChanged();
		}
	}
	
	public void CreateAlarm(String message,Date when)
	{
		Alarm alarm  =  new Alarm();
		alarm.setId(alarmIdCounter++);
		Log.i("CONTROLLER-create alarm","id alarm: "+alarmIdCounter);
		Bundle extras = new Bundle();
		extras.putString(AppConsts.Extra_Message, message);
		Log.i("CONTROLLER-create alarm","the message is: "+message);
		alarm.setExtras(extras);
		alarm.setAction(AppConsts.ACTION_ALARM);
		alarm.setIntervalMillis(0);
		alarm.setReciever(NotificationBroadCastReciever.class);
		alarm.setTriggerAtMillis(when.getTime());
		alarmHelper.setAlarm(context, alarm);
	}
	
	public void CancelAlarm(String message)
	{
		Log.i("CONTROLLER","cancel alarm");
		alarmHelper.cancelAlarm(context,message);
	}
	
	
}
