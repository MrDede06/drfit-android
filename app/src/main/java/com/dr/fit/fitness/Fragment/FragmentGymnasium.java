package com.dr.fit.fitness.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.dr.fit.fitness.Activity.NoInternetConnectionActivity;
import com.dr.fit.fitness.Adapter.GymnasiumAdapter;
import com.dr.fit.fitness.Helper.Utility;
import com.dr.fit.fitness.Model.GymnasiumModel;
import com.dr.fit.fitness.R;
import com.dr.fit.fitness.Retrofit.Gymlasium.Gymlasium;
import com.dr.fit.fitness.Retrofit.RetrofitAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Batuhan Özkaya on 10.06.2017.
 */

public class FragmentGymnasium extends Fragment {
    View rootView;
    List<GymnasiumModel> gymnasiumList = new ArrayList<>();
    ListView LVLeaderBoard;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_gymnasium, container, false);
        defineObjects();

        return rootView;
    }

    private void defineObjects(){
        /** Define ListViews **/
        LVLeaderBoard =  rootView.findViewById(R.id.LVLeaderBoard);

        try {
            if(!Utility.haveInternetConnection(getContext(), false)){
                startActivity(new Intent(getContext(), NoInternetConnectionActivity.class));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        getGymlasiumList();
    }

    private void getGymlasiumList(){
        RestAdapter retrofit = new RestAdapter.Builder().setEndpoint(getString(R.string.base_url)).build();

        final RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        retrofitAPI.Gymlasium(new Callback<Gymlasium>() {
            @Override
            public void success(Gymlasium gymlasium, Response response) {
                if(gymlasium.getResponse().equals("ok")){
                    for (int i = 0; i < gymlasium.getGymlasiumListList().size(); i++) {
                        gymnasiumList.add(new GymnasiumModel(gymlasium.getGymlasiumListList().get(i).getName(), gymlasium.getGymlasiumListList().get(i).getPoint(), gymlasium.getGymlasiumListList().get(i).getPhotoURL()));

                        GymnasiumAdapter gymnasiumAdapter = new GymnasiumAdapter(getContext(), gymnasiumList);
                        LVLeaderBoard.setAdapter(gymnasiumAdapter);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
