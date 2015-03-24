package il.ac.shenkar.totoapp.data;

import il.ac.shenkar.todoapp.common.Task;
import il.ac.shenkar.todoapp.common.User;

import java.util.ArrayList;
import java.util.List;

import android.database.SQLException;


public interface IDataAccess {

	void open() throws SQLException;
	void close();
	ArrayList<Task> getTaskList();

	User getUser(String userName, String password);
	
	Task addTask(Task task);
	void removeTask(Task task);
	void updateTask(Task task);
	void doneTask(Task task);
}


