package com.zajiclib.infotest;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;


/**
 * Class for displaying help with arrow and help messages
 */
public class HelpScreen implements View.OnClickListener {

    /**
     * Layout to generate help in
     */
    private final ConstraintLayout helpLayout;
    private final Context context;

    /**
     * Views to show help for
     */
    private final List<View> viewsToShowHelp;

    /**
     *
     */
    private final List<Point> pointsToAnnotate;

    /**
     * Strings for help (for the views with same index)
     */
    private final List<String> helpMessages, pointsMessages;
    private static final double ADJUST_RATIO = 1.6;

    /**
     * Offset for topbar
     */
    private static final int FRAGMENT_TOP_MARGIN_OFFSET = 130;
    private static final int DEFAULT_MARGIN = 5;

    /**
     * when help screen is used for views in fragment the top bar is omitted,
     * therefore this variable is used to compute extra offset if it is the case
     */
    private int topMarginOffset;

    private int displayWidth, displayHeight;

    /**
     * The only constructor
     *
     * @param context parent activity of fragments (for strings etc)
     * @param helpLayout Constraint layout for the views to be placed
     * @param isWithOffset provide true if the views are from the child fragment of the activity (top bar omitted)
     */
    public HelpScreen(@NonNull Context context, ConstraintLayout helpLayout, boolean isWithOffset) {
        this.helpLayout = helpLayout;
        this.helpLayout.removeAllViewsInLayout();
        this.context = context;
        this.viewsToShowHelp = new ArrayList<>();
        this.helpMessages = new ArrayList<>();
        this.pointsToAnnotate = new ArrayList<>();
        this.pointsMessages = new ArrayList<>();
        this.helpLayout.setOnClickListener(this);
        Point screenSizePoint = new Point();
        // measure screen size
        ((Activity) context).getWindowManager().getDefaultDisplay().getSize(screenSizePoint);
        this.displayWidth = screenSizePoint.x;
        this.displayHeight = screenSizePoint.y;
        this.helpLayout.setElevation(15);
        this.helpLayout.setFocusable(View.FOCUSABLE);
        this.helpLayout.setClickable(true);
        this.topMarginOffset = (isWithOffset ? FRAGMENT_TOP_MARGIN_OFFSET : 0 );
    }

    /**
     * Add help arrow to the view on screen with corresponding String
     * @param view view to annotate
     * @param textToDisplayAsHelp String to display for annotation
     */
    public void addViewToInfoScreen(View view, String textToDisplayAsHelp) {
        this.viewsToShowHelp.add(view);
        this.helpMessages.add(textToDisplayAsHelp);
    }

    /**
     * Adding annotation to some point on the screen
     * @param point Point data type (x, y)
     * @param textToDisplayAsHelp String to display for annotation
     */
    public void addPointToAnnotate(Point point, String textToDisplayAsHelp) {
        this.pointsToAnnotate.add(point);
        this.pointsMessages.add(textToDisplayAsHelp);
    }

