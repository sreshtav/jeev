from constants import *
import itertools


def filter_data(data):
	usabledata = {}
	for key in data:
		if form_key in key and key.index(form_key) == 0 and data[key] != 'false' and len(data[key]) >1:
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
	if len(data.keys()) == 0:
		raise Exception("Error","No data sent")
	keys = [2,3,4]
	questions = list()
	for i in keys:
		q_s = makequestion(data,i)
		for q in q_s:
			questions.append(q)
	return questions

def filteranswer(answers):
	return answers[0]