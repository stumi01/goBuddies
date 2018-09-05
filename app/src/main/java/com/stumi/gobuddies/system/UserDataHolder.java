package com.stumi.gobuddies.system;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import com.crashlytics.android.answers.CustomEvent;
import com.jakewharton.rxrelay2.BehaviorRelay;
import com.stumi.gobuddies.R;
import com.stumi.gobuddies.model.User;
import com.stumi.gobuddies.system.tracking.KPIEvent;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @author stumpfb on 11/08/2016.
 */
public class UserDataHolder {

    private static final String TAG = UserDataHolder.class.getSimpleName();
    private final String USER_NAME = "USER_NAME";

    private final String USER_TOKEN = "USER_TOKEN";

    private final String USER_TEAM = "USER_TEAM";

    private final Context context;

    private final EventBus eventBus;

    private User self;

    private int knownPartySize;

    private BehaviorRelay<Boolean> isInPartyRelay = BehaviorRelay.createDefault(false);

    private Point headLastPosition;

    private String partyName;

    public UserDataHolder(Context context, EventBus eventBus) {
        this.context = context;
        this.eventBus = eventBus;
    }

    public String getToken() {
        if (self == null) {
            loadSelf();
        }
        return self == null ? "" : self.getId();
    }

    public User getSelf() {
        if (self == null) {
            loadSelf();
        }
        return self;
    }

    public void saveSelf(User self) {
        this.self = self;
        SharedPreferences sharedPref =
                context.getSharedPreferences(context.getString(R.string.preference_file_key),
                        Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(USER_NAME, self.getName());
        editor.putString(USER_TOKEN, self.getId());
        editor.putInt(USER_TEAM, self.getTeam());
        editor.apply();
    }

    public void updateSelf(String name, int team) {
        getSelf();
        if (!name.equals(self.getName()) || team != self.getTeam()) {
            self.setName(name);
            self.setTeam(team);
            saveSelf(self);
        }
    }

    public void updateSelf(boolean isLead, int partySize, String partyName) {
        getSelf();
        if (partySize != knownPartySize || !partyName.equals(this.partyName) || isLead != self.isLeader()) {
            this.knownPartySize = partySize;
            this.partyName = partyName;
            self.setLeader(isLead);
            saveSelf(self);
            isInPartyRelay.accept(knownPartySize > 1);
            eventBus.post(new KPIEvent(new CustomEvent("Party Status Change").putCustomAttribute("Party size", partySize)));
        }

    }

    public boolean isInParty() {
        return knownPartySize > 1;
    }

    public Disposable subscribePartyStatusUpdates(Consumer<Boolean> onNext) {
        return isInPartyRelay.subscribe(onNext);
    }

    private void loadSelf() {
        SharedPreferences sharedPref =
                context.getSharedPreferences(context.getString(R.string.preference_file_key),
                        Context.MODE_PRIVATE);
        String token = sharedPref.getString(USER_TOKEN, null);
        String name = sharedPref.getString(USER_NAME, null);
        int team = sharedPref.getInt(USER_TEAM, 0);
        if (token != null) {
            self = new User(token, name, team);
        }

    }

    public boolean isTokenExists() {
        return getSelf() != null;
    }

    public Point getHeadLastPosition() {
        if (headLastPosition == null) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            headLastPosition = new Point(0, (int) (size.y * 0.7));
        }
        return headLastPosition;
    }

    public void setHeadLastPosition(int x, int y) {
        headLastPosition.set(x, y);
    }
}
