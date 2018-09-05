package com.stumi.gobuddies.system.location;

import com.stumi.gobuddies.base.BaseDevelopProvider;
import com.stumi.gobuddies.model.BaseResponseModel;
import com.stumi.gobuddies.system.helper.MockServer;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Field;

/**
 * @author stumpfb on 26/08/2016.
 */
public class DevelopLocationAPIProvider extends BaseDevelopProvider implements LocationAPIProvider {

    public DevelopLocationAPIProvider(MockServer mockServer) {
        super(mockServer);
    }

    @Override
    public Single<Response<LocationUpdateModel>> locationUpdate(@Field("lon") Double longitude,
                                                                @Field("lat") Double latitude) {
        return createResponse(mockServer.getLocationUpdate());
    }

    @Override
    public Single<Response<BaseResponseModel>> notifySleepState(@Field("v") int isSleep) {
        return createResponse(new BaseResponseModel());
    }
}
