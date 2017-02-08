package com.codepath.simpletodo;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TodoCursorAdapter extends CursorAdapter {
    public TodoCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_todo, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView tvBody = (TextView) view.findViewById(R.id.tvBody);
        TextView tvDueDate = (TextView) view.findViewById(R.id.tvDueDate);
        // Extract properties from cursor
        String body = cursor.getString(cursor.getColumnIndexOrThrow("body"));
        int priority = cursor.getInt(cursor.getColumnIndexOrThrow("priority"));

        int year = cursor.getInt(cursor.getColumnIndexOrThrow("year"));
        int month = cursor.getInt(cursor.getColumnIndexOrThrow("month"));
        int day = cursor.getInt(cursor.getColumnIndexOrThrow("day"));

        SimpleDateFormat outputFmt = new SimpleDateFormat("MM/dd/yyyy");
        String currentDateTimeString = outputFmt.format(new Date(year - 1900, month, day));

        // Populate fields with extracted properties
        tvBody.setText(body);
        tvDueDate.setText(currentDateTimeString);
    }
}