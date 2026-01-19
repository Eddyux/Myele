import os
from PIL import Image

TARGET_SIZE = (512, 512)
caipin_folder = "caipin"

total_images = 0
processed_images = 0
skipped_images = 0

print("=" * 60)
print("Start compressing images to 512x512")
print("=" * 60)

for restaurant_folder in os.listdir(caipin_folder):
    restaurant_path = os.path.join(caipin_folder, restaurant_folder)
    
    if not os.path.isdir(restaurant_path):
        continue
    
    print(f"\nProcessing: {restaurant_folder}")
    print("-" * 60)
    
    image_files = [f for f in os.listdir(restaurant_path) 
                   if f.lower().endswith(('.png', '.jpg', '.jpeg'))]
    
    for image_file in image_files:
        total_images += 1
        image_path = os.path.join(restaurant_path, image_file)
        
        try:
            img = Image.open(image_path)
            
            if img.size == TARGET_SIZE:
                print(f"  Skip {image_file} (already 512x512)")
                skipped_images += 1
                continue
            
            img_resized = img.resize(TARGET_SIZE, Image.Resampling.LANCZOS)
            
            if image_file.lower().endswith('.png'):
                img_resized.save(image_path, 'PNG', optimize=True)
            else:
                img_resized.save(image_path, 'JPEG', quality=95, optimize=True)
            
            print(f"  OK {image_file} ({img.size[0]}x{img.size[1]} -> 512x512)")
            processed_images += 1
            
        except Exception as e:
            print(f"  ERROR {image_file}: {str(e)}")

print("\n" + "=" * 60)
print(f"Done!")
print(f"Total: {total_images}")
print(f"Processed: {processed_images}")
print(f"Skipped: {skipped_images}")
print("=" * 60)
