package com.gaadiwala.todoapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A simple {@link DialogFragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditItemDialog.EditItemDialogListener} interface
 * to handle interaction events.
 * Use the {@link EditItemDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditItemDialog extends DialogFragment implements TextView.OnEditorActionListener {

    // the fragment initialization parameters
    private static final String ARG_PARAM1 = "itemText";

    private EditText etEditItemDialog;

    public EditItemDialog() {
        // Required empty public constructor
    }

    /**
     * factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param itemText Existing item text.
     * @return A new instance of DialogFragment EditItemDialog.
     */
    public static EditItemDialog newInstance(String itemText) {
        EditItemDialog fragment = new EditItemDialog();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, itemText);
        fragment.setArguments(args);
        return fragment;
    }

    public interface EditItemDialogListener {
        void onFinishEditItemDialog(String editedText);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        etEditItemDialog = (EditText) view.findViewById(R.id.etEditItemDialog);
        // Fetch arguments from bundle and set title
        String currText = getArguments().getString(ARG_PARAM1, "");
        etEditItemDialog.setText(currText);
        // set cursor to end of text value
        etEditItemDialog.setSelection(etEditItemDialog.length());
        getDialog().setTitle("Edit Item");
        // Show soft keyboard automatically and request focus to field
        etEditItemDialog.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_item_dialog, container, false);
        etEditItemDialog = (EditText) view.findViewById(R.id.etEditItemDialog);
        etEditItemDialog.setOnEditorActionListener(this);
        return view;
    }

    // Fires whenever the text field has an action performed
    // In this case, when the "Done" button (a checkmark) is pressed
    // REQUIRES a 'soft keyboard' (virtual keyboard)
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text to activity
            EditItemDialogListener listener = (EditItemDialogListener) getActivity();
            listener.onFinishEditItemDialog(etEditItemDialog.getText().toString());
            dismiss();
            return true;
        }
        return false;
    }
}
