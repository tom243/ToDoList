package il.ac.shenkar.totoapp.data;

import android.provider.BaseColumns;

/**
 * Defines table and column names for the tasks database.
 */

public class TasksDbContract {
	  /* Inner class that defines the table contents of all tasks */
		 public static final class TaskEntry implements BaseColumns {

		        // Table name
		        public static final String TABLE_NAME = "tasks";
		        
		        // Column names
		        public static final String COLUMN_TASK_DESC = "task_description";
		        
		        public static final String COLUMN_TASK_YEAR = "year";
		        public static final String COLUMN_TASK_MON = "month";
		        public static final String COLUMN_TASK_DAY = "day";
		        
		        public static final String COLUMN_TASK_HOUR = "hour";
		        public static final String COLUMN_TASK_MIN = "minutes";
		        
		        public static final String COLUMN_Status = "status";

		    }
 }
