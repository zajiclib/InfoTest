package com.zajiclib.infotest;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;


public class HelpScreen implements View.OnClickListener {

    private final ConstraintLayout helpLayout;
    private final Context context;
    private final List<View> viewsToShowHelp;
    private final List<String> helpMessages;
    private static final double ADJUST_RATIO = 1.6;

    private int displayWidth, displayHeight;

    public HelpScreen(@NonNull Context context, ConstraintLayout helpLayout) {
        this.helpLayout = helpLayout;
        this.context = context;
        this.viewsToShowHelp = new ArrayList<>();
        this.helpMessages = new ArrayList<>();
        this.helpLayout.setOnClickListener(this);
        Point screenSizePoint = new Point();
        ((MainActivity) context).getWindowManager().getDefaultDisplay().getSize(screenSizePoint);
        this.displayWidth = screenSizePoint.x;
        this.displayHeight = screenSizePoint.y;
        this.helpLayout.setElevation(15);
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
            iv.setMaxWidth(currentViewToAnnotate.getWidth());
            iv.setMaxHeight(currentViewToAnnotate.getHeight());
            iv.setAdjustViewBounds(true);
            iv.setId(View.generateViewId());
            iv.setBackgroundColor(Color.RED);
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.arrow_down);
            iv.setImageDrawable(drawable);

            // flip vertically
            if (!isBottom) iv.setScaleY(-1.0f);

            // arrow params
            ConstraintSet newSet = new ConstraintSet();
            int leftMargin = computeLeftMargin(currentViewToAnnotate);
            int topMargin = computeTopMargin(currentViewToAnnotate);

            helpLayout.addView(iv, 0);
            newSet.clone(helpLayout);

            newSet.connect(iv.getId(), ConstraintSet.TOP, helpLayout.getId(), ConstraintSet.TOP, topMargin);
            newSet.connect(iv.getId(),ConstraintSet.START, helpLayout.getId(), ConstraintSet.START, leftMargin);
            newSet.applyTo(helpLayout);





            helpLayout.setVisibility(View.VISIBLE);
        }
    }

    public boolean isOnRightSide(@NonNull View view) {
        int centerX = (int) view.getX() + view.getWidth() / 2;

        return centerX >= displayWidth / 2;
    }

    /**
     * Checks whether is the view on the bottom (half) side of the screen
     */
    public boolean isOnBottomSide(@NonNull View view) {
        int centerY = (int) view.getY() + view.getHeight() / 2;

        return centerY >= displayHeight / 2;
    }

    /**
     * Computes left margin for the arrow
     */
    public int computeLeftMargin(View view) {
        return (int) view.getX();
    }

    public int computeRightMargin(View view) {
        return (int) view.getX() + view.getWidth();
    }

    /**
     * Computes top margin for the arrow
     */
    public int computeTopMargin(View view) {
        boolean isBottom = isOnBottomSide(view);

        if (isBottom) {
            return (int) view.getY();
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
            return (int) (view.getY() - view.getHeight());
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
