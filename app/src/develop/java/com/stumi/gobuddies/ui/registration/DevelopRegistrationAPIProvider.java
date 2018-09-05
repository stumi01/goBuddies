package com.stumi.gobuddies.ui.registration;

import com.stumi.gobuddies.base.BaseDevelopProvider;
import com.stumi.gobuddies.system.helper.MockServer;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Field;

/**
 * @author stumpfb on 26/08/2016.
 */
public class DevelopRegistrationAPIProvider extends BaseDevelopProvider implements RegistrationAPIProvider {

    public DevelopRegistrationAPIProvider(MockServer mockServer) {
        super(mockServer);
    }

    @Override
    public Single<Response<RegistrationModel>> registerUser(@Field("name") String name,
                                                            @Field("team") int team) {
        return createResponse(mockServer.registerUser(name, team));
    }
}
