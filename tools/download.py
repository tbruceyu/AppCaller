# -*- coding: utf-8 -*-
import json
import os
from urllib import request
import requests

__KEY_SERVER_HOST = 'http://172.16.70.147:8889/api'


def search_user(keyword, pcursor=None):
    key_req_json = {
        'url': 'http://apissl.gifshow.com/rest/n/search/user?app=0&kpf=ANDROID_PHONE&ver=6.4&c=BAIDU',
        'commonParams': 'mod=huawei%28MI%208%29&appver=6.4.0.9003&ftt=K-F-T&isp=CTCC&kpn=KUAISHOU&'
                        'lon=33.337841&language=zh-cn&sys=ANDROID_9&max_memory=512&ud=0&country_code=cn&'
                        'pm_tag=11694575470&oc=BAIDU&hotfix_ver=&did_gt=1539073091012&iuid=&net=WIFI&'
                        'did=ANDROID_2ec0f9e99f32fb16&lat=33.979012',
        'field': {
            'keyword': keyword,
            'client_key': '3c2cd3f3',
            'os': 'android',
        },
        'header': {
            'User-Agent': ['kwai-android'],
            'Accept-Language': ['zh-cn'],
            'X-REQUESTID': ['193601906'],
            'Host': ['apissl.gifshow.com']
        },
        'method': 'post'
    }
    if pcursor is not None:
        key_req_json['field'] = {
            'keyword': keyword,
            'pcursor': str(pcursor),
            'client_key': '3c2cd3f3',
            'os': 'android',
        }
    res_json = requests.post(url=__KEY_SERVER_HOST, data=json.dumps(key_req_json)
                             , headers={'Content-Type': 'application/json'})
    request_data = json.loads(res_json.text)
    url = request_data['url']
    field_data = request_data['field']

    header_data = {}
    for k in request_data['header'].keys():
        header_data[k] = request_data['header'][k][0]
    search_data = json.loads(requests.post(url=url, data=field_data
                                           , headers=header_data).text)
    if len(search_data['users']) <= 0:
        return []
    return search_data['pcursor'], search_data['users']


def get_video(uid, pcursor=None):
    key_req_json = {
        'url': 'http://apissl.gifshow.com/rest/n/feed/profile2?app=0&kpf=ANDROID_PHONE&ver=6.4&c=BAIDU',
        'commonParams': 'mod=huawei%28MI%208%29&appver=6.4.0.9003&ftt=K-F-T&isp=CTCC&kpn=KUAISHOU&'
                        'lon=33.337841&language=zh-cn&sys=ANDROID_9&max_memory=512&ud=0&country_code=cn&'
                        'pm_tag=11694575470&oc=BAIDU&hotfix_ver=&did_gt=1539073091012&iuid=&net=WIFI&'
                        'did=ANDROID_2ec0f9e99f32fb16&lat=33.979012',
        'field': {
            "token": "", "user_id": str(uid), "lang": "zh", "count": "30", "privacy": "public",
            "referer": "ks://profile/133107719/5202502011931030729/1_i/1648554269758459910_h139/8",
            "browseType": "1", "client_key": "3c2cd3f3", "os": "android"
        },
        'header': {
            'User-Agent': ['kwai-android'],
            'Accept-Language': ['zh-cn'],
            'X-REQUESTID': ['193601906'],
            'Host': ['apissl.gifshow.com']
        },
        'method': 'post'
    }
    if pcursor is not None:
        key_req_json['field'] = {
            "token": "", "pcursor": str(pcursor), "user_id": str(uid), "lang": "zh", "count": "30", "privacy": "public",
            "referer": "ks://profile/133107719/5202502011931030729/1_i/1648554269758459910_h139/8",
            "browseType": "1", "client_key": "3c2cd3f3", "os": "android"
        }
    res_json = requests.post(url=__KEY_SERVER_HOST, data=json.dumps(key_req_json)
                             , headers={'Content-Type': 'application/json'})
    request_data = json.loads(res_json.text)
    url = request_data['url']
    field_data = request_data['field']

    header_data = {}
    for k in request_data['header'].keys():
        header_data[k] = request_data['header'][k][0]
    user_data = json.loads(requests.post(url=url, data=field_data
                                         , headers=header_data).text)
    return user_data['pcursor'], user_data['feeds']


if __name__ == "__main__":
    keyword = input("输入关键字:")

    user_count = 0
    pcursor, search_res = search_user(keyword)
    if len(search_res) == 0:
        print("没有找到用户")
        exit(1)
    users = search_res
    target_user = None
    while True:
        for user in users:
            print("{0}. {1}".format(user_count, user['user_name']))
            user_count += 1
        print("请输入用户编号(翻页请直接按回车键):")
        choose = input("输入关键字:")
        if choose.isnumeric():
            target_user = users[int(choose)]
            break
        else:
            pcursor, new_users = search_user(keyword, pcursor)
            users += new_users
    pcursor = None
    if not os.path.exists("output"):
        os.mkdir("output")
    feed_count = 0
    while True:
        pcursor, feeds = get_video(target_user['user_id'], pcursor)
        if len(feeds) == 0:
            print("已经全部下载完成！")
            break
        for feed in feeds:
            video_url = feed.get('main_mv_url', None)
            if video_url is None:
                video_url = feed['main_mv_urls'][0]['url']
            target_file = "./output/{0}.mp4".format(feed_count)
            if not os.path.exists(target_file):
                try:
                    request.urlretrieve(video_url, target_file)
                    print("下载视频成功:" + video_url)
                except Exception as e:
                    print("下载失败：" + video_url)
            feed_count += 1
