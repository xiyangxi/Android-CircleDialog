package com.mylhyl.circledialog.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.mylhyl.circledialog.CircleParams;
import com.mylhyl.circledialog.Controller;
import com.mylhyl.circledialog.params.DialogParams;
import com.mylhyl.circledialog.params.PopupParams;
import com.mylhyl.circledialog.res.drawable.TriangleDrawable;
import com.mylhyl.circledialog.view.listener.ItemsView;

import java.util.LinkedList;
import java.util.Queue;

import static com.mylhyl.circledialog.params.PopupParams.TRIANGLE_BOTTOM_CENTER;
import static com.mylhyl.circledialog.params.PopupParams.TRIANGLE_BOTTOM_LEFT;
import static com.mylhyl.circledialog.params.PopupParams.TRIANGLE_BOTTOM_RIGHT;
import static com.mylhyl.circledialog.params.PopupParams.TRIANGLE_LEFT_BOTTOM;
import static com.mylhyl.circledialog.params.PopupParams.TRIANGLE_LEFT_CENTER;
import static com.mylhyl.circledialog.params.PopupParams.TRIANGLE_LEFT_TOP;
import static com.mylhyl.circledialog.params.PopupParams.TRIANGLE_RIGHT_BOTTOM;
import static com.mylhyl.circledialog.params.PopupParams.TRIANGLE_RIGHT_CENTER;
import static com.mylhyl.circledialog.params.PopupParams.TRIANGLE_RIGHT_TOP;
import static com.mylhyl.circledialog.params.PopupParams.TRIANGLE_TOP_CENTER;
import static com.mylhyl.circledialog.params.PopupParams.TRIANGLE_TOP_LEFT;
import static com.mylhyl.circledialog.params.PopupParams.TRIANGLE_TOP_RIGHT;

/**
 * Created by hupei on 2018/8/15.
 */

public final class BuildViewPopupImpl extends BuildViewAbs {

    private static final int GRAVITY_TOP_CENTER = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
    private static final int GRAVITY_BOTTOM_CENTER = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
    private static final int GRAVITY_LEFT_TOP = Gravity.LEFT | Gravity.TOP;
    private static final int GRAVITY_LEFT_BOTTOM = Gravity.LEFT | Gravity.BOTTOM;
    private static final int GRAVITY_LEFT_CENTER = Gravity.LEFT | Gravity.CENTER_VERTICAL;
    private static final int GRAVITY_RIGHT_TOP = Gravity.RIGHT | Gravity.TOP;
    private static final int GRAVITY_RIGHT_BOTTOM = Gravity.RIGHT | Gravity.BOTTOM;
    private static final int GRAVITY_RIGHT_CENTER = Gravity.RIGHT | Gravity.CENTER_VERTICAL;

    private static final float TRIANGLE_WEIGHT = 0.1f;
    private LinearLayout mTriangleLinearLayout;
    private View mTriangleView;
    private ItemsView mItemsView;
    private int mTriangleDirection;
    private int mTriangleGravity;
    private int[] mScreenSize;
    private int mStatusBarHeight;
    private Controller.OnDialogLocationListener mResizeSizeListener;
    private Queue<Integer> mRemoveOnLayoutChangeListenerStrategy = new LinkedList<>();

    public BuildViewPopupImpl(Context context, CircleParams params, Controller.OnDialogLocationListener listener
            , int[] screenSize, int statusBarHeight) {
        super(context, params);
        this.mResizeSizeListener = listener;
        this.mScreenSize = screenSize;
        this.mStatusBarHeight = statusBarHeight;
    }

    @Override
    public ItemsView getBodyView() {
        return mItemsView;
    }

