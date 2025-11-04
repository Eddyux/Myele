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

# 验证任务19: 在外卖页面点击"配送最快"排序,选择列表中的第一个商家并完成下单
# 关键验证点:
# 1. 必须进入外卖页面
# 2. 必须在综合排序中选择配送最快
# 3. 必须下单商品
def validate_takeout_order():
    # 从数组中找到最后一个完成订单的记录
    order_record = None
    for record in reversed(all_data):
        if record.get('action') == 'complete_order':
            order_record = record
            break

    # 检测1: 验证完成订单操作存在
    if order_record is None:
        return False

    # 检测2: 验证page
    if order_record.get('page') != 'checkout':
        return False

    # 检测3: 验证extra_data存在
    if 'extra_data' not in order_record:
        return False

    extra_data = order_record['extra_data']

    # 检测4: 【关键】验证来自外卖页面
    if extra_data.get('from_page') != 'takeout':
        return False

    # 检测5: 【关键】验证支付成功
    if not extra_data.get('payment_success', False):
        return False

    # 检测6: 查找排序选择记录,验证选择了"配送最快"
    sort_record = None
    for record in reversed(all_data):
        if record.get('action') == 'select_sort_option':
            sort_record = record
            break

    if sort_record is None:
        return False

    # 检测7: 验证排序页面是外卖页面
    if sort_record.get('page') != 'takeout':
        return False

    # 检测8: 验证排序选项
    if 'extra_data' not in sort_record:
        return False

    sort_extra = sort_record['extra_data']
    if sort_extra.get('sort_option') != '配送最快':
        return False

    return True

# 运行验证并输出结果
result = validate_takeout_order()
print(result)
