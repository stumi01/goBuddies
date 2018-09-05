package com.stumi.gobuddies.di;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stumi.gobuddies.system.IntentManager;
import com.stumi.gobuddies.system.UserDataHolder;
import com.stumi.gobuddies.system.helper.BooleanTypeAdapter;
import com.stumi.gobuddies.system.helper.scheduler.Ticker;
import com.stumi.gobuddies.system.location.LocationProvider;
import com.stumi.gobuddies.system.network.RxErrorHandlingCallAdapterFactory;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author stumpfb on 26/08/2016.
 */
@Module
public abstract class BaseApplicationModule {

    private static final String SERVER_URL = "http://api.gobuddies.io:82/";

    private LocationProvider locationProvider;

    private UserDataHolder userDataHolder;

    private IntentManager intentManager;

    private EventBus eventBus;

    private final Context context;

    public BaseApplicationModule(Context context) {
        this.context = context;
    }

    @Provides
    public Context providesContext() {
        return context;
    }

    @Singleton
    @Provides
    public Retrofit providesRetrofit(OkHttpClient okClient, Gson gson, EventBus eventBus) {
        return new Retrofit.Builder().baseUrl(SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create(eventBus)).client(okClient)
                .build();
    }

    @Singleton
    @Provides
    public Gson providesGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Boolean.class, new BooleanTypeAdapter());
        builder.registerTypeAdapter(boolean.class, new BooleanTypeAdapter());
        return builder.create();
    }

    @Singleton
    @Provides
    public OkHttpClient providesOkHttpClient(UserDataHolder userDataHolder) {
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder().addInterceptor(chain -> {
            Request.Builder builder1 = chain.request().newBuilder();
            builder1.addHeader("token", userDataHolder.getToken());
            return chain.proceed(builder1.build());
        });

        return builder.addInterceptor(logInterceptor).build();
    }

    @Singleton
    @Provides
    public UserDataHolder providesUserTokenHolder(EventBus eventBus) {
        if (userDataHolder == null) {
            userDataHolder = new UserDataHolder(context, eventBus);
        }
        return userDataHolder;
    }

    @Singleton
    @Provides
    public IntentManager providesIntentManager(EventBus eventBus) {
        if (intentManager == null) {
            intentManager = new IntentManager(context, eventBus);
        }
        return intentManager;
    }

    @Singleton
    @Provides
    public EventBus providesEventBus() {
        if (eventBus == null) {
            eventBus = EventBus.getDefault();
        }
        return eventBus;
    }

    @Singleton
    @Provides
    public LocationProvider providesLocationProvider(Context context, EventBus eventBus) {
        if (locationProvider == null) {
            locationProvider = new LocationProvider(context, eventBus);
        }
        return locationProvider;
    }

    @Provides
    public Ticker providesTicker() {
        return new Ticker();
    }

}
