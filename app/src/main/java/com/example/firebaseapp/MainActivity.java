package com.example.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText namaET, addressET, updatenama, updateaddress;
    Button insertbtn, readbtn, updatebtn, deletebtn, fp_button;

    DatabaseReference mDatabaseReference;
    Student mStudent;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Student.class.getSimpleName());

        namaET = findViewById(R.id.name_et);
        addressET = findViewById(R.id.address_et);
        insertbtn = findViewById(R.id.insert_btn);
        readbtn = findViewById(R.id.read_btn);
        updatenama = findViewById(R.id.update_name_et);
        updateaddress = findViewById(R.id.update_address_et);
        updatebtn = findViewById(R.id.update_btn);
        deletebtn = findViewById(R.id.delete_btn);
        fp_button = findViewById(R.id.fp_btn);

        insertbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });

        readbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readData();
            }
        });

        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
            }
        });

        fp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CRUD_Football.class);
                startActivity(intent);
            }
        });
    }

    private void insertData(){
        Student student = new Student();
        String name = namaET.getText().toString();
        String address = addressET.getText().toString();
        if (name != "" && address != ""){
            student.setName(name);
            student.setAddress(address);

            mDatabaseReference.push().setValue(student);
            Toast.makeText(this, "Insert success!", Toast.LENGTH_SHORT).show();
        }
    }

    private void readData(){
        mStudent = new Student();
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()){
                    for (DataSnapshot currentData : snapshot.getChildren()){
                        key = currentData.getKey();
                        mStudent.setName(currentData.child("name").getValue().toString());
                        mStudent.setAddress(currentData.child("address").getValue().toString());
                    }
                }
                updatenama.setText(mStudent.getName());
                updateaddress.setText(mStudent.getAddress());
                Toast.makeText(MainActivity.this, "Data telah ditampilkan", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateData(){
        Student updateData = new Student();
        updateData.setName(updatenama.getText().toString());
        updateData.setAddress(updateaddress.getText().toString());

        mDatabaseReference.child(key).setValue(updateData);
        Toast.makeText(MainActivity.this, "Data telah diupdate", Toast.LENGTH_SHORT).show();
    }

    private void deleteData(){
        Student updateData = new Student();
        updateData.setName(updatenama.getText().toString());
        updateData.setAddress(updateaddress.getText().toString());

        mDatabaseReference.child(key).removeValue();
        Toast.makeText(MainActivity.this, "Data telah dihapus", Toast.LENGTH_SHORT).show();
    }
}