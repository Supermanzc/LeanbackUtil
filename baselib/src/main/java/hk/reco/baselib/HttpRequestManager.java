package hk.reco.baselib;


import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;


import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import hk.reco.baselib.util.Logger;
import hk.reco.baselib.util.QqHttpStatusException;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 创 建 人: 程超
 * 创建日期: 2017/8/17 13:26
 * 修改时间：
 * 修改备注：
 */


public class HttpRequestManager {


    private static final String TAG = HttpRequestManager.class.getName();

    private static final int TIMEOUT_SHORT = 6;
    private static final int TIMEOUT_NORMAL = 15;
    private static final int TIMEOUT_LONG = 60;
    private static final int WRITE_TIMEOUT = 30;


    private final HashMap<String, List<Cookie>> mCookieStore = new HashMap<>();
    private final Builder mDefaultBuilder;

    private static HttpRequestManager mInstance;

    private HttpRequestManager() {
        mDefaultBuilder = genBuilder(TIMEOUT_NORMAL, WRITE_TIMEOUT);
    }

    public static HttpRequestManager getInstance() {
        if (mInstance == null) {
            mInstance = new HttpRequestManager();
        }
        return mInstance;
    }

    public Builder getShortTimeoutBuilder() {
        return genBuilder(TIMEOUT_SHORT, WRITE_TIMEOUT);
    }

    public Builder getLongTimeoutBuilder() {
        return genBuilder(TIMEOUT_LONG, WRITE_TIMEOUT);
    }

    private Builder genBuilder(int timeout, int writeTimeout) {
        return new Builder()
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
                        mCookieStore.put(httpUrl.host(), list);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                        List<Cookie> cookies = mCookieStore.get(httpUrl.host());
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                });
    }

    /**
     * 请求数据，请求完毕后，回调。具体的数据解析，消息发送在model层做。
     *
     * @param url 请求的url
     *            //     * @param needToken 是否需要token
     */
    public String get(String url) throws IOException, QqHttpStatusException {
        return get(url, mDefaultBuilder);
    }

    /**
     * 请求数据，请求完毕后，回调。具体的数据解析，消息发送在model层做。
     *
     * @param url                 请求的url
     *                            //     * @param needToken           是否需要token
     * @param okHttpClientBuilder 需要调用者提供Builder
     */
    private String get(String url, Builder okHttpClientBuilder)
            throws IOException, QqHttpStatusException {
        Logger.d(TAG, "get request url:" + url);
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        Request request = builder.build();

        OkHttpClient okHttpClient = okHttpClientBuilder.build();
        Response response = okHttpClient.newCall(request).execute();
        String result;
        Logger.d(TAG, "get response code:" + response.code());
        if (response.code() == HttpURLConnection.HTTP_OK) {
            result = response.body().string();
            Logger.printLog2(TAG, "get response json:" + result);
        } else {
            Logger.e(TAG, "get response json error info:" + response.body().string());
            throw new QqHttpStatusException(response.code(), "QqHttpStatusException");
        }
        return result;
    }

    /**
     * 请求数据，请求完毕后，回调。具体的数据解析，消息发送在model层做。
     *
     * @param url        请求的url
     * @param jsonObject 请求的参数
     *                   //     * @param needToken  是否需要token
     * @return
     * @throws IOException
     */
    public String post(String url, JSONObject jsonObject)
            throws IOException {
        return post(url, jsonObject, mDefaultBuilder);
    }


    /**
     * 请求数据，请求完毕后，回调。具体的数据解析，消息发送在model层做。
     *
     * @param url                 请求的url
     * @param jsonObjectParam     请求的参数
     *                            //     * @param needToken           是否需要token
     * @param okHttpClientBuilder 需要调用者提供Builder
     * @return
     * @throws IOException
     */
    private String post(String url, JSONObject jsonObjectParam,
                        Builder okHttpClientBuilder) throws IOException {
        JSONObject jsonObject = jsonObjectParam;
        if (jsonObject == null) {
            jsonObject = new JSONObject();
        }
        Logger.d(TAG, "post request url:" + url);
        Logger.d(TAG, "post request json:" + jsonObject.toString());
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
//        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, jsonObject.toString());


        Request.Builder builder = new Request.Builder();
//        if (needToken) {
//            String token = Cache.getInstance().getAuthToken();
//            if (TextUtils.isEmpty(token)) {
//                throw new TokenException("need user auth token");
//            } else {
//                builder.addHeader("authToken", token);
//            }
//        }
//        builder.addHeader("version", DeviceUtil.getInstance().getVersionName());
//        builder.addHeader("deviceType", String.valueOf(1));
        Request request = builder
                .url(url)
                .post(body)
                .build();

        OkHttpClient okHttpClient = okHttpClientBuilder.build();
//        Cache.getInstance().setLatestOperatingTime(System.currentTimeMillis());
        Response response = okHttpClient.newCall(request).execute();
        String result;
        Logger.d(TAG, "post response code:" + response.code());
        if (response.code() == HttpURLConnection.HTTP_OK) {
            result = response.body().string();
            Logger.printLog2(TAG, "post response json:" + result);
        } else {
            Logger.e(TAG, "post response json error info:" + response.body().string());
            throw new IOException("server error.");
        }

        return result;
    }


    public String post(String url, FormBody body)
            throws IOException {
        return post(url, body, mDefaultBuilder);
    }

    /**
     * 请求数据，请求完毕后，回调。具体的数据解析，消息发送在model层做。
     *
     * @param url                 请求的url
     * @param body                请求的参数
     *                            //     * @param needToken           是否需要token
     * @param okHttpClientBuilder 需要调用者提供Builder
     * @return
     * @throws IOException
     */
    public String post(String url, FormBody body,
                       Builder okHttpClientBuilder) throws IOException {
        Logger.d(TAG, "post request url:" + url);
        if (body != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < body.size(); i++) {
                sb.append(body.name(i)).append(":").append(body.value(i)).append(",");
            }
            Logger.d(TAG, "post request json:" + sb.toString());
        }

        Request.Builder builder = new Request.Builder();
//        if (needToken) {
//            String token = Cache.getInstance().getAuthToken();
//            if (TextUtils.isEmpty(token)) {
//                throw new TokenException("need user auth token");
//            } else {
//                builder.addHeader("authToken", token);
//            }
//        }
//        builder.addHeader("version", DeviceUtil.getInstance().getVersionName());
//        builder.addHeader("deviceType", String.valueOf(1));
        Request request = builder
                .url(url)
                .post(body)
                .build();

//        if (sslSocketFactory == null || trustManager == null) {
//            init();
//        }
//        okHttpClientBuilder.sslSocketFactory(sslSocketFactory, trustManager);
//        okHttpClientBuilder.hostnameVerifier(DO_NOT_VERIFY);
        OkHttpClient okHttpClient = okHttpClientBuilder.build();
//        Cache.getInstance().setLatestOperatingTime(System.currentTimeMillis());
        Response response = okHttpClient.newCall(request).execute();
        String result;
        Logger.d(TAG, "post response code:" + response.code());
        if (response.code() == HttpURLConnection.HTTP_OK) {
            result = response.body().string();
            Logger.printLog2(TAG, "post response json:" + result);
        } else {
            Logger.e(TAG, "post response json error info:" + response.body().string());
            throw new IOException("server error.");
        }

        return result;
    }

    private SSLSocketFactory sslSocketFactory;
    private X509TrustManager trustManager;

    public void init() {
        trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {

            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };

        try {
            SSLContext sslContext;
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new X509TrustManager[]{trustManager}, null);
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
//            throw new RuntimeException(e);
        }
    }

    private static final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };
}
