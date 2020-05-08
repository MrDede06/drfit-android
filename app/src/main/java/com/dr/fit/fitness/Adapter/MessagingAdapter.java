package com.dr.fit.fitness.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.codesgood.views.JustifiedTextView;
import com.dr.fit.fitness.Helper.AppUtility;
import com.dr.fit.fitness.Helper.GSharedPreferences;
import com.dr.fit.fitness.Model.MessagingModel;
import com.dr.fit.fitness.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

/**
 * Created by Batuhan Özkaya on 30.06.2017.
 */

public class MessagingAdapter extends BaseAdapter {
    List<MessagingModel> messagingAdaptersList;
    Context context;
    GSharedPreferences sharedPreferences;
    RoundedImageView RIVProfilePicture;
    JustifiedTextView TVMessage;
    public MessagingAdapter(Context mContext, List<MessagingModel> mMessagingList){
        context = mContext;
        messagingAdaptersList = mMessagingList;
    }

    @Override
    public int getCount() {
        return messagingAdaptersList.size();
    }

    @Override
    public Object getItem(int position) {
        return messagingAdaptersList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View satirView;
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        sharedPreferences = new GSharedPreferences(context);

        satirView = layoutInflater.inflate(R.layout.item_messaging, null);

        final MessagingModel messagingModel = messagingAdaptersList.get(i);

        TVMessage = satirView.findViewById(R.id.TVMessage);
        RIVProfilePicture = satirView.findViewById(R.id.RIVProfilePicture);

        if(!messagingModel.isSystemOrUser()){
            AppUtility.checkProfilePictureAndLoad(context, RIVProfilePicture);
        }

        TVMessage.setText(messagingModel.getMessage());


        return satirView;
    }
}
