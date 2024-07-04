package com.example.nt118_appbookstores.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nt118_appbookstores.Models.ProductModel;
import com.example.nt118_appbookstores.R;

import java.util.List;

public class ProductAdapterRow extends RecyclerView.Adapter<ProductAdapterRow.ViewHolder> {

    private Context context;
    private List<ProductModel> productModelList;

    public ProductAdapterRow(Context context, List<ProductModel> productModelList) {
        this.context = context;
        this.productModelList = productModelList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapterRow.ViewHolder holder, int position) {
        Glide.with(context).load(productModelList.get(position).getImg_url()).into(holder.img);
        holder.name.setText(productModelList.get(position).getName());
        holder.author.setText(productModelList.get(position).getAuthor());
        holder.rates.setText(productModelList.get(position).getRates());
        holder.price.setText(productModelList.get(position).getPrices());
    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView img;
        TextView name, author, rates, price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.product_img);
            name = itemView.findViewById(R.id.product_name);
            author = itemView.findViewById(R.id.product_author);
            rates = itemView.findViewById(R.id.product_rates);
            price = itemView.findViewById(R.id.product_prices);
        }
    }

}
