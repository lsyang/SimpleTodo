package com.codepath.simpletodo;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;


public class EditItemFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    // TODO: make mEditPriority into dropdown
    private EditText mEditTitle;
    private TextView mEditDueDate;
    private EditText mEditPriority;
    private Button saveBtn;
    private Button cancelBtn;
    private Button changeDateBtn;

    private boolean isNew;
    private String title;
    private int year;
    private int month;
    private int day;
    private int priority;

    public interface EditNameDialogListener {
        void onFinishEditDialog(Boolean isNew, String title, int year, int month, int day, int priority);
    }


    public EditItemFragment(){
    }

    public static EditItemFragment newInstance(Boolean isNew, String title, int year, int month, int day, int priority){
        EditItemFragment frag = new EditItemFragment();
        Bundle args = new Bundle();
        args.putBoolean("isNew", isNew);
        args.putString("header", "Edit Item");
        args.putString("title", title);
        args.putInt("year", year);
        args.putInt("month", month);
        args.putInt("day", day);
        args.putInt("priority", priority);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_edit_item, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstaceState){
        super.onViewCreated(view, savedInstaceState);
        mEditTitle = (EditText) view.findViewById(R.id.txt_title);
        mEditDueDate = (TextView) view.findViewById(R.id.txt_due_date);
        mEditPriority = (EditText) view.findViewById(R.id.txt_pirority);
        saveBtn = (Button) view.findViewById(R.id.edit_save);
        cancelBtn = (Button) view.findViewById(R.id.edit_cancel);
        changeDateBtn = (Button) view.findViewById(R.id.edit_date);

        isNew = getArguments().getBoolean("isNew", true);
        String header = getArguments().getString("header", "");
        title = getArguments().getString("title", "");
        year = getArguments().getInt("year", 2017);
        month = getArguments().getInt("month", 01);
        day = getArguments().getInt("day", 01);
        priority = getArguments().getInt("priority", 1);


        getDialog().setTitle(header);
        mEditTitle.setText(title);
        mEditTitle.setSelection(title.length());
        displayDueDate();
        mEditPriority.setText(String.valueOf(priority));

        saveBtn.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onSave();
            }
        });
        changeDateBtn.setOnClickListener(new OnClickListener()

        {
            @Override
            public void onClick(View v)
            {
                showDatePickerDialog();
            }
        });
        cancelBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancel();
            }
        });

        getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }

    public void showDatePickerDialog() {
        DialogFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setTargetFragment(EditItemFragment.this, 300);
        datePickerFragment.show(getFragmentManager(), "timePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        displayDueDate();
    }

    private void displayDueDate() {
        SimpleDateFormat outputFmt = new SimpleDateFormat("MM/dd/yyyy");
        String currentDateTimeString = "Due Date: " + outputFmt.format(new Date(year - 1900, month, day));
        mEditDueDate.setText(currentDateTimeString);
    }

    public void onSave() {
        title = mEditTitle.getText().toString();
        priority = Integer.parseInt(mEditPriority.getText().toString());

        EditNameDialogListener listener = (EditNameDialogListener) getActivity();
        listener.onFinishEditDialog(isNew, title, year, month, day, priority);
        dismiss();
    }

    public void onCancel() {
        dismiss();
    }
}
