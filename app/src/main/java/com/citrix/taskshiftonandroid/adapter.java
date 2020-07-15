package com.citrix.taskshiftonandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class adapter extends RecyclerView.Adapter<adapter.PersonViewHolder> {

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.test, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @NonNull
    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        personViewHolder.taskId.setText(Items.get(i).taskid);
        personViewHolder.taskSummary.setText(Items.get(i).storySummary);
        personViewHolder.jira.setImageResource(Items.get(i).jiraLogo);
        personViewHolder.storyType.setImageResource(Items.get(i).storyType);
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
    adapter(List<Item> Items){
        this.Items = Items;
    }
    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView taskId;
        TextView taskSummary;
        ImageView jira;
        ImageView storyType;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.card);
            taskId = (TextView)itemView.findViewById(R.id.TS);
            taskSummary = (TextView)itemView.findViewById(R.id.summary);
            jira = (ImageView)itemView.findViewById(R.id.jira);
            storyType = (ImageView)itemView.findViewById(R.id.storyType);
        }
    }


}

