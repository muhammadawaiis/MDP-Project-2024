package com.example.mpdproject.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mpdproject.Models.SuppliersModelClass;
import com.example.mpdproject.R;
import com.example.mpdproject.ReceivedProduct;

import java.util.ArrayList;



public class SelectSuppliersAdapter extends RecyclerView.Adapter<SelectSuppliersAdapter.SuppliersViewHolder> implements Filterable {

  Context context;
  ArrayList<SuppliersModelClass> suppliersList;
  ArrayList<SuppliersModelClass> newArraylistFill;

    public SelectSuppliersAdapter(Context context, ArrayList<SuppliersModelClass> ordersList) {
        this.context = context;
        newArraylistFill = suppliersList;
        this.suppliersList = new ArrayList<>(newArraylistFill);
    }

    @NonNull
    @Override
    public SuppliersViewHolder  onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.customers_recyclerview_sample,parent,false);
        return new SuppliersViewHolder (v);
    }

    @Override
    public void onBindViewHolder(@NonNull SuppliersViewHolder  holder, @SuppressLint("RecyclerView") int position) {
        holder.supplierName.setText(suppliersList.get(position).getSupplierName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selectSupplier =new Intent(v.getContext(), ReceivedProduct.class);
                selectSupplier.putExtra("SupplierName",suppliersList.get(position).getSupplierName());
                selectSupplier.putExtra("SupplierEmail",suppliersList.get(position).getSupplierEmail());
                selectSupplier.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(selectSupplier);
            }
        });


    }

    @Override
    public int getItemCount()
    {
        return suppliersList.size();
    }




    @Override
    public Filter getFilter() {
        return newsFilter;
    }

    private final Filter newsFilter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<SuppliersModelClass> filteredSuppliersList =new ArrayList<>();
            if (constraint==null||constraint.length()==0){
                filteredSuppliersList.addAll(newArraylistFill);

            }
            else {
                String filterPattern=constraint.toString().toLowerCase().trim();
                for (SuppliersModelClass supplier :newArraylistFill){
                    if (supplier.getSupplierName().toLowerCase().contains(filterPattern))
                        filteredSuppliersList.add(supplier);

                }
            }
            FilterResults results=new FilterResults();
            results.values=filteredSuppliersList;
            results.count=filteredSuppliersList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            suppliersList.clear();
            suppliersList.addAll((ArrayList)results.values);
            notifyDataSetChanged();

        }
    };




    public static class SuppliersViewHolder  extends RecyclerView.ViewHolder {
        TextView supplierName;
        ImageView picOptionMenuSuppliers;


        public SuppliersViewHolder (@NonNull View itemView) {
            super(itemView);
            supplierName =itemView.findViewById(R.id.customersNameList);
            picOptionMenuSuppliers =itemView.findViewById(R.id.picOptionMenuCustomers);


        }
    }
}
