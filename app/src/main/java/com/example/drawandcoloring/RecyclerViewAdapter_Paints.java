package com.example.drawandcoloring;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter_Paints extends RecyclerView.Adapter<RecyclerViewAdapter_Paints.ViewHolder> {
    List<String> listOfPaints;
    Context context;

    public RecyclerViewAdapter_Paints(Context context,List<String> listOfPaints) {
        this.context=context;
        this.listOfPaints=listOfPaints;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view= LayoutInflater.from(context).inflate(R.layout.recyclerview_paints_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter_Paints.ViewHolder holder, int position) {
        String name=listOfPaints.get(position);
        holder.paint.setImageURI(Uri.parse(name));
        holder.name.setText(name);
    }

    @Override
    public int getItemCount() {
        return listOfPaints.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView paint;
        TextView name;
        RelativeLayout parent_layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            paint=itemView.findViewById(R.id.paint);
            name=itemView.findViewById(R.id.paint_name);
            parent_layout=itemView.findViewById(R.id.parent_layout);

            parent_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context, "this is selected="+paint.getBackground(), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(context, ColoringActivity.class);
                    intent.putExtra("paint",name.getText());
                    intent.putExtra("previous","main");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }
}
