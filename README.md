# VirtualCaller
基于 [VitrualAppEx](https://github.com/xxxyanchenxxx/VirtualAppEx) 开发
VirtualApp 9.0适配版本\
仅供个人学习使用\
\
商业版请去这里https://github.com/asLody/VirtualApp

**本项目仅用于学习使用，严禁用于任何商业用途**

# 使用方法:
1. 安装特定版本的客户端(放在clients目录下)
2. 
![image](https://github.com/tbruceyu/AppCaller/blob/master/images/screen_record.gif)\
3.
```
 curl http://xxx.xxx.xxx.xxx:8889/api -X POST -d '{"url":"http://xxxxxxx/","commonParams":"xxx=xxx&yyy=yyy&zzz=zzz","field":{"xxx":"xxx"},"header":{"User-Agent":["xxx"],"Accept-Language":["xxx"],"X-REQUESTID":["xxxx"],"Host":["xxx"]}}'
```

