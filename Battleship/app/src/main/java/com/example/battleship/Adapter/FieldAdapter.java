package com.example.battleship.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.battleship.R;


public class FieldAdapter extends RecyclerView.Adapter<FieldAdapter.MyAdapter> {

    private final int[] images;
    private final String[] names;
    protected ItemListener mListener;

    public FieldAdapter(int[] images, String[] names, ItemListener mListener) {
        this.images = images;
        this.names = names;
        this.mListener = mListener;
    }

    @Override
    public int getItemCount() {
        return names.length;
    }

    @NonNull
    @Override
    public MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_layout,null);
        return new MyAdapter(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter holder, int position) {
        holder.image.setImageResource(images[position]);
        holder.text.setText(names[position]);
    }

    public interface ItemListener {
        void onItemClick(String idField);
    }

    public class MyAdapter extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView image;
        TextView text;
        public MyAdapter(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            image = itemView.findViewById(R.id.imageView);
            text = itemView.findViewById(R.id.textView);
            text.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(text.getText().toString());
            }
        }
    }
}
