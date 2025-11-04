import subprocess
import json

subprocess.run(['adb', 'exec-out', 'run-as', 'com.example.myele', 'cat', 'files/messages.json'],
                stdout=open('messages.json', 'w'))

with open('messages.json', 'r', encoding='utf-8') as f:
    data = json.load(f)
    if isinstance(data, list):
        data = data[-1] if data else {}

def validate_search_and_filter():
    if data.get('action') != 'filter':
        return False
    if data.get('page') != 'search_result':
        return False
    if 'extra_data' not in data:
        return False
    extra_data = data['extra_data']
    # 【关键】搜索关键词必须是"烤鸡"
    if extra_data.get('keyword') != '烤鸡':
        return False
    # 【关键】价格区间必须是0-30
    if extra_data.get('price_min') != 0:
        return False
    if extra_data.get('price_max') != 30:
        return False
    return True

result = validate_search_and_filter()
print(result)
