package com.dr.fit.fitness.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dr.fit.fitness.Activity.GoalSelectionActivity;
import com.dr.fit.fitness.R;

/**
 * Created by batuhan on 4.05.2018.
 */

public class FragmentGoalSelection7 extends Fragment {
    View rootView;
    LinearLayout LLDownload;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_goal_selection_7, container, false);
        defineObjects();

        return rootView;
    }

    private void defineObjects(){
        LLDownload = rootView.findViewById(R.id.LLDownload);
        LLDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((GoalSelectionActivity)getActivity()).goToFragment(new FragmentGoalSelection8());
            }
        });
    }

}
