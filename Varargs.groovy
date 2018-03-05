import groovy.json.JsonBuilder

def vameth(Object... objs) {
	println "objs.getClass()=${objs.getClass()}; objs=${objs}"
	objs.each { obj -> println "- obj.getClass()=${obj.getClass()}; obj=${obj}" }

	
	def String[] args = objs as String[]
	println "args.getClass()=${args.getClass()}; args=${args}"
	args.each { arg -> println "- arg.getClass()=${arg.getClass()}; arg=${arg}" }
	
	println ''
}

vameth('a', "b")

def specParts = "aaa|bbb|ccc".split(/\|/)

def user = specParts[0]
def role = specParts[1]
def nspc = specParts[2]

vameth(user,role,nspc)
vameth('foo',user,role,nspc)
vameth('bar',"${user}")
