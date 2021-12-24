package com.example.whats_up_app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.whats_up_app.Fragments.LoginFragment;
import com.example.whats_up_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SignUpActivity extends AppCompatActivity {

    public static Context contextOfApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        replaceFragment(new LoginFragment());

        contextOfApplication = getApplicationContext();

    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.flFragment,fragment);
        transaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //  FirebaseUser To get User on Auth
        // this on start use to keep me in sign in
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            startActivity(new Intent(getApplicationContext(),HomePage.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        }else {

        }
    }
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }
}
