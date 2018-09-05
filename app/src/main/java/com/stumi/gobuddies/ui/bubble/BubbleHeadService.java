package com.stumi.gobuddies.ui.bubble;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stumi.gobuddies.GoBuddiesApplication;
import com.stumi.gobuddies.R;
import com.stumi.gobuddies.system.bubble.BubbleLayout;
import com.stumi.gobuddies.system.bubble.BubblesService;
import com.stumi.gobuddies.system.floatingview.FloatingView;
import com.stumi.gobuddies.system.floatingview.FloatingViewListener;
import com.stumi.gobuddies.system.floatingview.FloatingViewManager;
import com.stumi.gobuddies.ui.UIUtils;
import com.stumi.gobuddies.ui.floating.FloatingLayer;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.stumi.gobuddies.system.IntentManager.START_CHAT;
import static com.stumi.gobuddies.system.floatingview.FloatingViewManager.SHAPE_CIRCLE;

/**
 * @author stumpfb on 08/09/2016.
 */
public class BubbleHeadService extends BubblesService
        implements BubbleLayout.OnBubbleRemoveListener,
        BubbleLayout.OnBubbleClickListener, BubbleHeadView, FloatingViewListener,
        View.OnClickListener, FloatingView.MoveListener {

    private static final String TAG = "BubbleHeadService";

    @BindView(R.id.notification_messages)
    TextView messages;

    @BindView(R.id.notification_teams)
    TextView teams;

    @BindView(R.id.notification_requests)
    TextView requests;

    @BindView(R.id.bubble_team_image)
    ImageView bubble;

    @BindView(R.id.head_leader_icon)
    ImageView crown;

    private int lastBubbleImageResource = -1;

    @Inject
    BubbleHeadPresenter presenter;

    private BubbleLayout bubbleLayout;

    private FloatingLayer chatLayer;

    private FloatingViewManager floatingViewManager;

    private int width;

    @Override
    public void onCreate() {
        super.onCreate();
        injectDependencies();
        presenter.attachView(this);
        presenter.onCreate();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }


    @Override
    public IBinder onBind(Intent intent) {
        boolean startChat = intent.getBooleanExtra(START_CHAT, false);
        if (startChat) {
            showFloatingView(0, 0);
        }
        return super.onBind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getBooleanExtra(START_CHAT, false)) {
            showFloatingView(0, 0);
        }
        return START_STICKY;
    }

    //The incoming x y is the middle of the desired layout
    @Override
    public void initView(int x, int y) {
        floatingViewManager = new FloatingViewManager(this, this);
        floatingViewManager.setActionTrashIconImage(R.drawable.bubble_cross);

        addHead(x, y);

        messages.setVisibility(View.GONE);
        requests.setVisibility(View.GONE);
        this.chatLayer = new FloatingLayer(this);
    }

    public void addHead(int x, int y) {
        final FloatingViewManager.Options options = new FloatingViewManager.Options();
        final DisplayMetrics metrics = new DisplayMetrics();
        final WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;
        options.overMargin = (int) (16 * metrics.density);
        options.animateInitialMove = true;
        options.shape = SHAPE_CIRCLE;
        options.floatingViewX = x;
        options.floatingViewY = y;

        bubbleLayout = (BubbleLayout) LayoutInflater.from(this).inflate(R.layout.bubble_layout, null, false);
        ButterKnife.bind(this, bubbleLayout);

        bubbleLayout.setOnClickListener(this);
        bubbleLayout.setOnBubbleClickListener(this);
        bubbleLayout.setOnBubbleRemoveListener(this);

        floatingViewManager.addViewToWindow(bubbleLayout, options, this);
        if (lastBubbleImageResource != -1) {
            bubble.setImageResource(lastBubbleImageResource);
        }
    }

    @Override
    public void showFloatingView(int x, int y) {
        if (!chatLayer.isShown()) {
            floatingViewManager.removeAllViewToWindow();
            chatLayer.showView(x, y);
        }
    }

    @Override
    public void disableHeadClick() {
        bubbleLayout.setOnBubbleClickListener(null);
        bubbleLayout.setEnabled(false);
    }

    private void injectDependencies() {
        DaggerBubbleHeadComponent.builder()
                .applicationComponent(GoBuddiesApplication.get(this).getApplicationComponent()).build()
                .inject(this);
    }

    @Override
    public void bubbleTrashed(BubbleLayout bubble) {
        super.bubbleTrashed(bubble);
        presenter.bubbleTrashed();
    }

    @Override
    public void onBubbleClick(BubbleLayout bubble, MotionEvent event) {
        int[] coords = new int[2];
        bubble.getLocationOnScreen(coords);
        presenter.bubbleClick(coords[0], coords[1], event.getRawX(), event.getRawY());
    }

    @Override
    public void setUnreadMessages(int mess) {
        messages.setText("" + mess);
        showIfValueGreaterThanZero(messages, mess);

    }

    @Override
    public void setNearParties(int near) {
        teams.setText("" + near);
    }

    @Override
    public void setUnseenRequests(int reqs) {
        requests.setText("" + reqs);
        showIfValueGreaterThanZero(requests, reqs);
    }

    @Override
    public void setGroupName(String gn) {
        //TODO
    }

    @Override
    public void setGroupMembersCount(int gmc) {
        //TODO
    }

    @Override
    public void setGroupTeam(int team, int partySize) {
        int iconResource = UIUtils.getIconResource(team, partySize);
        if (lastBubbleImageResource != iconResource) {
            bubble.setImageResource(iconResource);
            lastBubbleImageResource = iconResource;
        }
    }

    @Override
    public void setLead(boolean isLead, int groupMemberCount) {
        boolean shouldTheCrownVisible = isLead && (groupMemberCount > 1);
        int crownVisibility = shouldTheCrownVisible ? View.VISIBLE : View.GONE;
        crown.setVisibility(crownVisibility);
    }

    private void showIfValueGreaterThanZero(View view, int value) {
        if (value > 0) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFinishFloatingView() {
        presenter.bubbleTrashed();
    }

    @Override
    public void onTouchFinished(boolean isFinishing, int x, int y) {
        if (!isFinishing) {
            presenter.bubbleMoved(x, y);
        }
    }

    private void alignView(View view, int alignParentLeft, int alignParentStart, int alignParentRight, int alignParentEnd) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.addRule(alignParentLeft);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            params.addRule(alignParentStart);
            params.removeRule(alignParentRight);
            params.removeRule(alignParentEnd);
        }
        view.setLayoutParams(params);
    }

    @Override
    public void onClick(View v) {
        int[] coords = new int[2];
        v.getLocationOnScreen(coords);
        presenter.bubbleClick(coords[0], coords[1], coords[0], coords[1]);
    }

    @Override
    public void onBubbleRemoved(BubbleLayout bubble) {
        presenter.bubbleRemoved();
    }

    @Override
    public void moved(int x, int y) {
        if (x > width / 2) {
            alignView(messages, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_START, RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.ALIGN_PARENT_END);
            alignView(requests, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_START, RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.ALIGN_PARENT_END);
        } else {
            alignView(messages, RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.ALIGN_PARENT_END, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_START);
            alignView(requests, RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.ALIGN_PARENT_END, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_START);
        }
    }
}
