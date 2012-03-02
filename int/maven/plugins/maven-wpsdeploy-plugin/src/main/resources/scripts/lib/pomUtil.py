import re, os

DEFAULTMVNREPO = 'http://maven.adeo.no/m2internal'
DEFAULTMVNSNAPSHOTREPO = 'http://maven.adeo.no/nexus/content/repositories/m2snapshot'
DEFAULTMVNLOCALREPO = ''

class Module:
	"""Denne modulen kan bli kalt med:
		- navgitte argument (artifactId='', groupId='', version='', type='')
		- ikke navngitte arugmenter i denne rekkefolgen ('groupId', 'artifactId', 'type', 'version')
	"""
	def __init__(self, *args, **kwargs):
		if kwargs:
			self.groupId = kwargs['groupId']
			self.artifactId = kwargs['artifactId']
			self.type = kwargs['type']
			self.version = kwargs['version']
			if kwargs.has_key('localRepo') and kwargs.has_key('repo') and kwargs.has_key('snapshotRepo'):
				self.localRepo = kwargs['localRepo']
				self.repo = kwargs['repo']
				self.snapshotRepo = kwargs['snapshotRepo']
		else:
			self.groupId = args[0]
			self.artifactId = args[1]
			self.type = args[2]
			self.version = args[3]
			if len(args) == 7:
				self.localRepo = args[4]
				self.repo = args[5]
				self.snapshotRepo = args[6]
				
		if not hasattr(self, 'localRepo'):
			self.localRepo = DEFAULTMVNLOCALREPO
			self.repo = DEFAULTMVNREPO
			self.snapshotRepo = DEFAULTMVNSNAPSHOTREPO
			

	def __hash__(self):
		return hash(self.artifactId + self.version)

	def __str__(self):
		return 'artifactId:\t%s\ngroupId:\t%s\nversion:\t%s\ntype:\t\t%s\n--' % (self.artifactId, self.groupId, self.version, self.type)

	def __eq__(self, other):
		if isinstance(other, self.__class__):
			return (self.artifactId == other.artifactId and
				self.groupId == other.groupId and
				self.type == other.type and
				self.version == other.version)
		else:
			return False
	
	def __resolveRepo(self):
		if re.search('-SNAPSHOT', self.version): return self.snapshotRepo
		else: return self.repo

	def getUrl(self):
		groupUrl = self.groupId.replace('.','/')
		url = '/%s/%s/%s' % (groupUrl, self.artifactId, self.version)
		fileUrl = self.localRepo + url
		httpUrl = self.__resolveRepo() + url
		if os.path.exists(fileUrl.replace('file://','')): return fileUrl
		else: return httpUrl
		
		

	def getWsdlifUrl(self):
		return '%s/%s-%s-wsdlif.zip' % (self.getUrl(), self.artifactId, self.version)