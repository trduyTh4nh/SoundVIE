package com.example.soundvieproject.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.soundvieproject.ActivityOutOfDate;
import com.example.soundvieproject.DB.Helper;
import com.example.soundvieproject.HomeActivity;
import com.example.soundvieproject.R;
import com.example.soundvieproject.model.Payment;
import com.example.soundvieproject.model.Premium;

import org.bson.types.ObjectId;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import io.realm.mongodb.App;


public class CurrentPremiumFragment extends Fragment {

    TextView tvPremiumPlan, tvPremiumPrice, tvExpire;
    Button btnCancel;
    Helper h = Helper.INSTANCE;

    public CurrentPremiumFragment() {

    }


    public static CurrentPremiumFragment newInstance(String param1, String param2) {
        CurrentPremiumFragment fragment = new CurrentPremiumFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_current_premium, container, false);
    }

    Payment p;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvPremiumPlan = view.findViewById(R.id.tvPremiumPlan);
        tvPremiumPrice = view.findViewById(R.id.tvPremiumPrice);
        tvExpire = view.findViewById(R.id.tvPremiumExpire);
        btnCancel = view.findViewById(R.id.btnCancel);

        h.getPayment(result -> {
            if(result.isSuccess()){
                Payment payment = result.get();

                Date payDate = payment.getNgayTT();
                Calendar calendarTT = Calendar.getInstance();

                long payMilisecond = calendarTT.getTimeInMillis();

                Calendar nextMonth = Calendar.getInstance();
                nextMonth.setTime(payDate);
                nextMonth.add(Calendar.MONTH, 1);

                long millisecondsInOneMonth = (nextMonth.getTimeInMillis() - calendarTT.getTimeInMillis());

                Log.d("Thời gian tháng sau", String.valueOf(millisecondsInOneMonth));

                if(millisecondsInOneMonth < 0){
                    Intent i = new Intent(getActivity(), ActivityOutOfDate.class);
                    Bundle transferData = new Bundle();
                    transferData.putString("idUserSignedPayment", payment.getIdUser());
                    i.putExtras(transferData);
                    startActivity(i);

                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                h.getPayment(result -> {
                    if (result.isSuccess()) {

                        Payment paymentCurrent = result.get();

                        Date ngayTT = paymentCurrent.getNgayTT();
                        Log.d("Ngày Thanh toán", String.valueOf(ngayTT));
                        Calendar calTT = Calendar.getInstance();
                        calTT.setTime(ngayTT);
                        long ttMilisecond = calTT.getTimeInMillis();

                        Calendar current = Calendar.getInstance();

                        long curMillisecond = current.getTimeInMillis();


                        if (ttMilisecond > curMillisecond) {
                            // không hủy được do lỗi
                            ShowPopUp();

                        } else {
                            // dialog xác nhận hủy
                            ShowPopUpConfirm(paymentCurrent.getIdUser());


                        }
                    } else {
                        Toast.makeText(getContext().getApplicationContext(), "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        h.getPayment(new App.Callback<Payment>() {
            @Override
            public void onResult(App.Result<Payment> result) {
                if (result.isSuccess()) {
                    p = result.get();

                    h.getPremium(p.getIdGoi().toString(), new App.Callback<Premium>() {
                        @Override
                        public void onResult(App.Result<Premium> result) {
                            if (result.isSuccess()) {
                                Premium pre = result.get();
                                tvPremiumPlan.setText(pre.getTenLoai());
                                double amount = pre.getGia();
                                DecimalFormat format = new DecimalFormat("#,###");
                                tvPremiumPrice.setText(format.format(amount) + " VND / " + (pre.getTenLoai().equals("Premium hàng tháng") ? "Tháng" : "Năm"));
                                Date regis = p.getNgayTT();
                                Calendar c = Calendar.getInstance();
                                c.setTime(regis);
                                c.add(Calendar.DATE, pre.getThoiHan());
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                sdf.setTimeZone(TimeZone.getTimeZone("GMT+7"));
                                Date date = c.getTime();
                                String f = sdf.format(date);
                                tvExpire.setText("Làm mới vào ngày " + f + ".");
                            }
                        }
                    });
                }
            }
        });
    }

    private void ShowPopUpConfirm(String idUser) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_confirm, null);

        builder.setView(dialogView);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button btnOK = dialogView.findViewById(R.id.dialog_button_positive);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button btnCancel = dialogView.findViewById(R.id.dialog_button_negative);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                h.removePremium(idUser, result -> {
                    if (result.isSuccess()) {
                        Toast.makeText(getActivity(), "Hủy thành công!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getActivity(), HomeActivity.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(getActivity(), "Lỗi hủy gói premium", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private void ShowPopUp() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_layout_error, null);

        builder.setView(dialogView);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button btnOK = dialogView.findViewById(R.id.dialog_button_positive);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button btnCancel = dialogView.findViewById(R.id.dialog_button_negative);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_DATE_SETTINGS);
                startActivity(intent);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
}