from constants import *
import itertools


def filter_data(data):
	usabledata = {}
	for key in data:
		if form_key in key and key.index(form_key) == 0 and data[key] != 'true' and data[key] != 'cm' and data[key] != 'false' and data[key] != 'Not sure' and len(data[key]) >1:
	   		key_new = (key.split(form_key)[1]).lower()
		        usabledata[key_new] = data[key]
	return usabledata

def makequestion(data,no_of_keys):
	data = filter_data(data)
	combs = list(itertools.combinations(data.keys(),no_of_keys))
	print no_of_keys,combs
	sub_questions = list()
	for keys in combs:
		question = "What animal is "
		for key in keys:
			question += data[key] + " and "
		question = question[:-4]
		sub_questions.append(question)
	return sub_questions

def makequestions(data):
	questions = list()
	if len(data.keys()) == 0:
		raise Exception("Error","No data sent")
	if len(data.keys()) == 1:
		if free_question_key in  data.keys()[0]:
			key = data.keys()[0]
			if data[key] != 'false' and data[key] != 'Not sure' and len(data[key]) >1:
				questions.append(data[key])
	else:
		keys = [2,3,4]
		for i in keys:
			q_s = makequestion(data,i)
			for q in q_s:
				questions.append(q)
	return questions

def filteranswer(answers):
	if len(answers.keys()):
		key = sorted(answers,key=lambda x:answers[x]['count'],reverse=True)[0]
		return answers[key]['text'] 
	else:	
		return "No Answer found"
