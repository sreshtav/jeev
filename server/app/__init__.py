from flask import Flask
app = Flask("jeev" , template_folder= "./app/templates", static_folder='./app/static')

# Entry point for all apps ....
import routes
