package com.example.nt118_appbookstores.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;


import com.denzcoskun.imageslider.models.SlideModel;
import com.example.nt118_appbookstores.R;
import com.example.nt118_appbookstores.Models.ProductModel;
import com.example.nt118_appbookstores.Adapters.ProductAdapter;
import com.example.nt118_appbookstores.Adapters.ProductAdapterRow;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Home extends Fragment {

    RecyclerView productSaleRec;

    RecyclerView productNewRec;
    RecyclerView productCommonRec;
    List<ProductModel> productModelList;
    List<ProductModel> productModelList2;
     ProductAdapter productAdapter;
    ProductAdapterRow productAdapterRow;

    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        db  = FirebaseFirestore.getInstance();

        ArrayList<SlideModel> imagelist = new ArrayList<>();

        imagelist.add(new SlideModel(R.drawable.bg_harrypotter, ScaleTypes.CENTER_CROP));
        imagelist.add(new SlideModel(R.drawable.bg_harrypotter, ScaleTypes.CENTER_CROP));
        imagelist.add(new SlideModel(R.drawable.bg_harrypotter, ScaleTypes.CENTER_CROP));

            // Tìm kiếm ImageSlider trong layout
            ImageSlider imageSlider = view.findViewById(R.id.product_image_slider);
            // Đặt danh sách ảnh vào ImageSlider
            imageSlider.setImageList(imagelist, ScaleTypes.CENTER_CROP);

        //insert data in recycler Sales
        productSaleRec = view.findViewById(R.id.product_sales);
        productSaleRec.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        productModelList = new ArrayList<>();
        productAdapter = new ProductAdapter(getActivity(),productModelList);
        productSaleRec.setAdapter(productAdapter);

        //search data in FireStore
        db.collection("Products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ProductModel productModel = document.toObject(ProductModel.class);
                                productModel.setId(document.getId());
                                productModelList.add(productModel);
                                productModelList2.add(productModel);
                                productAdapter.notifyDataSetChanged();
                                productAdapterRow.notifyDataSetChanged();

                            }
                        } else {
                            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //insert data in recycler Commons
        productNewRec = view.findViewById(R.id.product_news);
        productNewRec.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL, false));
        productNewRec.setAdapter(productAdapter);


        //insert data in recycler Commons
        productCommonRec = view.findViewById(R.id.product_commons);
        productCommonRec.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL, false));
        productModelList2 = new ArrayList<>();
        productAdapterRow = new ProductAdapterRow(getActivity(),productModelList2);
        productCommonRec.setAdapter(productAdapterRow);

        return view;
    }
}