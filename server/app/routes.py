from app import app
from flask import request
from models import utils,watson
from flask import jsonify

@app.route('/v1/user_question')
def user_question():
        headers = request.headers
	question = utils.makequestion(headers)
	response = watson.askWatson(question)
	customised_response = jsonify({"answer":response})
       	return customised_response

@app.errorhandler(500)
def internal_server_error(error):
	print "Internal server error ",error
	response = jsonify({"answer":"Please check back later.Server is experiencing problems"})
	response.status_code = 200
	return response
