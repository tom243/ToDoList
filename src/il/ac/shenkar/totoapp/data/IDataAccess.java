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
	
	void removeTask(Task task);
	Task addTask(Task task);
	void updateTask(Task task);
	
}


