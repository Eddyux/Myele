#!/usr/bin/env python3
"""
图片压缩脚本
将drawable文件夹中的大图片压缩到合理大小
"""
import os
from PIL import Image

# 设置目标文件夹
drawable_path = r"D:\AndroidStudioProjects\android_template\app\src\main\res\drawable"

# 压缩参数
MAX_SIZE = (800, 800)  # 最大尺寸
QUALITY = 85  # JPEG质量

def compress_image(image_path):
    """压缩单张图片"""
    try:
        # 获取文件大小
        original_size = os.path.getsize(image_path) / (1024 * 1024)  # MB

        # 如果小于500KB，跳过
        if original_size < 0.5:
            print(f"[SKIP] {os.path.basename(image_path)} (already small: {original_size:.2f}MB)")
            return

        # 打开图片
        img = Image.open(image_path)

        # 转换RGBA到RGB（如果需要保存为JPEG）
        if img.mode == 'RGBA':
            # 保留PNG格式但压缩
            img.thumbnail(MAX_SIZE, Image.Resampling.LANCZOS)
            img.save(image_path, 'PNG', optimize=True, quality=QUALITY)
        else:
            # 缩放图片
            img.thumbnail(MAX_SIZE, Image.Resampling.LANCZOS)
            # 保存为原格式
            if image_path.lower().endswith('.png'):
                img.save(image_path, 'PNG', optimize=True)
            else:
                img.save(image_path, 'JPEG', quality=QUALITY, optimize=True)

        # 获取压缩后的大小
        new_size = os.path.getsize(image_path) / (1024 * 1024)  # MB
        reduction = ((original_size - new_size) / original_size) * 100

        print(f"[OK] {os.path.basename(image_path)}: {original_size:.2f}MB -> {new_size:.2f}MB (reduced {reduction:.1f}%)")

    except Exception as e:
        print(f"[FAIL] {os.path.basename(image_path)}: {e}")

def main():
    """主函数"""
    print("Starting image compression...\n")

    # 获取所有图片文件
    image_files = []
    for file in os.listdir(drawable_path):
        if file.lower().endswith(('.png', '.jpg', '.jpeg')):
            image_files.append(os.path.join(drawable_path, file))

    print(f"Found {len(image_files)} image files\n")

    # 压缩每个图片
    for image_path in image_files:
        compress_image(image_path)

    print("\nCompression completed!")

if __name__ == "__main__":
    main()
