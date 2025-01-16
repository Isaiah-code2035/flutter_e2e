#!/bin/bash

# Create lib directory if it doesn't exist
mkdir -p lib

# Download Sikulix if not already present
if [ ! -f "lib/sikulixide-2.0.5.jar" ]; then
    echo "Downloading Sikulix..."
    curl -L "https://launchpad.net/sikuli/sikulix/2.0.5/+download/sikulixide-2.0.5.jar" -o "lib/sikulixide-2.0.5.jar"
fi

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "Java is not installed. Please install Java first."
    echo "You can install it using: brew install openjdk"
    exit 1
fi

# Make the jar executable
chmod +x lib/sikulixide-2.0.5.jar

echo "Sikulix setup complete!"
