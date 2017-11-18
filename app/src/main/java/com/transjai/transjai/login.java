package com.transjai.transjai;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class login extends AppCompatActivity {

    private Button login_facebook;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private CallbackManager callbackManager;

    private FirebaseAuth mAuth;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        debugHashKey();
        mAuth = FirebaseAuth.getInstance();
        mProgressDialog = new ProgressDialog(this);


        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        handleFacebookAccessToken(loginResult.getAccessToken());
                        Profile profile = Profile.getCurrentProfile();
                        if (profile != null) {
                            String facebook_id =  profile.getId();
                            String f_name = profile.getFirstName();
                            String m_name = profile.getMiddleName();
                            String l_name = profile.getLastName();
                            String full_name = profile.getName();
                            String profile_image = profile.getProfilePictureUri(1000, 1000).toString();
                            //Toast.makeText(getApplication(),"ยินดีต้อนรับ facebook: "+full_name, Toast.LENGTH_LONG).show();

                        }

                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        mProgressDialog.setMessage("กำลังเข้าสู่ระบบ ..");
        mProgressDialog.show();
    }

    private void handleFacebookAccessToken(AccessToken token) {
        // [START_EXCLUDE silent]
        // [END_EXCLUDE]

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mProgressDialog.dismiss();
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();

                            DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
                            DatabaseReference childrf = dbref.child("user");



                            //build child
                            //childrf.child(user.getUid()).child("info").setValue(new DataUer(Email,Password,user.getUid(),"null",DisplayName));
                            //String a = ""+user;
                            //Toast.makeText(getApplication(),"ยินดีต้อนรับ : "+user.getUid().toString(), Toast.LENGTH_LONG).show();

                            Profile profile = Profile.getCurrentProfile();
                            if (profile != null) {
                                String facebook_id =  profile.getId();
                                String f_name = profile.getFirstName();
                                String m_name = profile.getMiddleName();
                                String l_name = profile.getLastName();
                                String full_name = profile.getName();
                                String profile_image = profile.getProfilePictureUri(400, 400).toString();
                                Toast.makeText(getApplication(),"ยินดีต้อนรับ : "+full_name, Toast.LENGTH_LONG).show();

                                dbref.child("user").child(user.getUid()).child("info").child("f_name").setValue(f_name);
                                dbref.child("user").child(user.getUid()).child("info").child("m_name").setValue(m_name);
                                dbref.child("user").child(user.getUid()).child("info").child("l_name").setValue(l_name);
                                dbref.child("user").child(user.getUid()).child("info").child("full_name").setValue(full_name);
                                dbref.child("user").child(user.getUid()).child("info").child("profile_image").setValue(profile_image);

                            }





                            //Toast.makeText(login.this, "username & password Fail", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplication(), MainActivity.class);
                            finish();
                            startActivity(intent);
                            overridePendingTransition(R.anim.right_in, R.anim.left_out); //ใหม่ , เก่า

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
    }
    private void debugHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.transjai.transjai",
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }


}
