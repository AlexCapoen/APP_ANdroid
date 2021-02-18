package com.example.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class CourseAdapter extends
        RecyclerView.Adapter<CourseAdapter.ViewHolder>{

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView objectifAtteintTV, tempsCourseTV, calorieTV;
        public EditText tempsCourseNb, calorieNB;
        public CheckBox objectifAtteintCheckBox;

        public ViewHolder(View itemView){
            super(itemView);

            objectifAtteintTV = (TextView) itemView.findViewById(R.id.objectifAtteintTV);
            tempsCourseTV = (TextView) itemView.findViewById(R.id.tempsCourseTV);
            calorieTV = (TextView) itemView.findViewById(R.id.calorieTV);
            tempsCourseNb = (EditText) itemView.findViewById(R.id.tempsCourseNb);
            calorieNB = (EditText) itemView.findViewById(R.id.calorieNB);
            objectifAtteintCheckBox = (CheckBox) itemView.findViewById(R.id.objectifAtteintCheckBox);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_par_course, parent, false);
        return new ViewHolder(view);
    }

}
