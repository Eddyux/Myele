import subprocess
import json

def validate_home_refresh():
    subprocess.run(['adb', 'exec-out', 'run-as', 'com.example.myele', 'cat', 'files/messages.json'],
                    stdout=open('messages.json', 'w'))

    with open('messages.json', 'r', encoding='utf-8') as f:
        data = json.load(f)
        if isinstance(data, list):
            data = data[-1] if data else {}

    if data.get('action') != 'refresh_page':
        return 'false1'
    # 【关键】必须在主页
    if data.get('page') != 'home':
        return 'false2'
    if 'extra_data' not in data:
        return 'false3'
    extra_data = data['extra_data']
    # 【关键】必须是下拉刷新
    if extra_data.get('refresh_type') != 'pull_to_refresh':
        return 'false4'
    return True

if __name__ == '__main__':
    result = validate_home_refresh()
    print(result)
