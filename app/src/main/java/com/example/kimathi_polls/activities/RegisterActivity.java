package com.example.kimathi_polls.activities;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kimathi_polls.R;
import com.example.kimathi_polls.beans.Biometrics;
import com.example.kimathi_polls.beans.DekutStudent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class RegisterActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "AuthPrefs";
    private static final String AUTH_METHOD_KEY = "authMethod";

    EditText voterEmailEditText, voterPasswordEditText, confirmVoterPasswordEditText;
    Button submitRegisterButton;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    DatabaseReference dekutStudentRef;

    Biometrics biometrics = new Biometrics(this, new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    });

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            biometrics.showBiometricPrompt();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        dekutStudentRef = FirebaseDatabase.getInstance().getReference("dekut_student");

        voterEmailEditText = findViewById(R.id.voterEmailEditText);
        voterPasswordEditText = findViewById(R.id.voterPasswordEditText);
        confirmVoterPasswordEditText = findViewById(R.id.confirmVoterPasswordEditText);
        submitRegisterButton = findViewById(R.id.submitRegisterButton);
        progressBar = findViewById(R.id.progressBar);

        String emailPattern = "[a-zA-Z]+\\.[a-zA-Z]+\\d{2}@students\\.dkut\\.ac\\.ke";
        voterEmailEditText.setHint("e.g., john.doe20@students.dkut.ac.ke");

        // Prepopulate the dekut_student node if it is empty or does not exist
        dekutStudentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    prepopulateDekutStudentNode();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors
            }
        });

        submitRegisterButton.setOnClickListener(v -> {
            String email = voterEmailEditText.getText().toString();
            String password = voterPasswordEditText.getText().toString();
            String confirmPassword = confirmVoterPasswordEditText.getText().toString();
            progressBar.setVisibility(ProgressBar.VISIBLE);

            if (!email.matches(emailPattern)) {
                // Handle invalid email...
                voterEmailEditText.setError("Invalid email");
            } else {
                // Query the dekut_student node to check if the email exists
                Query query = dekutStudentRef.orderByChild("email").equalTo(email);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // The email exists in the dekut_student node, continue with the registration process
                            mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Navigate to UserProfileActivity
                                            Intent intent = new Intent(RegisterActivity.this, UserProfileActivity.class);
                                            startActivity(intent);
                                        } else {
                                            // Handle failure...
                                        }
                                    }
                                });
                        } else {
                            // The email does not exist in the dekut_student node, show an error message
                            voterEmailEditText.setError("You are not a DeKUT Student. If you think this is a mistake, contact the School Admin.");
                            voterEmailEditText.requestFocus();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle possible errors
                    }
                });
            }
        });
    }

    private void prepopulateDekutStudentNode() {
        // Female names
        List<DekutStudent> students = new ArrayList<>();
        students.add(new DekutStudent("wanjiru.jane18@students.dkut.ac.ke", "Jane", "Njeri", "Wanjiru", "E027-01-0991/2018", "BSc Civil Eng", "9-7-2000", "2018", 4, 2, "Degree", "ic_profile_1"));
        students.add(new DekutStudent("kemunto.lucy19@students.dkut.ac.ke", "Lucy", "Kemunto", "Wambui", "E027-01-1234/2019", "BCom", "12-3-2001", "2019", 3, 1, "Diploma", "ic_profile_2"));
        students.add(new DekutStudent("akinyi.susan18@students.dkut.ac.ke", "Susan", "Akinyi", "Atieno", "E027-01-1122/2018", "BSc Computer Science", "6-11-2000", "2018", 3, 2, "Degree", "ic_profile_3"));
        students.add(new DekutStudent("muthoni.joy19@students.dkut.ac.ke", "Joy", "Muthoni", "Wanjiru", "E027-01-1500/2019", "BSc Electrical Eng", "3-5-2001", "2019", 2, 1, "Diploma", "ic_profile_4"));
        students.add(new DekutStudent("kagendo.irene19@students.dkut.ac.ke", "Irene", "Kagendo", "Muthoni", "E027-01-1620/2019", "BSc Mechanical Eng", "8-9-2001", "2019", 1, 2, "Certificate", "ic_profile_5"));
        students.add(new DekutStudent("akinyi.ruth20@students.dkut.ac.ke", "Ruth", "Akinyi", "Mumbi", "E027-01-1845/2020", "BBIT", "10-2-2002", "2020", 1, 1, "Certificate", "ic_profile_6"));
        students.add(new DekutStudent("nyambura.caroline20@students.dkut.ac.ke", "Caroline", "Nyambura", "Wanjiru", "E027-01-2005/2020", "BSc Civil Eng", "2-4-2002", "2020", 2, 2, "Diploma", "ic_profile_7"));
        students.add(new DekutStudent("mueni.rose19@students.dkut.ac.ke", "Rose", "Mueni", "Njeri", "E027-01-1777/2019", "BCom", "15-8-2001", "2019", 3, 1, "Diploma", "ic_profile_8"));
        students.add(new DekutStudent("wambui.patricia19@students.dkut.ac.ke", "Patricia", "Wambui", "Muthoni", "E027-01-1378/2019", "BSc Computer Science", "20-10-2001", "2019", 4, 2, "Degree", "ic_profile_9"));
        students.add(new DekutStudent("nyambura.faith18@students.dkut.ac.ke", "Faith", "Nyambura", "Wanjiru", "E027-01-1256/2018", "BSc Electrical Eng", "14-12-2000", "2018", 4, 1, "Degree", "ic_profile_10"));

        // Male names
        students.add(new DekutStudent("kibocha.john20@students.dkut.ac.ke", "John", "Muturi", "Kibocha", "C027-01-0826/2020", "BBIT", "1-2-2000", "2020", 4, 2, "Degree", "ic_profile_11"));
        students.add(new DekutStudent("njau.john20@students.dkut.ac.ke", "John", "Ndung'u", "Njau", "C027-01-0757/2020", "BBIT", "1-9-2000", "2020", 4, 2, "Degree", "ic_profile_12"));
        students.add(new DekutStudent("gitau.james19@students.dkut.ac.ke", "James", "Gitau", "Maina", "C027-01-1001/2019", "BSc Mechanical Eng", "18-5-2001", "2019", 3, 1, "Diploma", "ic_profile_13"));
        students.add(new DekutStudent("njoroge.peter19@students.dkut.ac.ke", "Peter", "Njoroge", "Kamau", "C027-01-1540/2019", "BSc Electrical Eng", "7-8-2001", "2019", 2, 2, "Certificate", "ic_profile_14"));
        students.add(new DekutStudent("kariuki.kennedy20@students.dkut.ac.ke", "Kennedy", "Kariuki", "Njenga", "C027-01-1830/2020", "BSc Computer Science", "22-10-2002", "2020", 2, 1, "Certificate", "ic_profile_15"));
        students.add(new DekutStudent("mwangi.samuel20@students.dkut.ac.ke", "Samuel", "Mwangi", "Mbugua", "C027-01-1945/2020", "BCom", "3-1-2002", "2020", 1, 2, "Certificate", "ic_profile_16"));
        students.add(new DekutStudent("gitonga.felix20@students.dkut.ac.ke", "Felix", "Gitonga", "Kibet", "C027-01-2007/2020", "BBIT", "5-3-2002", "2020", 1, 1, "Certificate", "ic_profile_17"));
        students.add(new DekutStudent("njenga.david19@students.dkut.ac.ke", "David", "Njenga", "Kamau", "C027-01-1770/2019", "BSc Civil Eng", "30-12-2001", "2019", 3, 1, "Diploma", "ic_profile_18"));
        students.add(new DekutStudent("kipchoge.patrick19@students.dkut.ac.ke", "Patrick", "Kipchoge", "Korir", "C027-01-1398/2019", "BCom", "20-6-2001", "2019", 4, 2, "Degree", "ic_profile_19"));
        students.add(new DekutStudent("mwangi.kelvin18@students.dkut.ac.ke", "Kelvin", "Mwangi", "Macharia", "C027-01-1111/2018", "BSc Mechanical Eng", "17-9-2000", "2018", 4, 1, "Degree", "ic_profile_20"));
        students.add(new DekutStudent("njoroge.samuel18@students.dkut.ac.ke", "Samuel", "Njoroge", "Kamau", "C027-01-0976/2018", "BBIT", "11-11-2000", "2018", 3, 2, "Degree", "ic_profile_21"));
        students.add(new DekutStudent("kipruto.stephen18@students.dkut.ac.ke", "Stephen", "Kipruto", "Cheruiyot", "C027-01-0822/2018", "BSc Computer Science", "4-4-2000", "2018", 2, 1, "Degree", "ic_profile_22"));

        DatabaseReference dekutStudentRef = FirebaseDatabase.getInstance().getReference("dekut_student");
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        for (DekutStudent student : students) {
            String imageName = student.getProfilePhoto(); // e.g., "ic_profile_1"
            StorageReference imageRef = storageRef.child("profile_pics/" + imageName + ".jpg");

            // Get the image from the drawable folder
            Drawable drawable;
            drawable = getResources().getDrawable(getResources().getIdentifier(imageName, "drawable", getPackageName()));
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = imageRef.putBytes(data);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                // Get the download URL
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    // Save the DekutStudent object to the dekut_student node with the download URL
                    student.setProfilePhoto(uri.toString());
                    dekutStudentRef.push().setValue(student);
                });
            }).addOnFailureListener(exception -> {
                // Handle unsuccessful uploads
                Log.e(TAG, "Failed to upload image", exception);
            });
        }
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Biometrics.REQUEST_CODE) {
            biometrics.showBiometricPrompt();
        }
    }
}