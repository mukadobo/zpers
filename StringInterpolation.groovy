def rawAB = '''
a=${a}
b=${b}
'''

def rawJK = '''
j=${j}
k=${k}
'''

def stEngine = new groovy.text.SimpleTemplateEngine()
def tmplAB   = stEngine.createTemplate(rawAB)
def tmplJK   = stEngine.createTemplate(rawJK)

def result

def valuesAB = [
	a : 'AAA',
	b : 'BBB',
]

def valuesJK = [
	j : 'JJJ',
	k : 'KKK',
]

println ('-----')

result = tmplAB.make(valuesAB).toString().trim()
println(result)

println ('-----')

valuesAB['a'] = 'aaa'
result = tmplAB.make(valuesAB).toString().trim()
println(result)

println ('-----')

def bindingJK = tmplJK.make(valuesJK)
result = bindingJK.toString().trim()
println(result)

println ('-----')

valuesJK['j'] = 'jjj'
result = bindingJK.toString().trim()
println(result)

println ('-----')

try {
	result = tmplJK.make(valuesAB).toString().trim()
	println(result)
}
catch(MissingPropertyException e) {
	String msg = e.getMessage()
	println msg
	
	String prop = msg.replaceFirst(/^No such property: /, '').replaceFirst(/ .*/, '')
	println "Property '${prop}' is not defined."
}

println ('-----')

result = new org.apache.commons.lang3.text.StrSubstitutor(valuesAB).replace(rawAB)
println(result)

println ('-----')

valuesAB.b = 'bbb'
result = new org.apache.commons.lang3.text.StrSubstitutor(valuesAB).replace(rawAB)
println(result)

println ('-----')
