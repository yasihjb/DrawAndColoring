package com.example.drawandcoloring;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
//        Log.i("length",String.valueOf(all.getCount()));
        if (all.getCount()!=0){
            all.moveToPosition(position);
            byte[] data=all.getBlob(0);
//            Log.i("output",data.toString());
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
        RelativeLayout parent_layout;
        String type;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent_layout=itemView.findViewById(R.id.parent_layout);
            draw=itemView.findViewById(R.id.draw);
            id=itemView.findViewById(R.id.draw_id);

            parent_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(context.getApplicationContext(), String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(context.getApplicationContext(),ShowActivity.class);
                    intent.putExtra("selected_id",id.getText());
                    context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                }
            });
        }
    }
}
