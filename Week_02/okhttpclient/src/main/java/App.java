import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class App {

    public static void main(String[] args) {
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .get()
//                .url("https://www.baidu.com/")
                .url("http://localhost:8801")
                .build();

        try {
            Response response = okHttpClient
                    .newCall(request)
                    .execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
