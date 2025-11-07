import subprocess
import json

def validate_scheduled_order():
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

    # 检测1: 查找筛选记录，验证选择了跨天预定
    filter_record = None
    for record in reversed(all_data):
        if record.get('action') == 'apply_filter':
            filter_record = record
            break

    if filter_record is None:
        return 'false1'

    # 检测2: 验证筛选页面是外卖页面
    if filter_record.get('page') != 'takeout':
        return 'false2'

    # 检测3: 验证筛选选项中包含跨天预定
    if 'extra_data' not in filter_record:
        return 'false3'

    filter_extra = filter_record['extra_data']
    filters = filter_extra.get('filters', [])

    # 检查是否选择了跨天预定
    if '跨天预订' not in filters:
        return 'false4'

    # 检测4: 从数组中找到最后一个完成订单的记录
    order_record = None
    for record in reversed(all_data):
        if record.get('action') == 'complete_order':
            order_record = record
            break

    # 检测5: 验证完成订单操作存在
    if order_record is None:
        return 'false5'

    # 检测6: 验证page
    if order_record.get('page') != 'checkout':
        return 'false6'

    # 检测7: 验证extra_data存在
    if 'extra_data' not in order_record:
        return 'false7'

    extra_data = order_record['extra_data']

    # 检测8: 【关键】验证来自外卖页面
    if extra_data.get('from_page') != 'takeout':
        return 'false8'

    # 检测9: 【关键】验证支付成功
    if not extra_data.get('payment_success', False):
        return 'false9'

    # 检测10: 【关键】验证选择了预约配送
    if extra_data.get('delivery_type') != 'scheduled':
        return 'false10'

    # 检测11: 【关键】验证选择了明日
    if extra_data.get('delivery_date') != '明日':
        return 'false11'

    # 检测12: 验证选择了时间段（中午时间段应该是11:xx-13:xx之间）
    delivery_time_slot = extra_data.get('delivery_time_slot', '')
    if not delivery_time_slot:
        return 'false12'

    # 验证时间段是否在中午范围（11:00-14:00）
    # 时间段格式如 "11:30-11:50"
    try:
        start_time = delivery_time_slot.split('-')[0]
        hour = int(start_time.split(':')[0])
        # 中午时间段应该在11点到14点之间
        if hour < 11 or hour >= 14:
            return 'false13'
    except:
        return 'false14'

    return True

if __name__ == '__main__':
    # 运行验证并输出结果
    result = validate_scheduled_order()
    print(result)
