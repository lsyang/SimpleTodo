package com.codepath.simpletodo;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
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

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_todo, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvBody = (TextView) view.findViewById(R.id.tvBody);
        TextView tvDueDate = (TextView) view.findViewById(R.id.tvDueDate);

        String body = cursor.getString(cursor.getColumnIndexOrThrow("body"));
        int priority = cursor.getInt(cursor.getColumnIndexOrThrow("priority"));

        int year = cursor.getInt(cursor.getColumnIndexOrThrow("year"));
        int month = cursor.getInt(cursor.getColumnIndexOrThrow("month"));
        int day = cursor.getInt(cursor.getColumnIndexOrThrow("day"));

        SimpleDateFormat outputFmt = new SimpleDateFormat("MM/dd/yyyy");
        String currentDateTimeString = outputFmt.format(new Date(year - 1900, month, day));

        tvBody.setText(body);
        tvDueDate.setText("Due Date: " + currentDateTimeString);
        int color = 0;
        switch (priority){
            case 0: color = ContextCompat.getColor(context, R.color.priority_0);
                break;
            case 1: color = ContextCompat.getColor(context, R.color.priority_1);
                break;
            case 2: color = ContextCompat.getColor(context, R.color.priority_2);
                break;
            case 3: color = ContextCompat.getColor(context, R.color.priority_3);
        }
        view.setBackgroundColor(color);
    }
}