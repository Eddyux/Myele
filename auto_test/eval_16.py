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
# 2. 必须找到麻辣烫订单
# 3. 必须进入商家详情页
# 4. 必须分享到微信
def validate_share_malatang():
    # 从数组中找到最后一个分享商家的记录
    share_record = None
    for record in reversed(all_data):
        if record.get('action') == 'share_store':
            share_record = record
            break

    # 检测1: 验证分享操作存在
    if share_record is None:
        return False

    # 检测2: 验证page
    if share_record.get('page') != 'store_page':
        return False

    # 检测3: 验证extra_data存在
    if 'extra_data' not in share_record:
        return False

    extra_data = share_record['extra_data']

    # 检测4: 【关键】验证分享平台是微信
    if extra_data.get('platform') != '微信':
        return False

    # 检测5: 【关键】验证page_info包含restaurant_name且包含"麻辣烫"
    if 'page_info' not in share_record:
        return False

    page_info = share_record['page_info']
    restaurant_name = page_info.get('restaurant_name', '')

    # 检测6: 验证是麻辣烫订单相关的商家
    if '麻辣烫' not in restaurant_name:
        return False

    return True

# 运行验证并输出结果
result = validate_share_malatang()
print(result)
