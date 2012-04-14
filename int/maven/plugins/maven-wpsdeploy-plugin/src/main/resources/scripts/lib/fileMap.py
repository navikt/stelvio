import lib.fileMapPath as fileMapPath

delimiter = ','
mapPath = fileMapPath.getPath()
fileMap = {}

for line in open(mapPath).readlines():
	key, value = line.strip().split(delimiter, 1)
	fileMap[key] = value
	
def get(key):
	return fileMap[key]
	
def set(key, value):
	if key.count(delimiter):
		raise ValueError('A key cannot contain a "%s"!' % delimiter)
	fileMap[key] = value