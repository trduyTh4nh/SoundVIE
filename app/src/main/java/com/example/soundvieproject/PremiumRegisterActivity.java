package com.example.soundvieproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

public class PremiumRegisterActivity extends AppCompatActivity {
    LinearLayout monthly, visa, paypal;
    LinearLayout yearly;
    CheckBox monthlyRadio, yearlyRadio, radiovisa, radiopaypal;
    Button btnNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium_register);
        btnNext = findViewById(R.id.btnContinue);
        monthly = findViewById(R.id.monthly);
        yearly = findViewById(R.id.yearly);
        monthlyRadio = findViewById(R.id.radioMonthly);
        yearlyRadio = findViewById(R.id.radioYearly);
        visa = findViewById(R.id.visa);
        paypal = findViewById(R.id.paypal);
        radiovisa = findViewById(R.id.radivisa);
        radiopaypal = findViewById(R.id.radiopaypal);
        monthlyRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    yearlyRadio.setChecked(false);
                }
            }
        });
        yearlyRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    monthlyRadio.setChecked(false);
                }
            }
        });
        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monthlyRadio.setChecked(true);
            }
        });
        yearly.setOnClickListener(v -> {
            yearlyRadio.setChecked(true);
        });
        radiopaypal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    radiovisa.setChecked(false);
                }
            }
        });
        radiovisa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    radiopaypal.setChecked(false);
                }
            }
        });
        visa.setOnClickListener(v -> {
            radiovisa.setChecked(true);
        });
        paypal.setOnClickListener(v -> {
            radiopaypal.setChecked(true);
        });
        btnNext.setOnClickListener(v -> {
            if(radiovisa.isChecked()){
                Intent i = new Intent(PremiumRegisterActivity.this, VisaActivity.class);
                Bundle b = new Bundle();
                b.putString("_id", monthlyRadio.isChecked() ? "64b502e2fcded49715f78db3" : "64b506c1fcded49715f8e27f");
                b.putString("method", "VISA");
                i.putExtras(b);
                startActivity(i);
            } else {
                Toast.makeText(this, "Not yet implemented", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.btnCancel).setOnClickListener(v -> {finish();});
    }

}