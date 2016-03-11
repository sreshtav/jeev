from constants import *
def makequestion(data):
	if len(data.keys()) == 0:
		raise Exception("Error","No data sent")			
	question = "What animal is "
	if free_question_key in data:
                question = data[free_question_key]
        else:
                question_data = {}
                for key in data:
                	if form_key in key and key.index(form_key) == 0:
                   	    key_new = (key.split(form_key)[1]).lower()
                        question_data[key_new] = data[key]
		for key in question_data:
			question += question_data[key] + " and "
		question = question[:-4]
	return question
