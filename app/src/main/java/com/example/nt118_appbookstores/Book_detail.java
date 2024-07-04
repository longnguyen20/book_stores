package com.example.nt118_appbookstores;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nt118_appbookstores.Fragments.Home;
import com.example.nt118_appbookstores.Models.ProductModel;
import com.example.nt118_appbookstores.Models.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.nt118_appbookstores.Models.CartModel;

public class Book_detail extends AppCompatActivity {

    private ImageView bookImage,btnback;
    LinearLayout btn_add_to_cart,add_like;
    private TextView bookName, bookAuth, bookStar, bookPrice, bookCate, bookDec;
    private Context context;
    String productId,book_img_url;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        bookImage = findViewById(R.id.detail_image);
        bookName = findViewById(R.id.detail_name);
        bookAuth = findViewById(R.id.detail_author);
        bookStar = findViewById(R.id.tb_star);
        bookPrice = findViewById(R.id.detail_price);
        bookCate = findViewById(R.id.detail_cate);
        bookDec = findViewById(R.id.detail_Dec);
        db = FirebaseFirestore.getInstance();

        btnback = findViewById(R.id.btn_back);
        btn_add_to_cart = findViewById(R.id.add_cart);
        add_like = findViewById(R.id.add_like);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("productId")) {
            productId = intent.getStringExtra("productId");
            // You can now use the productId to fetch and display detailed information about the product
            fetchBookDetails(productId);
        }
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
            }
        });
    }
    private void addToCart() {

        // You can customize the data you want to store in the car
        CartModel cartItem = new CartModel();
        cartItem.setId(productId);

        cartItem.setName(bookName.getText().toString());
        cartItem.setPrices(bookPrice.getText().toString());
        cartItem.setImg_url(book_img_url); // Assuming you have a way to get the image URL
        cartItem.setBook_quantity("1"); // You might want to adjust the quantity


        String userId = FirebaseAuth.getInstance().getUid();
        DocumentReference userRef = db.collection("Users").document(userId);
        // Store the cart item in Firestore
        userRef.collection("Cart")
                .add(cartItem)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(Book_detail.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firestore", "Error adding to cart", e);
                        Toast.makeText(Book_detail.this, "Error adding to Cart", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override
    public void onBackPressed() {
        // If the activity has a parent activity, navigate up to it
        if (getSupportActionBar() != null && getSupportActionBar().getNavigationMode() == ActionBar.NAVIGATION_MODE_STANDARD) {
            NavUtils.navigateUpFromSameTask(this);
        } else {
            // Otherwise, finish the activity to return to the previous activity
            super.onBackPressed();
        }
    }

    private void fetchBookDetails(String productId) {
        // Query Firestore for book details based on the product ID
        db.collection("Products")
                .document(productId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Parse book details and update the UI
                            ProductModel book = document.toObject(ProductModel.class);
                            if (book != null) {
                                displayBookDetails(book);
                            }
                        } else {
                            Toast.makeText(this, "Book not found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Error fetching book details", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void displayBookDetails(ProductModel book) {
        // Load the book details into UI elements
        Glide.with(this).load(book.getImg_url()).into(bookImage);
        book_img_url = book.getImg_url();
        bookName.setText(book.getName());
        bookAuth.setText(book.getAuthor());
        bookStar.setText(book.getRates());
        bookPrice.setText(book.getPrices());
        bookCate.setText(book.getCategory());
        bookDec.setText(book.getDescription());
    }
}