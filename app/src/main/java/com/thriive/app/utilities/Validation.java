package com.thriive.app.utilities;

import android.text.TextUtils;
import android.util.Patterns;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioGroup;


public class Validation
{


    private static final String REQUIRED_MSG = "required";

    public static boolean hasText(EditText editText)
    {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() == 0)
        {
            editText.setError(REQUIRED_MSG);
            return false;
        }

        return true;
    }

    public static boolean Validate(RadioGroup radioGroup) {
        return radioGroup.getCheckedRadioButtonId() != -1;
    }

    public static boolean validEmail(EditText editText)
    {
        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (!TextUtils.isEmpty(text)) {
            if (Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                return true;
            } else {
                editText.setError("Invalid email address");
                return false;
            }
        }else {
            editText.setError(REQUIRED_MSG);
            return false;
        }
    }

    public static boolean validPhoneNumber(EditText editText)
    {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() == 10 && text.matches("[0-9]{10}"))
        {
            return true;

        }
        else {
            editText.setError(REQUIRED_MSG);
            return false;

        }
    }
    public static boolean validAutoComplete(AutoCompleteTextView textView)
    {

        String text = textView.getText().toString().trim();
        textView.setError(null);

        // length 0 means there is no text
        if (text.length() == 10 && text.matches("[0-9]{10}"))
        {
            return true;

        }
        else {
            textView.setError(REQUIRED_MSG);
            return false;

        }
    }

}


