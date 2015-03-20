package il.ac.shenkar.totoapp.data;

import il.ac.shenkar.totoapp.data.TasksDbContract.TaskEntry;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

public class TasksDBHelper extends SQLiteOpenHelper{

	// If you change the database schema, you must increment the database
	// version.
	private static final int DATABASE_VERSION = 4;

	private static final String DATABASE_NAME = "tasks.db";

	public TasksDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// Create a table to hold the friends;
		final String SQL_CREATE_LOCATION_TABLE = "CREATE TABLE "
				+ TaskEntry.TABLE_NAME + " (" 
				+ TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ TaskEntry.COLUMN_TASK_DESC + " TEXT NOT NULL,"
				+ TaskEntry.COLUMN_TASK_YEAR + " INTEGER,"
				+ TaskEntry.COLUMN_TASK_MON + " INTEGER,"
				+ TaskEntry.COLUMN_TASK_DAY + " INTEGER,"
				+ TaskEntry.COLUMN_TASK_HOUR + " INTEGER,"
				+ TaskEntry.COLUMN_TASK_MIN + " INTEGER,"
				+ TaskEntry.COLUMN_Status + " INTEGER)" ;
		
		db.execSQL(SQL_CREATE_LOCATION_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		db.execSQL("DROP TABLE IF EXISTS " + TaskEntry.TABLE_NAME);
		onCreate(db);

	}

}
