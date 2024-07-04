package com.example.nt118_appbookstores;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.annotation.SuppressLint;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.nt118_appbookstores.Fragments.Delivering;
import com.example.nt118_appbookstores.Fragments.Search;
import com.example.nt118_appbookstores.Fragments.Cart;
import com.example.nt118_appbookstores.Fragments.Home;
import com.example.nt118_appbookstores.Fragments.Profile;
import com.example.nt118_appbookstores.Fragments.books;
import com.example.nt118_appbookstores.databinding.ActivityMainBinding;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    String id;
    LinearLayout cart, home, search, books, user;
    ActivityMainBinding binding;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cart = findViewById(R.id.cart_ln);
        home = findViewById(R.id.home_ln);
        search = findViewById(R.id.search_ln);
        books = findViewById(R.id.book_ln);
        user = findViewById(R.id.user_ln);

        if (savedInstanceState == null) {
            // Kiểm tra xem Activity được tạo lần đầu tiên hay không để tránh thay đổi Fragment khi xoay màn hình
            replaceFragment(R.id.fragmentContainerView, new Home());
        }

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Khởi tạo và hiển thị Fragment khi LinearLayout được click
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, new Home())
                        .addToBackStack(null)
                        .commit();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Khởi tạo và hiển thị Fragment khi LinearLayout được click
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, new Search())
                        .addToBackStack(null)
                        .commit();
            }
        });
        books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Khởi tạo và hiển thị Fragment khi LinearLayout được click
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, new books())// để tạm thời thế chưa có frame book list
                        .addToBackStack(null)
                        .commit();
            }
        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Khởi tạo và hiển thị Fragment khi LinearLayout được click
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, new Cart())
                        .addToBackStack(null)
                        .commit();
            }
        });
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Khởi tạo và hiển thị Fragment khi LinearLayout được click
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, new Profile())
                        .addToBackStack(null)
                        .commit();
            }
        });

    }
    private void replaceFragment(int containerViewId, Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(containerViewId, fragment)
                .commit();
    }
}