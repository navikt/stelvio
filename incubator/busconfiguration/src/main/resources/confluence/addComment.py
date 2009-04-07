from xmlrpclib import Server
import sys
import re

if len(sys.argv) < 5:
        print("ERROR (addComment.py): Syntax: python addComment.py <environment> <user> <pwd> <comment> <bustag> ")
        sys.exit(1)

ENV = sys.argv[1]
USER=sys.argv[2]
PWD=sys.argv[3]
COMMENT=sys.argv[4]
BUSTAG=None
if(len(sys.argv) ==6):
	BUSTAG=sys.argv[5]

envMappings = {"Sandbox2":"Sandbox+2","Sandbox3":"Sandbox+3","Sandbox4":"Sandbox+4",
	       "K1":"Komponenttest+1","K2":"Komponenttest+2","K3":"Komponenttest+3","K4":"Komponenttest+4","K5":"Komponenttest+5",
	       "I1":"Integrasjonstest+1","I2":"Integrasjonstest+2","I3":"Integrasjonstest+3",
	       "T1":"Systemtest+1","T2":"Systemtest+2","T3":"Systemtest+3","T4":"Systemtest+4","T5":"Systemtest+5","T6":"Systemtest+6","T7":"Systemtest+7","T8":"Systemtest+8","T9":"Systemtest+9",
	       "Q1":"Q1","Q2":"Q2","Q5":"Q5","P":"Produksjon"}


if(envMappings.get(ENV) < 0 ):
        print("WARNING (addComment.py): Not adding comment for environment " + ENV + ". It is not found")
        sys.exit(0)
	
url = "http://confluence.adeo.no/display/stelvio/" + envMappings[ENV]

terms = re.match('(?i)(^.*?)(?:/display/)(.*?)/(.*$)',url).groups();

server = terms[0]
space = terms[1]
page = terms[2].replace('+',' ')

s = Server(server + "/rpc/xmlrpc")

token = s.confluence1.login(USER,PWD)

thePage = s.confluence1.getPage(token, space, page)

#Add comment
thePageId = thePage["id"]
s.confluence1.getComments(token, thePageId)
comment = {"pageId":thePageId,"content":COMMENT}
s.confluence1.addComment(token, comment)

#Update bustag version
if(BUSTAG != None and BUSTAG != ""):
	content = thePage["content"]
	for line in content.split('\n'):
        	if(line.find("WPS services")>=0):
                	oldline = line
                	break
	newline = "|| WPS services | Bus tag " + BUSTAG + " \\\\ | ||"
	content = content.replace(oldline, newline)
	thePage["content"] = content
	s.confluence1.storePage(token, thePage)

