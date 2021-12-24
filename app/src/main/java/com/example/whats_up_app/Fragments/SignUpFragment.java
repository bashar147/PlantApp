package com.example.whats_up_app.Fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.whats_up_app.Activity.HomePage;
import com.example.whats_up_app.Activity.SignUpActivity;
import com.example.whats_up_app.Classes.UploadUserData;
import com.example.whats_up_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpFragment extends Fragment implements DatePickerDialog.OnDateSetListener {



    public SignUpFragment() {

    }

    private EditText email , pass , dateOfBarth ,fullName;
    private Button register;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage fireBaseStorage;
    private FirebaseUser user;
    private String profilePickStatus;
    private UploadUserData userData;
    private  DatabaseReference myUserRef;
    private  StorageReference myUserStorage_Ref;
    Context applicationContext = SignUpActivity.getContextOfApplication();

    private CircleImageView profile_image;
    private static final int PICK_IMAGE = 1;
    static Uri imageUri = null;
    public static Boolean check123 = false;
    public static Bitmap bitmap = null;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        try {
            userData = new UploadUserData();
            firebaseDatabase = FirebaseDatabase.getInstance();
            fireBaseStorage = FirebaseStorage.getInstance();
            auth = FirebaseAuth.getInstance();
            user = FirebaseAuth.getInstance().getCurrentUser();
            myUserStorage_Ref = fireBaseStorage.getReference("users");
        }
        catch (Exception e){
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        profile_image = view.findViewById(R.id.profile_image);
        profile_image.setOnClickListener(this::profile_image_Click);
        email = view.findViewById(R.id.regEmail);
        fullName = view.findViewById(R.id.regUserName);
        dateOfBarth = view.findViewById(R.id.regConfPass);
        pass = view.findViewById(R.id.regPass);
        profilePickStatus = "Nothing";
        register = view.findViewById(R.id.signUpButton);
        register.setOnClickListener(this::RegisterClick);

        Button login = view.findViewById(R.id.toLoginFragment);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.flFragment,new LoginFragment());
                transaction.commit();
            }
        });

        dateOfBarth.setOnClickListener(this::DateOfBarth);
        return view;
    }

    public void references (){
//       try {
//
//         myUserRef = firebaseDatabase.getReference("users")
//         .child(auth.getCurrentUser().getUid());
//              myUserStorage_Ref = fireBaseStorage.getReference("users");
//       }catch (Exception e){
//           Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
//        }

    }

    public void RegisterClick(View view){

        String text_email = email.getText().toString().trim();
        String text_password = pass.getText().toString().trim();
        String name = fullName.getText().toString().trim();

        if (TextUtils.isEmpty(text_email) || (TextUtils.isEmpty(text_password)) || (TextUtils.isEmpty(name))){
            Toast.makeText(view.getContext().getApplicationContext(), "Empty Credentials", Toast.LENGTH_SHORT).show();
        }else if (text_password.length()<6){
            Toast.makeText(view.getContext().getApplicationContext(), "Password too short", Toast.LENGTH_SHORT).show();
        }else {
            registerUser(view,text_email,text_password);
        }
    }

    private void registerUser(View view,String textEmail, String textPassword) {
        auth.createUserWithEmailAndPassword(textEmail,textPassword)
                .addOnCompleteListener(getActivity(),new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            myUserRef = firebaseDatabase.getReference("users")
                                    .child(auth.getCurrentUser().getUid());

                            String fName  = fullName.getText().toString().trim();
                            String DOB = dateOfBarth.getText().toString().trim();
                            String profileImageUrl = profilePickStatus;

                             userData = new UploadUserData(
                                    fName,DOB,userData.getProfilePicUrl());

                            myUserRef.setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){
                                        Toast.makeText(view.getContext().getApplicationContext(), "Registering user successfully", Toast.LENGTH_SHORT).show();
                                        //Toast.makeText(getActivity(), "Data Upload Successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getContext().getApplicationContext(), HomePage.class));
                                        getActivity().finish();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "CantUploadDataMethod\n"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });


                        }else {
                            Toast.makeText(getActivity(), "Registration Failed! \n"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signInMethod() {
         String emailId = email.getText().toString().trim();
         String password = pass.getText().toString().trim();
         auth.signInWithEmailAndPassword(emailId,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
             @Override
             public void onSuccess(AuthResult authResult) {
                 //store_Image(imageUri);
                 //UploadData();
             }
         }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {
                 Toast.makeText(getActivity(), "Sign In Method\n"+e.getMessage(), Toast.LENGTH_SHORT).show();
             }
         });
    }

    private void UploadData() {

        String fName  = fullName.getText().toString().trim();
        String DOB = dateOfBarth.getText().toString().trim();
        String profileImageUrl = profilePickStatus;

        UploadUserData userData = new UploadUserData(
                fName,DOB,profileImageUrl);

        myUserRef.setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    Toast.makeText(getActivity(), "Data Upload Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getContext().getApplicationContext(), HomePage.class));
                    getActivity().finish();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "UploadDataMethod\n"+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private  @NotNull boolean checkProfilePicStatus(String Status){
        if (profilePickStatus.equals("Nothing")){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }

    public void DateOfBarth(@NotNull View view){
        int day = 1 , month = 1 , year = 2000;

        DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                dateOfBarth.setText(dayOfMonth + "/"+ month + "/" + year);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    public void store_Image(Uri uri){

        StorageReference reference = FirebaseStorage.getInstance().getReference();
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setTitle("Uploading Image...");
        pd.show();
        pd.setCanceledOnTouchOutside(true);
        final String randomKey = UUID.randomUUID().toString();
        StorageReference riversRef = reference.child(System.currentTimeMillis() +"."+ getFileExtinsion(uri));
        riversRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        check123 = true;
                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                //Image image = new Image(uri.toString());
                                //String modelId = root.push().getKey();
                                //root.child(modelId).setValue(image);
                                userData.setProfilePicUrl(uri.toString());
                                Toast.makeText(applicationContext, "Image Uploaded", Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                            }
                        });
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(applicationContext, "Failed To Upload\n"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        check123 = false;
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progressPercent = (100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        pd.setMessage("Progress: "+ (int) progressPercent + "%");
                    }
                });
    }

    private String getFileExtinsion(Uri mUri) {
        ContentResolver cr = applicationContext.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == getActivity().RESULT_OK) {
            imageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                profile_image.setImageBitmap(bitmap);
                store_Image(imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void profile_image_Click(View view){
        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(gallery,"select picture"),PICK_IMAGE);
    }
}