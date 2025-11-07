import subprocess
import json

def validate_coupons_page(result=None):
    subprocess.run(['adb', 'exec-out', 'run-as', 'com.example.myele', 'cat', 'files/messages.json'],
                    stdout=open('messages.json', 'w'))

    with open('messages.json', 'r', encoding='utf-8') as f:
        data = json.load(f)
        # 兼容对象和数组两种格式
        if isinstance(data, list):
            data = data[-1] if data else {}

    if data.get('action') != 'enter_coupons_page':
        return 'false1'
    if data.get('page') != 'coupons':
        return 'false2'
    if 'page_info' not in data:
        return 'false3'
    page_info = data['page_info']
    if page_info.get('title') != '红包卡券':
        return 'false4'
    if page_info.get('screen_name') != 'CouponsScreen':
        return 'false5'
    return True

if __name__ == '__main__':
    result = validate_coupons_page()
    print(result)
