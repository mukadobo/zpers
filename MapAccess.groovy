def m = [B:"BBB"]

m.A = "aaa"

def derive(s) { return "<<$s>>" }

println m.A
println derive(m.B)

def n = [B: derive(m.B)]

n.A = derive(m.A)

println n.A
println n.B

def kkk = ['A']
kkk += 'B'

if ('true'.equals('true')) kkk += 'C'

for (k in kkk)
	println k