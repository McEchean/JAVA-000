import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class HttpGet {
    private static final OkHttpClient CLIENT = new OkHttpClient();

    public static String Get(String url) throws Exception {
        if(!url.startsWith("http")) {
            url = String.format("http://%s", url);
        }
        Request req = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = CLIENT.newCall(req);
        Response res = call.execute();
        if(res.body() != null) {
            return res.body().string();
        }
        return "";
    }
}
