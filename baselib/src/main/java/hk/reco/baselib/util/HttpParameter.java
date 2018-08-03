package hk.reco.baselib.util;


/**
 * 创 建 人：Cheng Chao
 * 创建日期：2018/7/16
 * 修改时间：
 * 修改备注：
 */

public class HttpParameter {

    public static String PARTNER_KEY = "a2bc69CA0C11D286691FBesfg";
    public static String PARTNER_PRIVATE_KEY = "abcd32wxsgke123ks93ksg";
    private static final String PREFIX = "OpitrtqeGzopIlwxs";

    public String appId = "12345691";
    public String appKey = "AWCpIpkuKBOBVVbxJg";
    public String appPrivateKey = "uUATYGWPZRDWwDdIbd";
    public String deviceId = "4611692615497155511";
    public String deviceKey = "9F4F34593A571B0878D9F75BD3115374369CA0C11D286691FB7EB110DC75F8D2";

    static {
        PARTNER_KEY = "cc6162e58e";
        PARTNER_PRIVATE_KEY = "cac1";
    }


    public static HttpParameter instance;

    private HttpParameter() {
        PARTNER_KEY = PARTNER_KEY + "2c85483";
        PARTNER_PRIVATE_KEY = PARTNER_PRIVATE_KEY + "da318753";
    }

    public static HttpParameter getInstance() {
        if (instance == null) {
            synchronized (HttpParameter.class) {
                if (instance == null) {
                    instance = new HttpParameter();
                }
            }
        }
        return instance;
    }

    public void initParameters(String appIdParam, String appKeyParam, String appPrivateKeyParam,
                               String deviceIdParam, String deviceKeyParam) {
        appId = appIdParam;
        appKey = appKeyParam;
        appPrivateKey = appPrivateKeyParam;
        deviceId = deviceIdParam;
        deviceKey = deviceKeyParam;
    }

    public String createWthxSign(long timestamp) {
        return MD5Util.MD5Encode(PARTNER_KEY + "1_2" + PARTNER_PRIVATE_KEY + "3_4" + timestamp).toLowerCase();
    }


    public String createSign(long time) {
        return MD5Util.MD5Encode(PREFIX + "_" + appId + "_" + appKey + "_" + appPrivateKey + "_" + time).toLowerCase();
    }

}
