package saiapps.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by SaiMadhuri on 6/5/2016.
 */
public class ToDoListArrayAdapter extends ArrayAdapter<Object> {

    int layoutResourceId;
    private Context context;
    private ToDoListDataDetails[] details;
    private LayoutInflater inflater;

    public ToDoListArrayAdapter(Context context, int resource, ToDoListDataDetails[] details) {
        super(context, resource, details);
        this.layoutResourceId = resource;
        this.details = details;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private class ToDoListDataHolder {
        TextView title;
        TextView summary;
        TextView date;
        TextView description;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ToDoListDataHolder holder = null;

        if(inflater == null)
            return null;

        if(convertView == null) {
            convertView = inflater.inflate(layoutResourceId, parent, false);
            holder = new ToDoListDataHolder();

            holder.title = (TextView) convertView.findViewById(R.id.task_title);
            holder.summary = (TextView) convertView.findViewById(R.id.task_summary);
            holder.date = (TextView) convertView.findViewById(R.id.task_date);
            holder.description = (TextView) convertView.findViewById(R.id.task_description);

            convertView.setTag(holder);
        }
        else {
            holder = (ToDoListDataHolder)convertView.getTag();
        }

        ToDoListDataDetails detail = this.details[position];

        holder.title.setText(detail.Title);
        holder.summary.setText(detail.Summary);
        holder.date.setText(detail.DueDate.toString());
        holder.description.setText(detail.DetailedDescription);

        return convertView;
    }
}
