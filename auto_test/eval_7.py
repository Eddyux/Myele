import subprocess
import json

# 从设备获取文件
# run里面的com.example.myele是APP名字；files/messages.json是文件路径
subprocess.run(['adb', 'exec-out', 'run-as', 'com.example.myele', 'cat', 'files/messages.json'],
                stdout=open('messages.json', 'w'))

# 读取文件
with open('messages.json', 'r', encoding='utf-8') as f:
    data = json.load(f)

# 验证任务7: 进入"我的"中"我的订单-全部"页面
def validate_my_orders_all():
    # 检测1: 验证action
    if data.get('action') != 'enter_orders_page':
        return False

    # 检测2: 验证page
    if data.get('page') != 'orders':
        return False

    # 检测3: 验证timestamp
    if 'timestamp' not in data or not isinstance(data['timestamp'], (int, float)):
        return False

    # 检测4: 验证page_info
    if 'page_info' not in data:
        return False

    page_info = data['page_info']
    if page_info.get('screen_name') != 'MyOrdersScreen':
        return False

    # 检测5: 验证extra_data中的selected_tab
    if 'extra_data' not in data:
        return False

    extra_data = data['extra_data']
    if extra_data.get('selected_tab') != '全部':
        return False

    # 检测6: 验证source为profile
    if extra_data.get('source') != 'profile':
        return False

    return True

# 运行验证并输出结果
result = validate_my_orders_all()
print(result)
