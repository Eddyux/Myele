import subprocess
import json

def validate_addresses_page(result=None):
    subprocess.run(['adb', 'exec-out', 'run-as', 'com.example.myele', 'cat', 'files/messages.json'],
                    stdout=open('messages.json', 'w'))

    with open('messages.json', 'r', encoding='utf-8') as f:
        data = json.load(f)
        if isinstance(data, list):
            data = data[-1] if data else {}

    if data.get('action') != 'enter_addresses_page':
        return False
    # 【关键】必须进入地址页面
    if data.get('page') != 'addresses':
        return False
    return True

if __name__ == '__main__':
    result = validate_addresses_page()
    print(result)
