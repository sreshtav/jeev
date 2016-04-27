from constants import *
import itertools
import nltk
from nltk.corpus import wordnet as wn
from operator import itemgetter
def filter_data(data):
	usabledata = {}
	for key in data:
		if form_key in key and key.index(form_key) == 0 and data[key] != 'true' and data[key] != 'cm' and data[key] != 'false' and data[key] != 'Not sure' and len(data[key]) >1:
	   		key_new = (key.split(form_key)[1]).lower()
		        usabledata[key_new] = data[key]
	return usabledata

def makequestion(data,no_of_keys):
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
		data = filter_data(data)
		keys = [len(data.keys())]
		for i in keys:
			q_s = makequestion(data,i)
			for q in q_s:
				questions.append(q)
	return questions


def highlight_answer(answer,questions):
	sent_detector = nltk.data.load('tokenizers/punkt/english.pickle')
	question_words = []
	question_words_roots = []
	for q in questions:
		q_tagged = nltk.pos_tag(nltk.word_tokenize(q))
		for word,tag in q_tagged:
			if tag not in ['CC','DT', 'WP', 'IN', 'CD', ',', '.'] and word not in ['is','are','am','was','were', 'the', 'of', 'a', 'as']:
				question_words.append(word)

	text = answer['answer']
	sentences = sent_detector.tokenize(text)
	answer_words = [[] for i in range(len(sentences))]
	answer_words_roots = [[] for i in range(len(sentences))]
	match_score = [0] * len(sentences)
	for ix,s in enumerate(sentences):
		a_s_tagged = nltk.pos_tag(nltk.word_tokenize(s))
		for word,tag in a_s_tagged:
			if tag not in ['CC','DT', 'WP', 'IN', 'CD', ',', '.'] and word not in ['is','are','am','was','were', 'the', 'of', 'a', 'as']:
				answer_words[ix].append(word)

	for w in question_words:
		synsets = wn.synsets(w)
		if len(synsets):
			question_words_roots.append(synsets[0].name().split('.')[0])
		else:
			question_words_roots.append(w)

	for ix,list_w in enumerate(answer_words):
		for w in list_w:
			synsets = wn.synsets(w)
			if len(synsets):
				answer_words_roots[ix].append(synsets[0].name().split('.')[0])
			else:
				answer_words_roots[ix].append(w)

	print question_words_roots,answer_words_roots
	question_set = set(question_words_roots)
	for ix,list_root_words in enumerate(answer_words_roots):
		answer_set = set(list_root_words)
		match_score[ix] = len(question_set.intersection(answer_set))
	sorted_scores_indexes = sorted(range(len(match_score)), key=lambda i:match_score[i],reverse = True)[:3]
	answer['indexes'] = sorted_scores_indexes
	return answer

def filteranswer(answers):
	ans = ''
	if len(answers.keys()):
		key = sorted(answers,key=lambda x:answers[x]['count'],reverse=True)[0]
		ans = {"answer":answers[key]['text'],"animal":answers[key]['title']}
	else:	
		ans = "No Answer found"
	return ans