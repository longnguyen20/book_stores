package com.example.nt118_appbookstores;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nt118_appbookstores.Adapters.CartAdapter;
import com.example.nt118_appbookstores.Fragments.Cart;
import com.example.nt118_appbookstores.Models.CartModel;
import com.example.nt118_appbookstores.Models.OrderModel;
import com.example.nt118_appbookstores.SuccessPayment;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import vn.momo.momo_partner.AppMoMoLib;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.LinearLayoutManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Orders extends AppCompatActivity {
    private String amount = "10000";
    private String fee = "0";
    int environment = 0;//developer default
    private String merchantName = "NguyenVu";
    private String merchantCode = "MOMOC2IC20220510";
    private String merchantNameLabel = "Nhà cung cấp AppBookStore";
    private String description = "Mua hàng online";
    ConstraintLayout tv_order;

    RecyclerView cartProductRec;
    List<CartModel> cartModelList;
    CartAdapter cartAdapter;
    FirebaseFirestore firestore;
    TextView tvTotalNumber, tv_price,total_price;
    ImageView btn_back;
    int totalAmount = 0;
    CheckBox cb_nhanh,cb_tk,cb_momo,cb_nh;

    String address = "123 Hồ Chí Minh";
    String date = getCurrentDate();
    String delivery = "Giao hàng nhanh";
    String paymentStatus = "Đã thanh toán bằng Momo";
    String totalPrice = "10000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT);

        tv_order = findViewById(R.id.buynow);

        cb_nhanh = findViewById(R.id.cb_nhanh);
        cb_tk =findViewById(R.id.cb_tk);
        cb_momo = findViewById(R.id.cb_MoMo);
        cb_nh = findViewById(R.id.cb_nh);

        cb_nhanh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cb_tk.setChecked(false);
                }
            }
        });

        cb_tk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cb_nhanh.setChecked(false);
                }
            }
        });
        cb_momo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cb_nh.setChecked(false);
                }
            }
        });

        cb_nh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cb_momo.setChecked(false);
                }
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String totalAmountString = extras.getString("total");
            totalAmount = Integer.parseInt(totalAmountString) - 10000;
        } else {

        }
        tv_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb_tk.isChecked())
                {
                    delivery = "Giao hàng tiết kiệm";
                }
                if(cb_momo.isChecked())
                {
                    requestPayment("12932198182");
                }
                else
                {
                    paymentStatus = "Chưa thanh toán";
                    OrderModel order = new OrderModel();
                    order.setAddress(address);
                    order.setDate(date);
                    order.setDelivery(delivery);
                    order.setPaymentStatus(paymentStatus);
                    order.setTotalPrice(totalPrice);

                    // Cập nhật thông tin đơn hàng lên Firestore
                    updateOrderToFirestore(order);
                    Intent intent = new Intent(Orders.this, SuccessPayment.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

        firestore = FirebaseFirestore.getInstance();
        cartProductRec = findViewById(R.id.order_product_item);
        cartProductRec.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        cartModelList = new ArrayList<>();
        cartAdapter = new CartAdapter(this, cartModelList);
        cartProductRec.setAdapter(cartAdapter);
        tvTotalNumber = findViewById(R.id.tv_total_number);
        tv_price = findViewById(R.id.price);
        total_price = findViewById(R.id.total_price);

        btn_back = findViewById(R.id.btn_back);


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
                            Toast.makeText(Orders.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        btn_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, new Cart())
                        .addToBackStack(null)
                        .commit();
            }
        });

        cartAdapter.setOnCartItemDeleteListener(new CartAdapter.OnCartItemDeleteListener() {
            @Override
            public void onCartItemDelete(int position) {
                deleteCartItem(position);
            }
        });
    }
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
        totalPrice = String.valueOf(totalAmount-10000);
        tv_price.setText(String.format(Locale.getDefault(),"%,dđ", totalAmount));
        tvTotalNumber.setText(String.format(Locale.getDefault(), "%,dđ", totalAmount-10000));
        total_price.setText(String.format(Locale.getDefault(), "%,dđ", totalAmount-10000));
        amount = String.valueOf(totalAmount-10000);
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
                            Toast.makeText(Orders.this, "Failed to delete item", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        // Định dạng ngày theo ý muốn
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(currentDate);
    }

    private void requestPayment(String iddonhang) {
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);


        Map<String, Object> eventValue = new HashMap<>();
        //client Required
        eventValue.put("merchantname", merchantName); //Tên đối tác. được đăng ký tại https://business.momo.vn. VD: Google, Apple, Tiki , CGV Cinemas
        eventValue.put("merchantcode", merchantCode); //Mã đối tác, được cung cấp bởi MoMo tại https://business.momo.vn
        eventValue.put("amount", amount); //Kiểu integer
        eventValue.put("orderId", iddonhang); //uniqueue id cho Bill order, giá trị duy nhất cho mỗi đơn hàng
        eventValue.put("orderLabel", iddonhang); //gán nhãn

        //client Optional - bill info
        eventValue.put("merchantnamelabel", "Dịch vụ");//gán nhãn
        eventValue.put("fee", fee); //Kiểu integer
        eventValue.put("description", description); //mô tả đơn hàng - short description

        //client extra data
        eventValue.put("requestId", merchantCode + "merchant_billId_" + System.currentTimeMillis());
        eventValue.put("partnerCode", merchantCode);
        //Example extra data
        JSONObject objExtraData = new JSONObject();
        try {
            objExtraData.put("site_code", "008");
            objExtraData.put("site_name", "CGV Cresent Mall");
            objExtraData.put("screen_code", 0);
            objExtraData.put("screen_name", "Special");
            objExtraData.put("movie_name", "Kẻ Trộm Mặt Trăng 3");
            objExtraData.put("movie_format", "2D");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        eventValue.put("extraData", objExtraData.toString());

        eventValue.put("extra", "");
        AppMoMoLib.getInstance().requestMoMoCallBack(this, eventValue);


    }

    //Get token callback from MoMo app an submit to server side
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
            if (data != null) {
                if (data.getIntExtra("status", -1) == 0) {
                    //TOKEN IS AVAILABLE
                    Log.d("Thanh toán thành công", data.getStringExtra("message"));
                    String token = data.getStringExtra("data"); //Token response
                    String phoneNumber = data.getStringExtra("phonenumber");
                    String env = data.getStringExtra("env");
                    if (env == null) {
                        env = "app";
                    }


                    if (token != null && !token.equals("")) {
                        // Tạo một đối tượng Order
                        OrderModel order = new OrderModel();
                        order.setAddress(address);
                        order.setDate(date);
                        order.setDelivery(delivery);
                        order.setPaymentStatus(paymentStatus);
                        order.setTotalPrice(totalPrice);

                        // Cập nhật thông tin đơn hàng lên Firestore
                        updateOrderToFirestore(order);
                        Intent intent = new Intent(Orders.this, SuccessPayment.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Log.d("Thanh toán thành công", "không thành công");
                    }
                } else if (data.getIntExtra("status", -1) == 1) {
                    //TOKEN FAIL
                    String message = data.getStringExtra("message") != null ? data.getStringExtra("message") : "Thất bại";
                    Log.d("Thanh toán thành công", "không thành công");
                } else if (data.getIntExtra("status", -1) == 2) {
                    //TOKEN FAIL
                    Log.d("Thanh toán thành công", "không thành công");
                } else {
                    //TOKEN FAIL
                    Log.d("Thanh toán thành công", "không thành công");
                }
            } else {
                Log.d("Thanh toán thành công", "không thành công");
            }
        } else {
            Log.d("Thanh toán thành công", "không thành công");
        }
    }

    private void updateOrderToFirestore(OrderModel order) {
        // Kết nối đến Firebase Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Thực hiện cập nhật đơn hàng lên Firestore
        String userId = FirebaseAuth.getInstance().getUid();
        DocumentReference userRef = db.collection("Users").document(userId);
        // Store the cart item in Firestore
        userRef.collection("Order")
                .add(order)
                .addOnSuccessListener(documentReference -> Log.d("Firestore", "Đơn hàng đã được cập nhật thành công"))
                .addOnFailureListener(e -> Log.w("Firestore", "Lỗi khi cập nhật đơn hàng", e));
    }
}
