import os
import sys
import shutil
from sikuli import *

# Set up paths
project_root = os.path.abspath(os.path.join(os.path.dirname(os.path.dirname(getBundlePath())), ".."))
screenshots_dir = os.path.join(project_root, "src", "test", "resources", "images", "login")

# Create screenshots directory if it doesn't exist
if not os.path.exists(screenshots_dir):
    os.makedirs(screenshots_dir)

# Wait for the page to load
wait(3)

# Capture email input field (look for the label "Email" and capture below it)
try:
    email_label = find("Email")
    email_region = Region(email_label.x - 150, email_label.y, 300, 100)
    email_region.highlight()
    email_region.save(os.path.join(screenshots_dir, "emailinputlogin.png"))
except:
    print("Could not find email input")

# Capture password input field (look for the label "Password" and capture below it)
try:
    password_label = find("Password")
    password_region = Region(password_label.x - 150, password_label.y, 300, 100)
    password_region.highlight()
    password_region.save(os.path.join(screenshots_dir, "password-input.png"))
except:
    print("Could not find password input")

# Capture login button (look for text "Login")
try:
    login_button = find("Login")
    login_region = Region(login_button.x - 50, login_button.y - 15, 100, 30)
    login_region.highlight()
    login_region.save(os.path.join(screenshots_dir, "login-button.png"))
except:
    print("Could not find login button")

print("Login elements captured successfully")
