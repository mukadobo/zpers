
def depict(String stencil, Map substitutions)
{
    def depiction = new groovy.text.StreamingTemplateEngine().createTemplate(stencil).make(substitutions)

//    println "=========="
//    println stencil
    println "=========="
    println depiction
    println "=========="
    println ''

    depiction
}

def test11()
{
    def stencil = 
'''
$a
$b
    $f + $t
<% if (f) out.print "(($a))\\n" 
%><% if (t) out.print "(($b))\\n" 
%><% if (f) out.print """
    zero
        one
    zero
        - then b=$b"""
%><% if (t) out.print """\\
    nil
        uno
    nil
        - voila b=$b
%><% if (!h.isEmpty()) out.print """
    h ~ $h
        a ~ "$a"
        b ~ "$a/$b"
%><% if (!H.isEmpty()) out.print """
    H ~ $H
        a ~ "$a"
        b ~ "$a/$b"
"""%># EOF
'''.trim()

    def substitutions = [
        a : 'AAA',
        b : 'BBB',
        f : false,
        t : true,
        h : 'HHH',
        H : '',
    ]

    depict(stencil, substitutions)
}

def test22()
{
    def stencil = new File('./Stenciling+stencil.txt').text.trim()

    def substitutions = [
        PROJECT_ID    : 'topo-z999',
        RT_NAME       : 'rt-from',
        SVC_NAME      : 'svc-to',
        HOSTNAME      : '',
        VOLUME_MOUNTS : '',
   ]

    depict(stencil, substitutions)
}

def test33()
{
    def volumeMounts = [
        '- name: "MMM"',
        '  mountPath: "/mmm/mm/mmmm/m"',
        '- name: "NNN"',
        '  mountPath: "/nn/nnn/n/nnnn"',
    ].join("\n")
    println "volumeMounts=\n${volumeMounts}\n"

    def stencil = new File('./Stenciling+stencil.txt').text.trim()

    def substitutions = [
        PROJECT_ID    : 'topo-z999',
        RT_NAME       : 'rt-from',
        SVC_NAME      : 'svc-to',
        HOSTNAME      : 'hostname',
        VOLUME_MOUNTS : volumeMounts,
    ]

    depict(stencil, substitutions)
}

// def v11 = test11()
def v22 = test22()
def v33 = test33()

// assert v11.equals(v22)