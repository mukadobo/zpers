@interface NonCPS { }
import groovy.json.JsonBuilder
def trace(pfx, obj) { println "<TRACE> $pfx: ${new JsonBuilder(obj)}" }

// =============================================================================================================================


// =============================================================================================================================

def d = [ aaa : 'aaa', bbb : 'bbb', ]
def m = [ aaa : 'AAA',              ccc : 'CCC' ]

println "d=${d}"
println "m=${m}"
println ''

def s = [:] << d << m
println "d=${d}"
println "m=${m}"
println "s=${s}"
println ''

def t = [:] << (d << m)
println "d=${d}"
println "m=${m}"
println "t=${t}"
println ''

def u = [:] << d <<
[
	bbb: '222', ddd: '444'
]
println "d=${d}"
println "m=${m}"
println "t=${t}"
println "u=${u}"
println ''

