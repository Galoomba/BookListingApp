package com.example.starhood.booklistingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Starhood on 6/9/17.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.Data> {
    private ArrayList<BookData> dataList;
    private Context context;

    public DataAdapter(ArrayList<BookData> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    public void addAll(List list) {
        dataList.addAll(list);
    }

    public void clear() {
        int size = this.dataList.size();
        this.dataList.clear();
        notifyItemRangeRemoved(0, size);
    }

    public BookData getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public DataAdapter.Data onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item, parent, false);


        DataAdapter.Data viewHolder = new DataAdapter.Data(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DataAdapter.Data holder, int position) {

        BookData data = dataList.get(position);
        holder.name.setText(data.getBookName());
        holder.writer.setText(data.getBookWriter());
        holder.description.setText(data.getBookDetails());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class Data extends RecyclerView.ViewHolder {
        protected TextView name;
        protected TextView writer;
        protected TextView description;

        public Data(View itemView) {
            super(itemView);

            this.name = (TextView) itemView.findViewById(R.id.RBookTitle);
            this.writer = (TextView) itemView.findViewById(R.id.RWriter);
            this.description = (TextView) itemView.findViewById(R.id.RDesc);
        }
    }
}
