package plugins;

import android.content.Context;
import android.util.Pair;

import com.lody.virtual.helper.collection.ArrayMap;
import com.tby.http.SmHttpRequest;
import com.tby.http.server.HttpServerUtils;
import com.tby.http.server.IPathHandler;
import com.tby.http.server.MainServerThread;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import utils.JsonUtil;

public class PluginRunner {
    private static PluginRunner sInstance = new PluginRunner();
    private Context context;
    private ArrayMap<String, Pair<IPlatformPlugin, Integer>> pluginMap = new ArrayMap<>();

    public static PluginRunner get() {
        return sInstance;
    }

    public PluginRunner() {
        KuaishouPlugin kuaishouPlugin = new KuaishouPlugin();
        pluginMap.put("com.smile.gifmaker", Pair.<IPlatformPlugin, Integer>create(kuaishouPlugin, 8889));
    }

    public synchronized void startWith(Context context, String processName) {
        final Pair<IPlatformPlugin, Integer> pluginPair = pluginMap.get(processName);
        if (pluginPair == null) {
            return;
        }
        if (this.context != null) throw new RuntimeException("HttpPlugin could only start once!");

        final IPlatformPlugin plugin = pluginPair.first;
        final int port = pluginPair.second;
        for (Map.Entry<String, Pair<IPlatformPlugin, Integer>> entry : pluginMap.entrySet()) {
            entry.getValue().first.init(context);
        }

        this.context = context;
        MainServerThread thread = new MainServerThread(port);
        thread.addPathHandler("/api", new IPathHandler() {
            @Override
            public void handleRequest(ChannelHandlerContext ctx, FullHttpRequest httpRequest) throws Exception {
                String body = HttpServerUtils.getBody(httpRequest);

                JSONObject jsonObject = new JSONObject(body);
                String url = jsonObject.getString("url");
                String commonParams = jsonObject.getString("commonParams");
                Map<String, List<String>> headers = JsonUtil.parseHeaders(jsonObject.getJSONObject("header"));
                Map<String, String> fields = JsonUtil.parseFields(jsonObject.optJSONObject("field"));
                SmHttpRequest request = plugin.process(url, commonParams, fields, headers);

                HttpServerUtils.send(ctx, packageResult(request).toString(), HttpResponseStatus.OK);
            }
        });
        thread.start();
    }

    private JSONObject packageResult(SmHttpRequest request) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("url", request.url);
        JSONObject headerJson = new JSONObject();
        for (Map.Entry<String, List<String>> entry : request.headers.entrySet()) {
            headerJson.put(entry.getKey(), new JSONArray(entry.getValue()));
        }
        json.put("header", headerJson);
        json.put("method", request.method);
        JSONObject fieldJson = new JSONObject();
        if (request.fields != null) {
            for (Map.Entry<String, String> entry : request.fields.entrySet()) {
                fieldJson.put(entry.getKey(), entry.getValue());
            }
        }
        json.put("field", fieldJson);
        return json;
    }
}
