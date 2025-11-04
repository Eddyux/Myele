import subprocess
import json

# 从设备获取文件
# run里面的com.example.myele是APP名字；files/messages.json是文件路径
subprocess.run(['adb', 'exec-out', 'run-as', 'com.example.myele', 'cat', 'files/messages.json'],
                stdout=open('messages.json', 'w'))

# 读取文件
with open('messages.json', 'r', encoding='utf-8') as f:
    data = json.load(f)

# 验证所有必需字段
def validate_coupons_page():
    # 检测1: 验证action
    if data.get('action') != 'enter_coupons_page':
        return False

    # 检测2: 验证page
    if data.get('page') != 'coupons':
        return False

    # 检测3: 验证timestamp
    if 'timestamp' not in data or not isinstance(data['timestamp'], (int, float)):
        return False

    # 检测4: 验证page_info
    if 'page_info' not in data:
        return False

    page_info = data['page_info']
    if page_info.get('title') != '红包卡券':
        return False

    if page_info.get('screen_name') != 'CouponsScreen':
        return False

    return True

# 运行验证并输出结果
result = validate_coupons_page()
print(result)
