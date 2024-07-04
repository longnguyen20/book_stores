package com.example.nt118_appbookstores.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nt118_appbookstores.Book_detail;
import com.example.nt118_appbookstores.R;
import com.example.nt118_appbookstores.Models.ProductModel;
import java.util.List;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.VỉewHoder> {

    private Context context;
    private List<ProductModel> productModelList;

    public ProductAdapter(Context context, List<ProductModel> productModelList) {
        this.context = context;
        this.productModelList = productModelList;
    }



    @NonNull
    @Override
    public VỉewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new VỉewHoder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_books, parent ,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VỉewHoder holder, int position) {
        Glide.with(context).load(productModelList.get(position).getImg_url()).into(holder.img);
        holder.name.setText(productModelList.get(position).getName());
        holder.author.setText(productModelList.get(position).getAuthor());
        holder.rates.setText(productModelList.get(position).getRates());
        holder.price.setText(productModelList.get(position).getPrices());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the Book_detail activity with details of the clicked item
                int clickedPosition = holder.getAdapterPosition();
                if (clickedPosition != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(context, Book_detail.class);
                    intent.putExtra("productId", productModelList.get(clickedPosition).getId()); // Pass any necessary data
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {

        return productModelList.size();
    }

    public class VỉewHoder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView name, author, rates, price;
        LinearLayout book_detail;
        public VỉewHoder(@NonNull View itemView) {
            super(itemView);
            book_detail = itemView.findViewById(R.id.detail);
            img = itemView.findViewById(R.id.product_img);
            name = itemView.findViewById(R.id.product_name);
            author = itemView.findViewById(R.id.product_author);
            rates = itemView.findViewById(R.id.product_rates);
            price = itemView.findViewById(R.id.product_prices);
        }
    }
}
