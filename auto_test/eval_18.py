import subprocess
import json

# 从设备获取文件
try:
    subprocess.run(['adb', 'exec-out', 'run-as', 'com.example.myele', 'cat', 'files/messages.json'],
                    stdout=open('messages.json', 'w'))
except:
    pass

# 读取文件
try:
    with open('messages.json', 'r', encoding='utf-8') as f:
        all_data = json.load(f)
except:
    all_data = []

# 验证任务18: 进入"我的"-"我的订单",点击"订单详情页",选择"联系商家"发送"缺少可乐,要求退款"
# 关键验证点:
# 1. 必须进入我的订单-订单详情页
# 2. 必须选择联系商家
# 3. 必须发送"缺少可乐,要求退款"
def validate_contact_merchant():
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

    # 检测4: 【关键】验证收件人类型是商家(不能是骑手)
    if extra_data.get('recipient_type') != 'merchant':
        return False

    # 检测5: 【关键】验证发送了正确的消息
    if extra_data.get('message') != '缺少可乐,要求退款':
        return False

    return True

# 运行验证并输出结果
result = validate_contact_merchant()
print(result)
