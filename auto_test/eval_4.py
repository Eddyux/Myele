import subprocess
import json

# 从设备获取文件
# run里面的com.example.myele是APP名字；files/messages.json是文件路径
subprocess.run(['adb', 'exec-out', 'run-as', 'com.example.myele', 'cat', 'files/messages.json'],
                stdout=open('messages.json', 'w'))

# 读取文件
with open('messages.json', 'r', encoding='utf-8') as f:
    data = json.load(f)

# 验证任务4: 在主页顶部搜索框输入"瑞幸咖啡"并点击搜索
def validate_search_action():
    # 检测1: 验证action
    if data.get('action') != 'perform_search':
        return False

    # 检测2: 验证page (从主页搜索)
    if data.get('page') != 'home':
        return False

    # 检测3: 验证timestamp
    if 'timestamp' not in data or not isinstance(data['timestamp'], (int, float)):
        return False

    # 检测4: 验证extra_data中的search_query
    if 'extra_data' not in data:
        return False

    extra_data = data['extra_data']
    if extra_data.get('search_query') != '瑞幸咖啡':
        return False

    # 检测5: 验证是否点击了搜索按钮
    if extra_data.get('search_triggered') != True:
        return False

    return True

# 运行验证并输出结果
result = validate_search_action()
print(result)
