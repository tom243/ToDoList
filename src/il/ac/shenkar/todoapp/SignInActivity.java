package il.ac.shenkar.todoapp;

import il.ac.shenkar.todoapp.common.User;

import com.example.todoapp.R;
import com.example.todoapp.R.id;
import com.example.todoapp.R.layout;
import com.example.todoapp.R.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SignInActivity extends Activity {

	private EditText userNameEditText;
	private EditText passwordEditText;
	private SignInController controller;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);
		controller = new SignInController(this);
		// ask the controller if the user is logged in.
		if (controller.isLogedIn()) {
			// In case the user is logged in start the main activity.
			startMainActivity();
			return;
		}
		// get the useName and password edit text view
		userNameEditText = (EditText) findViewById(R.id.editTextUserName);
		passwordEditText = (EditText) findViewById(R.id.editTextPassword);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_in, menu);
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
	
	 public void SignInClicked(View v)
	    {
	    	//get the password and the user name from the edit text.
	    	if(userNameEditText!=null && passwordEditText!=null)
	    	{
	    		String userName  = userNameEditText.getText().toString();
	    		String pass = passwordEditText.getText().toString();
	    		User user = controller.getUser(userName,pass);
	    		//the user is exists, set the IsLogin flag to true.
	    		if(user!=null)	{
	    			controller.setLogedIn(user);
	    			startMainActivity();
	    			return;
	    		}
	    		//log in was failed.
	    		Toast.makeText(this, "User name or password is incorrect", Toast.LENGTH_LONG).show();
	    	}
			
		}
	    
	
	public void startMainActivity() {
		// Explicit intent.
		Log.i("sign-in","1");
		Intent i = new Intent(this, MainActivity.class);
		// Start the activity
		Log.i("sign-in","2");
		startActivity(i);
		Log.i("sign-in","");
		finish();
	}

}
