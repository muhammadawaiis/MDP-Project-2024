package com.example.mpdproject.Adapters;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mpdproject.Models.SuppliersModelClass;
import com.example.mpdproject.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class SuppliersAdapter extends RecyclerView.Adapter<SuppliersAdapter.SuppliersViewHolder> implements Filterable {

    Context context;
    ArrayList<SuppliersModelClass> suppliersList;
    ArrayList<SuppliersModelClass> newArraylistFill;

    private static final String SUPPLIERS_LIST = "Suppliers_List";

    public SuppliersAdapter(Context context, ArrayList<SuppliersModelClass> suppliersList) {
        this.context = context;
        newArraylistFill = suppliersList;
        this.suppliersList = new ArrayList<>(newArraylistFill);
    }

    @NonNull
    @Override
    public SuppliersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.customers_recyclerview_sample, parent, false);
        return new SuppliersViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SuppliersViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.ProductName.setText(suppliersList.get(position).getSupplierName());

        holder.ProductName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Highlight: Separate dialog creation logic
                showUpdateDialog(view.getContext(), suppliersList.get(position));
            }
        });

        holder.PicOptionMenuProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.ProductName.getContext());
                builder.setTitle("Select Option");
                builder.setMessage("Select any option to perform action");
                builder.setCancelable(true);
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                        Query applesQuery = ref.child(SUPPLIERS_LIST).orderByChild("SupplierTimeStamps")
                                .equalTo(suppliersList.get(position).getSupplierTimeStamps());

                        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                    appleSnapshot.getRef().removeValue();
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(context, "Error: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                                Log.e(TAG, "onCancelled", databaseError.toException());
                            }
                        });
                    }
                });
                builder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Highlight: Separate dialog creation logic
                        showUpdateDialog(view.getContext(), suppliersList.get(position));
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return suppliersList.size();
    }

    @Override
    public Filter getFilter() {
        return newsFilter;
    }

    private final Filter newsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<SuppliersModelClass> filteredSuppliersList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredSuppliersList.addAll(newArraylistFill);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (SuppliersModelClass supplier : newArraylistFill) {
                    if (supplier.getSupplierName().toLowerCase().contains(filterPattern)) {
                        filteredSuppliersList.add(supplier);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredSuppliersList;
            results.count = filteredSuppliersList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            suppliersList.clear();
            suppliersList.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

    // Highlight: ViewHolder naming convention
    public static class SuppliersViewHolder extends RecyclerView.ViewHolder {
        TextView ProductName;
        ImageView PicOptionMenuProducts;

        public SuppliersViewHolder(@NonNull View itemView) {
            super(itemView);
            ProductName = itemView.findViewById(R.id.customersNameList);
            PicOptionMenuProducts = itemView.findViewById(R.id.picOptionMenuCustomers);
        }
    }

    // Highlight: Dialog management for updating supplier information
    private void showUpdateDialog(Context context, SuppliersModelClass supplier) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.update_customer_sample_dialog);
        dialog.show();
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        EditText CName = dialog.findViewById(R.id.customerNameUpdate);
        EditText CEmail = dialog.findViewById(R.id.customerEmailUpdate);
        EditText CPhone = dialog.findViewById(R.id.customerPhoneUpdate);
        EditText CAddress = dialog.findViewById(R.id.customerAddressUpdate);
        EditText CBank = dialog.findViewById(R.id.customerBankDetailsUpdate);
        EditText CDiscount = dialog.findViewById(R.id.customerDiscountUpdate);
        EditText CNotes = dialog.findViewById(R.id.customerNotestUpdate);
        ImageView backArrow = dialog.findViewById(R.id.iconBackUpdateCustomer);
        ImageView actionUpdate = dialog.findViewById(R.id.iconSaveCustomersRickUpdate);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // Highlight: Check for null before accessing UI elements
        if (CName != null && CEmail != null && CPhone != null && CAddress != null && CBank != null
                && CDiscount != null && CNotes != null) {
            CName.setText(supplier.getSupplierName());
            CEmail.setText(supplier.getSupplierEmail());
            CPhone.setText(supplier.getSupplierPhone());
            CAddress.setText(supplier.getSupplierAddress());
            CBank.setText(supplier.getSupplierBankDetail());
            CDiscount.setText(supplier.getSupplierDiscount());
            CNotes.setText(supplier.getSupplierNotes());
        }

        actionUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Highlight: Ensure dialog dismissal
                dialog.dismiss();

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("SupplierName", CName.getText().toString());
                hashMap.put("SupplierEmail", CEmail.getText().toString());
                hashMap.put("SupplierPhone", CPhone.getText().toString());
                hashMap.put("SupplierAddress", CAddress.getText().toString());
                hashMap.put("SupplierBankDetail", CBank.getText().toString());
                hashMap.put("SupplierDiscount", CDiscount.getText().toString());
                hashMap.put("SupplierNotes", CNotes.getText().toString());

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                Query suppliersQuery = ref.child(SUPPLIERS_LIST).orderByChild("SupplierTimeStamps")
                        .equalTo(supplier.getSupplierTimeStamps());

                suppliersQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot supplierSnapshot : dataSnapshot.getChildren()) {
                            supplierSnapshot.getRef().updateChildren(hashMap);
                            // Highlight: Snackbar provides better UI integration for messages
                            Snackbar.make(v, "Updated Successfully", Snackbar.LENGTH_LONG).show();
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Highlight: Snackbar provides better UI integration for error messages
                        Snackbar.make(v, "Error: " + databaseError.getMessage(), Snackbar.LENGTH_LONG).show();
                        Log.e(TAG, "onCancelled", databaseError.toException());
                    }
                });
            }
        });
    }
}
