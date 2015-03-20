package il.ac.shenkar.todoapp;

import il.ac.shenkar.todoapp.common.AppConsts;
import il.ac.shenkar.todoapp.common.User;
import il.ac.shenkar.totoapp.data.DAO;
import il.ac.shenkar.totoapp.data.IDataAccess;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class SignInController { 

	IDataAccess dao;
	Context context;
	
	public SignInController(Context context) {
		dao = DAO.getInstance(context.getApplicationContext());
		this.context = context;
	}
	
	public boolean isLogedIn()	{
		SharedPreferences prefs = context.getSharedPreferences(AppConsts.SharedPrefsName, 0);
		if(prefs!=null){
			return prefs.getBoolean(AppConsts.SharedPrefs_IsLogin, false);
		}
		return false;
	}
	
	public User getUser(String userName,String password) {
		return dao.getUser(userName, password);
	}
	
	public void setLogedIn(User user) {
		if(user!=null)
		{
			SharedPreferences prefs = context.getSharedPreferences(AppConsts.SharedPrefsName, 0);
			if(prefs!=null)	{
				Editor editor = prefs.edit();
				editor.putBoolean(AppConsts.SharedPrefs_IsLogin, true);
				editor.putString(AppConsts.SharedPrefs_UserName, user.getUserName());
				editor.commit();
			}
		}
	}
}
