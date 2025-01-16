import webbrowser
from sikuli import *

# Open the webpage
webbrowser.open("https://team-building-balancer.web.app/#/onboarding")

# Wait for the page to load
wait(5)

# Switch to Chrome
switchApp("Chrome")
wait(2)
