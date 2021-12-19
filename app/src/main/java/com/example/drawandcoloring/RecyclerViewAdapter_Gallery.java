package com.example.drawandcoloring;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter_Gallery extends RecyclerView.Adapter<RecyclerViewAdapter_Gallery.ViewHolder> {
    Context context;
    Cursor all;

    public RecyclerViewAdapter_Gallery(Context context,Cursor cursor) {
        this.context = context;
        this.all=cursor;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view= LayoutInflater.from(context).inflate(R.layout.recyclerview_gallery_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.i("length",String.valueOf(all.getCount()));
        if (all.getCount()!=0){
            all.moveToPosition(position);
            byte[] data=all.getBlob(0);
            Log.i("output",data.toString());
            holder.draw.setImageBitmap(DatabaseBitmapUtility.getView(data));
            holder.id.setText(all.getString(1));
        }else if (all.getCount()==0){
            ((GalleryActivity)context).IfDbIsEmptyDoThis();
        }
    }


    @Override
    public int getItemCount() {
        return all.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView draw;
        TextView id;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            draw=itemView.findViewById(R.id.draw);
            id=itemView.findViewById(R.id.draw_id);
        }
    }
}
