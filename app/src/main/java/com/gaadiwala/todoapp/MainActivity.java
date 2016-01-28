package com.gaadiwala.todoapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AddItemDialog.AddItemDialogListener,
        EditItemDialog.EditItemDialogListener {

//    private final int REQUEST_CODE = 21;

    ArrayList<String> todoItems;
    ArrayAdapter<String> aTodoAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateArrayItems();
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(aTodoAdapter);

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
                // Edit item using DialogFragment
                showEditItemDialog(position);
                // Edit item using an activity - Not removing purposefully!
/*
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                // put "extras" in the bundle for access in second activity
                i.putExtra("position", position);
                i.putExtra("text", todoItems.get(position));
                startActivityForResult(i, REQUEST_CODE);
*/
            }
        });
    }

    // Edit item using an activity - Not removing purposefully!
/*
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
*/

    // read items from the file and fill the adapter
    public void populateArrayItems() {
        readItems();
        aTodoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems);
    }

    // button click handler for adding an item
    public void onAddItem(View view) {
        showAddItemDialog();
    }

    // invokes a dialog for user text input
    private void showAddItemDialog() {
        FragmentManager fm = getSupportFragmentManager();
        AddItemDialog addItemDialog = AddItemDialog.newInstance("Add an item");
        addItemDialog.show(fm, "fragment_add_item_dialog");
    }

    // invokes a dialog for editing an existing item
    private void showEditItemDialog(int position) {
        FragmentManager fm = getSupportFragmentManager();
        EditItemDialog editItemDialog = EditItemDialog.newInstance(todoItems.get(position));
        editItemDialog.show(fm, "fragment_edit_item_dialog");
        todoItems.remove(position);
    }

    @Override
    // Process the text returned by 'add item dialog'
    public void onFinishAddItemDialog(String inputText) {
        if (inputText != null) {
            aTodoAdapter.add(inputText);
            writeItems();
        }
    }

    @Override
    // Process the text returned by 'edit item dialog'
    public void onFinishEditItemDialog(String editedText) {
        if (editedText != null) {
            // This is not a clean solution as edited items are
            // getting added to the end instead of in-place editing
            aTodoAdapter.add(editedText);
            writeItems();
        }
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
