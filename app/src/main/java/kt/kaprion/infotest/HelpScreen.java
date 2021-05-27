package kt.kaprion.infotest;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
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


    @Override
    public void onClick(View v) {
//        displayCurrentHelp();
        this.helpLayout.setVisibility(View.GONE);
    }
}
