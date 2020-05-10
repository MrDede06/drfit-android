package com.dr.fit.fitness.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dr.fit.fitness.Activity.NoInternetConnectionActivity;
import com.dr.fit.fitness.Activity.ProfileSettingsActivity;
import com.dr.fit.fitness.Helper.AppUtility;
import com.dr.fit.fitness.Helper.GSharedPreferences;
import com.dr.fit.fitness.Helper.Utility;
import com.dr.fit.fitness.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * Created by Batuhan Özkaya on 10.06.2017.
 */

public class FragmentProfile extends Fragment {
    View rootView;
    RelativeLayout RLSettings, RLInviteFriends;
    RoundedImageView RIVProfilePicture;
    TextView TVFullName, TVProfilePointValue, TVTOSAndPP;
    GSharedPreferences sharedPreferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        defineObjects();

        return rootView;
    }

    private void defineObjects(){
        /** Define Others **/
        sharedPreferences = new GSharedPreferences(getContext());
        RIVProfilePicture = rootView.findViewById(R.id.RIVProfilePicture);
        /** Define TextViews **/
        TVFullName = rootView.findViewById(R.id.TVFullName);
        TVProfilePointValue = rootView.findViewById(R.id.TVProfilePointValue);
        TVTOSAndPP = rootView.findViewById(R.id.TVTOSAndPP);
        /** Define RelativeLayouts **/
        RLSettings = rootView.findViewById(R.id.RLSettings);
        RLInviteFriends = rootView.findViewById(R.id.RLInviteFriends);

        RLSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ProfileSettingsActivity.class));
            }
        });

        RLInviteFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareCompat.IntentBuilder.from(getActivity()).setType("text/plain").setChooserTitle("Training").setText("http://dededevops.com").startChooser();
            }
        });

        TVTOSAndPP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.privacy_url)));
                startActivity(browserIntent);
            }
        });

        try {
            if(!Utility.haveInternetConnection(getContext(), false)){
                startActivity(new Intent(getContext(), NoInternetConnectionActivity.class));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setContentInformationWithShared();
    }

    private void setContentInformationWithShared(){
        AppUtility.checkProfilePictureAndLoad(getContext(), RIVProfilePicture);

        TVFullName.setText(sharedPreferences.GET_NAME());
        TVProfilePointValue.setText(String.valueOf(sharedPreferences.GET_USER_POINT()));
    }
}
