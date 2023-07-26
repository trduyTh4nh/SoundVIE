package com.example.soundvieproject.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.soundvieproject.DB.Helper;
import com.example.soundvieproject.R;
import com.example.soundvieproject.model.Payment;
import com.example.soundvieproject.model.Premium;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import io.realm.mongodb.App;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CurrentPremiumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurrentPremiumFragment extends Fragment {

    TextView tvPremiumPlan, tvPremiumPrice, tvExpire;
    Button btnCancel;
    Helper h = Helper.INSTANCE;
    public CurrentPremiumFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CurrentPremiumFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        // Inflate the layout for this fragment
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
        h.getPayment(new App.Callback<Payment>() {
            @Override
            public void onResult(App.Result<Payment> result) {
                if(result.isSuccess()){
                    p = result.get();

                    h.getPremium(p.getIdGoi().toString(), new App.Callback<Premium>() {
                        @Override
                        public void onResult(App.Result<Premium> result) {
                            if(result.isSuccess()){
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
}