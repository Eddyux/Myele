import subprocess
import json

def validate_payment_setting():
    subprocess.run(['adb', 'exec-out', 'run-as', 'com.example.myele', 'cat', 'files/messages.json'],
                    stdout=open('messages.json', 'w'))

    with open('messages.json', 'r', encoding='utf-8') as f:
        data = json.load(f)
        if isinstance(data, list):
            data = data[-1] if data else {}

    if data.get('action') != 'change_setting':
        return 'false1'
    if data.get('page') != 'settings':
        return 'false2'
    if 'extra_data' not in data:
        return 'false3'
    extra_data = data['extra_data']
    # 【关键】设置类型必须是"免密支付"
    if extra_data.get('setting_type') != '免密支付':
        return 'false4'
    # 【关键】必须开启
    if not extra_data.get('enabled', False):
        return 'false5'
    # 【关键】必须显示弹窗
    if not extra_data.get('show_dialog', False):
        return 'false6'
    return True

if __name__ == '__main__':
    result = validate_payment_setting()
    print(result)
