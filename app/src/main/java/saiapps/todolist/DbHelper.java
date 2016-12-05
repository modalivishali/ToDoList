package saiapps.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by SaiMadhuri on 6/7/2016.
 */
public class DbHelper extends SQLiteOpenHelper {
    private static final String LOGTAG = "DBHelper";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "todo.db";
    public static final String TABLE_TODO = "todo_list";

    public DbHelper(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOGTAG, " onCreate");
        try{
            String CREATE_TODO_TABLE = "CREATE TABLE " +
                    TABLE_TODO + "("
                    + ToDoTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + ToDoTable.COLUMN_TITLE + " TEXT NOT NULL,"
                    + ToDoTable.COLUMN_DATE + " DATETIME NOT NULL,"
                    + ToDoTable.COLUMN_SUMMARY + " TEXT,"
                    + ToDoTable.COLUMN_DESCRIPTION + " TEXT" + ")";

            db.execSQL(CREATE_TODO_TABLE);
        } catch (Exception e) {
            Log.e(LOGTAG, " onCreate:  Could not create SQL database: " + e);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(LOGTAG, "Upgrading database, this will drop tables and recreate.");
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
            onCreate(db);
        } catch (Exception e) {
            Log.e(LOGTAG, " onUpgrade:  Could not update SQL database: " + e);
        }
    }

    public void addTodo(ToDoListDataDetails detail) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        ContentValues values = new ContentValues();
        Log.d(LOGTAG, String.format("Title: %s, Summary: %s", detail.Title, detail.Summary));
        values.put(ToDoTable.COLUMN_TITLE, detail.Title);
        values.put(ToDoTable.COLUMN_DATE, dateFormat.format(detail.DueDate));
        values.put(ToDoTable.COLUMN_SUMMARY, detail.Summary);
        values.put(ToDoTable.COLUMN_DESCRIPTION, detail.DetailedDescription);

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_TODO, null, values);
        db.close();
    }

    public boolean deleteToDo(int id ) {
        boolean result;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODO, ToDoTable.COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
        result = true;
        db.close();
        return result;
    }

    public ArrayList<ToDoListDataDetails> getAllToDos() {
        ArrayList<ToDoListDataDetails> todoList = new ArrayList<ToDoListDataDetails>();
        // Select All Query
        SQLiteDatabase db = this.getWritableDatabase();
        ToDoCursorWrapper cursor = new ToDoCursorWrapper(db.query(TABLE_TODO, null, null, null, null, null, null));
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                todoList.add(cursor.getDetail());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return todoList;
    }
}
