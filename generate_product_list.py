#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
生成菜品图片批量生成列表
"""
import json
from collections import defaultdict

# 读取产品数据
with open('app/src/main/assets/data/products.json', 'r', encoding='utf-8') as f:
    products = json.load(f)

# 读取餐厅数据
with open('app/src/main/assets/data/restaurants.json', 'r', encoding='utf-8') as f:
    restaurants = json.load(f)

# 创建餐厅ID到名称的映射
restaurant_map = {r['restaurantId']: r['name'] for r in restaurants}

# 按餐厅分组菜品
products_by_restaurant = defaultdict(list)
for product in products:
    rest_id = product['restaurantId']
    products_by_restaurant[rest_id].append(product['name'])

# 生成文档内容
output = []
output.append('# 批量生成菜品图片 - 按餐厅分组\n\n')
output.append('## 使用说明\n\n')
output.append('将以下代码复制到你的图片生成脚本中，按餐厅批量生成菜品图片。\n\n')
output.append('---\n\n')

for rest_id in sorted(products_by_restaurant.keys()):
    rest_name = restaurant_map.get(rest_id, rest_id)
    product_names = products_by_restaurant[rest_id]

    output.append(f'## {rest_name}\n\n')
    output.append('```python\n')
    output.append('batch_generate_image(\n')
    output.append('    background="生成一些菜品图，尽可能真实，图片尺寸为1024x1024",\n')
    output.append('    themes=[\n')

    for name in product_names:
        output.append(f'        "{name}",\n')

    output.append('    ],\n')
    output.append(f'    save_dir="generated_images/{rest_id}"\n')
    output.append(')\n')
    output.append('```\n\n')

# 写入文件
with open('菜品图片生成列表.md', 'w', encoding='utf-8') as f:
    f.write(''.join(output))

print(f'已生成文档: 菜品图片生成列表.md')
print(f'总计: {len(products_by_restaurant)} 家餐厅')
for rest_id in sorted(products_by_restaurant.keys()):
    rest_name = restaurant_map.get(rest_id, rest_id)
    print(f'  - {rest_name}: {len(products_by_restaurant[rest_id])} 个菜品')
