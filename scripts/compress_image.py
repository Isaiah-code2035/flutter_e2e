from PIL import Image
import sys

def compress_image(input_path, output_path, max_size=(300, 100)):
    # Open the image
    with Image.open(input_path) as img:
        # Convert to RGB if needed
        if img.mode != 'RGB':
            img = img.convert('RGB')
        
        # Resize image while maintaining aspect ratio
        img.thumbnail(max_size, Image.Resampling.LANCZOS)
        
        # Save with compression
        img.save(output_path, 'PNG', optimize=True, quality=95)
        
        print(f"Original size: {img.size}")
        print(f"Compressed and saved to: {output_path}")

if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Usage: python compress_image.py input_path output_path")
        sys.exit(1)
        
    input_path = sys.argv[1]
    output_path = sys.argv[2]
    compress_image(input_path, output_path)
