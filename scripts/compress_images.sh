#!/bin/bash

# Directory containing the images
IMAGE_DIR="/Users/oluwabamise/flutter-e2e-tests/src/test/resources/images/registration"

# Create a backup directory
BACKUP_DIR="${IMAGE_DIR}/backup"
mkdir -p "$BACKUP_DIR"

# Backup original images
echo "Backing up original images to ${BACKUP_DIR}"
cp "${IMAGE_DIR}"/*.png "${BACKUP_DIR}/"

# Process each PNG file
for img in "${IMAGE_DIR}"/*.png; do
    echo "Processing $img"
    
    # Get original dimensions
    dims=$(sips -g pixelHeight -g pixelWidth "$img" | grep -E 'pixel(Height|Width):' | awk '{print $2}')
    height=$(echo "$dims" | head -n 1)
    width=$(echo "$dims" | tail -n 1)
    
    echo "Original dimensions: ${width}x${height}"
    
    # Compress the image
    sips -s format png -s formatOptions low "$img" --out "$img.tmp"
    mv "$img.tmp" "$img"
    
    # Get new file size
    size=$(ls -lh "$img" | awk '{print $5}')
    echo "New file size: $size"
    echo "-------------------"
done

echo "Done! Original images backed up in ${BACKUP_DIR}"
