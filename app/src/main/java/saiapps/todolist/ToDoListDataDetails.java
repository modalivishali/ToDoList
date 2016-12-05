package saiapps.todolist;

import java.util.Date;

/**
 * Created by SaiMadhuri on 6/5/2016.
 */
public class ToDoListDataDetails {
    public int Id;
    public String Title;
    public String Summary;
    public Date DueDate;
    public String DetailedDescription;

    public ToDoListDataDetails() {super();}

    public ToDoListDataDetails(String title, String summary, Date dueDate, String description) {
        this.Title = title;
        this.Summary = summary;
        this.DueDate = dueDate;
        this.DetailedDescription = description;
    }
}
