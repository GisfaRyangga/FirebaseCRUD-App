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

public class CRUD_Football extends AppCompatActivity {
    EditText player_namaET, regionET, updataPlayernama, updateRegion;
    Button insert_player_btn, read_player_btn, update_player_btn, delete_player_btn, student_button;

    DatabaseReference mDatabaseReference;
    FootballPlayer footballPlayer;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_football);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(FootballPlayer.class.getSimpleName());

        player_namaET = findViewById(R.id.player_name_et);
        regionET = findViewById(R.id.player_region_et);
        insert_player_btn = findViewById(R.id.insert_player_btn);
        read_player_btn = findViewById(R.id.read_player_btn);
        updataPlayernama = findViewById(R.id.update_player_name_et);
        updateRegion = findViewById(R.id.update_player_region_et);
        update_player_btn = findViewById(R.id.update_player_btn);
        delete_player_btn = findViewById(R.id.delete_player_btn);
        student_button = findViewById(R.id.student_btn);

        insert_player_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertPlayer();
            }
        });

        read_player_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readPlayer();
            }
        });

        update_player_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePlayer();
            }
        });

        delete_player_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePlayer();
            }
        });

        student_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CRUD_Football.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void insertPlayer(){
        FootballPlayer mfootballPlayer = new FootballPlayer();
        String name = player_namaET.getText().toString();
        String region = regionET.getText().toString();
        if (name != "" && region != ""){
            mfootballPlayer.setName(name);
            mfootballPlayer.setRegion(region);

            mDatabaseReference.push().setValue(mfootballPlayer);
            Toast.makeText(this, "Insert player data success!", Toast.LENGTH_SHORT).show();
        }
    }

    private void readPlayer(){
        footballPlayer = new FootballPlayer();
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()){
                    for (DataSnapshot currentData : snapshot.getChildren()){
                        key = currentData.getKey();
                        footballPlayer.setName(currentData.child("name").getValue().toString());
                        footballPlayer.setRegion(currentData.child("region").getValue().toString());
                    }
                }
                updataPlayernama.setText(footballPlayer.getName());
                updateRegion.setText(footballPlayer.getRegion());
                Toast.makeText(CRUD_Football.this, "Data pemain telah ditampilkan", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updatePlayer(){
        FootballPlayer update_pemain = new FootballPlayer();
        update_pemain.setName(updataPlayernama.getText().toString());
        update_pemain.setRegion(updateRegion.getText().toString());

        mDatabaseReference.child(key).setValue(update_pemain);
        Toast.makeText(CRUD_Football.this, "Data pemain telah diupdate", Toast.LENGTH_SHORT).show();
    }

    private void deletePlayer(){
        FootballPlayer deletePlayer = new FootballPlayer();
        deletePlayer.setName(updataPlayernama.getText().toString());
        deletePlayer.setRegion(updateRegion.getText().toString());

        mDatabaseReference.child(key).removeValue();
        Toast.makeText(CRUD_Football.this, "Data pemain telah dihapus", Toast.LENGTH_SHORT).show();
    }
}