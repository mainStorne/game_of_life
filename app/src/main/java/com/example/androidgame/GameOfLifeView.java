package com.example.androidgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Handler;

public class GameOfLifeView extends View {


    public static int ALIVE_COLOR = Color.RED;
    public static final int DEAD_COLOR = Color.WHITE;


    public static final int cellSize = 50;
    public boolean isRunning;

    public World world;

    private float scale = 1f;


    Handler handler;

    Drawer drawer;

    private final Paint paint = new Paint();

    int nColumns, nRows;

    int columnWidth, rowHeight;
    private int fps;

    static public int FILL_METHOD = FillMethod.DEFAULT_FILL_METHOD;


    public GameOfLifeView(Context context) {
        super(context);
        initWorld();

    }

    public GameOfLifeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWorld();
    }

    public GameOfLifeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initWorld();
    }


    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }


    public void updateMs(int ms) {
        this.fps = ms;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {


        super.onDraw(canvas);
        canvas.save();
        drawCells(canvas);
        canvas.restore();
    }

    private void drawCells(Canvas canvas) {
        if (0.5f <= scale) {
            float left = ((float) Math.floor((getLeft() - getTranslationX()) / cellSize) - 1);
            float top = ((float) Math.floor((getTop() - getTranslationY()) / cellSize) - 1);
            float right = (float) Math.ceil((getRight() - getTranslationX()) / cellSize) / scale;
            float bottom = (float) Math.ceil((getBottom() - getTranslationY()) / cellSize) / scale;

            paint.setStrokeWidth(3);
            paint.setColor(Color.GRAY);
            for (float y = top; y <= bottom; y += 1) {
                canvas.drawLine(left * cellSize, y * cellSize, right * cellSize, y * cellSize, paint);
            }
            for (float x = left; x <= right; x += 1) {
                canvas.drawLine(x * cellSize, top * cellSize, x * cellSize, bottom * cellSize, paint);
            }
        }


        for (int i = 0; i < nColumns; i++) {
            for (int j = 0; j < nRows; j++) {
                Cell cell = world.get(i, j);
                paint.setColor(cell.alive ? ALIVE_COLOR : DEAD_COLOR);
                canvas.drawCircle((cell.x + 0.5f) * cellSize, (cell.y + 0.5f) * cellSize,
                        cellSize * 0.5f, paint);
            }
        }
    }


    public void toggleIsRunning() {
        isRunning = !isRunning;
        if (isRunning) {
            handler.postDelayed(drawer, 0);
        }
    }


    private void initWorld() {
        drawer = new Drawer(this);
        handler = new Handler();
        isRunning = false;
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        nColumns = point.x / cellSize;
        nRows = point.y / cellSize;

        columnWidth = point.x / nColumns;
        rowHeight = point.y / nRows;


        world = new World(nColumns, nRows);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int i = (int) (event.getX() / columnWidth);
            int j = (int) (event.getY() / rowHeight);

            switch (FILL_METHOD) {
                case FillMethod.DEFAULT_FILL_METHOD:
                    Cell cell = world.get(i, j);
                    cell.invert();
                    invalidate();
                    break;

                case FillMethod.BOX_FILL_METHOD:
                    if (i - 2 > 0 && i + 2 < nColumns && j - 2 > 0 && j + 2 < nRows) {
                        for (int k = i - 2; k <= i + 2; k++) {
                            for (int l = j - 2; l < j + 2; l++) {

                                world.get(k, l).invert();
                                invalidate();

                            }

                        }
                    }
                    break;
            }
        }
        return super.onTouchEvent(event);
    }


    private static class Drawer implements Runnable {
        final GameOfLifeView gameOfLifeView;

        public Drawer(GameOfLifeView gameOfLifeView) {
            this.gameOfLifeView = gameOfLifeView;
        }

        @Override
        public void run() {
            gameOfLifeView.world.nextGeneration();
            gameOfLifeView.invalidate();
            if (gameOfLifeView.isRunning) {
                gameOfLifeView.handler.postDelayed(this, gameOfLifeView.fps);
            }
        }
    }

    public static class FillMethod {

        public static final int DEFAULT_FILL_METHOD = 0;

        public static final int BOX_FILL_METHOD = 1;


    }

}

