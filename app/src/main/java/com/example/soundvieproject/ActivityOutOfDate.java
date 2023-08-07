package com.example.soundvieproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.soundvieproject.DB.Helper;
import com.example.soundvieproject.model.Payment;

import org.bson.types.ObjectId;

import java.util.Calendar;
import java.util.Date;

import io.realm.mongodb.App;
import io.realm.mongodb.mongo.result.InsertOneResult;

public class ActivityOutOfDate extends AppCompatActivity {

    Button btnRenew, btnCancel;
    Helper h = Helper.INSTANCE;

    Payment p;

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_of_date);

        btnRenew = findViewById(R.id.btnRenew);
        btnCancel = findViewById(R.id.btnCancelPremium);

        Bundle getData = getIntent().getExtras();

        String idUserSigned = getData.getString("idUserSignedPayment");

        btnRenew.setOnClickListener(v -> {
//            h.updatePayment(idUserSigned, result -> {
//                if(result.isSuccess()){
//                    Log.d("Update payment", "Thành công");
//
//                }
//                else
//                    Log.d("Update payment", "Thất bại");
//            });

            Intent i = new Intent(this, VisaActivity.class);
            h.getPayment(new App.Callback<Payment>() {
                @Override
                public void onResult(App.Result<Payment> result) {
                    Bundle signal = new Bundle();
                    signal.putString("callback", "update");
                    signal.putString("_id", result.get().getIdGoi().toString());
                    signal.putString("method", "VISA");
                    i.putExtras(signal);
                    i.putExtras(signal);
                    startActivity(i);
                }
            });


        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                h.getPayment(result -> {
                    if (result.isSuccess()) {

                        Payment paymentCurrent = result.get();

                        Date ngayTT = paymentCurrent.getNgayTT();
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
                        Toast.makeText(ActivityOutOfDate.this, "Lỗi", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void ShowPopUp() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityOutOfDate.this);
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
    private void ShowPopUpConfirm(String idUser) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityOutOfDate.this);
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
                        Intent i = new Intent(ActivityOutOfDate.this, HomeActivity.class);
                        startActivity(i);
                        Toast.makeText(ActivityOutOfDate.this, "Hủy thành công!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ActivityOutOfDate.this, "Lỗi hủy gói premium", Toast.LENGTH_SHORT).show();
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
}