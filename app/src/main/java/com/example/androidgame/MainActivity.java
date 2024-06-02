package com.example.androidgame;


import android.graphics.Color;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity {

    private GameOfLifeView gameOfLifeView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_of_life);
    }

    @Override
    protected void onStart() {
        super.onStart();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        gameOfLifeView = findViewById(R.id.gameoflife);

        final SeekBar speedSlider = (SeekBar) findViewById(R.id.speed_slider);
        if (null != speedSlider) {
            speedSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    gameOfLifeView.updateMs(progress);
                }

                public void onStartTrackingTouch(SeekBar seekBar) {}
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });
        }

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (null != fab) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gameOfLifeView.toggleIsRunning();
                    if (gameOfLifeView.isRunning) {
                        fab.setImageResource(android.R.drawable.ic_media_pause);
                    } else {
                        fab.setImageResource(android.R.drawable.ic_media_play);
                    }
                }
            });

        }



    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_game_of_life, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_clear) {
            gameOfLifeView.world.clear();
            gameOfLifeView.invalidate();
            return true;
        } else if (id == R.id.action_fill_box) {
            GameOfLifeView.FILL_METHOD = GameOfLifeView.FillMethod.BOX_FILL_METHOD;
            gameOfLifeView.invalidate();
            return true;

        }else if (id == R.id.action_fill) {
            gameOfLifeView.world.init();
            gameOfLifeView.invalidate();
            return true;
        } else if (id == R.id.action_fill_method_default) {
            GameOfLifeView.FILL_METHOD = GameOfLifeView.FillMethod.DEFAULT_FILL_METHOD;
            gameOfLifeView.invalidate();
            return true;
        } else if (id == R.id.action_color_blue) {
            GameOfLifeView.ALIVE_COLOR = Color.BLUE;
            gameOfLifeView.invalidate();
            return true;
        } else if (id == R.id.action_color_red) {
            GameOfLifeView.ALIVE_COLOR = Color.RED;
            gameOfLifeView.invalidate();
            return true;
        } else {
            boolean result = super.onOptionsItemSelected(item);
            return result;
        }
    }


}
