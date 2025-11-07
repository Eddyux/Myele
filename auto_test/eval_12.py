import subprocess
import json

def validate_change_phone():
    subprocess.run(['adb', 'exec-out', 'run-as', 'com.example.myele', 'cat', 'files/messages.json'],
                   stdout=open('messages.json', 'w'))

    with open('messages.json', 'r', encoding='utf-8') as f:
        data = json.load(f)
        if isinstance(data, list):
            data = data[-1] if data else {}

    if data.get('action') != 'enter_change_phone_page':
        return 'false1'
    if data.get('page') != 'change_phone':
        return 'false2'
    return True

if __name__ == '__main__':
    result = validate_change_phone()
    print(result)