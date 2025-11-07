import subprocess
import json

def validate_cancel_order():
    subprocess.run(['adb', 'exec-out', 'run-as', 'com.example.myele', 'cat', 'files/messages.json'],
                    stdout=open('messages.json', 'w'))

    with open('messages.json', 'r', encoding='utf-8') as f:
        data = json.load(f)
        if isinstance(data, list):
            data = data[-1] if data else {}

    if data.get('action') != 'cancel_order':
        return 'false1'
    if data.get('page') != 'order':
        return 'false2'
    if 'extra_data' not in data:
        return 'false3'
    extra_data = data['extra_data']
    # 【关键】订单状态必须是"待接单"
    if extra_data.get('order_status') != '待接单':
        return 'false4'
    # 【关键】必须选择取消原因
    if not extra_data.get('cancel_reason'):
        return 'false5'
    # 【关键】必须显示弹窗
    if not extra_data.get('show_dialog', True):
        return 'false6'
    return True

if __name__ == '__main__':
    result = validate_cancel_order()
    print(result)
