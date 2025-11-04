import subprocess
import json

# 从设备获取文件
subprocess.run(['adb', 'exec-out', 'run-as', 'com.example.myele', 'cat', 'files/messages.json'],
                stdout=open('messages.json', 'w'))

# 读取文件
with open('messages.json', 'r', encoding='utf-8') as f:
    all_data = json.load(f)

# 验证任务15: 进入"我的"-"我的订单"，进入"订单详情页"，找到配送中的订单，选择"联系骑手"，发送"出了什么情况，怎么还没到"
# 关键验证点：
# 1. 订单状态必须是"配送中"（不能是"已完成"或其他状态）
# 2. 收件人类型必须是骑手
# 3. 发送的消息必须是"出了什么情况，怎么还没到"
def validate_contact_rider():
    # 从数组中找到最后一个发送消息的记录
    message_record = None
    for record in reversed(all_data):
        if record.get('action') == 'send_message':
            message_record = record
            break

    # 检测1: 验证发送消息操作存在
    if message_record is None:
        return False

    # 检测2: 验证page
    if message_record.get('page') != 'chat':
        return False

    # 检测3: 验证extra_data存在
    if 'extra_data' not in message_record:
        return False

    extra_data = message_record['extra_data']

    # 检测4: 【关键】验证收件人类型是骑手（不能是商家）
    if extra_data.get('recipient_type') != 'rider':
        return False

    # 检测5: 【关键】验证发送了正确的消息
    if extra_data.get('message') != '出了什么情况，怎么还没到':
        return False

    # 检测6: 【关键】验证订单状态为"配送中"（不能是"已完成"、"待接单"等其他状态）
    if extra_data.get('order_status') != '配送中':
        return False

    return True

# 运行验证并输出结果
result = validate_contact_rider()
print(result)
