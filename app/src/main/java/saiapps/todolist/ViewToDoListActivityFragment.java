package saiapps.todolist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by SaiMadhuri on 6/6/2016.
 */
public class ViewToDoListActivityFragment extends Fragment implements AdapterView.OnItemLongClickListener {
    private static final String LOGTAG = "ViewToDoList";
    ListView listView;
    ToDoListArrayAdapter adapter;
    DbHelper dbHelper;
    ArrayList<ToDoListDataDetails> toDoListDetails;

    public ViewToDoListActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_to_do_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dbHelper = new DbHelper(getActivity(), null);
        Button button = (Button)getActivity().findViewById(R.id.addBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                builder.setTitle("Add ToDo item.");
                builder.setView(R.layout.add_todo_list);
                builder.setCancelable(true);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Dialog f = (Dialog) dialog;
                        EditText title = (EditText) f.findViewById(R.id.add_title);
                        EditText summary = (EditText) f.findViewById(R.id.add_summary);
                        EditText dueDate = (EditText) f.findViewById(R.id.add_due_date);
                        EditText description = (EditText) f.findViewById(R.id.add_description);

                        if(title.getText().toString() == null || title.getText().toString().trim().isEmpty() || dueDate.getText().toString() == null || dueDate.getText().toString().trim().isEmpty()) {
                            Toast.makeText(getActivity(), "Cannot add todo item.", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                Date date = new SimpleDateFormat("MM-dd-yyyy").parse(dueDate.getText().toString());
                                Log.w(LOGTAG, "Adding todo.");
                                ToDoListDataDetails detail = new ToDoListDataDetails(title.getText().toString(), summary.getText().toString(), date , description.getText().toString());
                                dbHelper.addTodo(detail);
                                toDoListDetails = getDetails();
                                updateAdapter(toDoListDetails);
                            } catch (ParseException e) {
                                StringWriter sw = new StringWriter();
                                e.printStackTrace(new PrintWriter(sw));
                                String exceptionAsString = sw.toString();
                                Toast.makeText(getActivity(), "Exception occurred. Exception detail: " + exceptionAsString, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Toast.makeText(getActivity(), "You clicked on Cancel.", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
                builder.create();
            }
        });

        toDoListDetails = getDetails();
        updateAdapter(toDoListDetails);
    }

    private void updateAdapter(ArrayList<ToDoListDataDetails> todoListDetails) {
        ToDoListDataDetails[] details = todoListDetails.toArray(new ToDoListDataDetails[todoListDetails.size()]);
        listView = (ListView)getActivity().findViewById(R.id.listView);
        adapter = new ToDoListArrayAdapter(getActivity(), R.layout.todo_list_items, details);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToDoListDataDetails detail = toDoListDetails.get(position);
                String message;
                String messageType;
                if(detail.DetailedDescription == "" || detail.DetailedDescription.isEmpty()) {
                    messageType = "Alert";
                    message = String.format("No description available for [%s].", detail.Title);
                } else {
                    messageType = "Description";
                    message = String.format("%s", detail.DetailedDescription);
                }

                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle(messageType);
                alertDialog.setMessage(message);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
    }

    private ArrayList<ToDoListDataDetails> getDetails() {
        ArrayList<ToDoListDataDetails> details = dbHelper.getAllToDos();
        Log.w(LOGTAG, "Fetching todo. Total count: " + String.format("%d", details.size()));
        return details;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        ToDoListDataDetails detail = toDoListDetails.get(position);
        Log.w(LOGTAG, String.format("Position: %d, Deleting title. Id: %d, Title: %s", position, detail.Id, detail.Title));
        dbHelper.deleteToDo(detail.Id);
        toDoListDetails = getDetails();
        adapter = new ToDoListArrayAdapter(getActivity(), R.layout.todo_list_items,  toDoListDetails.toArray(new ToDoListDataDetails[toDoListDetails.size()]));
        listView.setAdapter(adapter);
        return false;
    }
}
