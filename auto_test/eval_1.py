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
        return False
    if data.get('page') != 'coupons':
        return False
    if 'page_info' not in data:
        return False
    page_info = data['page_info']
    if page_info.get('title') != '红包卡券':
        return False
    if page_info.get('screen_name') != 'CouponsScreen':
        return False
    return True

if __name__ == '__main__':
    result = validate_coupons_page()
    print(result)
