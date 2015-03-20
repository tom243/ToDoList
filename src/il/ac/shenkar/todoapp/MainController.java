package il.ac.shenkar.todoapp;

import il.ac.shenkar.todoapp.common.OnDataSourceChangeListener;
import il.ac.shenkar.todoapp.common.Task;
import il.ac.shenkar.totoapp.data.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;

public class MainController {
	// The data model, act as a cache 
	private HashMap<Integer,Task> tasks;
	private Context context;	// maybe delete
	private IDataAccess dao;
	
	//observers list.
	private List<OnDataSourceChangeListener> dataSourceChangedListenrs = 
			new ArrayList<OnDataSourceChangeListener>();


	public MainController(Context context) {
		this.context = context;
		dao = DAO.getInstance(context.getApplicationContext());
	}

	public ArrayList<Task> getAllTasks() {
		try {
			if (tasks != null)
				return new ArrayList<Task>(tasks.values());
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
		tasks =  new HashMap<Integer, Task>();
		for (Task task : tasksList) {
			tasks.put(task.getId(), task);
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
			
			if(tasks.containsKey(retTask.getId())) return;	// checking task that is already exist in our list
			//TODO decide the logic, update? exception?
			// toast a message and ignore
			tasks.put(retTask.getId(), retTask);
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
	}


	/*
	 * done task from the data source.
	 */
	public void doneTask(Task t) {
		
		//open the database connection
		dao.open();
		//remove the task from the database.
		dao.removeTask(t);
		//remove from the local cache.
		removeFromCache(t);
		//close the connection.
		dao.close();
		invokeDataSourceChanged();
	}
	
	
	public void updateTask(Task task){
		dao.open();
		//update the task in the database.
		dao.updateTask(task);
		//close the connection.
		dao.close();
		//update the local cache.
		if(tasks.containsKey(task.getId())){
			tasks.get(task.getId()).setItemDescription(task.getItemDescription());
			
			tasks.get(task.getId()).setStatus(task.getStatus());
		}
		invokeDataSourceChanged();
	}
	
	
	public void removeFromCache(Task t)
	{
		if ( tasks.containsKey(t.getId()) )
			tasks.remove(t.getId());
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
	
}
