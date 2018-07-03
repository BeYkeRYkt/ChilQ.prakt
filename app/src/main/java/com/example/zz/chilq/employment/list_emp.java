package com.example.zz.chilq.employment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.zz.chilq.MainActivity;
import com.example.zz.chilq.R;
import com.example.zz.chilq.adapter.child_task_list_adapter;
import com.example.zz.chilq.model.task_model;
import com.example.zz.chilq.model.user_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class list_emp extends Fragment {

    // CardView
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    private DatabaseReference myRef,taskRef;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private ChildEventListener mChildEventListener;

    private ProgressBar pbListTask;

    private List<task_model> task_modelList= new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_emp, container,
                false);
        getActivity().setTitle(R.string.fragment_my_tasks_title);

        pbListTask=(ProgressBar)rootView.findViewById(R.id.progress);
        pbListTask.setVisibility(View.VISIBLE);


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        myRef = FirebaseDatabase.getInstance().getReference().child("Child").child("123").child("Parent");
        // CardView
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(rootView.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        //mAdapter = new parent_task_list_adapter(myDataset);
        if(!task_modelList.isEmpty()){
            task_modelList.clear();
        }
        mAdapter = new child_task_list_adapter(task_modelList);
        mRecyclerView.setAdapter(mAdapter);
        


        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    user_model userModel= null;
                    userModel = dataSnapshot.getValue(user_model.class);
                    taskRef = FirebaseDatabase.getInstance().getReference().child("Tasks");
                    taskRef.orderByChild("name_parent").equalTo(userModel.getS_uid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                task_model taskModel=null;
                                taskModel=snapshot.getValue(task_model.class);
                                task_modelList.add(taskModel);
                                mAdapter.notifyDataSetChanged();
                                pbListTask.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }}
                    );

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("Не смогли установить onDisconnect:" + databaseError.getMessage());

                }
            };
        }
        myRef.addChildEventListener(mChildEventListener);

        return rootView;
    }

    @Override
    public void  onDestroy(){
        if(mChildEventListener!=null){
            myRef.removeEventListener(mChildEventListener);
            mChildEventListener=null;
        }
        super.onDestroy();
    }

}
