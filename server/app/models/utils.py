from constants import *
import itertools
import urllib


def filter_data(data):
	usabledata = {}
	for key in data:
		if form_key in key and key.index(form_key) == 0 and data[key] != 'true' and data[key] != 'cm' and data[key] != 'false' and data[key] != 'Not sure' and len(data[key]) >1:
	   		key_new = (key.split(form_key)[1]).lower()
		        usabledata[key_new] = data[key]
	return usabledata

def makequestion(data,no_of_keys):
	combs = list(itertools.combinations(data.keys(),no_of_keys))
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
	context_animal = ''
	if len(data.keys()) == 0:
		raise Exception("Error","No data sent")
	if len(data.keys()) <= 2 and free_question_key in data.keys():
		if 'qu_animal_name' in data.keys():
			context_animal = data['qu_animal_name']
		key = free_question_key
		if data[key] != 'false' and data[key] != 'Not sure' and len(data[key]) >1:
			ques = data[key]
			if "their" in ques:
				ques = ques.replace("their",context_animal+"'s")
			elif "it" in ques:
				ques = ques.replace("it",context_animal)
			elif "they" in ques:
				ques = ques.replace("they",context_animal+"s")
			elif "them" in ques:
				ques = ques.replace("them",context_animal+"s")
			questions.append(ques)
	else:
		data = filter_data(data)
		keys = [len(data.keys())]
		for i in keys:
			q_s = makequestion(data,i)
			for q in q_s:
				questions.append(q)
	return questions,context_animal

def filteranswer(answers,context_animal):
	if len(answers.keys()):
		if context_animal and len(context_animal):
			context_animal = urllib.unquote(context_animal)
			answers = {k:v for k,v in answers.iteritems() if answers[k]['title'] == context_animal}
		key = sorted(answers,key=lambda x:answers[x]['count'],reverse=True)[0]
		return {"answer":answers[key]['text'],"animal":answers[key]['title']}
	else:	
		return "No Answer found"
