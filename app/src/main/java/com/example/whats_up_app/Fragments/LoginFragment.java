  package com.example.whats_up_app.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.whats_up_app.Activity.HomePage;
import com.example.whats_up_app.R;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class LoginFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public LoginFragment() {

    }

    public static @NotNull LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
      private EditText email ,pass;
      private Button login ;
      private FirebaseAuth auth;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_login, container, false);

        Button signup = view.findViewById(R.id.logSign_up);
        email = view.findViewById(R.id.logEmail);
        pass = view.findViewById(R.id.logPass);
        login = view.findViewById(R.id.loginButton);
        auth = FirebaseAuth.getInstance();
        login.setOnClickListener(this::LoginClick);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.flFragment,new SignUpFragment());
                transaction.commit();
            }
        });
        return view;
    }
      public void LoginClick(View view){
          String text_email= email.getText().toString().trim();
          String text_password = pass.getText().toString().trim();
          if (text_email.isEmpty() || text_password.isEmpty()){
              Toast.makeText(view.getContext().getApplicationContext(), "Please Enter All Fields", Toast.LENGTH_SHORT).show();
          }else {
          loginUsers(view,text_email,text_password);
          }
      }
      private void loginUsers(View view,String email, String password) {
          auth.signInWithEmailAndPassword(email,password)
                  .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                      @Override
                      public void onSuccess(AuthResult authResult) {
                          Toast.makeText(view.getContext().getApplicationContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
                          startActivity(new Intent(view.getContext().getApplicationContext(), HomePage.class));
                          getActivity().finish();
                      }
                  }).addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                  Toast.makeText(view.getContext().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
              }
          });
      }

      private void googleSignIn(){
          // Configure Google Sign In
          GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                  //.requestIdToken(getString(R.string.default_web_client_id))
                  .requestEmail()
                  .build();
          // I needto get google sign in id

//          mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
      }

}