import subprocess
import json

subprocess.run(['adb', 'exec-out', 'run-as', 'com.example.myele', 'cat', 'files/messages.json'],
                stdout=open('messages.json', 'w'))

with open('messages.json', 'r', encoding='utf-8') as f:
    data = json.load(f)
    if isinstance(data, list):
        data = data[-1] if data else {}

def validate_cart_checkout():
    if data.get('action') != 'cart_checkout_success':
        return False
    if data.get('page') != 'cart':
        return False
    if 'extra_data' not in data:
        return False
    extra_data = data['extra_data']
    # 【关键】必须全选
    if not extra_data.get('select_all', False):
        return False
    # 【关键】必须进入结算页面
    if not extra_data.get('entered_checkout', False):
        return False
    # 【关键】必须支付成功
    if not extra_data.get('payment_success', False):
        return False
    return True

result = validate_cart_checkout()
print(result)
