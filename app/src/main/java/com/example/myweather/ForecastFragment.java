package com.example.myweather;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import org.json.JSONObject;

import java.util.ArrayList;

public class ForecastFragment extends Fragment {

    ListView listView;
    SearchView searchView;
    ArrayAdapter<String> adapter;

    public ForecastFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final ArrayList<Weather> data = new ArrayList<>();

        data.add(new Weather("Seoul", "25도", "Clear"));
        data.add(new Weather("Suwon-si", "25도", "Clouds"));
        data.add(new Weather("Incheon", "25도", "Rain"));
        data.add(new Weather("Busan", "25도", "Snow"));
        data.add(new Weather("Daegu", "25도", "Clear"));
        data.add(new Weather("Jeju City", "25도", "Snow"));
        data.add(new Weather("Gangneung", "25도", "Rain"));

        View view = inflater.inflate(R.layout.fragment_forecast, container, false);

        listView = (ListView) view.findViewById(R.id.mainMenu);
        MyFirstAdapter adapter = new MyFirstAdapter(data);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("citykey", position);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
