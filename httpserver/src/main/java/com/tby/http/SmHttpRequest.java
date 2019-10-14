package com.tby.http;

import java.util.List;
import java.util.Map;

public class SmHttpRequest {
    // 0 means GET, 1 means POST
    public int method = 0;
    public String url;
    public Map<String, List<String>> headers;
    public Map<String, String> fields;
}
