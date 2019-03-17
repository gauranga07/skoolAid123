package com.example.kyle.uwitimemanagemeent;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class MyRecyclerViewAdapter2 extends RecyclerView
        .Adapter<MyRecyclerViewAdapter2
        .DataObjectHolder> {

    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<SchoolCard> mDataset;
    private static MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener{
        ImageView label;
        TextView title;
        TextView s;
        TextView i;

        public DataObjectHolder(View itemView) {
            super(itemView);
            label = (ImageView) itemView.findViewById(R.id.imageViewlogo);
            title = (TextView) itemView.findViewById(R.id.title_text);
            s = (TextView) itemView.findViewById(R.id.sDate);
            i = (TextView) itemView.findViewById(R.id.id);
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }

    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public MyRecyclerViewAdapter2(ArrayList<SchoolCard> myDataset) {
        mDataset = myDataset;
    }

    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item2, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.label.setImageResource(mDataset.get(position).photoId);
        holder.title.setText(mDataset.get(position).Title);
        holder.i.setText(mDataset.get(position).ID);
        holder.s.setText(mDataset.get(position).Startd);
    }

    public void addItem(SchoolCard dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}
