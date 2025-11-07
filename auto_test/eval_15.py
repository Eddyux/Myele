import subprocess
import json

def validate_contact_rider(result=None):
    # 从设备获取文件
    subprocess.run(['adb', 'exec-out', 'run-as', 'com.example.myele', 'cat', 'files/messages.json'],
                    stdout=open('messages.json', 'w'))

    # 读取文件
    with open('messages.json', 'r', encoding='utf-8') as f:
        all_data = json.load(f)

    # 从数组中找到最后一个发送消息的记录
    message_record = None
    for record in reversed(all_data):
        if record.get('action') == 'send_message':
            message_record = record
            break

    # 检测1: 验证发送消息操作存在
    if message_record is None:
        return 'false1'

    # 检测2: 验证page
    if message_record.get('page') != 'chat':
        return 'false2'

    # 检测3: 验证extra_data存在
    if 'extra_data' not in message_record:
        return 'false3'

    extra_data = message_record['extra_data']

    # 检测4: 【关键】验证收件人类型是骑手（不能是商家）
    if extra_data.get('recipient_type') != 'rider':
        return 'false4'

    # 检测5: 【关键】验证发送了正确的消息
    if extra_data.get('message') != '出了什么情况，怎么还没到':
        return 'false5'

    # 检测6: 【关键】验证订单状态为"配送中"（不能是"已完成"、"待接单"等其他状态）
    if extra_data.get('order_status') != '配送中':
        return 'false6'

    return True

if __name__ == '__main__':
    # 运行验证并输出结果
    result = validate_contact_rider()
    print(result)
