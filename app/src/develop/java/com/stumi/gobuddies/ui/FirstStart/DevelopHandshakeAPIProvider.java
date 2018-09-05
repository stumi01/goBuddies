package com.stumi.gobuddies.ui.firstStart;

import com.stumi.gobuddies.base.BaseDevelopProvider;
import com.stumi.gobuddies.system.helper.MockServer;

import io.reactivex.Single;
import retrofit2.Response;

/**
 * @author stumpfb on 09/10/2016.
 */
public class DevelopHandshakeAPIProvider extends BaseDevelopProvider implements HandshakeAPIProvider {

    public DevelopHandshakeAPIProvider(MockServer mockServer) {
        super(mockServer);
    }

    @Override
    public Single<Response<ValidationModel>> validation() {
        return Single.just(Response.success(mockServer.getValidation()));
/*                Response.error(ResponseBody.create(MediaType.parse("text/html"),""), new okhttp3.Response.Builder() //
                .code(401).message("OK").protocol(Protocol.HTTP_1_1)
                .request(new Request.Builder().url("http://localhost/").build()).build()));*/
    }
}
