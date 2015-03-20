package il.ac.shenkar.totoapp.data;

import il.ac.shenkar.todoapp.common.Task;
import il.ac.shenkar.todoapp.common.User;
import il.ac.shenkar.totoapp.data.TasksDbContract.TaskEntry;

import java.util.ArrayList;




import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class DAO implements IDataAccess{

	private static DAO instance;
	private Context context;
	private TasksDBHelper dbHelper;
	private String[] tasksColumns = { TaskEntry._ID, TaskEntry.COLUMN_TASK_DESC,
			TaskEntry.COLUMN_TASK_YEAR, TaskEntry.COLUMN_TASK_MON, TaskEntry.COLUMN_TASK_DAY,
			TaskEntry.COLUMN_TASK_HOUR, TaskEntry.COLUMN_TASK_MIN,TaskEntry.COLUMN_Status, };
	// Database fields
	private SQLiteDatabase database;
	
	DAO(Context context) {
		this.context = context;
		dbHelper = new TasksDBHelper(context);
	}
	
	/*
	 * Single tone implement.
	 */
	public static DAO getInstance(Context context)
	{
		if(instance ==  null)
			instance = new DAO(context);
		return instance;
	}
	
	@Override
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	@Override
	public void close() {
		dbHelper.close();
	}

	@Override
	public ArrayList<Task> getTaskList() {
		ArrayList<Task> tasks = new ArrayList<Task>();

		Cursor cursor = database.query(TaskEntry.TABLE_NAME, tasksColumns,null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Task t = cursorToTask(cursor);
			tasks.add(t);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return tasks;
	}
	
	
	/*
	 * Create task object from the cursor.
	 */
	private Task cursorToTask(Cursor cursor) {
		Task t = new Task();
		t.setId(cursor.getInt(cursor.getColumnIndex(TaskEntry._ID)));
		t.setItemDescription(cursor.getString(cursor.getColumnIndex(TaskEntry.COLUMN_TASK_DESC)));
		
		t.setYear(cursor.getInt(cursor.getColumnIndex(TaskEntry.COLUMN_TASK_YEAR)));
		t.setMonth(cursor.getInt(cursor.getColumnIndex(TaskEntry.COLUMN_TASK_MON)));
		t.setDay(cursor.getInt(cursor.getColumnIndex(TaskEntry.COLUMN_TASK_DAY)));
		t.setHourOfDay(cursor.getInt(cursor.getColumnIndex(TaskEntry.COLUMN_TASK_HOUR)));
		t.setMinute(cursor.getInt(cursor.getColumnIndex(TaskEntry.COLUMN_TASK_MIN)));
		t.setStatus(cursor.getInt(cursor.getColumnIndex(TaskEntry.COLUMN_Status)));
		
		return t;
	}
	
	
	@Override
	public Task addTask(Task task) {
		Log.i("DAO", "addTask start");
		
		if (task == null)
			return null;
		//build the content values.
		ContentValues values = new ContentValues();
		values.put(TaskEntry.COLUMN_TASK_DESC, task.getItemDescription());
		values.put(TaskEntry.COLUMN_TASK_YEAR, task.getYear());
		values.put(TaskEntry.COLUMN_TASK_MON, task.getMonth());
		values.put(TaskEntry.COLUMN_TASK_DAY, task.getDay());
		values.put(TaskEntry.COLUMN_TASK_HOUR, task.getHourOfDay());
		values.put(TaskEntry.COLUMN_TASK_MIN, task.getMinute());
		values.put(TaskEntry.COLUMN_Status,task.getStatus());
		
		//do the insert.
		long insertId = database.insert(TaskEntry.TABLE_NAME, null, values);
		
		//get the entity from the data base - extra validation, entity was insert properly.
		Cursor cursor = database.query(TaskEntry.TABLE_NAME, tasksColumns,
				TaskEntry._ID + " = " + insertId, null, null, null, null);
		cursor.moveToFirst();
		//create the task object from the cursor.
		Task newTask = cursorToTask(cursor);
		cursor.close();
		return newTask;
	}


	@Override
	public void removeTask(Task task) {
		long id = task.getId();
		database.delete(TaskEntry.TABLE_NAME, TaskEntry._ID + " = " + id,null);
	}


 	@Override
	public User getUser(String userName, String password) {
		// Check in the storage (In the cloud, the database etc..)
		// if exists, return the user object,otherwise
		// return null.

		User user = new User();
		user.setUserName(userName);
		user.setPassword(password);
		return user;
	}

	@Override
	public void updateTask(Task task) {
		long id= task.getId();
		ContentValues values = new ContentValues();
		values.put(TaskEntry.COLUMN_TASK_DESC, task.getItemDescription());
		values.put(TaskEntry.COLUMN_TASK_YEAR, task.getYear());
		values.put(TaskEntry.COLUMN_TASK_MON, task.getMonth());
		values.put(TaskEntry.COLUMN_TASK_DAY, task.getDay());
		values.put(TaskEntry.COLUMN_TASK_HOUR, task.getHourOfDay());
		values.put(TaskEntry.COLUMN_TASK_MIN, task.getMinute());
		values.put(TaskEntry.COLUMN_Status,task.getStatus()); 
		database.update(TaskEntry.TABLE_NAME, values, TaskEntry._ID + " = " + id, null);

	}
	
/*	
	@Override
	public ArrayList<Task> getTaskList() {
		return taskList;
	}

	public void addTaskToList(String description) {
		
		Task newTask = new Task(description);
		taskList.add(newTask);
	}
	
	public void removeTask(int ID) {
		taskList.remove(ID);
	}
*/
}
