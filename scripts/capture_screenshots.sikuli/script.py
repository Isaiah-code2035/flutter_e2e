import os
import time
import webbrowser
from sikuli import *

# Set up paths
BASE_PATH = "/Users/oluwabamise/flutter-e2e-tests"
IMAGE_PATH = os.path.join(BASE_PATH, "src/test/resources/images")

def create_dir_if_not_exists(path):
    if not os.path.exists(path):
        os.makedirs(path)
        print("Created directory: %s" % path)

def wait_for_user():
    input("Press ENTER to continue...")

def capture_element(name, save_path):
    print(f"\nCapture {name}")
    print("Move your mouse to the element and press CTRL to capture")
    time.sleep(3)
    capture = Screen().userCapture()
    if capture:
        capture.save(save_path)
        print(f"Saved to {save_path}")
    else:
        print("Capture cancelled")

def capture_login_page():
    # Create login directory if it doesn't exist
    login_dir = os.path.join(IMAGE_PATH, "login")
    if not os.path.exists(login_dir):
        os.makedirs(login_dir)

    # Open login page
    url = "https://team-building-balancer.web.app/#/signin"
    print(f"\nOpening {url}")
    webbrowser.open(url)
    time.sleep(5)  # Wait for page to load

    # Email input
    capture_element("email input field", os.path.join(login_dir, "email-input.png"))

    # Password input
    capture_element("password input field", os.path.join(login_dir, "password-input.png"))

    # Login button
    capture_element("login button", os.path.join(login_dir, "login-button.png"))

def capture_signup_link():
    # Create registration directory
    reg_path = os.path.join(IMAGE_PATH, "registration")
    create_dir_if_not_exists(reg_path)
    
    print("\nStarting signup link capture...")
    print("Please follow these steps:")
    print("1. Wait for the signin page to load")
    print("2. Press CMD + SHIFT + 4")
    print("3. Draw a SMALL rectangle around ONLY the text 'Don't have account? Signup'")
    print("4. Try to keep the capture area as small as possible")
    print("5. Make sure the text is clearly visible and not cut off")
    print("6. Press ENTER when done")
    print("\nPress ENTER to start")
    wait_for_user()
    
    print("\nCapture the signup link")
    print("Press CMD + SHIFT + 4, draw a SMALL rectangle, then press ENTER")
    wait_for_user()

def capture_registration_page():
    # Create registration directory
    registration_dir = os.path.join(IMAGE_PATH, "registration")
    if not os.path.exists(registration_dir):
        os.makedirs(registration_dir)

    # Open registration page
    url = "https://team-building-balancer.web.app/#/signup"
    print(f"\nOpening {url}")
    webbrowser.open(url)
    time.sleep(5)  # Wait for page to load

    # Name input
    capture_element("name input field", os.path.join(registration_dir, "name-input.png"))

    # Email input
    capture_element("email input field", os.path.join(registration_dir, "email-input.png"))

    # Password input
    capture_element("password input field", os.path.join(registration_dir, "password-input.png"))

    # Confirm password input
    capture_element("confirm password field", os.path.join(registration_dir, "confirm-password.png"))

    # Register button
    capture_element("register button", os.path.join(registration_dir, "register-button.png"))

# Main execution
if __name__ == "__main__":
    # Create base directories
    os.makedirs(IMAGE_PATH, exist_ok=True)

    # Capture login page elements
    capture_login_page()

    # Capture registration page elements
    capture_registration_page()
