package com.yatsotechs.journaljourneyv7;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class JourneyRVAdapter extends RecyclerView.Adapter<JourneyRVAdapter.ViewHolder>{

    //creating variables for our list, context interface and position
    private ArrayList<Model> modelArrayList;
    private Context context;
    private JourneyClickInterface journeyClickInterface;
    int lastPos = -1;

    //creating a constructor
    public JourneyRVAdapter(ArrayList<Model> modelArrayList, Context context, JourneyClickInterface journeyClickInterface) {
        this.modelArrayList = modelArrayList;
        this.context = context;
        this.journeyClickInterface = journeyClickInterface;

    }

    @NonNull
    @Override
    public JourneyRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating our layout file below on line
        View view = LayoutInflater.from(context).inflate(R.layout.row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //setting data to our recycler view item on below line.
        Model model = modelArrayList.get(position);
        holder.titleTV.setText(model.getJourneyName());
        holder.DescTV.setText(model.getJourneyDescription());
        holder.LocationTV.setText(model.getJourneyLocation());


        Picasso.get().load(model.getImageURL()).placeholder(R.drawable.lb)
                .fit()
                .centerCrop()
                .into(holder.imageIV);


        //Picasso.get().load(model.getImageURL()).into(holder.imageIV);

        /*
        //Picasso.get().load(model.getImageURL()).into(holder.imageIV);

        //load image from glide library
        //Glide.with(context).load(model.getImageURL()).into(holder.imageIV)
        */

        // adding animation to recycler view item on below line.
        setAnimation(holder.itemView, position);
        holder.rowLY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                journeyClickInterface.onJourneyClick(position);
            }
        });

    }

    private void setAnimation(View itemView, int position) {
        ///animations implement own logic!!!
        if (position > lastPos) {
            // on below line we are setting animation.
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            itemView.setAnimation(animation);
            lastPos = position;
        }
    }

    @Override
    public int getItemCount() {
      return modelArrayList.size();
       // return (modelArrayList == null) ? 0 : modelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        //creating variable for our imageview and text views below
        private ImageView imageIV, share;
        private TextView titleTV, DescTV, LocationTV;
        private LinearLayout rowLY;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //initializing all our variables on below line
            imageIV = itemView.findViewById(R.id.rImageView);
            titleTV = itemView.findViewById(R.id.rTitleView);
            DescTV = itemView.findViewById(R.id.rDescriptionTv);
            LocationTV = itemView.findViewById(R.id.rLocationTv);
            rowLY = itemView.findViewById(R.id.cardRow);
            share = itemView.findViewById(R.id.shareImageView);


        }
    }

    // creating an interface for on click
    public interface JourneyClickInterface {
        void onJourneyClick(int position);

    }

}
