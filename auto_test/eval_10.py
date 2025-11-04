import subprocess
import json

subprocess.run(['adb', 'exec-out', 'run-as', 'com.example.myele', 'cat', 'files/messages.json'],
                stdout=open('messages.json', 'w'))

with open('messages.json', 'r', encoding='utf-8') as f:
    data = json.load(f)
    if isinstance(data, list):
        data = data[-1] if data else {}

def validate_payment_setting():
    if data.get('action') != 'change_setting':
        return False
    if data.get('page') != 'settings':
        return False
    if 'extra_data' not in data:
        return False
    extra_data = data['extra_data']
    # 【关键】设置类型必须是"免密支付"
    if extra_data.get('setting_type') != '免密支付':
        return False
    # 【关键】必须开启
    if not extra_data.get('enabled', False):
        return False
    # 【关键】必须显示弹窗
    if not extra_data.get('show_dialog', False):
        return False
    return True

result = validate_payment_setting()
print(result)
