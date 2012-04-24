'''http://en.wikipedia.org/wiki/Insertion_sort'''

def dict(sequence):
	returnDict = {}
	for key, value in sequence:
		returnDict[key] = value
	return returnDict
	
	
def sorted(list, key=None, reverse=False):
	if key:
		if not list: 
			return []
		else:
			pivot = list[0]
			pivotKey = key(pivot)
			lesser = sorted([x for x in list[1:] if key(x) < pivotKey], key)
			greater = sorted([x for x in list[1:] if key(x) >= pivotKey], key)
			list = lesser + [pivot] + greater
	else:
		list = list[:]
		list.sort()
		
	if reverse:
		list.reverse()
	
	return list