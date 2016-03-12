from app import app
from flask import request
from models import utils,watson
from flask import jsonify

@app.route('/v1/user_question')
def user_question():
	arguments = request.args
	questions = utils.makequestions(arguments)
	responses = list()
	for question in questions:
		res_ques = watson.askWatson(question)
		print res_ques
		for r in res_ques:
			if r in responses:
				responses[r].count += 1
			else:
				responses[r] = res_ques[r]
	print responses
	fin_response = utils.filteranswer(responses)
	customised_response = jsonify({"answer":fin_response})
   	return customised_response

@app.errorhandler(500)
def internal_server_error(error):
	print "Internal server error ",error
	response = jsonify({"answer":"Please check back later.Server is experiencing problems"})
	response.status_code = 200
	return response
