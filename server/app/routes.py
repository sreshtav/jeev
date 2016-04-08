from app import app
from flask import request
from models import utils,watson
from flask import jsonify

@app.route('/v1/user_question')
def user_question():
	arguments = request.args
	questions,context_animal = utils.makequestions(arguments)
	responses = {}
	for question in questions:
		res_ques = watson.askWatson(question)
		for r in res_ques:
			if r in responses:
				responses[r]['count'] += 1
			else:
				responses[r] = res_ques[r]
	fin_response = utils.filteranswer(responses,context_animal)
	customised_response = jsonify(fin_response)
   	return customised_response

@app.errorhandler(500)
def internal_server_error(error):
	print "Internal server error ",error
	response = jsonify({"answer":"Please check back later.Server is experiencing problems"})
	response.status_code = 200
	return response
