package com.example.firebaseapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PullActivity extends AppCompatActivity {

    private Button push, query1, query2, logout;
    private EditText studentID;
    private int sID;

    private RecyclerView cycle;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layout;

    private FirebaseDatabase fire;
    private DatabaseReference data;
    private DatabaseReference table;

    private List<Grade> list;

    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull);

        Intent intent = getIntent();

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        fire = FirebaseDatabase.getInstance();
        data = fire.getReference();
        table = data.child("simpsons/grades/");

        studentID = findViewById(R.id.studentId);
        push = findViewById(R.id.push);
        query1 = findViewById(R.id.query1);
        query2 = findViewById(R.id.query2);
        logout = findViewById(R.id.logout);
        cycle = findViewById(R.id.recyclerView);
    }

    public void pushClick(View view){
        Intent push = new Intent(PullActivity.this,PushActivity.class);
        startActivity(push);
    }

    public void query1Click(View view){
        sID = Integer.parseInt(studentID.getText().toString());

        Query query = table.orderByChild("student_id").equalTo(sID);
        query.addValueEventListener(vEL);
    }

    public void query2Click(View view){
        sID = Integer.parseInt(studentID.getText().toString());

        Query query = table.orderByChild("student_id").startAt(sID);
        query.addValueEventListener(vEL);
    }

    ValueEventListener vEL = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()){
                list = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Grade grade = snapshot.getValue(Grade.class);
                    list.add(grade);
                }

                List<String> courses = new ArrayList<>();
                List<String> grades = new ArrayList<>();
                List<Integer> id = new ArrayList<>();

                for(Grade temp : PullActivity.this.list){
                    courses.add(temp.getcourse_name());
                    grades.add(temp.getgrade());
                    id.add(temp.getstudent_id());
                }

                PullActivity.this.cycle.setHasFixedSize(true);
                layout = new LinearLayoutManager(PullActivity.this);
                cycle.setLayoutManager(layout);
                PullActivity.this.cycle.setAdapter(adapter);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Toast.makeText(PullActivity.this,
                    "Unexpected Failure.", Toast.LENGTH_SHORT).show();
        }
    };

    public void logoutClick(View view){
        mAuth.signOut();
        user = null;
        finish();
    }
}
