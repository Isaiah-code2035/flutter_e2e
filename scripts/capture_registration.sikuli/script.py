import webbrowser
from sikuli import *

# Open the webpage
webbrowser.open("https://team-building-balancer.web.app/#/onboarding")

# Wait for the page to load
wait(5)

# Switch to the browser window
switchApp("Chrome")
wait(2)

# Click the signup link
click("Sign up")
wait(2)

# Function to capture and save
def capture_element(name):
    # Show instruction
    popup("Click OK, then draw a SMALL rectangle around the " + name + " (just the input box, not the label)")
    # Capture region
    region = selectRegion()
    # Save the capture
    region.save("src/test/resources/images/registration/" + name + ".png")
    popup("Saved " + name + ".png")

# Capture registration elements
capture_element("name-input")
capture_element("email-input")
capture_element("password-input")
capture_element("confirm-password")
capture_element("register-button")
