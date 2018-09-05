package com.stumi.gobuddies.ui.floating.settings;

import com.stumi.gobuddies.base.BaseDevelopProvider;
import com.stumi.gobuddies.model.BaseResponseModel;
import com.stumi.gobuddies.system.helper.MockServer;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Field;

/**
 * @author stumpfb on 06/09/2016.
 */
public class DevelopSettingsApiProvider extends BaseDevelopProvider implements SettingsApiProvider {

    public DevelopSettingsApiProvider(MockServer mockServer) {
        super(mockServer);
    }

    @Override
    public Single<Response<BaseResponseModel>> updateSettings(@Field("name") String name,
                                                              @Field("team") int team) {

        return createResponse(mockServer.updateSettings(name,team));
    }


}
