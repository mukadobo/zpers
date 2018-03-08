def names = ["hello", "loctn", "xform", "xxxxx"]
def svcPorts = """
hello
loctn ~ 9301

   xform~8888 foo
Hihello~123
xxxxxQ~123

"""

for (name in names) {
	println("----------------------")
	
	println("name=$name")
	
	def findPort = (svcPorts =~ /\b$name[ \t]*~[ \t]*(\d+)/)
	
	if (findPort.size() == 0) { println "no-match"; continue; }
	
	println findPort[0][1]
}
println("----------------------")
