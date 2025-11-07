import subprocess
import json

def validate_reviews_page(result=None):
    subprocess.run(['adb', 'exec-out', 'run-as', 'com.example.myele', 'cat', 'files/messages.json'],
                    stdout=open('messages.json', 'w'))

    with open('messages.json', 'r', encoding='utf-8') as f:
        data = json.load(f)
        if isinstance(data, list):
            data = data[-1] if data else {}

    if data.get('action') != 'enter_reviews_page':
        return 'false1'
    if data.get('page') != 'reviews':
        return 'false2'
    if 'extra_data' not in data:
        return 'false3'
    extra_data = data['extra_data']
    # 【关键】必须选择"待评价"标签
    if extra_data.get('selected_tab') != '待评价':
        return 'false4'
    return True

if __name__ == '__main__':
    result = validate_reviews_page()
    print(result)