    /**
     * Method for displaying/showing the help in appropriate form
     */
    public void show() {
        for (int i = 0; i < viewsToShowHelp.size(); i++) {
            // retrieving view to annotate
            View currentViewToAnnotate = viewsToShowHelp.get(i);
            boolean isBottom = isOnBottomSide(currentViewToAnnotate);

            TextView tv = new TextView(context);
            tv.setId(View.generateViewId());
            tv.setTextColor(Color.WHITE);
            tv.setText(helpMessages.get(i));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            helpLayout.addView(tv);
            tv.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

            ImageView iv = new ImageView(context);
            iv.setMaxWidth(currentViewToAnnotate.getWidth());
            iv.setMaxHeight(currentViewToAnnotate.getHeight()*2);
            iv.setAdjustViewBounds(true);
            iv.setId(View.generateViewId());
//            iv.setBackgroundColor(Color.RED);
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.arrow_down);
            iv.setImageDrawable(drawable);
            iv.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

            // flip vertically
            if (!isBottom) iv.setScaleY(-1.0f);

            // arrow params
            ConstraintSet newSet = new ConstraintSet();
            iv.requestLayout();
            int leftMargin = computeStartMargin(currentViewToAnnotate, iv) - iv.getMeasuredWidth()/2;
            int topMargin;
            if (isBottom) {
                topMargin = computeTopMargin(currentViewToAnnotate) - iv.getMeasuredHeight();
            } else {
                topMargin = computeTopMargin(currentViewToAnnotate);
            }

            helpLayout.addView(iv);
            newSet.clone(helpLayout);

            newSet.connect(iv.getId(), ConstraintSet.TOP, helpLayout.getId(), ConstraintSet.TOP, topMargin);
            newSet.connect(iv.getId(), ConstraintSet.START, helpLayout.getId(), ConstraintSet.START, leftMargin);
            newSet.applyTo(helpLayout);

            newSet = new ConstraintSet();

            newSet.clone(helpLayout);

            tv.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            // if the view is on the bottom half
            if (isBottom) {
                newSet.connect(tv.getId(), ConstraintSet.BOTTOM, iv.getId(), ConstraintSet.TOP);
            } else {
                newSet.connect(tv.getId(), ConstraintSet.TOP, iv.getId(), ConstraintSet.BOTTOM);
            }

            newSet.connect(tv.getId(), ConstraintSet.START, iv.getId(), ConstraintSet.START);
            newSet.connect(tv.getId(), ConstraintSet.END, iv.getId(), ConstraintSet.END);

            newSet.applyTo(helpLayout);
            int maxWidth;
            if (isOnRightSide(currentViewToAnnotate)) {
                maxWidth = (int) (displayWidth - currentViewToAnnotate.getX());
            } else {
                maxWidth = (int) currentViewToAnnotate.getX() + currentViewToAnnotate.getWidth() / 2;
            }

            tv.setMaxWidth(maxWidth);
            tv.setGravity(Gravity.CENTER_HORIZONTAL);

        }

        // generating help for points
        for (int i = 0; i < pointsToAnnotate.size(); i++) {
            Point point = pointsToAnnotate.get(i);
            boolean isRight = isOnRightSide(point);
            boolean isBottom = isOnBottomSide(point);

            ImageView iv = new ImageView(context);
            int ivHeight = (isRight ? (displayWidth - point.x) : point.x);
            int ivWidth = ivHeight / 2;
            iv.setMaxWidth(ivWidth);
            iv.setMaxHeight(ivHeight);
            iv.setAdjustViewBounds(true);
            iv.setId(View.generateViewId());
//            iv.setBackgroundColor(Color.RED);
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.arrow_down);
            iv.setImageDrawable(drawable);
            //iv.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

            // flip vertically
            if (!isBottom) iv.setScaleY(-1.0f);

            int topMargin = (isBottom ? (point.y - iv.getMeasuredHeight()) : point.y);
            int leftMargin = point.x + iv.getMeasuredWidth() / 2;

            TextView tv = new TextView(context);
            tv.setId(View.generateViewId());
            tv.setText(pointsMessages.get(i));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            tv.setTextColor(Color.WHITE);
            int maxWidth = (isRight ? (displayWidth - point.x) :  point.x);

            tv.setMaxWidth(maxWidth);
            tv.setGravity(Gravity.CENTER_HORIZONTAL);

            ConstraintSet newSet = new ConstraintSet();

            helpLayout.addView(iv);
            helpLayout.addView(tv);

            newSet.clone(helpLayout);

            newSet.connect(iv.getId(), ConstraintSet.TOP, helpLayout.getId(), ConstraintSet.TOP, topMargin);
            newSet.connect(iv.getId(), ConstraintSet.START, helpLayout.getId(), ConstraintSet.START, leftMargin);
            newSet.applyTo(helpLayout);

            newSet = new ConstraintSet();

            newSet.clone(helpLayout);

            tv.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            // if the view is on the bottom half
            if (isBottom) {
                newSet.connect(tv.getId(), ConstraintSet.BOTTOM, iv.getId(), ConstraintSet.TOP);
            } else {
                newSet.connect(tv.getId(), ConstraintSet.TOP, iv.getId(), ConstraintSet.BOTTOM);
            }

