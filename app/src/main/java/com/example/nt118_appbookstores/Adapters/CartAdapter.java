package com.example.nt118_appbookstores.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nt118_appbookstores.Models.CartModel;
import com.example.nt118_appbookstores.R;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>{
    Context context;
    private List<CartModel> cartItems;

    public CartAdapter(Context context, List<CartModel> cartModelList) {
        this.context = context;
        this.cartItems = cartModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your cart item layout here
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(cartItems.get(position).getImg_url()).into(holder.img);
        holder.name.setText(cartItems.get(position).getName());
        holder.price.setText(cartItems.get(position).getPrices());
        holder.quantity.setText(cartItems.get(position).getBook_quantity());
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }



    // Interface lắng nghe sự kiện xóa sản phẩm
    public interface OnCartItemDeleteListener {
        void onCartItemDelete(int position);
    }

    // Biến để lưu trữ listener
    private static OnCartItemDeleteListener onCartItemDeleteListener;

    // Phương thức thiết lập listener
    public void setOnCartItemDeleteListener(OnCartItemDeleteListener listener) {
        this.onCartItemDeleteListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img, btn_closed;
        TextView name, price, quantity;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.image_view);
            name = itemView.findViewById(R.id.tv_name);
            price = itemView.findViewById(R.id.tv_price);
            quantity = itemView.findViewById(R.id.tv_quantity);
            btn_closed = itemView.findViewById(R.id.iv_closed);
            btn_closed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && onCartItemDeleteListener != null) {
                        onCartItemDeleteListener.onCartItemDelete(position);
                }
            }});
        }
    }
}