def makequestion(data):
	question = "What is an animal that is "
	for key in data:
		question += data[key] + " and "
	question = question[:-4]
	print question
	return question
