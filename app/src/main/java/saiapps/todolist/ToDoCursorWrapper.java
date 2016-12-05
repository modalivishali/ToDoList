package saiapps.todolist;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by SaiMadhuri on 6/7/2016.
 */
public class ToDoCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public ToDoCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public ToDoListDataDetails getDetail() {

        try{
            ToDoListDataDetails detail = new ToDoListDataDetails();
            detail.Id = getInt(getColumnIndex(ToDoTable.COLUMN_ID));
            detail.Title = getString(getColumnIndex(ToDoTable.COLUMN_TITLE));
            detail.Summary = getString(getColumnIndex(ToDoTable.COLUMN_SUMMARY));
            detail.DetailedDescription = getString(getColumnIndex(ToDoTable.COLUMN_DESCRIPTION));
            detail.DueDate = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(getString(getColumnIndex(ToDoTable.COLUMN_DATE))));
            return detail;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
