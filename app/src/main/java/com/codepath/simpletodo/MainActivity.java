package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> todoItems;
    ArrayAdapter<String> aTodoAdaptor;
    ListView lvItems;
    EditText editText;
    private final int EDIT_ITEM_REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        File filesDir = getFilesDir();
        File file = new File(filesDir, "todo.txt");
        try {
            todoItems = new ArrayList<String>(FileUtils.readLines(file));
        } catch (IOException e) {

        }
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File file = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(file, todoItems);
        } catch (IOException e) {

        }
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
