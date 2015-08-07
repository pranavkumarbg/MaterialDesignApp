package com.rhcloud.phpnew_pranavkumar.materialdesignapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by Pranav on 8/6/2015.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    List<Info> data= Collections.emptyList();

    public MyAdapter(Context context, List<Info> data)
    {

       inflater= LayoutInflater.from(context);
        this.data=data;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.custom_row,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Info current=data.get(position);
        holder.textView.setText(current.title);
        holder.imageView.setImageResource(current.iconId);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView;
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView= (TextView) itemView.findViewById(R.id.list_message);
            imageView= (ImageView) itemView.findViewById(R.id.list_icon);
        }
    }
}
