def validate_task_27(result=None):
    # 验证 result 存在
    if result is None:
        return 'false1'

    # 检测整个 result 中是否包含 "22.1"
    if '22.1' in str(result):
        return True
    else:
        return 'false2'

if __name__ == '__main__':
    result = validate_task_27()
    print(result)
