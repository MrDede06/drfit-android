package com.dr.fit.fitness.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dr.fit.fitness.Model.AllWorkoutModel;
import com.dr.fit.fitness.Activity.ProgramDetailActivity;
import com.dr.fit.fitness.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by batuhanozkaya on 21.05.2017.
 */

public class AllWorkoutAdapter extends BaseAdapter {
    List<AllWorkoutModel> allWorkoutList;
    Context context;
    TextView TVWorkoutTitle, TVWorkoutDescription;
    RoundedImageView IVPhoto;

    public AllWorkoutAdapter(Context mContext, List<AllWorkoutModel> mAllWorkoutList){
        context = mContext;
        allWorkoutList = mAllWorkoutList;
    }

    @Override
    public int getCount() {
        return allWorkoutList.size();
    }

    @Override
    public Object getItem(int position) {
        return allWorkoutList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View satirView;
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();

        satirView = layoutInflater.inflate(R.layout.all_workout_listview_item, null);

        RelativeLayout RLWorkout = satirView.findViewById(R.id.RLWorkout);
        TVWorkoutTitle =  satirView.findViewById(R.id.TVWorkoutTitle);
        TVWorkoutDescription =  satirView.findViewById(R.id.TVWorkoutDescription);
        IVPhoto =  satirView.findViewById(R.id.IVPhoto);
        Button btnCategory =  satirView.findViewById(R.id.btnCategory);

        final AllWorkoutModel allWorkoutModel = allWorkoutList.get(i);

        setContents(allWorkoutModel.getWorkoutTitle(), allWorkoutModel.getWorkoutDescription(), allWorkoutModel.getImageURL());

        RLWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProgramDetailActivity.class);
                intent.putExtra("ID", String.valueOf(allWorkoutModel.getID()));
                intent.putExtra("WorkoutTitle", allWorkoutModel.getWorkoutTitle());
                context.startActivity(intent);
            }
        });

        return satirView;
    }

    private void setContents(String Title, String Description, String ImageURL){
        TVWorkoutTitle.setText(Title);
        TVWorkoutDescription.setText(Description);
        Picasso.with(context).load(ImageURL).into(IVPhoto);
    }
}
