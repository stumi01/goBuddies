package com.stumi.gobuddies.system.network;

import android.support.annotation.Nullable;
import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.stumi.gobuddies.events.NetworkError;
import com.stumi.gobuddies.events.UnauthorizedEvent;
import com.stumi.gobuddies.events.UserKickedFromPartyEvent;

import org.greenrobot.eventbus.EventBus;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.UnknownHostException;

import io.reactivex.Single;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.stumi.gobuddies.system.network.ErrorCodesConstant.UN_AUTHORIZED;

/**
 * @author stumpfb on 23/12/2016.
 */

public class RxErrorHandlingCallAdapterFactory extends CallAdapter.Factory {

    private static final String TAG = "RxErrorHandlingCall";

    private final RxJava2CallAdapterFactory original;

    private final EventBus eventbus;

    private RxErrorHandlingCallAdapterFactory(EventBus eventbus) {
        this.eventbus = eventbus;
        original = RxJava2CallAdapterFactory.create();

    }

    public static CallAdapter.Factory create(EventBus eventbus) {
        return new RxErrorHandlingCallAdapterFactory(eventbus);
    }

    @Override
    public CallAdapter<?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        return new RxCallAdapterWrapper(original.get(returnType, annotations, retrofit));
    }

    private class RxCallAdapterWrapper implements CallAdapter<Single<?>> {

        private final CallAdapter<?> wrapped;

        public RxCallAdapterWrapper(CallAdapter<?> wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public Type responseType() {
            return wrapped.responseType();
        }

        @SuppressWarnings("unchecked")
        @Override
        public <R> Single<?> adapt(Call<R> call) {

            return ((Single) wrapped.adapt(call)).onErrorResumeNext(new Function<Throwable, Single>() {
                @Override
                public Single apply(Throwable throwable) throws Exception {
                    return Single.error(asRetrofitException(throwable));
                }

            }).doOnSuccess(new Consumer<Response>() {
                @Override
                public void accept(Response response) throws Exception {
                    if (response.code() == UN_AUTHORIZED) {
                        eventbus.post(new UnauthorizedEvent());
                    } else if (response.code() == ErrorCodesConstant.KICKED_FROM_PARTY) {
                        eventbus.post(new UserKickedFromPartyEvent());
                    }
                }

            });
        }

        private RetrofitException asRetrofitException(Throwable throwable) {
            String errorMessage = throwable.getMessage();
            String customErrorMessage = null;//parseJsonError(errorMessage);
            if (customErrorMessage == null) {
                customErrorMessage = checkForCommonErrors(throwable);
            }
            return new RetrofitException(errorMessage, customErrorMessage);
        }
    }

    @Nullable
    private String parseJsonError(final String json) {

        try {
           /* final JsonNode rootNode = Mapper.getMapperInstance().readTree(json);

            final ErrorsModel jsonModel = new ErrorsModel();
            jsonModel.setErrors((List<SportsBookErrorModel>) Mapper.getMapperInstance()
                    .readValue(rootNode.get("errors").toString(),
                            new TypeReference<List<SportsBookErrorModel>>() {
                            }));
            return jsonModel.getErrors().get(0).getTitle();
            */
        } catch (final Exception e) {
            Log.e(TAG, "Error while deserializing error message", e);
        }
        return null;
    }

    private String checkForCommonErrors(final Throwable throwable) {
        String errorMessage = throwable.getMessage();
        if (throwable instanceof UnknownHostException) {
            eventbus.post(new NetworkError());
        }
        return errorMessage;
    }
}

