package com.example.pawfectcareapp.Utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

public class GenericTextWatcher implements TextWatcher, View.OnKeyListener {
    private View currentView;
    private View nextView;
    private View prevView;

    public GenericTextWatcher(View currentView, View nextView, View prevView) {
        this.currentView = currentView;
        this.nextView = nextView;
        this.prevView = prevView;

        // Set OnKeyListener to detect backspace
        currentView.setOnKeyListener(this);
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() == 1 && nextView != null) {
            nextView.requestFocus(); // Move to next field when a digit is entered
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
            EditText editText = (EditText) v;
            if (editText.getText().toString().isEmpty() && prevView != null) {
                prevView.requestFocus(); // Move to the previous field when backspace is pressed on an empty field
                return true;
            }
        }
        return false;
    }
}
