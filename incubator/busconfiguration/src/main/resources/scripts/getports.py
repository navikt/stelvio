import java
lineSeparator = java.lang.System.getProperty('line.separator')

# get Nodes
Node_ids = AdminConfig.getid('/Node:/')
Node_idarray = Node_ids.split(lineSeparator)

# get Ports
EndPoint_ids = AdminConfig.getid('/EndPoint:/')
EndPoint_idarray = EndPoint_ids.split(lineSeparator)
NamedEndPoint_ids = AdminConfig.getid('/NamedEndPoint:/')
NamedEndPoint_idarray = NamedEndPoint_ids.split(lineSeparator)

# print
for x in range(len(Node_idarray)):
        for y in range(len(EndPoint_idarray)):
                if EndPoint_idarray[y].find(AdminConfig.showAttribute(Node_idarray[x],'name')) > 0:
                        print AdminConfig.showAttribute(Node_idarray[x],'name'),AdminConfig.showAttribute(NamedEndPoint_idarray[y],'endPointName'),AdminConfig.showAttribute(EndPoint_idarray[y],'port')
