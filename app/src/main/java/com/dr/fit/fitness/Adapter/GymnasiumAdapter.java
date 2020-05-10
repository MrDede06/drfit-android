package com.dr.fit.fitness.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dr.fit.fitness.Helper.AppUtility;
import com.dr.fit.fitness.Helper.GSharedPreferences;
import com.dr.fit.fitness.Model.GymnasiumModel;
import com.dr.fit.fitness.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by batuhanozkaya on 22.05.2017.
 */

public class GymnasiumAdapter extends BaseAdapter {
    private List<GymnasiumModel> GymnasiumList;
    private Context context;

    private TextView TVFullName, TVPoint, TVPointText;
    private LinearLayout LLBackground;
    private RoundedImageView RIVProfilePicture;
    public GymnasiumAdapter(Context mContext, List<GymnasiumModel> mGymnasiumList){
        context = mContext;
        GymnasiumList = mGymnasiumList;
    }

    @Override
    public int getCount() {
        return GymnasiumList.size();
    }

    @Override
    public Object getItem(int i) {
        return GymnasiumList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View satirView;
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        satirView = layoutInflater.inflate(R.layout.gymnasium_item, null);

        /** Defining -> Start **/
        TVFullName =  satirView.findViewById(R.id.TVFullName);
        TVPoint =  satirView.findViewById(R.id.TVProfilePointValue);
        TVPointText =  satirView.findViewById(R.id.TVProfilePoint);
        LLBackground = satirView.findViewById(R.id.LLBackground);
        RIVProfilePicture =  satirView.findViewById(R.id.RIVProfilePicture);
        /** Defining -> Finish **/

        final GymnasiumModel gymnasiumModel = GymnasiumList.get(position);

        /** Set Background and font colors -> START **/
        if(position == 0 || position == 1 || position == 2){
            TVFullName.setText(position + 1 + ". " + gymnasiumModel.getName());
            TVPoint.setText(String.valueOf(gymnasiumModel.getPoint()));
            loadProfilePictures(gymnasiumModel.getImage());
        }else if(position == 3 || position == 4){ //4. ve 5. sıradakilerden itibaren tasarım değişiyor.
            setTextViewAndLL(gymnasiumModel.getName(), String.valueOf(gymnasiumModel.getPoint()), gymnasiumModel.getImage());
        }else if(position == 5){
            setTextViewAndLL(gymnasiumModel.getName(), String.valueOf(gymnasiumModel.getPoint()), gymnasiumModel.getImage());
        }else{
            setTextViewAndLL(gymnasiumModel.getName(), String.valueOf(gymnasiumModel.getPoint()), gymnasiumModel.getImage());
        }
        /** Set Background and font colors -> FINISH **/


        return satirView;
    }

    public void setTextViewAndLL(String Name, String Point, String ImageURL){
        LLBackground.setBackground(ContextCompat.getDrawable(context, R.drawable.background_profile_background));
        TVFullName.setTextColor(ContextCompat.getColor(context, R.color.text_color));
        TVPoint.setTextColor(ContextCompat.getColor(context, R.color.text_color));
        TVPointText.setTextColor(ContextCompat.getColor(context, R.color.text_color));
        loadProfilePictures(ImageURL);

        TVFullName.setText(Name);
        TVPoint.setText(Point);
    }

    private void loadProfilePictures(String ImageURL){
        Picasso.with(context).load(ImageURL).into(RIVProfilePicture);
    }
}
