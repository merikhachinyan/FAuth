package com.example.mk.fauth.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class UserViewModel extends ViewModel {

    private MutableLiveData<String> name;
    private MutableLiveData<String> email;
    private MutableLiveData<String> password;

    public void setName(String userName) {
        name.setValue(userName);
    }

    public void setEmail(String userEmail) {
        email.setValue(userEmail);
    }

    public void setPassword(String userPassword) {
        password.setValue(userPassword);
    }

    public MutableLiveData<String> getName() {
        if (name == null) {
            name = new MutableLiveData<>();
        }
        return name;
    }

    public MutableLiveData<String> getEmail() {
        if (email == null) {
            email = new MutableLiveData<>();
        }
        return email;
    }

    public MutableLiveData<String> getPassword() {
        if (password == null) {
            password = new MutableLiveData<>();
        }
        return password;
    }
}