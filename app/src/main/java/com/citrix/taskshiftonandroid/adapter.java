package com.citrix.taskshiftonandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class adapter extends RecyclerView.Adapter<adapter.CardViewHolder> implements ItemTouchHelperAdapter {

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.test, viewGroup, false);
        CardViewHolder cvh = new CardViewHolder(v);
        return cvh;
    }

    @NonNull
    @Override
    public void onBindViewHolder(CardViewHolder cardViewHolder, int i) {
        cardViewHolder.taskId.setText(Items.get(i).taskid);
        cardViewHolder.taskSummary.setText(Items.get(i).storySummary);
        cardViewHolder.jira.setImageResource(Items.get(i).jiraLogo);
        cardViewHolder.storyType.setImageResource(Items.get(i).storyType);
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return Items.size();
    }

    List<Item> Items;

    //try to implement interface (listener)

    @Override
    public void onItemDismiss(int position) {
        Items.remove(position);
        notifyItemRemoved(position);
    }
    @Override
    public void onItemMove(int from, int to) {
        Collections.swap(Items, from, to);
        notifyItemMoved(from, to);
    }
    //interface above



    adapter(List<Item> Items){
        this.Items = Items;
    }
    public static class CardViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView taskId;
        TextView taskSummary;
        ImageView jira;
        ImageView storyType;

        CardViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.card);
            taskId = (TextView)itemView.findViewById(R.id.TS);
            taskSummary = (TextView)itemView.findViewById(R.id.summary);
            jira = (ImageView)itemView.findViewById(R.id.jira);
            storyType = (ImageView)itemView.findViewById(R.id.storyType);
        }
    }

    //for add item
    //public void remove(int position){
    //    Items.remove(position);
    //    notifyItemRemoved(position);
    //}
    //for delete item
    //public void add(int position,String data) {
    //    Items.add(position,data);
    //    notifyItemInserted(position);
    //}

}

