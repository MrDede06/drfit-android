package com.dr.fit.fitness.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dr.fit.fitness.Activity.BuyPremiumActivity;
import com.dr.fit.fitness.Activity.WorkoutPreviewActivity;
import com.dr.fit.fitness.Helper.GSharedPreferences;
import com.dr.fit.fitness.Model.SubcateModel;
import com.dr.fit.fitness.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by batuhanozkaya on 22.05.2017.
 */

public class SubcateAdapter extends BaseAdapter {

    private List<SubcateModel> SubcateList;
    private Context context;
    private GSharedPreferences sharedPreferences;

    public SubcateAdapter(Context mContext, List<SubcateModel> mSubcateList){
        context = mContext;
        SubcateList = mSubcateList;
        sharedPreferences = new GSharedPreferences(mContext);
    }

    @Override
    public int getCount() {
        return SubcateList.size();
    }

    @Override
    public Object getItem(int i) {
        return SubcateList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        View satirView;
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        satirView = layoutInflater.inflate(R.layout.program_details_item, null);

        /** Defining -> Start **/
        TextView TVProgramTitle =  satirView.findViewById(R.id.TVProgramTitle);
        //TextView TVProgramDescription = satirView.findViewById(R.id.TVProgramDescription);
        Button btnCategory =  satirView.findViewById(R.id.btnCategory);
        RoundedImageView IVPhoto =  satirView.findViewById(R.id.IVPhoto);
        CardView CVPhoto =  satirView.findViewById(R.id.CVPhoto);
        /** Defining -> Finish **/

        final SubcateModel subcateModel = SubcateList.get(position);

        TVProgramTitle.setText(subcateModel.getProgramTitle());
        //TVProgramDescription.setText(subcateModel.getProgramDescription());
        Picasso.with(context).load(subcateModel.getImage()).into(IVPhoto);

        if(subcateModel.isPremium()){
            btnCategory.setText(context.getString(R.string.premium));
        }else{
            btnCategory.setText(context.getString(R.string.free));
        }

        CVPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(subcateModel.isPremium()){
                    if(sharedPreferences.GET_IS_PREMUIM()){
                        GoToWorkout(position, subcateModel);
                    }else{
                        GoToPremium();
                    }
                }else{
                    GoToWorkout(position, subcateModel);
                }
            }
        });

        return satirView;
    }

     private void GoToWorkout(int Position, SubcateModel subcateModel){
        Intent intent = new Intent(context, WorkoutPreviewActivity.class);
        intent.putExtra("ID", String.valueOf(subcateModel.getProgramID()));
        intent.putExtra("SubcateID", String.valueOf(subcateModel.getSubcateID()));
        intent.putExtra("Position", Position);
        intent.putExtra("TotalTime", subcateModel.getTotalTime());
        intent.putExtra("ExerciseCount", subcateModel.getExerciseCount());
        intent.putExtra("Place", subcateModel.getPlace());
        intent.putStringArrayListExtra("AllVideos", (ArrayList<String>) subcateModel.getAllVideos());

        context.startActivity(intent);
     }

     private void GoToPremium(){
         context.startActivity(new Intent(context, BuyPremiumActivity.class));
     }
}
