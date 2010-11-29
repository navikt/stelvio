if [ $# == 0 ]
then

	errorpages="errorpages.txt"

 	echo "!This script can take a file with a list of 
errorpages that shall be updated as an input parameter.
The list of filenames must be seperated with a new line.
Since no input parameter was provided, the default 
'errorpages.txt' will be used.!"

else
	errorpages=$1	
fi

 
if [ ! -f $errorpages ]
then
	echo "$errorpages doesn't exist"
	exit 1
fi

errorfiletemplate="errorpagetemplate.html"
errorfiletemplateData=""
errorfiletemplateData=`cat $errorfiletemplate`

echo "Updating errorfiles with template..."

noOfFilesUpdated=0

while read errorpagefilename
do
if [ `echo $errorpagefilename |egrep [a-zA-Z0-9].` ] 
then
	if [ ! '$errorpagefilename == *.html'  ]
	then
		echo "Since $errorpagefilename isn't an html-file it wount be updated"
	else
		if [ -f $errorpagefilename ]
		then
			echo "$errorfiletemplateData" > $errorpagefilename
			echo "$errorpagefilename was updated"
			noOfFilesUpdated=`expr $noOfFilesUpdated + 1`
		else
			echo "$errorpagefilename dosn't exist"
		fi
	fi
fi
done < $errorpages

if [ $noOfFilesUpdated == 0 ]
then
	echo "No errorfiles was updated"
else
	echo "$noOfFilesUpdated errorfiles was updated with the template"
fi

exit 0

