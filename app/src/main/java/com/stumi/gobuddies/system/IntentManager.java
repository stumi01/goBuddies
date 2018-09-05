package com.stumi.gobuddies.system;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.orhanobut.logger.Logger;
import com.stumi.gobuddies.R;
import com.stumi.gobuddies.events.HideFloatingView;
import com.stumi.gobuddies.events.ShowBubbleView;
import com.stumi.gobuddies.events.ShowFloatingView;
import com.stumi.gobuddies.events.SwitchToFragment;
import com.stumi.gobuddies.model.GoBuddiesTab;
import com.stumi.gobuddies.ui.bubble.BubbleHeadService;
import com.stumi.gobuddies.ui.registration.RegistrationActivity;

import org.greenrobot.eventbus.EventBus;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * @author stumpfb on 13/08/2016.
 */
public class IntentManager {

    public static final String START_CHAT = "START_CHAT";
    private static final String TAG = "IntentManager";
    private final Context context;
    private final EventBus eventBus;
    private Intent service;

    public IntentManager(Context context, EventBus eventBus) {
        this.context = context;
        this.eventBus = eventBus;
        Logger.init();
    }


    public void startTeamFinderFromRegistration() {
        Intent i = context.getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(i);
    }

    public void startTeamFinder(int x, int y) {
        if (service == null) {
            startBubbleHeads(true);
        } else {
            eventBus.post(new ShowFloatingView(x, y));
        }
    }

    public void startRegistrationActivity() {
        Intent intent = new Intent(context, RegistrationActivity.class);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public void switchToPartyFragment() {
        eventBus.post(new SwitchToFragment(GoBuddiesTab.PARTY));
    }

    public void startBubbleHeads(boolean startChatImmediately) {
        if (service == null) {
            service = new Intent(context, BubbleHeadService.class).putExtra(START_CHAT, startChatImmediately);
            context.startService(service);
        } else {
            eventBus.post(new ShowBubbleView());
        }
    }

    public void stopBubbleHeads() {
        if (service != null) {
            context.stopService(service);
            //context.unbindService(bubbleServiceConnection);
            service = null;
        }
    }

    public void startGooglePlayForUpdate() {
        final String appPackageName =
                context.getPackageName(); // getPackageName() from Context or Activity  object
        try {
            context.startActivity(
                    new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public void sendFeedbackEmail() {
        eventBus.post(new HideFloatingView());
        Intent contactIntent = new Intent(Intent.ACTION_SENDTO,
                Uri.fromParts("mailto", context.getString(R.string.mailto), null));
        contactIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.mail_subject));
        contactIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.mail_text));
        context.startActivity(Intent.createChooser(contactIntent, context.getString(R.string.mail_chooser))
                .addFlags(FLAG_ACTIVITY_NEW_TASK));
    }
}
