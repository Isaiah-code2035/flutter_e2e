#!/bin/bash

# Create images directory if it doesn't exist
mkdir -p src/test/resources/images/onboarding

# Download and start Sikulix IDE
java -jar lib/sikulixide-2.0.5.jar

echo "Instructions for capturing screenshots:"
echo "1. Open the Flutter app in Chrome at https://team-building-balancer.web.app/#/onboarding"
echo "2. Use Sikulix IDE to capture the following elements:"
echo "   - welcome-text.png: The welcome text on the onboarding page"
echo "   - get-started-button.png: The 'Get Started' button"
echo "3. Save the screenshots in src/test/resources/images/onboarding/"
echo "4. Close Sikulix IDE when done"

# Run the Sikuli scripts
sikuli-ide -r "scripts/capture_screenshots.sikuli"
sikuli-ide -r "scripts/capture_elements.sikuli"
sikuli-ide -r "scripts/capture_login_elements.sikuli"

echo "Screenshots captured successfully"
