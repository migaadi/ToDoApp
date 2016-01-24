package com.gaadiwala.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    private EditText etEditItem;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        etEditItem = (EditText) findViewById(R.id.etEditItem);
        position = getIntent().getIntExtra("position", 0);
        etEditItem.setText(getIntent().getStringExtra("text"));
        // set cursor to end of text value
        etEditItem.setSelection(etEditItem.length());
    }

    // button click handler for editing an item
    public void onEditItem(View view) {
        Intent data = new Intent();
        // Pass relevant data back as result
        data.putExtra("editedItem", etEditItem.getText().toString());
        data.putExtra("position", position);
        // Activity finished ok, return the data
        setResult(RESULT_OK, data); // set result code and bundle data for response
        finish(); // close the activity, pass data to parent
    }
}
