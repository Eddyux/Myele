import subprocess
import json

# 从设备获取文件
# run里面的com.example.myele是APP名字；files/messages.json是文件路径
subprocess.run(['adb', 'exec-out', 'run-as', 'com.example.myele', 'cat', 'files/messages.json'],
                stdout=open('messages.json', 'w'))

# 读取文件
with open('messages.json', 'r', encoding='utf-8') as f:
    data = json.load(f)

# 验证任务2: 在外卖页面点击综合排序，选择好评优先
def validate_sort_selection():
    # 检测1: 验证action
    if data.get('action') != 'select_sort_option':
        return False

    # 检测2: 验证page
    if data.get('page') != 'takeout':
        return False

    # 检测3: 验证timestamp
    if 'timestamp' not in data or not isinstance(data['timestamp'], (int, float)):
        return False

    # 检测4: 验证extra_data中的sort_option
    if 'extra_data' not in data:
        return False

    extra_data = data['extra_data']
    if extra_data.get('sort_option') != '好评优先':
        return False

    # 检测5: 验证是否点击了综合排序按钮
    if extra_data.get('sort_type') != '综合排序':
        return False

    return True

# 运行验证并输出结果
result = validate_sort_selection()
print(result)
