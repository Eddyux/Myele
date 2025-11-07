import subprocess
import json

def validate_sort_selection(result=None):
    subprocess.run(['adb', 'exec-out', 'run-as', 'com.example.myele', 'cat', 'files/messages.json'],
                    stdout=open('messages.json', 'w'))

    with open('messages.json', 'r', encoding='utf-8') as f:
        data = json.load(f)
        if isinstance(data, list):
            data = data[-1] if data else {}

    if data.get('action') != 'select_sort_option':
        return 'false1'
    if data.get('page') != 'takeout':
        return 'false2'
    if 'extra_data' not in data:
        return 'false3'
    extra_data = data['extra_data']
    # 【关键】排序选项必须是"好评优先"
    if extra_data.get('sort_option') != '好评优先':
        return 'false4'
    # 【关键】必须点击"综合排序"
    if extra_data.get('sort_type') != '综合排序':
        return 'false5'
    return True

if __name__ == '__main__':
    result = validate_sort_selection()
    print(result)
