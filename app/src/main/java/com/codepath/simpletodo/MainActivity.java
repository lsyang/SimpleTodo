package com.codepath.simpletodo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class MainActivity extends AppCompatActivity {

    ListView lvItems;
    EditText editText;
    SQLiteDatabase db;
    Cursor todoCursor;
    TodoCursorAdapter todoAdapter;
    private final int EDIT_ITEM_REQUEST_CODE = 20;

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

        editText = (EditText) findViewById(R.id.etEditText);

        lvItems.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // create an intent and pass on the position and text
            String textToEdit = todoCursor.getString(todoCursor.getColumnIndexOrThrow("body"));
            int year = todoCursor.getInt(todoCursor.getColumnIndexOrThrow("year"));
                int month = todoCursor.getInt(todoCursor.getColumnIndexOrThrow("month"));
                int day = todoCursor.getInt(todoCursor.getColumnIndexOrThrow("day"));
                launchEditItemView(position, textToEdit, year, month, day);
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

    private Items getItem(){
        Long index = todoCursor.getLong(todoCursor.getColumnIndexOrThrow("_id"));
        return cupboard().withDatabase(db).get(Items.class, index);
    }

    private void writeItems(String body, int priority) {
        Items item = new Items(body, priority);
        cupboard().withDatabase(db).put(item);
        reloadView();
    }

    public void reloadView(){
        todoCursor = cupboard().withDatabase(db).query(Items.class).getCursor();
        todoAdapter.changeCursor(todoCursor);
    }

    public void onAddItem(View view){
        String body = editText.getText().toString();
        editText.setText("");
        // TODO: take in priorty as well
        writeItems(body, 2);
    }

    public void launchEditItemView(int position, String textToEdit, int year, int month, int day) {
        Intent editItemIntent = new Intent(this, EditItemActivity.class);
        editItemIntent.putExtra("position", position);
        editItemIntent.putExtra("textToEdit", textToEdit);
        editItemIntent.putExtra("year", year);
        editItemIntent.putExtra("month", month);
        editItemIntent.putExtra("day", day);

        startActivityForResult(editItemIntent, EDIT_ITEM_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == EDIT_ITEM_REQUEST_CODE) {
            String newText = data.getExtras().getString("newText");
            int year = data.getExtras().getInt("year", 0);
            int month = data.getExtras().getInt("month", 0);
            int day = data.getExtras().getInt("day", 0);

            Items item = getItem();
            item.setBody(newText);
            item.setDate(year, month, day);
            cupboard().withDatabase(db).put(item);
            reloadView();

        }
    }
}
