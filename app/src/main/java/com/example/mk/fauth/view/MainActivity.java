package com.example.mk.fauth.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.example.mk.fauth.R;
import com.example.mk.fauth.databinding.ActivityMainBinding;
import com.example.mk.fauth.viewmodel.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mFirebaseAuthListener;

    private UserViewModel userViewModel;

    private UserProfileChangeRequest.Builder profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void sendVerification() {
        final FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.v("Tag", "Verify Success");

                        user.updateProfile(profile.setDisplayName(userViewModel.getName().getValue()).build());
                    } else {
                        Log.v("Tag", "Verify Failed");
                    }
                }
            });
        }
    }


    private void init() {
        mAuth = FirebaseAuth.getInstance();

        mFirebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    sendVerification();
                }
            }
        };
        mAuth.addAuthStateListener(mFirebaseAuthListener);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setActivity(this);
        binding.setLifecycleOwner(this);
        binding.setUserModel(userViewModel);

        profile = new UserProfileChangeRequest.Builder();
    }

    private void auth(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.v("Tag", "Success");
                        } else {
                            Log.v("Tag", "Failed" + task.getException().getMessage());
                        }
                    }
                });
    }

    public void onCreateAccount(View view) {
        String email = userViewModel.getEmail().getValue();
        String password = userViewModel.getPassword().getValue();

        if (email != null && password != null) {
            if (isValidEmail(email) && !password.isEmpty()) {
                auth(email, password);
            }
        }
    }

    public void checkName(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}