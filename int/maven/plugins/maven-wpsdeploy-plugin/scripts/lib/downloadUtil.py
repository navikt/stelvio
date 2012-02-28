import os,re
import urllib

#Class used to make urlopen produce an exception
class __MyUrlOpener(urllib.FancyURLopener):
    def http_error_default(*args, **kwargs):
        return urllib.URLopener.http_error_default(*args, **kwargs)

urllib._urlopener = __MyUrlOpener()

def downloadFile(url, toDir): 
	fileName = re.search('.*/(\S+)$', url).group(1)
	filePath = toDir +'/'+ fileName
	urllib.urlretrieve(url, filePath)
	
def getPage(url): return urllib.urlopen(url).read()