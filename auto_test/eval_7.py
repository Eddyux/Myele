import subprocess
import json

def validate_my_orders_all(result=None):
    subprocess.run(['adb', 'exec-out', 'run-as', 'com.example.myele', 'cat', 'files/messages.json'],
                    stdout=open('messages.json', 'w'))

    with open('messages.json', 'r', encoding='utf-8') as f:
        data = json.load(f)
        if isinstance(data, list):
            data = data[-1] if data else {}

    if data.get('action') != 'enter_orders_page':
        return False
    if data.get('page') != 'orders':
        return False
    if 'extra_data' not in data:
        return False
    extra_data = data['extra_data']
    # 【关键】必须选择"全部"标签
    if extra_data.get('selected_tab') != '全部':
        return False
    return True

if __name__ == '__main__':
    result = validate_my_orders_all()
    print(result)
