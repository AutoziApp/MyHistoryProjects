/**
 * <pre>
 * Copyright 2015 Soulwolf Ching
 * Copyright 2015 The Android Open Source Project for android-dialog-builder
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </pre>
 */
package net.soulwolf.widget.dialogbuilder;


import com.goldnut.app.sdk.R;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.IdRes;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;

/**
 * author: Soulwolf Created on 2015/8/4 21:41.
 * email : Ching.Soulwolf@gmail.com
 */
public abstract class MasterDialog implements IMasterDialog{

    protected ViewGroup mDecorView;

    protected DialogBuilder mDialogBuilder;

    protected View mContentView;

    protected View mShadowView;

    protected ViewGroup mContainer;

    protected Activity mContext;

    protected boolean mShowing;

    protected boolean mCancelable;

    protected boolean isPlaying;
    
    protected String TAG;

    public MasterDialog(DialogBuilder builder){
        if(builder == null){
            throw new NullPointerException("DialogBuilder == null");
        }
        this.mDialogBuilder = builder;
        this.mCancelable = mDialogBuilder.isCancelable();
        this.mContext = mDialogBuilder.getContext();
        this.mDecorView = (ViewGroup) mContext.getWindow().getDecorView();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        this.mContainer = (ViewGroup) inflater.inflate(R.layout.dl_dialog_container,mDecorView,false);
        this.mShadowView = mContainer.findViewById(R.id.di_dialog_container_shadow);
        this.mContentView = onCreateView(inflater, mContainer);
        this.mContentView.setBackgroundDrawable(mDialogBuilder.getBackground());
        parserLayoutGravity();

    }

    private void parserLayoutGravity() {
        this.mContainer.addView(mContentView, mDialogBuilder.getLayoutParams());
        //this.mContainer.setPadding(0, 0, 0, getNavigationBarHeight());
    }

    protected abstract View onCreateView(LayoutInflater inflater,ViewGroup container);

    @Override
    public void show() {
        if(isShowing()){
            return;
        }
        attachToWindow();
        if(mDialogBuilder.getOnShowListener() != null){
            mDialogBuilder.getOnShowListener().onShow(this);
        }
    }

    private int getNavigationBarHeight(){
        return Utils.getNavigationBarHeight(getContext());
    }

    private void attachToWindow() {
        if(isShowShadow()){
            mShadowView.setVisibility(View.VISIBLE);
        }else {
            mShadowView.setVisibility(View.GONE);
        }
        mDecorView.addView(mContainer, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        playDialogInAnimation();
        mContainer.requestFocus();
        mContainer.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getAction() == KeyEvent.ACTION_UP) {
                    if (mDialogBuilder.getOnKeyListener() != null) {
                        return onBackPressed()
                                || mDialogBuilder.getOnKeyListener()
                                .onKey(MasterDialog.this, keyCode, event);
                    }
                    return onBackPressed();
                }
                return false;
            }
        });
        mShadowView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return onBackPressed();
            }
        });
        mShowing = true;
    }

    public boolean onBackPressed(){
        if(mCancelable){
            cancel();
        }
        return mCancelable;
    }

    protected void playDialogInAnimation() {
        clearAnimation();
        Animation inAnimation = mDialogBuilder.getDialogInAnimation();
        if(inAnimation != null){
            mContentView.startAnimation(inAnimation);
            if(isShowShadow()){
                AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f,1.0f);
                alphaAnimation.setDuration(inAnimation.getDuration());
                mShadowView.startAnimation(alphaAnimation);
            }
        }
    }


    @Override
    public void cancel() {
        if(isShowing()){
            dismiss();
            if(mDialogBuilder.getOnCancelListener() != null){
                mDialogBuilder.getOnCancelListener().onCancel(this);
            }
        }
    }

    @Override
    public void dismiss() {
        if(isShowing()){
            Animation outAnimation = mDialogBuilder.getDialogOutAnimation();
            if(outAnimation != null){
                playDialogOutAnimation(outAnimation);
            }else {
                detachedFromWindow();
            }
        }
    }

    private void playDialogOutAnimation(Animation outAnimation) {
        if(isPlaying){
            return;
        }
        clearAnimation();
        outAnimation.setAnimationListener(new SimpleAnimationListener(){
            @Override
            public void onAnimationEnd(Animation animation) {
                isPlaying = false;
                detachedFromWindow();
            }
        });
        isPlaying = true;
        mContentView.startAnimation(outAnimation);
        if(isShowShadow()){
            AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.0f);
            alphaAnimation.setDuration(outAnimation.getDuration());
            mShadowView.startAnimation(alphaAnimation);
        }
    }

    private void clearAnimation(){
        mContainer.clearAnimation();
        mShadowView.clearAnimation();
    }

    private void detachedFromWindow() {
        mDecorView.removeView(mContainer);
        if(mDialogBuilder.getOnDismissListener() != null){
            mDialogBuilder.getOnDismissListener().onDismiss(this);
        }
        mShowing = false;
    }

    @Override
    public boolean isShowing() {
        return mShowing;
    }

    public View findViewById(@IdRes int id){
        if(mContentView != null){
            return mContentView.findViewById(id);
        }
        return null;
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    protected boolean isShowShadow(){
        return true;
    }
    
    public void setTag(String tag){
    	this.TAG = tag;
    }
    
    public String getTag(){
    	return this.TAG;
    }
}
