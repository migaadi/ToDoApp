package com.gaadiwala.todoapp;

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

    private final int REQUEST_CODE = 21;

    ArrayList<String> todoItems;
    ArrayAdapter<String> aTodoAdapter;
    ListView lvItems;
    private EditText etEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateArrayItems();
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(aTodoAdapter);
        etEditText = (EditText) findViewById(R.id.etEditText);

        // Long Click Listener for an item deletion
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                todoItems.remove(position);
                aTodoAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });

        // Handle tap on an item for editing
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                // put "extras" in the bundle for access in second activity
                i.putExtra("position", position);
                i.putExtra("text", todoItems.get(position));
                startActivityForResult(i, REQUEST_CODE);
            }
        });
    }

    @Override
    // Process result received from 'edit item' activity
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if ((resultCode == RESULT_OK) && (requestCode == REQUEST_CODE)) {
            // Extract 'edited item' value from extras
            String editedItem = data.getStringExtra("editedItem");
            int position = data.getIntExtra("position", 0);
            todoItems.remove(position);
            todoItems.add(position, editedItem);
            aTodoAdapter.notifyDataSetChanged();
            writeItems();
        }
    }

    // read items from the file and fill the adapter
    public void populateArrayItems() {
        readItems();
        aTodoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems);
    }

    // button click handler for adding an item
    public void onAddItem(View view) {
        aTodoAdapter.add(etEditText.getText().toString());
        etEditText.setText("");
        writeItems();
    }

    // read items from the persistent storage (a file)
    public void readItems() {
        File filesDir = getFilesDir();
        File file = new File(filesDir, "todo.txt");
        try{
            todoItems = new ArrayList<String>(FileUtils.readLines(file));
        } catch (IOException e) {
        }
    }

    // write data back to persistent storage (a file)
    public void writeItems() {
        File filesDir = getFilesDir();
        File file = new File(filesDir, "todo.txt");
        try{
            FileUtils.writeLines(file, todoItems);
        } catch (IOException e) {
        }
    }
}
