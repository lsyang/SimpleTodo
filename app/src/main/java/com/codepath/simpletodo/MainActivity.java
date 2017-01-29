package com.codepath.simpletodo;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import com.google.gson.Gson;

import java.util.ArrayList;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> todoItems;
    ArrayAdapter<String> aTodoAdaptor;
    ListView lvItems;
    EditText editText;
    SQLiteDatabase db;
    Gson gson = new Gson();
    private final int EDIT_ITEM_REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Database dbHelper = new Database(this);
        db = dbHelper.getWritableDatabase();

        populateArrayItems();
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(aTodoAdaptor);
        editText = (EditText) findViewById(R.id.etEditText);
        lvItems.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // create an intent and pass on the position and text
                String textToEdit = (String)parent.getItemAtPosition(position);
                launchEditItemView(position, textToEdit);
            }
        });
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
                todoItems.remove(position);
                aTodoAdaptor.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });
    }

    public void populateArrayItems(){
        todoItems = new ArrayList<String>();
        readItems();
        aTodoAdaptor = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems);
    }

    private void readItems() {
        if (cupboard().withDatabase(db).query(Items.class).get()!= null) {
            Items items = cupboard().withDatabase(db).query(Items.class).get();
            todoItems = gson.fromJson(items.text, ArrayList.class);
        }
    }

    private void writeItems() {
        String jsonItems = gson.toJson(todoItems);
        Items items;
        if (cupboard().withDatabase(db).query(Items.class).get()!= null) {
            items = cupboard().withDatabase(db).query(Items.class).get();
            items.text = jsonItems;
        } else {
            items = new Items(jsonItems);

        }
        cupboard().withDatabase(db).put(items);
    }

    public void onAddItem(View view){
        aTodoAdaptor.add(editText.getText().toString());
        editText.setText("");
        writeItems();
    }

    public void launchEditItemView(int position, String textToEdit) {
        Intent editItemIntent = new Intent(this, EditItemActivity.class);
        editItemIntent.putExtra("position", position);
        editItemIntent.putExtra("textToEdit", textToEdit);

        startActivityForResult(editItemIntent, EDIT_ITEM_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == EDIT_ITEM_REQUEST_CODE) {
            String newText = data.getExtras().getString("newText");
            int position = data.getExtras().getInt("position", 0);

            todoItems.set(position, newText);
            aTodoAdaptor.notifyDataSetChanged();
            writeItems();
        }
    }
}
