package com.example.mpdproject.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mpdproject.Models.BillingModel;
import com.example.mpdproject.R;

import java.util.ArrayList;




public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.AdminOrdersAdapterViewHolder> implements Filterable {

  Context context;
  ArrayList<BillingModel> OrdersList;
  ArrayList<BillingModel> newArraylistFill;

    public ReportsAdapter(Context context, ArrayList<BillingModel> ordersList) {
        this.context = context;
        newArraylistFill = ordersList;
        OrdersList = new ArrayList<>(newArraylistFill);
    }

    @NonNull
    @Override
    public AdminOrdersAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.reports_detail_recycler_sample,parent,false);
        return new AdminOrdersAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminOrdersAdapterViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.ProductName.setText(OrdersList.get(position).getProductName());
        holder.Name.setText(OrdersList.get(position).getName());
        holder.Price.setText(OrdersList.get(position).getProductTotal_Price());
        holder.Quantity.setText(OrdersList.get(position).getProductQuantity());
        holder.Status.setText(OrdersList.get(position).getStatus());
        holder.Remainig.setText(OrdersList.get(position).getRemaining());





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
            ArrayList<BillingModel> filteredNewsList=new ArrayList<>();
            if (constraint==null||constraint.length()==0){
                 filteredNewsList.addAll(newArraylistFill);
            }
            else {
                String filterPattern=constraint.toString().toLowerCase().trim();
                for (BillingModel news:newArraylistFill){
                    // Ensure that the necessary fields are not null before performing comparisons
                    if (news.getProductName() != null && news.getProductPrice() != null) {
                        if (news.getProductName().toLowerCase().contains(filterPattern)
                                || news.getProductPrice().toLowerCase().contains(filterPattern)) {
                            filteredNewsList.add(news);
                        }
                    }
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
        TextView Name,ProductName,Price,Quantity,Date,Remainig,Status;



        public AdminOrdersAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            ProductName=itemView.findViewById(R.id.reportDetailProductName);
            Name=itemView.findViewById(R.id.reportDetailName);
            Price=itemView.findViewById(R.id.reportDetailPrice);
            Quantity=itemView.findViewById(R.id.reportDetailQuantity);
            Remainig=itemView.findViewById(R.id.reportDetailRemainingPayment);
            Status=itemView.findViewById(R.id.reportDetailPaymentStatus);





        }
    }
}
