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

import android.graphics.drawable.Drawable;
import android.support.annotation.AnimRes;
import android.view.animation.Animation;

/**
 * author: Soulwolf Created on 2015/8/4 22:54.
 * email : Ching.Soulwolf@gmail.com
 */
interface IDialogBuilder {

    /**
     * Set dialog cancel callback !
     * Such as a touch of the area outside of Dialog!
     * or press back key,Cancel button click!
     * @param listener cancel listener{@link OnCancelListener};
     *
     * @see IMasterDialog#cancel() ;
     * @return current instance!
     */
    public DialogBuilder setOnCancelListener(OnCancelListener listener);

    /**
     * Set dialog list button click!
     * @param listener item click listener {@link OnItemClickListener}
     * @return current instance!
     */
    public DialogBuilder setOnItemClickListener(OnItemClickListener listener);

    /**
     * Set dialog from window remove callback!
     * @param listener dialog dismiss listener!
     *
     * @see IMasterDialog#dismiss()
     * @return current instance!
     */
    public DialogBuilder setOnDismissListener(OnDismissListener listener);

    /**
     * Set dialog add to window callback!
     * @param listener dialog add to window!
     *
     * @see IMasterDialog#show() ;
     * @return current instance!
     */
    public DialogBuilder setOnShowListener(OnShowListener listener);

    /**
     * Set key pressed listener!
     * @param listener key press listener!
     * @return current instance!
     */
    public DialogBuilder setOnKeyListener(OnKeyListener listener);

    /**
     * Set dialog in window and out window animation resource!
     * @param in dialog in window animation resource!
     * @param out dialog out window animation resource!
     * @return current instance!
     */
    public DialogBuilder setAnimation(@AnimRes int in,@AnimRes int out);

    /**
     * Set dialog in window and out window animation!
     * @param in dialog in window animation!
     * @param out dialog out window animation!
     * @return current instance!
     */
    public DialogBuilder setAnimation(Animation in,Animation out);

    /**
     *
     * @param isCancelable
     * @return current instance!
     */
    public DialogBuilder setCancelable(boolean isCancelable);

    public DialogBuilder setLayoutParams(int width,int height);

    public DialogBuilder setGravity(@Gravity.GravityMode int gravity);

    public DialogBuilder setMargin(int l,int t,int r,int b);

    public DialogBuilder setBackground(int color);

    public DialogBuilder setBackgroundResource(int resource);

    public DialogBuilder setBackground(Drawable drawable);

    public DialogBuilder setIgnoreStatusBar(boolean isIgnoreStatusBar);
}
