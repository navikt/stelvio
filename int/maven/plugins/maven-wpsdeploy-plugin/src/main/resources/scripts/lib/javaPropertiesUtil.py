from glob import glob
from java.util import Properties
from java.io import FileInputStream
import lib.logUtil as log
l = log.getLogger(__name__)
	
class PropertiesReader:
	def __init__(self):
		self.props = Properties()
	
	def load(self, prop):
		if prop.lower().endswith('.properties'):
			self.loadFile(prop)
		else:
			self.loadDir(prop)
	
	def loadFile(self, propertiesFile):
		f = FileInputStream(propertiesFile)
		self.props.load(f)
	
	def loadDir(self, path):
		files = glob(path + '/*.properties')
		for f in files:
			self.loadFile(f)
		
	def get(self, key):
		p = self.props.getProperty(key)
		if not p:
			return ""
		else:
			return p.strip()