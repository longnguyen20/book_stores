package com.example.nt118_appbookstores.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.nt118_appbookstores.Address;
import com.example.nt118_appbookstores.ChangePass;
import com.example.nt118_appbookstores.Edit_profile;
import com.example.nt118_appbookstores.LoginActivity;
import com.example.nt118_appbookstores.Notifications;
import com.example.nt118_appbookstores.Order_management;
import com.example.nt118_appbookstores.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends Fragment {

    Button editProfile, editAddress, editPass, his_order, notification, logout;
    FirebaseAuth auth;

    FirebaseDatabase db;

    DatabaseReference ref;

    TextView user_name;

    CircleImageView profileImg;
    public Profile() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        editProfile = view.findViewById(R.id.btn_editProfile);
        editAddress = view.findViewById(R.id.btn_address);
        editPass = view.findViewById(R.id.btn_changePass);
        his_order = view.findViewById(R.id.btn_historyOrder);
        notification = view.findViewById(R.id.btn_notification);
        logout = view.findViewById(R.id.btn_logout);


        editProfile.setOnClickListener(view1 -> {
            Intent intent = new Intent(view.getContext(), Edit_profile.class);
            startActivity(intent);
        });

        editAddress.setOnClickListener(view2 -> {
            Intent intent = new Intent(view.getContext(), Address.class);
            startActivity(intent);
        });

        editPass.setOnClickListener(view3 -> {
            Intent intent = new Intent(view.getContext(), ChangePass.class);
            startActivity(intent);
        });

        his_order.setOnClickListener(view4 -> {
            Intent intent = new Intent(view.getContext(), Order_management.class);
            startActivity(intent);
        });

        editPass.setOnClickListener(view5 -> {
            Intent intent = new Intent(view.getContext(), ChangePass.class);
            startActivity(intent);
        });

        notification.setOnClickListener(view6 -> {
            Intent intent = new Intent(view.getContext(), Notifications.class);
            startActivity(intent);
        });

        logout.setOnClickListener(vierw7 ->{
            Intent intent = new Intent(view.getContext(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        return view;


    }
}