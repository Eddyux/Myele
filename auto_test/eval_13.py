import subprocess
import json

def validate_close_notification():
    # 从设备获取文件
    subprocess.run(['adb', 'exec-out', 'run-as', 'com.example.myele', 'cat', 'files/messages.json'],
                    stdout=open('messages.json', 'w'))

    # 读取文件
    with open('messages.json', 'r', encoding='utf-8') as f:
        all_data = json.load(f)

    # 从数组中找到最后一个change_setting操作，且setting_type为系统消息通知的记录
    data = None
    for record in reversed(all_data):
        if record.get('action') == 'change_setting':
            extra_data = record.get('extra_data', {})
            if extra_data.get('setting_type') == '系统消息通知':
                data = record
                break

    if data is None:
        return 'false1'

    # 检测1: 验证action
    if data.get('action') != 'change_setting':
        return 'false2'

    # 检测2: 验证page
    if data.get('page') != 'settings':
        return 'false3'

    # 检测3: 验证timestamp
    if 'timestamp' not in data or not isinstance(data['timestamp'], (int, float)):
        return 'false4'

    # 检测4: 验证extra_data
    if 'extra_data' not in data:
        return 'false5'

    extra_data = data['extra_data']

    # 检测5: 验证设置类型
    if extra_data.get('setting_type') != '系统消息通知':
        return 'false6'

    # 检测6: 验证已关闭
    if extra_data.get('enabled', True):
        return 'false7'

    # 检测7: 验证显示了关闭成功弹窗
    if not extra_data.get('show_dialog', False):
        return 'false8'

    return True

if __name__ == '__main__':
    # 运行验证并输出结果
    result = validate_close_notification()
    print(result)
