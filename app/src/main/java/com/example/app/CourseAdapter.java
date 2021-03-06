package com.example.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;





/*
public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder>{

    private Context context;
    private List<Course> listCourses;

    public CourseAdapter(Context context, List<Course> listCourses) {
        this.context = context;
        this.listCourses = listCourses;
    }


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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Course listCourse = listCourses.get(position);

        holder.tempsCourseTV.setText(listCourse.gettVtempsCourse());
        holder.calorieTV.setText(listCourse.getTvCalorieBrulee());
        holder.objectifAtteintTV.setText(listCourse.getTvObjectifAttein());
        holder.calorieNB.setText(listCourse.getCalorieBurlee()+"");
        holder.tempsCourseNb.setText(listCourse.getTempsDeCourse()+"");
        holder.objectifAtteintCheckBox.setChecked(listCourse.isObjectifAtteint());
    }

    @Override
    public int getItemCount() {
        return listCourses.size();
    }


}

 */

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder>{

    private Context context;
    private List<StructureItem> listCourses;
    private  RecyclerViewClickListener listener;


    public CourseAdapter(Context context, List<StructureItem> listCourses, RecyclerViewClickListener listener) {
        this.context = context;
        this.listCourses = listCourses;
        this.listener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.liste_courses, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StructureItem listCourse = listCourses.get(position);
        holder.textViewHead.setText(listCourse.getHead());
        holder.textViewDesc.setText(listCourse.getDesc());
    }

    @Override
    public int getItemCount() {
        return listCourses.size();
    }

    public interface RecyclerViewClickListener{
        void onClick(View v, int position);
    }




    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView textViewHead, textViewDesc;

        public ViewHolder(View itemView){
            super(itemView);

            textViewHead = (TextView) itemView.findViewById(R.id.textViewHead);
            textViewDesc = (TextView) itemView.findViewById(R.id.textViewDesc);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //listener.onClick(v, getBindingAdapterPosition());
        }
    }

}
