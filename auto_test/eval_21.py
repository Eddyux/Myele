import subprocess
import json

# 验证任务21: 进入"我的"-"我的账单"页面,切换到"月账单"
# 关键验证点:
# 1. 必须进入我的账单页面
# 2. 必须切换到月账单
def validate_mybills_monthly(result=None):
    # 从设备获取文件
    subprocess.run(['adb', 'exec-out', 'run-as', 'com.example.myele', 'cat', 'files/messages.json'],
                    stdout=open('messages.json', 'w'))

    # 读取文件
    try:
        with open('messages.json', 'r', encoding='utf-8') as f:
            all_data = json.load(f)
    except:
        return 'false1'

    # 检查是否有数据
    if not all_data:
        return "false2"

    # 检测1: 验证进入我的账单页面
    entered_mybills = False
    for record in all_data:
        if record.get('action') == 'enter_mybills_page' and record.get('page') == 'mybills':
            entered_mybills = True
            break

    if not entered_mybills:
        return "false3"

    # 检测2: 验证切换到月账单
    switched_to_monthly = False
    for record in all_data:
        if record.get('action') == 'switch_to_monthly_bill' and record.get('page') == 'mybills':
            extra_data = record.get('extra_data', {})
            if extra_data.get('selected_tab') == '月账单':
                switched_to_monthly = True
                break

    if not switched_to_monthly:
        return "false4"

    # 检测3: 验证result中是否包含"41.48元"
    if result is not None and '41.48' in str(result):
        return True
    else:
        return "false5"

if __name__ == '__main__':
    # 运行验证并输出结果
    result = validate_mybills_monthly()
    print(result)
