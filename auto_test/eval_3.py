import subprocess
import json

subprocess.run(['adb', 'exec-out', 'run-as', 'com.example.myele', 'cat', 'files/messages.json'],
                stdout=open('messages.json', 'w'))

with open('messages.json', 'r', encoding='utf-8') as f:
    data = json.load(f)
    if isinstance(data, list):
        data = data[-1] if data else {}

def validate_reviews_page():
    if data.get('action') != 'enter_reviews_page':
        return False
    if data.get('page') != 'reviews':
        return False
    if 'extra_data' not in data:
        return False
    extra_data = data['extra_data']
    # 【关键】必须选择"待评价"标签
    if extra_data.get('selected_tab') != '待评价':
        return False
    return True

result = validate_reviews_page()
print(result)