    @Override
    public void buildBodyView() {
        if (mParams.dialogParams.width != 1f)
            mParams.dialogParams.width = LayoutParams.WRAP_CONTENT;

        final PopupParams popupParams = mParams.popupParams;

        switch (popupParams.triangleGravity) {
            case TRIANGLE_LEFT_TOP:
                mTriangleDirection = Gravity.LEFT;
                mTriangleGravity = Gravity.TOP;
                mParams.dialogParams.gravity = GRAVITY_LEFT_TOP;
                break;
            case TRIANGLE_TOP_LEFT:
                mTriangleDirection = Gravity.TOP;
                mTriangleGravity = Gravity.LEFT;
                mParams.dialogParams.gravity = GRAVITY_LEFT_TOP;
                break;
            case TRIANGLE_LEFT_BOTTOM:
                mTriangleDirection = Gravity.LEFT;
                mTriangleGravity = Gravity.BOTTOM;
                mParams.dialogParams.gravity = GRAVITY_LEFT_BOTTOM;
                break;
            case TRIANGLE_BOTTOM_LEFT:
                mTriangleDirection = Gravity.BOTTOM;
                mTriangleGravity = Gravity.LEFT;
                mParams.dialogParams.gravity = GRAVITY_LEFT_BOTTOM;
                break;
            case TRIANGLE_LEFT_CENTER:
                mTriangleDirection = Gravity.LEFT;
                mTriangleGravity = Gravity.CENTER_VERTICAL;
                mParams.dialogParams.gravity = GRAVITY_LEFT_CENTER;
                break;
            case TRIANGLE_RIGHT_TOP:
                mTriangleDirection = Gravity.RIGHT;
                mTriangleGravity = Gravity.TOP;
                mParams.dialogParams.gravity = GRAVITY_RIGHT_TOP;
                break;
            case TRIANGLE_TOP_RIGHT:
                mTriangleDirection = Gravity.TOP;
                mTriangleGravity = Gravity.RIGHT;
                mParams.dialogParams.gravity = GRAVITY_RIGHT_TOP;
                break;
            case TRIANGLE_RIGHT_BOTTOM:
                mTriangleDirection = Gravity.RIGHT;
                mTriangleGravity = Gravity.BOTTOM;
                mParams.dialogParams.gravity = GRAVITY_RIGHT_BOTTOM;
                break;
            case TRIANGLE_BOTTOM_RIGHT:
                mTriangleDirection = Gravity.BOTTOM;
                mTriangleGravity = Gravity.RIGHT;
                mParams.dialogParams.gravity = GRAVITY_RIGHT_BOTTOM;
                break;
            case TRIANGLE_RIGHT_CENTER:
                mTriangleDirection = Gravity.RIGHT;
                mTriangleGravity = Gravity.CENTER_VERTICAL;
                mParams.dialogParams.gravity = GRAVITY_RIGHT_CENTER;
                break;
            case TRIANGLE_TOP_CENTER:
                mTriangleDirection = Gravity.TOP;
                mTriangleGravity = Gravity.CENTER_HORIZONTAL;
                mParams.dialogParams.gravity = GRAVITY_TOP_CENTER;
                break;
            case TRIANGLE_BOTTOM_CENTER:
                mTriangleDirection = Gravity.BOTTOM;
                mTriangleGravity = Gravity.CENTER_HORIZONTAL;
                mParams.dialogParams.gravity = GRAVITY_BOTTOM_CENTER;
                break;
        }

        LinearLayout rootLinearLayout = buildLinearLayout();
        rootLinearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        //箭头在左右情况，布局改为水平
        if (mTriangleDirection == Gravity.LEFT || mTriangleDirection == Gravity.RIGHT) {
            rootLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        }

        mRoot = rootLinearLayout;
        if (popupParams.triangleShow) {
            mTriangleLinearLayout = new LinearLayout(mContext);
            if (mTriangleDirection == Gravity.TOP || mTriangleDirection == Gravity.BOTTOM) {
                mTriangleLinearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            } else {
                mTriangleLinearLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
            }
            mTriangleView = new View(mContext);
            mTriangleView.setLayoutParams(new LayoutParams(50, 50));
            mTriangleLinearLayout.addView(mTriangleView);
            int backgroundColor = popupParams.backgroundColor != 0
                    ? popupParams.backgroundColor : mParams.dialogParams.backgroundColor;
            Drawable triangleDrawable = new TriangleDrawable(mTriangleDirection, backgroundColor);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mTriangleView.setBackground(triangleDrawable);
            } else {
                mTriangleView.setBackgroundDrawable(triangleDrawable);
            }
        }
        CardView cardView = buildCardView();
        cardView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
        mItemsView = new BodyRecyclerView(mContext, mParams.popupParams
                , mParams.dialogParams, mParams.rvItemListener);
        final View recyclerView = mItemsView.getView();
        cardView.addView(recyclerView);

