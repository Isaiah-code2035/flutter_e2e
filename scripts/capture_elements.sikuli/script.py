import os
import sys
import shutil
from sikuli import *

# Set up paths
project_root = os.path.abspath(os.path.join(os.path.dirname(os.path.dirname(getBundlePath())), ".."))
screenshots_dir = os.path.join(project_root, "src", "test", "resources", "images", "registration")

# Create screenshots directory if it doesn't exist
if not os.path.exists(screenshots_dir):
    os.makedirs(screenshots_dir)

# Open the webpage
webbrowser.open("https://team-building-balancer.web.app/#/onboarding")

# Wait for the page to load
wait(3)

# Switch to the browser window
switchApp("Chrome")
wait(2)

# Capture signup link
try:
    signup_link = find("Sign up")
    signup_region = Region(signup_link.x - 50, signup_link.y - 15, 100, 30)
    signup_region.highlight()
    signup_region.save(os.path.join(screenshots_dir, "signup-link.png"))
except:
    print("Could not find signup link")

# Capture name input field
try:
    name_label = find("Name")
    name_region = Region(name_label.x - 150, name_label.y, 300, 100)
    name_region.highlight()
    name_region.save(os.path.join(screenshots_dir, "name-input.png"))
except:
    print("Could not find name input")

# Capture email input field
try:
    email_label = find("Email")
    email_region = Region(email_label.x - 150, email_label.y, 300, 100)
    email_region.highlight()
    email_region.save(os.path.join(screenshots_dir, "email-input.png"))
except:
    print("Could not find email input")

# Capture password input field
try:
    password_label = find("Password")
    password_region = Region(password_label.x - 150, password_label.y, 300, 100)
    password_region.highlight()
    password_region.save(os.path.join(screenshots_dir, "password-input.png"))
except:
    print("Could not find password input")

# Capture confirm password field
try:
    confirm_password_label = find("Confirm Password")
    confirm_password_region = Region(confirm_password_label.x - 150, confirm_password_label.y, 300, 100)
    confirm_password_region.highlight()
    confirm_password_region.save(os.path.join(screenshots_dir, "confirm-password.png"))
except:
    print("Could not find confirm password input")

# Capture register button
try:
    register_button = find("Register")
    register_region = Region(register_button.x - 50, register_button.y - 15, 100, 30)
    register_region.highlight()
    register_region.save(os.path.join(screenshots_dir, "register-button.png"))
except:
    print("Could not find register button")

# Capture login button
try:
    login_button = find("Login")
    login_region = Region(login_button.x - 50, login_button.y - 15, 100, 30)
    login_region.highlight()
    login_region.save(os.path.join(screenshots_dir, "login-button.png"))
except:
    print("Could not find login button")

# Capture welcome text
try:
    welcome_text = find("Welcome to Team Building Balancer")
    welcome_region = Region(welcome_text.x - 150, welcome_text.y, 300, 100)
    welcome_region.highlight()
    welcome_region.save(os.path.join(screenshots_dir, "welcome-text.png"))
except:
    print("Could not find welcome text")

# Capture get started button
try:
    get_started_button = find("Get Started")
    get_started_region = Region(get_started_button.x - 50, get_started_button.y - 15, 100, 30)
    get_started_region.highlight()
    get_started_region.save(os.path.join(screenshots_dir, "get-started-button.png"))
except:
    print("Could not find get started button")

print("All elements captured successfully")
