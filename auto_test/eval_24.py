import subprocess
import json

# 从设备获取文件
subprocess.run(['adb', 'exec-out', 'run-as', 'com.example.myele', 'cat', 'files/messages.json'],
                stdout=open('messages.json', 'w'))

# 读取文件
try:
    with open('messages.json', 'r', encoding='utf-8') as f:
        all_data = json.load(f)
except:
    print("false1")
    exit()

# 验证任务24: 在外卖页面筛选食无忧和预约配送,找一个商家给于骁和余味分别下一单明日中午到达的爆品
# 关键验证点:
# 1. 必须进入外卖页面
# 2. 必须筛选食无忧和预约配送
# 3. 必须给于骁和余味分别下单(两次订单,不同收货人)
# 4. 必须是明日到达
def validate_dual_orders_with_filter():
    # 检查是否有数据
    if not all_data:
        return "false2"

    # 检测1: 验证进入外卖页面并使用筛选功能
    filtered_food_safety = False
    filtered_cross_day = False

    for record in all_data:
        if record.get('action') == 'apply_filter' and record.get('page') == 'takeout':
            extra_data = record.get('extra_data', {})
            filters = extra_data.get('filters', [])
            # filters是一个列表,包含筛选项的字符串
            # 检查是否筛选了食无忧
            if 'FOOD_SAFETY' in filters or 'food_safety' in filters or '食无忧' in str(filters):
                filtered_food_safety = True
            # 检查是否筛选了跨天预定
            if 'CROSS_DAY_BOOKING' in filters or 'cross_day_booking' in filters or '跨天预订' in str(filters):
                filtered_cross_day = True

    if not filtered_food_safety:
        return "false3"

    if not filtered_cross_day:
        return "false4"

    # 检测2: 验证有两个不同地址的订单
    order_addresses = []
    for record in all_data:
        if record.get('action') == 'complete_order' and record.get('page') == 'checkout':
            extra_data = record.get('extra_data', {})
            # 检查是否是预约配送
            delivery_date = extra_data.get('delivery_date')
            if delivery_date and '明日' in delivery_date:
                # 记录收货人
                address_name = extra_data.get('delivery_address_name', '')
                if address_name:
                    order_addresses.append(address_name)

    # 检查是否有于骁和余味两个人的订单
    has_yuxiao = any('于骁' in addr for addr in order_addresses)
    has_yuwei = any('余味' in addr for addr in order_addresses)

    if not has_yuxiao:
        return "false5"

    if not has_yuwei:
        return "false6"

    # 检测3: 确认至少有两个订单
    if len(order_addresses) < 2:
        return "false7"

    return True

# 运行验证并输出结果
result = validate_dual_orders_with_filter()
print(result)
