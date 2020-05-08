package com.dr.fit.fitness.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dr.fit.fitness.Activity.BuyPremiumActivity;
import com.dr.fit.fitness.Activity.GoalSelectionActivity;
import com.dr.fit.fitness.Activity.MyWorkoutPreviewActivity;
import com.dr.fit.fitness.Helper.AppUtility;
import com.dr.fit.fitness.Helper.GSharedPreferences;
import com.dr.fit.fitness.Helper.Utility;
import com.dr.fit.fitness.Model.MyWorkoutModel;
import com.dr.fit.fitness.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

/**
 * Created by batuhanozkaya on 21.05.2017.
 */

public class MyWorkoutAdapter extends RecyclerView.Adapter<MyWorkoutAdapter.MyViewHolder> {
    private ArrayList<MyWorkoutModel> dataSet;
    private Context context;
    private GSharedPreferences sharedPreferences;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView TVName, TVDate;
        RoundedImageView IVPhoto;
        ImageView IVLockPhoto, IVMyWorkoutStatus;

        public MyViewHolder(final View itemView, ArrayList<MyWorkoutModel> data) {
            super(itemView);

            this.TVName = itemView.findViewById(R.id.TVTitle);
            this.IVPhoto = itemView.findViewById(R.id.IVPhoto);
            this.TVDate = itemView.findViewById(R.id.TVDate);
            this.IVLockPhoto = itemView.findViewById(R.id.IVLockPhoto);
            this.IVMyWorkoutStatus = itemView.findViewById(R.id.IVMyWorkoutStatus);
        }
    }

    public MyWorkoutAdapter(Context mContext, ArrayList<MyWorkoutModel> data) {
        this.context = mContext;
        this.dataSet = data;
        sharedPreferences = new GSharedPreferences(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_workout_recyclerview_item, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view, dataSet);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        String Day = context.getString(R.string.day);
        String Ready = context.getString(R.string.ready);
        String Not_Ready = context.getString(R.string.not_ready);
        String Complete = context.getString(R.string.complete);


        TextView TVName = holder.TVName;
        TextView TVDate = holder.TVDate;
        RoundedImageView IVPhoto = holder.IVPhoto;
        ImageView IVLockPhoto = holder.IVLockPhoto;
        ImageView IVWorkoutMyStatus = holder.IVMyWorkoutStatus;

        TVName.setText(dataSet.get(listPosition).getName());

        if(!sharedPreferences.GET_IS_PROGRAM_COMPLATE()){
            TVDate.setText(context.getString(R.string.complete_your_test_description));
        }else{
            if(sharedPreferences.GET_IS_PROGRAM_PREPARING()){
                IVLockPhoto.setImageResource(R.drawable.min30);
                TVName.setText(context.getString(R.string.workout_preparing_title));
                TVDate.setText(context.getString(R.string.workout_preparing_description));
                IVWorkoutMyStatus.setImageResource(R.drawable.play);
            }else{
                if(!sharedPreferences.GET_IS_PREMUIM()){
                    TVName.setText(context.getString(R.string.purchase_your_workout));
                    TVDate.setText(context.getString(R.string.purchase_your_workout_description));
                    IVPhoto.setImageResource(R.drawable.premium_workout_image);
                    AppUtility.makeGrayScalePhoto(IVPhoto);
                    IVLockPhoto.setVisibility(View.GONE);
                }else{
                    TVName.setText(Day + " " + (dataSet.get(listPosition).getID() + 1) + " " + Not_Ready);
                    TVDate.setText((dataSet.get(listPosition).getID() + 1) + ". " + context.getString(R.string.workout) + " " + Not_Ready);
                    IVPhoto.setImageResource(R.drawable.premium_workout_image);
                    IVLockPhoto.setVisibility(View.GONE);

                    if(sharedPreferences.GET_DAY_IN_WEEK_FOR_ARRAY() > dataSet.get(listPosition).getID()){ //Daha önce oynatılmış ve tamamlanmış videolardır.
                        TVName.setText(Day + " " + (dataSet.get(listPosition).getID() + 1) + " " + Complete);
                        TVDate.setText((dataSet.get(listPosition).getID() + 1) + ". " + context.getString(R.string.workout) + " " + Complete);
                        IVWorkoutMyStatus.setImageResource(R.drawable.complete);
                    }else if(sharedPreferences.GET_DAY_IN_WEEK_FOR_ARRAY() == dataSet.get(listPosition).getID()){ //Mevcut oynatılacak videodur.
                        if(sharedPreferences.GET_IS_NEXT_VIDEO_AVAILABLE().equals("waiting")){
                            TVName.setText(Day + " " + (dataSet.get(listPosition).getID() + 1) + " " + Complete);
                            TVDate.setText((dataSet.get(listPosition).getID() + 1) + ". " + context.getString(R.string.workout) + " " + Complete);
                            IVWorkoutMyStatus.setImageResource(R.drawable.play);
                        }else{
                            TVName.setText(Day + " " + (dataSet.get(listPosition).getID() + 1) + " " + Ready);
                            TVDate.setText((dataSet.get(listPosition).getID() + 1) + ". " + context.getString(R.string.workout) + " " + Ready);
                            IVWorkoutMyStatus.setImageResource(R.drawable.play);
                        }
                    }else{ //Burası diğer videolardır, her zaman kilitlidir demektir.
                        IVWorkoutMyStatus.setImageResource(R.drawable.close);
                    }
                }
            }
        }

        IVPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!sharedPreferences.GET_IS_PROGRAM_COMPLATE()){
                    context.startActivity(new Intent(context, GoalSelectionActivity.class));
                }else{
                    if(sharedPreferences.GET_IS_PROGRAM_PREPARING()){
                        Utility.showAlertDialogOneButton(context, context.getString(R.string.getting_ready));
                    }else{
                        if(!sharedPreferences.GET_IS_PREMUIM()){
                            ((Activity) context).startActivityForResult(new Intent(context, BuyPremiumActivity.class), 1);
                        }else{
                            if(sharedPreferences.GET_DAY_IN_WEEK_FOR_ARRAY() > dataSet.get(listPosition).getID()){ //Daha önce oynatılmış ve tamamlanmış videolardır.
                                startExercise(listPosition);
                            }else if(sharedPreferences.GET_DAY_IN_WEEK_FOR_ARRAY() == dataSet.get(listPosition).getID()){ //Mevcut oynatılacak videodur.
                                if(sharedPreferences.GET_IS_NEXT_VIDEO_AVAILABLE().equals("waiting")){
                                    startExercise(listPosition);
                                }else{
                                    startExercise(listPosition);
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    private void startExercise(int listPosition){
        Intent intent = new Intent(context, MyWorkoutPreviewActivity.class);
        intent.putExtra("ID", String.valueOf(dataSet.get(listPosition).getID()));
        intent.putExtra("Position", dataSet.get(listPosition).getID());
        intent.putExtra("TotalTime", dataSet.get(listPosition).getTotalTime());
        intent.putExtra("ExerciseCount", dataSet.get(listPosition).getExerciseCount());
        intent.putExtra("Place", String.valueOf(dataSet.get(listPosition).getPlace()));
        intent.putExtra("isPremiumExercise", true);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}
