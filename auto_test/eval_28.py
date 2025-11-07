def validate_task_28(result=None):
    # 验证 result 存在
    if result is None:
        return 'false1'

    # 检测整个 result 中是否包含 "午间"
    if '午间' in str(result):
        return True
    else:
        return 'false2'

if __name__ == '__main__':
    result = validate_task_28()
    print(result)
