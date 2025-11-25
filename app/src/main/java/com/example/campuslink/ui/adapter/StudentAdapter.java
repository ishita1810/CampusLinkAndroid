package com.example.campuslink.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campuslink.R;
import com.example.campuslink.data.local.Student;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private List<Student> studentList;
    private OnStudentAttendanceChangedListener listener;

    public interface OnStudentAttendanceChangedListener {
        void onStudentAttendanceChanged(Student student, boolean isPresent);
    }

    public StudentAdapter(List<Student> studentList, OnStudentAttendanceChangedListener listener) {
        this.studentList = studentList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.tvStudentName.setText(student.name);
        holder.tvStudentId.setText(student.id);
        holder.cbPresent.setOnCheckedChangeListener(null); // Clear listener to prevent unwanted calls
        holder.cbPresent.setChecked(false); // Default to not present

        holder.cbPresent.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (listener != null) {
                listener.onStudentAttendanceChanged(student, isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    static class StudentViewHolder extends RecyclerView.ViewHolder {

        TextView tvStudentName, tvStudentId;
        CheckBox cbPresent;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStudentName = itemView.findViewById(R.id.tvStudentName);
            tvStudentId = itemView.findViewById(R.id.tvStudentId);
            cbPresent = itemView.findViewById(R.id.cbPresent);
        }
    }
}
