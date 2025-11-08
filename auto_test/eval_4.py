import subprocess
import json

def validate_search_action(result=None):
    subprocess.run(['adb', 'exec-out', 'run-as', 'com.example.myele', 'cat', 'files/messages.json'],
                    stdout=open('messages.json', 'w'))

    with open('messages.json', 'r', encoding='utf-8') as f:
        data = json.load(f)
        if isinstance(data, list):
            data = data[-1] if data else {}

    if data.get('action') != 'perform_search':
        return False
    if 'extra_data' not in data:
        return False
    extra_data = data['extra_data']
    # 【关键】搜索关键词必须是"瑞幸咖啡"
    if extra_data.get('search_query') != '瑞幸咖啡':
        return False

    return True

if __name__ == '__main__':
    result = validate_search_action()
    print(result)
