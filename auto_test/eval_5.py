import subprocess
import json

# 从设备获取文件
# run里面的com.example.myele是APP名字；files/messages.json是文件路径
subprocess.run(['adb', 'exec-out', 'run-as', 'com.example.myele', 'cat', 'files/messages.json'],
                stdout=open('messages.json', 'w'))

# 读取文件
with open('messages.json', 'r', encoding='utf-8') as f:
    data = json.load(f)

# 验证任务5: 在首页下拉页面触发刷新
def validate_home_refresh():
    # 检测1: 验证action
    if data.get('action') != 'refresh_page':
        return False

    # 检测2: 验证page
    if data.get('page') != 'home':
        return False

    # 检测3: 验证timestamp
    if 'timestamp' not in data or not isinstance(data['timestamp'], (int, float)):
        return False

    # 检测4: 验证page_info
    if 'page_info' not in data:
        return False

    page_info = data['page_info']
    if page_info.get('screen_name') != 'HomeScreen':
        return False

    # 检测5: 验证extra_data中的refresh_type
    if 'extra_data' not in data:
        return False

    extra_data = data['extra_data']
    if extra_data.get('refresh_type') != 'pull_to_refresh':
        return False

    return True

# 运行验证并输出结果
result = validate_home_refresh()
print(result)
