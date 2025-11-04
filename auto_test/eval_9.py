import subprocess
import json

subprocess.run(['adb', 'exec-out', 'run-as', 'com.example.myele', 'cat', 'files/messages.json'],
                stdout=open('messages.json', 'w'))

with open('messages.json', 'r', encoding='utf-8') as f:
    data = json.load(f)
    if isinstance(data, list):
        data = data[-1] if data else {}

def validate_cancel_order():
    if data.get('action') != 'cancel_order':
        return False
    if data.get('page') != 'order':
        return False
    if 'extra_data' not in data:
        return False
    extra_data = data['extra_data']
    # 【关键】订单状态必须是"待接单"
    if extra_data.get('order_status') != '待接单':
        return False
    # 【关键】必须选择取消原因
    if not extra_data.get('cancel_reason'):
        return False
    # 【关键】必须显示弹窗
    if not extra_data.get('show_dialog', True):
        return False
    return True

result = validate_cancel_order()
print(result)
