package com.tomtom.cookieclock;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.tomtom.cookieclock.repository.GammerRepoHelper;
import com.tomtom.cookieclock.repository.GammerResultDAO;

import java.util.Collections;
import java.util.List;

public class ResultsActivity extends AppCompatActivity implements RecyclerItemClickListener.OnItemClickListener  {

    RecyclerView rv;
    List<GammerResultDAO> gammersList;
    GammerRepoHelper gamers;
    RVAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_results);

        rv = (RecyclerView)findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);

        rv.addOnItemTouchListener(new RecyclerItemClickListener(this, this));

        hideActionBar();

        gamers = new GammerRepoHelper(getApplicationContext());
        gammersList = gamers.getResults();
        Collections.sort(gammersList, new TimeComparator());

        adapter = new RVAdapter(gammersList);
        rv.setAdapter(adapter);
    }

    private void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        View results = findViewById(R.id.results);

        results.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void onItemClick(View childView, int position) {
        Toast.makeText(getApplicationContext(), gammersList.get(position).getEmail(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemLongPress(View childView, int position) {
        Toast.makeText(getApplicationContext(),  "usunieto: " + gammersList.get(position).getName(), Toast.LENGTH_SHORT).show();
        gamers.deleteGammer(gammersList.get(position));
        gammersList = gamers.getResults();
        Collections.sort(gammersList, new TimeComparator());

        adapter = new RVAdapter(gammersList);
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
}
