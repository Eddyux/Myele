import subprocess
import json

subprocess.run(['adb', 'exec-out', 'run-as', 'com.example.myele', 'cat', 'files/messages.json'],
               stdout=open('messages.json', 'w'))

with open('messages.json', 'r', encoding='utf-8') as f:
    data = json.load(f)
    if isinstance(data, list):
        data = data[-1] if data else {}

def validate_change_phone():
    if data.get('action') != 'enter_change_phone_page':
        return False
    if data.get('page') != 'change_phone':
        return False
    return True

result = validate_change_phone()
print(result)