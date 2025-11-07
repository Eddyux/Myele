import subprocess
import json

def validate_add_address(result=None):
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

    # 从数组中找到最后一个添加地址的记录
    add_record = None
    for record in reversed(all_data):
        if record.get('action') == 'add_address':
            add_record = record
            break

    # 检测1: 验证添加地址操作存在
    if add_record is None:
        return 'false1'

    # 检测2: 验证page
    if add_record.get('page') != 'address':
        return 'false2'

    # 检测3: 验证extra_data存在
    if 'extra_data' not in add_record:
        return 'false3'

    extra_data = add_record['extra_data']

    # 检测4: 【关键】验证详细地址
    address = extra_data.get('address', '')
    if '华中师范大学元宝山学生公寓二期' not in address:
        return 'false4'

    # 检测5: 【关键】验证姓名
    if extra_data.get('name') != '于骁':
        return 'false5'

    # 检测6: 【关键】验证手机号
    if extra_data.get('phone') != '13022222222':
        return 'false6'

    # 检测7: 【关键】验证标签为学校
    if extra_data.get('tag') != '学校':
        return 'false7'

    # 检测8: 【关键】验证门牌号为613
    detail_address = extra_data.get('detail_address', '')
    if '613' not in detail_address:
        return 'false8'

    return True

if __name__ == '__main__':
    # 运行验证并输出结果
    result = validate_add_address()
    print(result)
