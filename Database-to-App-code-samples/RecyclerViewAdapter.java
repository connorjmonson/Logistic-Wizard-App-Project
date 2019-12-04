package com.example.jeremy.logisticwizard;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.content.Context;
import android.content.Intent;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private List<workorder_info> workOrders;
    private Context context;

    // Constructor for the adapter
    public RecyclerViewAdapter(Context context, List<workorder_info> workOrders) {
        this.workOrders = workOrders;
        this.context = context;
    }

    // Used to create new views
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace contents of a view
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        workorder_info workorder = workOrders.get(position);
        holder.textView.setText(workorder.order_title);
        holder.priority.setText(workorder.order_priority);
        holder.date.setText(workorder.order_dates);
        holder.creator.setText(workorder.order_creator);
        holder.progress.setText(workorder.order_status);
    }

    // Return size of dataset
    @Override
    public int getItemCount() {
        return workOrders.size();
    }


    // Provide a reference to the views for each data item
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public TextView priority;
        public TextView date;
        public TextView creator;
        public TextView progress;
        public MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.Title);
            priority = itemView.findViewById(R.id.priority);
            date = itemView.findViewById(R.id.date);
            creator = itemView.findViewById(R.id.creator);
            progress = itemView.findViewById(R.id.progress);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context c = view.getContext();
                    Intent order_intent = new Intent(c, workorder_view.class);
                    order_intent.putExtra("orderTitle", textView.getText());


                    c.startActivity(order_intent);
                }
            });
        }
    }
}
