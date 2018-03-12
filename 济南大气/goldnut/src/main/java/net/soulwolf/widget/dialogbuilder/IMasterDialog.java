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

import android.content.Context;

/**
 * author: Soulwolf Created on 2015/8/4 21:36.
 * email : Ching.Soulwolf@gmail.com
 */
interface IMasterDialog{

    /**
     * The dialog add to window!
     */
    public void show();

    /**
     * The cancel dialog!
     */
    public void cancel();

    /**
     * The dialog from window remove!
     */
    public void dismiss();

    /**
     * The current dialog display state!
     * @return current dialog display state!
     */
    public boolean isShowing();


    /**
     * Get dialog context!
     * @return dialog context!
     */
    public Context getContext();
}
