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
 * {@link AddItemDialog.AddItemDialogListener} interface
 * to handle interaction events.
 * Use the {@link AddItemDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddItemDialog extends DialogFragment implements TextView.OnEditorActionListener {
    // the fragment initialization parameter
    private static final String ARG_PARAM1 = "title";

    private EditText etAddNewItem;

    public AddItemDialog() {
        // Required empty public constructor
    }

    /**
     * factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param title Dialog title.
     * @return A new instance of DialogFragment AddItemDialog.
     */
    public static AddItemDialog newInstance(String title) {
        AddItemDialog fragment = new AddItemDialog();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, title);
        fragment.setArguments(args);
        return fragment;
    }

    public interface AddItemDialogListener {
        void onFinishAddItemDialog(String inputText);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        etAddNewItem = (EditText) view.findViewById(R.id.etAddNewItem);
        // Fetch arguments from bundle and set title
        String title = getArguments().getString(ARG_PARAM1, "Enter item");
        getDialog().setTitle(title);
        // Show soft keyboard automatically and request focus to field
        etAddNewItem.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_item_dialog, container, false);
        etAddNewItem = (EditText) view.findViewById(R.id.etAddNewItem);
        etAddNewItem.setOnEditorActionListener(this);
        return view;
    }

    // Fires whenever the text field has an action performed
    // In this case, when the "Done" button (a checkmark) is pressed
    // REQUIRES a 'soft keyboard' (virtual keyboard)
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text to activity
            AddItemDialogListener listener = (AddItemDialogListener) getActivity();
            listener.onFinishAddItemDialog(etAddNewItem.getText().toString());
            dismiss();
            return true;
        }
        return false;
    }
}
