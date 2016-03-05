from app import app
from flask import request
from models import utils,watson
@app.route('/')
def index():
	return "got request"
@app.route('/v1/user_question')
def user_question():
        headers = request.headers
	data = {}
	for key,value in headers:
		if "QU-" in key.upper() and key.upper().index("QU-") == 0:
			key_new = (key.upper().split('QU-')[1].lower())
			data[key_new] = value
	question = utils.makequestion(data)
	response = watson.askWatson(question)
        return response
