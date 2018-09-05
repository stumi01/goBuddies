/*
 * Copyright Txus Ballesteros 2015 (@txusballesteros)
 *
 * This file is part of some open source application.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 * Contact: Txus Ballesteros <txus.ballesteros@gmail.com>
 */
package com.stumi.gobuddies.system.bubble;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

public abstract class BubblesService extends Service {

    private final BubblesServiceBinder binder = new BubblesServiceBinder();

    private final List<BubbleLayout> bubbles = new ArrayList<>();

    private BubbleTrashLayout bubblesTrash;

    private WindowManager windowManager;

    private BubblesLayoutCoordinator layoutCoordinator;

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        for (BubbleLayout bubble : bubbles) {
            recycleBubble(bubble);
        }
        bubbles.clear();
        return super.onUnbind(intent);
    }

    private void recycleBubble(final BubbleLayout bubble) {
        new Handler(Looper.getMainLooper()).post(() -> {
            getWindowManager().removeView(bubble);
            for (BubbleLayout cachedBubble : bubbles) {
                if (cachedBubble == bubble) {
                    bubble.notifyBubbleRemoved();
                    bubbles.remove(cachedBubble);
                    break;
                }
            }
        });
    }

    private WindowManager getWindowManager() {
        if (windowManager == null) {
            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        }
        return windowManager;
    }

    public void removeBubble(BubbleLayout bubble) {
        recycleBubble(bubble);
    }

    public void bubbleTrashed(BubbleLayout bubble) {
        removeBubble(bubble);
    }

    public class BubblesServiceBinder extends Binder {

        public BubblesService getService() {
            return BubblesService.this;
        }
    }
}