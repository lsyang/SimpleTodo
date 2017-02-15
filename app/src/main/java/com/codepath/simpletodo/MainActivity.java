package com.codepath.simpletodo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.simpletodo.EditItemFragment.EditNameDialogListener;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class MainActivity extends AppCompatActivity implements EditNameDialogListener {

    ListView lvItems;
    SQLiteDatabase db;
    Cursor todoCursor;
    TodoCursorAdapter todoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Database handler = new Database(this);
        db = handler.getWritableDatabase();
        todoCursor = cupboard().withDatabase(db).query(Items.class).getCursor();

        lvItems = (ListView) findViewById(R.id.lvItems);
        todoAdapter = new TodoCursorAdapter(this, todoCursor);
        lvItems.setAdapter(todoAdapter);

        lvItems.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // create an intent and pass on the position and text
                String textToEdit = todoCursor.getString(todoCursor.getColumnIndexOrThrow("body"));
                int year = todoCursor.getInt(todoCursor.getColumnIndexOrThrow("year"));
                int month = todoCursor.getInt(todoCursor.getColumnIndexOrThrow("month"));
                int day = todoCursor.getInt(todoCursor.getColumnIndexOrThrow("day"));
                int priority = todoCursor.getInt(todoCursor.getColumnIndexOrThrow("priority"));
                showEditDialog(false, textToEdit, year, month, day, priority);
            }
        });

        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
                Items item = getItem();
                cupboard().withDatabase(db).delete(item);

                reloadView();
                return true;
            }
        });

    }

    private void showEditDialog(Boolean isNew, String title, int year, int month, int day, int priority){
        FragmentManager fm = getSupportFragmentManager();
        EditItemFragment editItemFragment = EditItemFragment.newInstance(isNew, title, year, month, day, priority);
        editItemFragment.show(fm, "fragment_edit_name");
    }

    private Items getItem(){
        Long index = todoCursor.getLong(todoCursor.getColumnIndexOrThrow("_id"));
        return cupboard().withDatabase(db).get(Items.class, index);
    }

    private void writeItems(String body, int year, int month, int day, int priority) {
        Items item = new Items(body, priority, year, month, day);
        cupboard().withDatabase(db).put(item);
        reloadView();
    }

    public void reloadView(){
        todoCursor = cupboard().withDatabase(db).query(Items.class).getCursor();
        todoAdapter.changeCursor(todoCursor);
    }

    public void onAddItem(View view){
        showEditDialog(true, "", 2017, 01, 01, 2);
    }

    @Override
    public void onFinishEditDialog(Boolean isNew, String title, int year, int month, int day, int priority) {
        if (isNew) {
            writeItems(title, year, month, day, priority);
        } else {
            Items item = getItem();
            item.setBody(title);
            item.setDate(year, month, day);
            item.setPriority(priority);
            cupboard().withDatabase(db).put(item);
            reloadView();
        }
    }
}
