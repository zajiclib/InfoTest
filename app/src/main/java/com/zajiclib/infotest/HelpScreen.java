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


    public void setDisplayAllTogether(boolean displayAllTogether) {
        this.displayAllTogether = displayAllTogether;
    }


    /**
     * Display all views together on one screen
     */
    public void displayAllHelp() {

        for (int i = 0; i < viewsToShowHelp.size(); i++) {

            View view = viewsToShowHelp.get(i);
            ImageView ivCircle = new ImageView(context);
            ImageView ivArrow = new ImageView(context);
            TextView tvHelp = new TextView(context);

            int viewWidth = view.getWidth();
            int viewHeight = view.getHeight();

            Drawable circle = ContextCompat.getDrawable(context, R.drawable.circle_custom);
            Drawable arrow = ContextCompat.getDrawable(context, R.drawable.arrow_custom);
            ivArrow.setImageDrawable(arrow);
            ivArrow.requestLayout();
            ivArrow.setId(View.generateViewId());

            ivCircle.setImageDrawable(circle);
            ivCircle.requestLayout();
            ivCircle.setId(View.generateViewId());

            tvHelp.setId(View.generateViewId());
            tvHelp.setText(helpMessages.get(i));
            tvHelp.setTextColor(Color.WHITE);

            // image for circle
            RelativeLayout.LayoutParams paramsIv = new RelativeLayout.LayoutParams((int) (viewWidth * ADJUST_RATIO), (int) (viewHeight * ADJUST_RATIO));

            // calculating center coords for the view
            int centerX, centerY;
            centerX = (int) (view.getX() + (viewWidth / 2));
            centerY = (int) (view.getY() + (viewHeight / 2));

            int absolutePositionX = centerX - (int) ((viewWidth * ADJUST_RATIO) / 2 );
            int absolutePositionY = centerY - (int) ((viewHeight * ADJUST_RATIO) / 2);

            paramsIv.leftMargin = absolutePositionX;
            paramsIv.topMargin = absolutePositionY;

            helpLayout.addView(ivCircle, paramsIv);

            // arrow params
            RelativeLayout.LayoutParams paramsIvArrow = new RelativeLayout.LayoutParams((int) (viewWidth), (int) (viewHeight));

            paramsIvArrow.leftMargin = (int) (absolutePositionX - viewWidth);
            paramsIvArrow.topMargin = (int) (absolutePositionY + (viewHeight / 2));

            helpLayout.addView(ivArrow, paramsIvArrow);

            // textview params
            RelativeLayout.LayoutParams paramsForTextV = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            int height, width;
            tvHelp.measure(0,0);
            height = tvHelp.getMeasuredHeight();
            width = tvHelp.getMeasuredWidth();
            paramsForTextV.leftMargin = (int) (ivArrow.getX() - width);
            paramsForTextV.topMargin = (int) ivArrow.getY() - 20;

            helpLayout.addView(tvHelp, paramsForTextV);

        }

        helpLayout.setVisibility(View.VISIBLE);
    }

    /**
     * Method for displaying help in appropriate form
     */
    public void displayHelp() {
        for (int i = 0; i < viewsToShowHelp.size(); i++) {

            View currentViewToAnnotate = viewsToShowHelp.get(i);
            boolean isRight = isInRightHalf(currentViewToAnnotate);
            boolean isBottom = isOnBottomSide(currentViewToAnnotate);
            boolean isMiddle = isInMiddle(currentViewToAnnotate);

            ImageView iv = new ImageView(context);
            iv.requestLayout();
            TextView tv = new TextView(context);
            tv.setTextColor(Color.WHITE);
            tv.setText(helpMessages.get(i));
            tv.requestLayout();
            Drawable drawable;
            if (isMiddle) {
                drawable = ContextCompat.getDrawable(context, R.drawable.arrow_down);
            } else {
                drawable = ContextCompat.getDrawable(context, R.drawable.arrow_custom);
            }

            iv.setImageDrawable(drawable);

            // flip horizontally
            if (!isRight) iv.setScaleX(-1.0f);

            // flip vertically
            if (!isBottom) iv.setScaleY(-1.0f);


            // arrow params
            RelativeLayout.LayoutParams paramsArrow = new RelativeLayout.LayoutParams((int) (currentViewToAnnotate.getWidth() * ADJUST_RATIO), (int) (currentViewToAnnotate.getHeight() * ADJUST_RATIO));

            paramsArrow.leftMargin = computeLeftMargin(currentViewToAnnotate, iv);
            paramsArrow.topMargin = computeTopMargin(currentViewToAnnotate, iv);

            helpLayout.addView(iv, paramsArrow);

        }
    }

    /**
     * Decides if the view is located on the right half or not
     */
    public boolean isInRightHalf(@NonNull View view) {
        int centerX = (int) view.getX() + view.getWidth() / 2;

        return centerX >= displayWidth;
    }

    /**
     * Checks whether is the view right in the middle or not (because of the different drawable used)
     */
    public boolean isInMiddle(@NonNull View view) {
        int centerX = (int) view.getX() + view.getWidth() / 2;

        return centerX == displayWidth / 2;
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
    public int computeLeftMargin(View view, ImageView iv) {
        boolean isRight = isInRightHalf(view);
        boolean isMiddle = isInMiddle(view);

        if (isMiddle) {
            return (int) view.getX() + view.getWidth() / 2;
        }

        if (isRight) {
            return (int) view.getX();
        }

        return (int) view.getX() + view.getWidth();
    }

    /**
     * Computes top margin for the arrow
     */
    public int computeTopMargin(View view, ImageView imageView) {
        boolean isBottom = isOnBottomSide(view);

        if (isBottom) {
            return (int) view.getX() - imageView.getHeight();
        }

        return (int) view.getX() + view.getHeight();
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
