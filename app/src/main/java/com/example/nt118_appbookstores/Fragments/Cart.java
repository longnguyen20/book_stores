package com.example.nt118_appbookstores.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nt118_appbookstores.Models.CartModel;
import com.example.nt118_appbookstores.Orders;
import com.example.nt118_appbookstores.R;
import com.example.nt118_appbookstores.Adapters.CartAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class Cart extends Fragment {

    RecyclerView cartProductRec;
    List<CartModel> cartModelList;
    CartAdapter cartAdapter;
    FirebaseFirestore firestore;
    TextView tvTotalNumber;

    ConstraintLayout buynow;
    int totalAmount = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        firestore = FirebaseFirestore.getInstance();
        cartProductRec = view.findViewById(R.id.cart_product_item);
        cartProductRec.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL, false));
        cartModelList= new ArrayList<>();
        cartAdapter = new CartAdapter(getActivity(),cartModelList);
        cartProductRec.setAdapter(cartAdapter);
        tvTotalNumber = view.findViewById(R.id.tv_total_number);
        buynow = view.findViewById(R.id.buynow);

        buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a Bundle to hold the data
                Bundle bundle = new Bundle();
                bundle.putString("total", String.valueOf(totalAmount));

                // Create an Intent with the data bundle
                Intent intent = new Intent(getActivity(), Orders.class);
                intent.putExtras(bundle);

                // Start the Activity with the Intent
                startActivity(intent);
            }
        });

        String userId = FirebaseAuth.getInstance().getUid();
        DocumentReference userRef = firestore.collection("Users").document(userId);
        userRef.collection("Cart")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CartModel cartModel = document.toObject(CartModel.class);
                                cartModel.setId(document.getId());
                                cartModelList.add(cartModel);
                                cartAdapter.notifyDataSetChanged();

                                calculateTotalAmount();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        cartAdapter.setOnCartItemDeleteListener(new CartAdapter.OnCartItemDeleteListener() {
            @Override
            public void onCartItemDelete(int position) {
                deleteCartItem(position);
            }
        });

        return view;

    }


    //Tính tổng tiền trong giỏ hàng
    private void calculateTotalAmount() {
        totalAmount = 0;
        for (CartModel cartItem : cartModelList) {
            String priceString = cartItem.getPrices();

            try {
                int price = Integer.parseInt(priceString.replaceAll("\\D+", "")); // Lấy chỉ số từ chuỗi số
                totalAmount += price;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        tvTotalNumber.setText(String.format(Locale.getDefault(), "%,dđ", totalAmount));
    }

    private void deleteCartItem(int position) {
        // Lấy thông tin sản phẩm cần xóa
        CartModel cartItemToDelete = cartModelList.get(position);

        // Thực hiện xóa sản phẩm khỏi Firestore
        String userId = FirebaseAuth.getInstance().getUid();
        DocumentReference cartItemRef = firestore.collection("Users")
                .document(userId)
                .collection("Cart")
                .document(cartItemToDelete.getId());

        cartItemRef.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Xóa sản phẩm khỏi danh sách và cập nhật lại RecyclerView
                            cartModelList.remove(position);
                            cartAdapter.notifyItemRemoved(position);

                            // Tính toán lại tổng giá tiền
                            calculateTotalAmount();
                        } else {
                            Toast.makeText(getActivity(), "Failed to delete item", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}