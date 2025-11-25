package com.example.campuslink.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campuslink.R;
import com.example.campuslink.data.local.Student;

import java.util.List;

public class FeeAdapter extends RecyclerView.Adapter<FeeAdapter.FeeViewHolder> {

    private List<Student> studentList;
    private OnFeeStatusChangedListener listener;

    public interface OnFeeStatusChangedListener {
        void onFeeStatusChanged(Student student);
    }

    public FeeAdapter(List<Student> studentList, OnFeeStatusChangedListener listener) {
        this.studentList = studentList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fee, parent, false);
        return new FeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeeViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.tvStudentName.setText(student.name);
        holder.tvStudentId.setText(student.id);
        holder.tvFeeStatus.setText("Status: " + student.feeStatus);
        holder.btnChangeStatus.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFeeStatusChanged(student);
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public void setStudents(List<Student> students) {
        this.studentList = students;
        notifyDataSetChanged();
    }

    static class FeeViewHolder extends RecyclerView.ViewHolder {

        TextView tvStudentName, tvStudentId, tvFeeStatus;
        Button btnChangeStatus;

        public FeeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStudentName = itemView.findViewById(R.id.tvStudentName);
            tvStudentId = itemView.findViewById(R.id.tvStudentId);
            tvFeeStatus = itemView.findViewById(R.id.tvFeeStatus);
            btnChangeStatus = itemView.findViewById(R.id.btnChangeStatus);
        }
    }
}
