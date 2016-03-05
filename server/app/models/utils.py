def makequestion(data):
	question = "What animal is "
	for key in data:
		question += data[key] + " and "
	question = question[:-4]
	return question
