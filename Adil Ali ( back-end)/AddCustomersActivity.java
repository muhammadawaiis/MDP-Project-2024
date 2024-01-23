package com.example.mpdproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mpdproject.databinding.ActivityAddCustomersBinding;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;


public class AddCustomersActivity extends AppCompatActivity {
    ActivityAddCustomersBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddCustomersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        binding.iconBackAddCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddCustomersActivity.this,CustomersActivity.class));
            }
        });

        //upload customer data to firebase database
        binding.iconSaveCustomersRick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.customerNameAdd.getText().toString().isEmpty()
                        || binding.customerPhoneAdd.getText().toString().isEmpty()
                        || binding.customerEmailAdd.getText().toString().isEmpty()) {
//                    binding.customerNameAdd.setError("Please enter name");
//                    binding.customerNameAdd.requestFocus();
//                    binding.customerPhoneAdd.setError("Please enter phone");
//                    binding.customerPhoneAdd.requestFocus();
//                    binding.customerEmailAdd.setError("Please enter email");
//                    binding.customerEmailAdd.requestFocus();
                    Toast.makeText(AddCustomersActivity.this, "Please enter name, phone, and email", Toast.LENGTH_LONG).show();
                    return;
                }
                else {
                    try {


                        HashMap<String, Object> hashMap = new HashMap<>();
                        String timeStampss = String.valueOf((new Date().getTime()));
                        hashMap.put("CustomerName", binding.customerNameAdd.getText().toString());
                        hashMap.put("CustomerEmail", binding.customerEmailAdd.getText().toString());
                        hashMap.put("CustomerPhone", binding.customerPhoneAdd.getText().toString());
                        hashMap.put("CustomerAddress", binding.customerAddressAdd.getText().toString());
                        hashMap.put("CustomerBankDetail", binding.customerBankDetailsAdd.getText().toString());
                        hashMap.put("CustomerDiscount", binding.customerDiscountAdd.getText().toString());
                        hashMap.put("CustomerNotes", binding.customerNotestAdd.getText().toString());
                        hashMap.put("CustomerTimeStamps", timeStampss);
                        FirebaseDatabase.getInstance().getReference().child("Customers_List").push().setValue(hashMap);
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(AddCustomersActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    // After successful addition of data
                    Toast.makeText(AddCustomersActivity.this, "Added Successfully", Toast.LENGTH_LONG).show();
                    binding.customerNameAdd.setText("");
                    binding.customerEmailAdd.setText("");
                    binding.customerPhoneAdd.setText("");
                    // Clear other fields as needed
                    //Toast.makeText(AddCustomersActivity.this, "Added Successfully", Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}