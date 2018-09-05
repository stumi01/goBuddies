package com.stumi.gobuddies.system.helper;

import android.util.Log;

import com.squareup.leakcanary.AnalysisResult;
import com.squareup.leakcanary.DisplayLeakService;
import com.squareup.leakcanary.HeapDump;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public final class LeakSlackUploadService extends DisplayLeakService
        implements Callback<LeakSlackUploadService.UploadFileResponse> {

    private SlackApi slackApi;


    @Override
    public void onCreate() {
        super.onCreate();
        slackApi = new Retrofit.Builder()
                .baseUrl("https://harrteam.slack.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build() //
                .create(SlackApi.class);
    }

    @Override
    protected void afterDefaultHandling(HeapDump heapDump, AnalysisResult result, String leakInfo) {
        Log.e(TAG, "afterDefaultHandling " + leakInfo);
        if (!result.leakFound || result.excludedLeak) {
            Log.e(TAG, "!result.leakFound || result.excludedLeak");
            return;
        }
        String name = classSimpleName(result.className);
        if (!heapDump.referenceName.equals("")) {
            name += "(" + heapDump.referenceName + ")";
        }

        String title = name + " has leaked5";
        String initialComment = leakInfo.substring(0, leakInfo.indexOf("Details:", 0));
        RequestBody file = RequestBody
                .create(MediaType.parse("multipart/form-data"), heapDump.heapDumpFile);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", heapDump.heapDumpFile.getName(), file);
        final Call<UploadFileResponse> call = slackApi.uploadFile(SlackApi.TOKEN,
                body,
                null,
                heapDump.heapDumpFile.getName(), title, initialComment,
                SlackApi.MEMORY_LEAK_CHANNEL);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<UploadFileResponse> call,
                           Response<UploadFileResponse> response) {
        Log.d(TAG, response.body().toString());

    }

    @Override
    public void onFailure(Call<UploadFileResponse> call, Throwable t) {
        Log.d(TAG, t.getLocalizedMessage());
    }

    /**
     * See https://api.slack.com/ for documentation.
     */
    public interface SlackApi {

        String TOKEN = "xoxp-20966953415-20966320609-140386079458-70e5eda60f27c8e966881607fd2d8f26";

        String MEMORY_LEAK_CHANNEL = "#issues";

        @Multipart
        @POST("/api/files.upload")
        Call<UploadFileResponse> uploadFile(
                @Query("token") String token,
                @Part MultipartBody.Part file, @Query("filetype") String filetype,
                @Query("filename") String filename, @Query("title") String title,
                @Query("initial_comment") String initialComment,
                @Query("channels") String channels);
    }

    public static class UploadFileResponse {

        boolean ok;

        String error;

        @Override
        public String toString() {
            return "UploadFileResponse{" +
                    "ok=" + ok +
                    ", error='" + error + '\'' +
                    '}';
        }
    }

    private static final String TAG = "LeakListenerService";

    private static String classSimpleName(String className) {
        int separator = className.lastIndexOf('.');
        return separator == -1 ? className : className.substring(separator + 1);
    }
}
