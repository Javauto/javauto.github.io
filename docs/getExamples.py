from bs4 import BeautifulSoup
import urllib2

def getExampleCode( link ):
	'''get the example code as a string from a certain doc page'''
	bsoup = BeautifulSoup( urllib2.urlopen(link) )
	text = bsoup.pre
	return text.get_text()

# get the main page and create soup object
print "Reading page..."
page = urllib2.urlopen("http://javauto.org/docs/docs.html")
soup = BeautifulSoup(page)

# get each link on the page
funcLinks = soup.find_all("a")

# get a list of function links
links = []
for func in funcLinks:
	if func.get_text().find(" ") == -1:
		links.append(func.get('href'))
links = links[1:]

# the base page that the links are relative to
base = "http://javauto.org/docs/"

# create a master list for function names and example code
master = [] # each index will be like [function name, example code]

# now we get the code example for each link and append to master list
print "Getting code examples..."
for link in links:
	code = getExampleCode(base + link)
	name = link.split("/")[-1].split(".")[0]
	master.append([name, code])

# construct a text file with all our data
total = ""
for function in master:
	total = total + "* " + function[0] + "\n" + function[1] + "\n\n\n"

# write our examples
f = open("examples.txt", "w")
f.write(total)
f.close()

print "Done"