        if (mTriangleDirection == Gravity.LEFT || mTriangleDirection == Gravity.TOP) {
            if (popupParams.triangleShow) {
                mRoot.addView(mTriangleLinearLayout);
            }
            mRoot.addView(cardView);
        } else {
            mRoot.addView(cardView);
            if (popupParams.triangleShow) {
                mRoot.addView(mTriangleLinearLayout);
            }
        }
        mRoot.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom
                    , int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (mParams.dialogParams.width == LayoutParams.WRAP_CONTENT
                        || mParams.dialogParams.width != LayoutParams.MATCH_PARENT) {
                    mParams.dialogParams.width = mRoot.getWidth();
                }
                handleAtLocation(popupParams, bottom, oldBottom, this);
            }
        });
    }

    @Override
    public void refreshContent() {
        if (mItemsView != null) {
            mItemsView.refreshItems();
        }
    }

    private void handleAtLocation(PopupParams popupParams, int bottom, int oldBottom
            , View.OnLayoutChangeListener layoutChangeListener) {

        if (popupParams.triangleShow && mTriangleView != null) {
            final LayoutParams triangleViewLayoutParams = (LayoutParams) mTriangleView.getLayoutParams();
            if (popupParams.triangleSize == null) {
                int triangleWidth = (int) (mParams.dialogParams.width * TRIANGLE_WEIGHT);
                triangleViewLayoutParams.width = triangleWidth;
                triangleViewLayoutParams.height = triangleWidth;
            } else {
                int[] triangleSize = popupParams.triangleSize;
                triangleViewLayoutParams.width = triangleSize[0];
                triangleViewLayoutParams.height = triangleSize[1];
            }

            if (bottom != 0 && oldBottom != 0 && bottom == oldBottom) {

                popupParams.triangleOffSet = triangleViewLayoutParams.width;
                if (mTriangleDirection == Gravity.LEFT || mTriangleDirection == Gravity.RIGHT) {
                    int topMargin = popupParams.triangleOffSet;
                    if (mTriangleGravity == Gravity.CENTER_VERTICAL) {
                        popupParams.triangleOffSet = (mRoot.getHeight() / 2);
                        topMargin = popupParams.triangleOffSet - triangleViewLayoutParams.width / 2;
                    } else if (mTriangleGravity == Gravity.BOTTOM) {
                        topMargin = mRoot.getHeight() - popupParams.triangleOffSet * 2;
                    }
                    triangleViewLayoutParams.topMargin = topMargin;
                } else {
                    int leftMargin = popupParams.triangleOffSet;
                    if (mTriangleGravity == Gravity.CENTER_HORIZONTAL) {
                        popupParams.triangleOffSet = (mRoot.getWidth() / 2);
                        leftMargin = popupParams.triangleOffSet - triangleViewLayoutParams.width / 2;
                    } else if (mTriangleGravity == Gravity.RIGHT) {
                        leftMargin = mRoot.getWidth() - popupParams.triangleOffSet * 2;
                    }
                    triangleViewLayoutParams.leftMargin = leftMargin;
                }

                mRemoveOnLayoutChangeListenerStrategy.add(oldBottom);
                boolean removeOnLayoutChangeListener = true;
                if (mRemoveOnLayoutChangeListenerStrategy.size() == 3) {
                    int pollFirst = mRemoveOnLayoutChangeListenerStrategy.poll();
                    while (!mRemoveOnLayoutChangeListenerStrategy.isEmpty()) {
                        Integer poll = mRemoveOnLayoutChangeListenerStrategy.poll();
                        if (poll != null && pollFirst != poll.intValue()) {
                            removeOnLayoutChangeListener = false;
                            break;
                        }
                    }
                    if (removeOnLayoutChangeListener) {
                        mRoot.removeOnLayoutChangeListener(layoutChangeListener);
                    }
                }
                resizeDialogSize(mParams.dialogParams, popupParams.anchorView
                        , mTriangleDirection, mTriangleGravity, popupParams.triangleOffSet
                        , triangleViewLayoutParams.width);
            }
            mTriangleView.setLayoutParams(triangleViewLayoutParams);
        } else {
            mRoot.removeOnLayoutChangeListener(layoutChangeListener);
            resizeDialogSize(mParams.dialogParams, popupParams.anchorView
                    , mTriangleDirection, mTriangleGravity, popupParams.triangleOffSet, 0);
        }
    }

    private void resizeDialogSize(DialogParams dialogParams, View anchorView
            , int triangleDirection, int triangleGravity, int triangleOffSet
            , int triangleSize) {
        final View view = anchorView;
        final int[] location = new int[2];
        view.getLocationOnScreen(location);
        final int screenWidth = mScreenSize[0];

        int dialogX = triangleDirection == Gravity.TOP || triangleDirection == Gravity.BOTTOM
                ? view.getWidth() / 2 : view.getWidth();
        if (triangleGravity == Gravity.LEFT) {
            dialogX += location[0] - triangleSize / 2 - triangleOffSet;
        } else if (triangleGravity == Gravity.RIGHT) {
            dialogX = screenWidth - location[0] - dialogX - triangleSize / 2 - triangleOffSet;
        } else if (triangleGravity == Gravity.CENTER_HORIZONTAL) {
            dialogX = -1 * (screenWidth / 2 - location[0]) + dialogX;
        } else {
            dialogX += 0;
        }
        dialogParams.xOff = dialogX;

        final int screenHeight = mScreenSize[1];
        int dialogY;
        if (triangleGravity == Gravity.TOP) {
            dialogY = location[1] - mStatusBarHeight + view.getHeight() / 2 - triangleSize / 2 - triangleOffSet;
        } else if (triangleGravity == Gravity.BOTTOM) {
            dialogY = screenHeight - location[1] - view.getHeight() / 2 - triangleSize / 2 - triangleOffSet;
        } else if (triangleGravity == Gravity.CENTER_VERTICAL) {
            dialogY = -1 * (screenHeight / 2 - location[1]) - mStatusBarHeight / 2 + view.getHeight() / 2;
        } else {
            if (triangleDirection == Gravity.TOP
                    && (triangleGravity == Gravity.LEFT || triangleGravity == Gravity.RIGHT
                    || triangleGravity == Gravity.CENTER_HORIZONTAL)) {
                dialogY = location[1] - mStatusBarHeight + view.getHeight();
            } else {
                dialogY = screenHeight - location[1];
            }
        }
        dialogParams.yOff = dialogY;

        if (mResizeSizeListener != null) {
            mResizeSizeListener.dialogAtLocation(dialogParams.xOff, dialogParams.yOff);
        }
    }
}

