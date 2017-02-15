package com.codepath.simpletodo;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditItemActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    EditText editItem;
    EditText editDueDate;
    int year;
    int month;
    int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        String textToEdit = getIntent().getStringExtra("textToEdit");
        editItem = (EditText) findViewById(R.id.editItem);
        editItem.setText(textToEdit);
        editItem.setSelection(textToEdit.length());

        year = getIntent().getIntExtra("year", 0);
        month = getIntent().getIntExtra("month", 0);
        day = getIntent().getIntExtra("day", 0);
        displayDueDate(year, month, day);
    }

    public void onSubmit(View v) {
        Intent data = new Intent();
        data.putExtra("newText", editItem.getText().toString());
        data.putExtra("year", year);
        data.putExtra("month", month);
        data.putExtra("day", day);
        setResult(RESULT_OK, data);
        finish();
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        displayDueDate(year, month, day);

    }

    private void displayDueDate(int year, int month, int day){
        SimpleDateFormat outputFmt = new SimpleDateFormat("MM/dd/yyyy");
        String currentDateTimeString = "Due Date: " + outputFmt.format(new Date(year - 1900, month, day));
        editDueDate = (EditText) findViewById(R.id.editDueDate);
        editDueDate.setText(currentDateTimeString);
    }

}
