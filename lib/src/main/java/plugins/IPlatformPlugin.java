package plugins;

import android.content.Context;

import com.tby.http.SmHttpRequest;

import java.util.List;
import java.util.Map;

public interface IPlatformPlugin {
    void init(Context context);

    SmHttpRequest process(String url, String commonParams, Map<String, String> fields, Map<String, List<String>> headers);
}
