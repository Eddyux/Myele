def validate_task_27(result=None):
    # 验证 result 存在
    if result is None:
        return False

    # 检测整个 result 中是否包含 "22.1"
    if 'final_message' in result and '22.1' in result['final_message']:
        return True
    else:
        return False

if __name__ == '__main__':
    result = validate_task_27()
    print(result)
