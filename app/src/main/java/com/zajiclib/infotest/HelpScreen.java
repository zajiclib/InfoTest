package com.zajiclib.infotest;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class HelpScreen implements View.OnClickListener {

    private final RelativeLayout helpLayout;
    private final Context context;
    private final List<View> viewsToShowHelp;
    private final List<String> helpMessages;
    private boolean displayAllTogether;
    private int currentlyDisplayedIndex;
    private static final double ADJUST_RATIO = 1.6;

    private int displayWidth, displayHeight;

    public HelpScreen(@NonNull Context context, RelativeLayout helpLayout) {
        this.helpLayout = helpLayout;
        this.context = context;
        this.viewsToShowHelp = new ArrayList<>();
        this.helpMessages = new ArrayList<>();
        this.helpLayout.setOnClickListener(this);
        Point screenSizePoint = new Point();
        ((MainActivity) context).getWindowManager().getDefaultDisplay().getSize(screenSizePoint);
        this.displayWidth = screenSizePoint.x;
        this.displayHeight = screenSizePoint.y;
        this.currentlyDisplayedIndex = 0;
        this.helpLayout.setFocusable(View.FOCUSABLE);
        this.helpLayout.setClickable(true);
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        this.context.getDisplay().getRealMetrics(displayMetrics);
//        this.displayWidth = displayMetrics.heightPixels;
//        this.displayHeight = displayMetrics.widthPixels;
    }

    public void addViewsToInfoScreen(View view, String textToDisplayAsHelp) {
        this.viewsToShowHelp.add(view);
        this.helpMessages.add(textToDisplayAsHelp);
    }


    /**
     * Method for displaying help in appropriate form
     */
    public void displayHelp() {
        for (int i = 0; i < viewsToShowHelp.size(); i++) {

            // retrieving view to annotate
            View currentViewToAnnotate = viewsToShowHelp.get(i);
            boolean isBottom = isOnBottomSide(currentViewToAnnotate);

            ImageView iv = new ImageView(context);
            iv.setAdjustViewBounds(true);
//            iv.setBackgroundColor(Color.RED);
            iv.requestLayout();
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.arrow_down);
            iv.setImageDrawable(drawable);

            // flip vertically
            if (!isBottom) iv.setScaleY(-1.0f);

            // arrow params
            RelativeLayout.LayoutParams paramsArrow = new RelativeLayout.LayoutParams((int) (currentViewToAnnotate.getWidth()), (int) (currentViewToAnnotate.getHeight()));

            paramsArrow.leftMargin = computeLeftMargin(currentViewToAnnotate);
            paramsArrow.topMargin = computeTopMargin(currentViewToAnnotate);

            helpLayout.addView(iv, paramsArrow);


            // setting up the annotation text view
            TextView tv = new TextView(context);
            tv.setTextColor(Color.WHITE);
            tv.setText(helpMessages.get(i));
            tv.requestLayout();

            RelativeLayout.LayoutParams paramsTv = new RelativeLayout.LayoutParams((int) currentViewToAnnotate.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);

            paramsTv.leftMargin = computeLeftMargin(currentViewToAnnotate);
            paramsTv.topMargin = computeTopMargin(currentViewToAnnotate, tv,  true);

            helpLayout.addView(tv, paramsTv);

            helpLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Checks whether is the view on the bottom (half) side of the screen
     */
    public boolean isOnBottomSide(@NonNull View view) {
        int centerY = (int) view.getY() + view.getWidth() / 2;

        return centerY >= displayHeight / 2;
    }

    /**
     * Computes left margin for the arrow
     */
    public int computeLeftMargin(View view) {
        return (int) view.getX();
    }

    /**
     * Computes top margin for the arrow
     */
    public int computeTopMargin(View view) {
        boolean isBottom = isOnBottomSide(view);

        if (isBottom) {
            return (int) view.getY() - view.getHeight();
        }

        return (int) view.getY() + view.getHeight();
    }

    /**
     * Computes top margin for the textview
     */
    public int computeTopMargin(View view, TextView textView, boolean isForTextView) {
        if (!isForTextView) return computeTopMargin(view);

        boolean isBottom = isOnBottomSide(view);

        if (isBottom) {
            return (int) (view.getY() - ADJUST_RATIO * view.getHeight());
        }

        return (int) (view.getY() + 2 * view.getHeight());
    }

    /**
     * Default on click event
     */
    @Override
    public void onClick(View v) {
//        displayCurrentHelp();
        this.helpLayout.setVisibility(View.GONE);
    }
}
