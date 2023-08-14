package com.example.myapplication.ui.home;

import android.widget.EditText;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment, \nthere i will tell you about app ");
    }
    public LiveData<String> getText() {
        return mText;
    }
    boolean checkInput(EditText editText){
        return editText != null && editText.getText() != null && !editText.getText().toString().isEmpty();
    }
}