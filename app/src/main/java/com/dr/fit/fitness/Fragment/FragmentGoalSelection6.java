package com.dr.fit.fitness.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dr.fit.fitness.Activity.FirstScreenActivity;
import com.dr.fit.fitness.Activity.GoalSelectionActivity;
import com.dr.fit.fitness.R;

/**
 * Created by Batuhan Özkaya on 11.06.2017.
 */

public class FragmentGoalSelection6 extends Fragment {
    View rootView;
    Button btnStartMyBodyTest;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_goal_selection_6, container, false);
        defineObjects();

        return rootView;
    }

    private void defineObjects(){
        btnStartMyBodyTest = rootView.findViewById(R.id.btnStartMyBodyTest);

        btnStartMyBodyTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((GoalSelectionActivity)getActivity()).goToFragment(new FragmentGoalSelection7());

            }
        });
    }

}
