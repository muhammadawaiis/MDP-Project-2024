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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mpdproject.IssueProducts;
import com.example.mpdproject.Models.CustomersModelClass;
import com.example.mpdproject.R;


import java.util.ArrayList;




public class SelectCustomersAdapter extends RecyclerView.Adapter<SelectCustomersAdapter.AdminOrdersAdapterViewHolder> implements Filterable {

  Context context;
  ArrayList<CustomersModelClass> OrdersList;
  ArrayList<CustomersModelClass> newArraylistFill;

    public SelectCustomersAdapter(Context context, ArrayList<CustomersModelClass> ordersList) {
        this.context = context;
        newArraylistFill = ordersList;
        OrdersList = new ArrayList<>(newArraylistFill);
    }

    @NonNull
    @Override
    public AdminOrdersAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.customers_recyclerview_sample,parent,false);
        return new AdminOrdersAdapterViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull AdminOrdersAdapterViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CustomersModelClass customer = OrdersList.get(position);
        if (customer != null) {
            //holder.ProductName.setText(OrdersList.get(position).getCustomerName());
            holder.ProductName.setText(customer.getCustomerName());


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent selectCustomer = new Intent(v.getContext(), IssueProducts.class);
                    selectCustomer.putExtra("CustomerName", OrdersList.get(position).getCustomerName());
                    selectCustomer.putExtra("CustomerEmail", OrdersList.get(position).getCustomerEmail());
                    selectCustomer.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try {
                        v.getContext().startActivity(selectCustomer);
                    }catch (Exception e){
                        e.printStackTrace();
                        // Handle the exception, e.g., show an error message
                        Toast.makeText(v.getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return OrdersList.size();
    }




    @Override
    public Filter getFilter() {
        return newsFilter;
    }

    private final Filter newsFilter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<CustomersModelClass> filteredNewsList=new ArrayList<>();
            if (constraint==null||constraint.length()==0){
                 filteredNewsList.addAll(newArraylistFill);

            }
            else {
                String filterPattern=constraint.toString().toLowerCase().trim();
                for (CustomersModelClass news:newArraylistFill){
                    if (news.getCustomerName().toLowerCase().contains(filterPattern))
                        filteredNewsList.add(news);

                }
            }
            FilterResults results=new FilterResults();
            results.values=filteredNewsList;
            results.count=filteredNewsList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            OrdersList.clear();
            OrdersList.addAll((ArrayList)results.values);
            notifyDataSetChanged();

        }
    };




    public static class AdminOrdersAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView ProductName;
        ImageView PicOptionMenuProducts;


        public AdminOrdersAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            ProductName=itemView.findViewById(R.id.customersNameList);
            PicOptionMenuProducts=itemView.findViewById(R.id.picOptionMenuCustomers);



        }
    }
}
