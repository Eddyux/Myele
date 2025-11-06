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

# 验证任务16: 在"我的"-"全部订单"中找到麻辣烫订单,点进商家,点击右上角更多,分享给微信好友
# 关键验证点:
# 1. 必须进入全部订单页面
# 2. 必须找到麻辣烫订单（通过订单菜品或商家名称验证）
# 3. 必须进入商家详情页
# 4. 必须分享到微信
def validate_share_malatang():
    # 检测1: 验证是否进入了订单页面并找到包含麻辣烫的订单
    found_malatang_order = False
    for record in all_data:
        if record.get('action') == 'navigate' and record.get('page') == 'order_detail':
            extra_data = record.get('extra_data', {})
            # 检查商家名称是否包含"麻辣烫"
            restaurant_name = extra_data.get('restaurant_name', '')
            # 检查订单菜品中是否包含"麻辣烫"
            order_items = extra_data.get('order_items', [])

            if '麻辣烫' in restaurant_name or any('麻辣烫' in item for item in order_items):
                found_malatang_order = True
                break

    if not found_malatang_order:
        return False

    # 检测2: 从数组中找到最后一个分享商家的记录
    share_record = None
    for record in reversed(all_data):
        if record.get('action') == 'share_store':
            share_record = record
            break

    # 检测3: 验证分享操作存在
    if share_record is None:
        return False

    # 检测4: 验证page
    if share_record.get('page') != 'store_page':
        return False

    # 检测5: 验证extra_data存在
    if 'extra_data' not in share_record:
        return False

    extra_data = share_record['extra_data']

    # 检测6: 【关键】验证分享平台是微信
    if extra_data.get('platform') != '微信':
        return False

    # 检测7: 【关键】验证page_info包含restaurant_name且包含"麻辣烫"
    if 'page_info' not in share_record:
        return False

    page_info = share_record['page_info']
    restaurant_name = page_info.get('restaurant_name', '')

    # 检测8: 验证是麻辣烫订单相关的商家
    if '麻辣烫' not in restaurant_name:
        return False

    return True

# 运行验证并输出结果
result = validate_share_malatang()
print(result)