            newSet.connect(tv.getId(), ConstraintSet.START, iv.getId(), ConstraintSet.START);
            newSet.connect(tv.getId(), ConstraintSet.END, iv.getId(), ConstraintSet.END);

            newSet.applyTo(helpLayout);
        }

        // finally view the layout
        helpLayout.setVisibility(View.VISIBLE);
    }

    /**
     * Resets whole help screen
     */
    public void resetHelpScreen() {
        this.helpLayout.removeAllViews();
        this.helpLayout.setVisibility(View.GONE);
    }

    /**
     * make help layout go gone
     */
    public void makeHelpScreenGone() {
        this.helpLayout.setVisibility(View.GONE);
    }

    /**
     * Checks if the view is on the right side
     */
    public boolean isOnRightSide(@NonNull View view) {
        int centerX = (int) view.getX() + view.getWidth() / 2;

        return centerX >= displayWidth / 2;
    }

    /**
     * Checks if the point is on the right side or not
     */
    public boolean isOnRightSide(@NonNull Point point) {
        return point.x >= displayWidth / 2;
    }

    /**
     * Checks whether is the view on the bottom (half) side of the screen
     */
    public boolean isOnBottomSide(@NonNull View view) {
        int centerY = (int) view.getY() + view.getHeight() / 2;

        return centerY >= displayHeight / 2;
    }

    /**
     * Checks whether is the point on the bottom half side of the screen
     */
    public boolean isOnBottomSide(@NonNull Point point) {
        return point.y >= displayHeight / 2;
    }

    /**
     * Computes left margin for the arrow
     */
    public int computeStartMargin(View view, ImageView iv) {
        return (int) (view.getX() + view.getWidth() / 2) - (iv.getWidth()/2);
    }

    /**
     * Computes left margin of the view
     */
    public int computeStartMargin(View view) {
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

        int y = (int) view.getY();
        int height = view.getMeasuredHeight();
        int result;

        if (isBottom) {
            result = y + topMarginOffset;
        } else {
            result =  (int) y + height + topMarginOffset;
        }

        return result;
    }

    /**
     * Check whether is the help screen visible
     * @return boolean value if the help layout s visible
     */
    public boolean isHelpScreenVisible() {
        return this.helpLayout.getVisibility() == View.VISIBLE;
    }

    /**
     * Add a drawable into the middle of the screen in help layout
     * @param drawable drawable to display
     * @param textToViewUnderDrawable String to display under ImageView
     * @param view view to align hint image too
     */
    public void addHintImage(Drawable drawable, String textToViewUnderDrawable, View view) {
        ImageView iv = new ImageView(context);
        iv.setAdjustViewBounds(true);
        iv.setId(View.generateViewId());
//            iv.setBackgroundColor(Color.RED);
//        drawable = ContextCompat.getDrawable(context, R.drawable.arrow_down);
        iv.setImageDrawable(drawable);
        iv.setMaxWidth(view.getWidth());
        iv.setMaxHeight(view.getHeight());

        TextView tv = new TextView(context);
        tv.setId(View.generateViewId());
        tv.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        tv.setText(textToViewUnderDrawable);
        tv.setPadding(10,0,10,0);

        helpLayout.addView(iv);
        helpLayout.addView(tv);


        ConstraintSet newSet = new ConstraintSet();
        newSet.clone(helpLayout);
        int marginTop = (int) view.getY() - 30;
        int marginStart = (int) view.getX();

        newSet.connect(iv.getId(), ConstraintSet.START, helpLayout.getId(), ConstraintSet.START, marginStart);
        newSet.connect(iv.getId(), ConstraintSet.TOP, helpLayout.getId(), ConstraintSet.TOP, marginTop);

        newSet.connect(tv.getId(), ConstraintSet.START, iv.getId(), ConstraintSet.START, 15);
        newSet.connect(tv.getId(), ConstraintSet.END, iv.getId(), ConstraintSet.END, 15);
        newSet.connect(tv.getId(), ConstraintSet.TOP, iv.getId(), ConstraintSet.BOTTOM, DEFAULT_MARGIN);


        newSet.applyTo(helpLayout);
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
