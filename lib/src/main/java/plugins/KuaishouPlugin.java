package plugins;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.util.Pair;

import com.tby.http.SmHttpRequest;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import plugins.kuaishou.com.yxcorp.gifshow.retrofit.k;

public class KuaishouPlugin implements IPlatformPlugin {
    private static final String TAG = "KuaishouPlugin";

    @Override
    public void init(Context context) {
        k.init(context.getClassLoader());
    }

    @Override
    public SmHttpRequest process(String url, String commonParams,  Map<String, String> fields, Map<String, List<String>> headers) {
        Uri uri;
        if (url.contains("?")) {
            uri = Uri.parse(url + "&" + commonParams);
        } else {
            uri = Uri.parse(url + "?" + commonParams);
        }
        HashMap<String, String> queryParams = new HashMap<>();
        for (String name : uri.getQueryParameterNames()) {
            String param = URLDecoder.decode(uri.getQueryParameter(name));
            queryParams.put(name, param);
        }
        try {
            Object instance = k.TYPE.newInstance();
            Map<String, String> verifyFields = new HashMap<>();
            for (Map.Entry<String, String> entry : fields.entrySet()) {
                verifyFields.put(entry.getKey(), URLDecoder.decode(entry.getValue()));
            }
            Pair<String, String> pair = k.computeSignature.call(instance, null, queryParams, verifyFields);
            fields.put(pair.first, pair.second);
            SmHttpRequest request = new SmHttpRequest();
            request.url = uri.toString();
            request.headers = headers;
            request.fields = fields;
            return request;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void test() {
        try {
            HashMap<String, String> queryParams = new HashMap<>();
            queryParams.put("isp", "");
            queryParams.put("mod", "vivo(vivo X7)");
            queryParams.put("pm_tag", "");
            queryParams.put("lon", "333.3377");
            queryParams.put("country_code", "CN");
            queryParams.put("kpf", "ANDROID_PHONE");
            queryParams.put("extId", "133ba30d52909ef26899bdd142");
            queryParams.put("did", "ANDROID_85baa81d74");
            queryParams.put("kpn", "KUAISHOU");
            queryParams.put("net", "WIFI");
            queryParams.put("app", "0");
            queryParams.put("oc", "BAIDU");
            queryParams.put("ud", "0");
            queryParams.put("hotfix_ver", "");
            queryParams.put("c", "BAIDU");
            queryParams.put("sys", "ANDROID_5.1.1");
            queryParams.put("appver", "6.4.0.9003");
            queryParams.put("ftt", "");
            queryParams.put("language", "zh-cn");
            queryParams.put("iuid", "");
            queryParams.put("lat", "333.979038");
            queryParams.put("did_gt", "15576532");
            queryParams.put("ver", "6.4");
            queryParams.put("max_memory", "256");

            HashMap<String, String> fieldParams = new HashMap<>();
            fieldParams.put("source", "1");
            fieldParams.put("volume", "0.0");
            fieldParams.put("browseType", "1");
            fieldParams.put("seid", "d3bb1356-df6f-44a8-a528-7375dfc876d3");
            fieldParams.put("pv", "false");
            fieldParams.put("needInterestTag", "false");
            fieldParams.put("client_key", "3c2c3");
            fieldParams.put("coldStart", "false");
            fieldParams.put("count", "20");
            fieldParams.put("pcursor", "");
            fieldParams.put("os", "android");
            fieldParams.put("refreshTimes", "1");
            fieldParams.put("id", "8");
            fieldParams.put("type", "7");
            fieldParams.put("page", "1");


            Object instance = k.TYPE.newInstance();
            Pair<String, String> res = k.computeSignature.call(instance, null, queryParams, fieldParams);
            Log.d(TAG, "sig:" + res.second);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
