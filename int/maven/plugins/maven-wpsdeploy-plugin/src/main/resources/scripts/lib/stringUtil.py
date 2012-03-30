False, True = 0,1 #Define False, True
def strip(string, charsToBeRemoved=' '):
	removedChar = True
	while removedChar:
		removedChar = False
		for char in charsToBeRemoved:
			if string.startswith(char):
				string = string[1:]
				removedChar = True
			if string.endswith(char):
				string = string[:-1]
				removedChar = True
	return string
	