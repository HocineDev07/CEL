package com.hocinedev.cel.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.hocinedev.cel.Constants;
import com.hocinedev.cel.R;
import com.hocinedev.cel.databinding.UserItemBinding;

import java.text.SimpleDateFormat;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeHolder> {
    private final Context context;
    private List<DocumentSnapshot> arrayListUsers;
    private DocumentSnapshot snapshot;
    private OnItemClickListener mListener;

    private SimpleDateFormat simpleDateFormat;

    public HomeAdapter(Context context, List<DocumentSnapshot> arrayListCommands) {
        this.context = context;
        this.arrayListUsers = arrayListCommands;
    }

    @NonNull
    @Override
    public HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeHolder(UserItemBinding.inflate(LayoutInflater.from(context), parent, false), mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeHolder holder, int position) {
        snapshot = arrayListUsers.get(position);
        holder.textFirstLastName.setText(snapshot.getString(Constants.FIRST_LAST_NAME));
        holder.textKaser.setText(snapshot.getString(Constants.KASER));
        holder.textSpeciality.setText(snapshot.getString(Constants.SPECIALITY));
        if(snapshot.getBoolean(Constants.GRADUATE)){
            holder.textGraduate.setText(context.getString(R.string.txt_graduate));
            holder.textGraduate.setTextColor(context.getResources().getColor(R.color.primary));
            holder.imageGraduate.setImageResource(R.drawable.ic_graduate);
        }
    }

    @Override
    public int getItemCount() {
        return arrayListUsers.size();
    }

    class HomeHolder extends RecyclerView.ViewHolder {

        TextView textFirstLastName,textKaser,textSpeciality,textGraduate;
        ImageView imageGraduate;

        public HomeHolder(UserItemBinding binding, final OnItemClickListener listener) {
            super(binding.getRoot());
            textFirstLastName = binding.textViewFirstLastName;
            textKaser = binding.textViewKaser;
            textSpeciality = binding.textViewSpeciality;
            textGraduate = binding.textViewGraduate;
            imageGraduate = binding.imageGraduate;

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

}
