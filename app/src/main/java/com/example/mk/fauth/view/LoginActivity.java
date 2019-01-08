package com.example.mk.fauth.view;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mk.fauth.R;
import com.example.mk.fauth.databinding.ActivityLoginBinding;
import com.example.mk.fauth.viewmodel.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setActivity(this);
        binding.setModel(userViewModel);
        binding.setLifecycleOwner(this);
    }

    public void loginUser(View view) {
        String email = userViewModel.getEmail().getValue();
        String password = userViewModel.getPassword().getValue();

        if (email != null && password != null) {
            if (!email.isEmpty() && !password.isEmpty()) {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    if (user != null) {
                                        if (user.isEmailVerified()) {
                                            Log.v("Tag", user.getDisplayName());
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Verify account", Toast.LENGTH_SHORT).show();
                                            Log.v("Tag", user.getEmail());
                                        }
                                    }
                                } else {
                                    Log.v("Tag", "Not found");
                                }
                            }
                        });
            }
        } else {
            Log.v("Tag", "Enter valid email or password");
        }
    }
}
