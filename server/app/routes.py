from app import app
from flask import request
from models import utils,watson
from constants import *


@app.route('/v1/user_question')
def user_question():
        headers = request.headers
	if free_question_key in headers:
		question = headers[free_question_key]
	else:
		data = {}
		for key,value in headers:
			if form_key in key.upper() and key.upper().index(form_key) == 0:
				key_new = (key.upper().split(form_key)[1].lower())
				data[key_new] = value
		question = utils.makequestion(data)
	response = watson.askWatson(question)
       	return response
