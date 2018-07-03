package com.example.zz.chilq;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zz.chilq.employment.CreateTask;
import com.example.zz.chilq.employment.my_emp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class main_parent extends Fragment implements View.OnClickListener {

    private TextView display_name, display_uid;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_parent, container,
                false);

        getActivity().setTitle("Родитель");
        bundle=this.getArguments();

        rootView.findViewById(R.id.my_child).setOnClickListener(this);
        rootView.findViewById(R.id.create_task).setOnClickListener(this);
        rootView.findViewById(R.id.list_task).setOnClickListener(this);

        display_name=(TextView)rootView.findViewById(R.id.display_name_parent);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        display_name.setText(user.getDisplayName());

        return rootView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_child: {
                Class fragmentClass;
                fragmentClass = list_child.class;
                actToFragment(fragmentClass);
                break;
            }
            case R.id.list_task:
            {
                Class fragmentClass;
                fragmentClass = my_emp.class;
                actToFragment(fragmentClass);
                break;
            }
            case R.id.create_task:
            {
                Class fragmentClass;
                fragmentClass = CreateTask.class;
                actToFragment(fragmentClass);
                break;
            }

        }
    }

    private void actToFragment(Class fragmentClass){
        if(fragmentClass!=null) {
            try {
                Fragment myFragment = null;
                myFragment = (Fragment) fragmentClass.newInstance();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                myFragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.content_frame, myFragment).addToBackStack(null).commit();
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
