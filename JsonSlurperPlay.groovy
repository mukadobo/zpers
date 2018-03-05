
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import groovy.json.JsonParserType

def String cleanupUnquotedJson(String text)
{
	text
		.trim()
		.replaceAll(/^[^{]/, /{ $0/)
		.replaceAll(/[^}]$/, /$0 }/)
		.replaceAll(/%/,    /%25/)
		.replaceAll(/\\:/,  /%3A/)
		.replaceAll(/\\,/,  /%2C/)
		.replaceAll(/\\"/,  /%22/)
		.replaceAll(/\\\{/, /%7B/)
		.replaceAll(/\\}/,  /%7D/)
		.replaceAll(/\\\[/, /%5B/)
		.replaceAll(/\\\]/, /%5D/)
		.replaceAll(/""/, /"""/)
		.replaceAll(/[^\]\[{},:"]+/, /"$0"/)
		.replaceAll(/""/, /"/)
		.replaceAll(/" +"/, / /)
		.replaceAll(/,"(\s*)""(\w+)":/, /,$1"$2":/)
		.replaceAll(/%3A/, /:/)
		.replaceAll(/%2C/, /,/)
		.replaceAll(/%22/, /"/)
		.replaceAll(/%7B/, /{/)
		.replaceAll(/%7D/, /}/)
		.replaceAll(/%5B/, /[/)
		.replaceAll(/%5D/, /]/)
		.replaceAll(/%25/, /%/)
		.replaceAll(/\\([\]\[{}:,])/, /$1/)
}

def Object parseUnquotedJson(String text)
{
	def cleaned = cleanupUnquotedJson(text)
	
	return new JsonSlurper().parseText(cleaned)
}

def Object parseWellFormedJson(String text)
{
	return new JsonSlurper().parseText(text)
}

def raaa = '''
{"a":"aa aa", 
"bout": {"b11":"bbb","b22":[11,22,33], 
"c": {"c11":"ccc", "c22":[99,88,77]}, "c33":""},
"daa": {"d11":"ddd",}, "dbb": {"d22":"DDD"},
"e2": "a\\:A\\, b\\:\\{b1\\:B\\}\\, c\\:\\{\\}\\, d\\:\\[d1\\,d%2\\,d3\\]",
"f": [ { "faa":"FAA" }, { "fbb":"FBB" }]
 }
'''.replaceAll(/\n/, ' ').trim()

def rbbb = raaa.replaceAll(/"/,'')
	.replace(/a:aa aa/, /"a":"aa aa"/)
	.replace(/b11:bbb/, /b11:"bb'bb"/)
	.replace(/d11:ddd/, /d11:"d-d.1~foo+x|z z z"/)
	
def rccc = cleanupUnquotedJson(raaa)

println ("raaa=$raaa")
println ("rbbb=$rbbb")
println ("rccc=$rccc")
println ()


def test111 (String dirty)
{
	println ("dirty=$dirty")
	println ()
	
	def clean = cleanupUnquotedJson(dirty)
	println ("clean=$clean")
	println ()

	def obj = parseUnquotedJson(dirty)
	println ("obj=$obj")
	println ()

	println ("obj.asJson=${new JsonBuilder(obj)}")
	println ()
	
	println('--------------------')
	
	return obj
}

def obj = test111(raaa)
test111(obj.e2)

println("===============================================")

def test222(Object objGiven)
{
	println("objGiven=${objGiven}")
	println()
	
	def jsonGiven = new JsonBuilder(objGiven).toString()
	println("jsonGiven=${jsonGiven}")
	println()
	
	def jsonStripped = jsonGiven.replaceAll(/"/,'')
								.replaceAll(/docker-registry.default.svc/, $/"$0\\/$)
								.replaceAll(/4097ef28e356d1/,              '$0"')
								.replaceAll(/@sha256/, $/$0\\/$)
	println("jsonStripped=${jsonStripped}")
	println()
	
	def jsonCleaned = cleanupUnquotedJson(jsonStripped)
	println("jsonCleaned=${jsonCleaned}")
	println()
	
	def objRebuilt = parseWellFormedJson(jsonCleaned)
	println("objRebuilt=${objRebuilt}")
	println()

	def jsonRebuilt = new JsonBuilder(objRebuilt).toString()
	println("jsonRebuilt=${jsonRebuilt}")
	println()
	
	println ("jsonGiven.equals(jsonRebuilt) : ${jsonGiven.equals(jsonRebuilt)}")
	
	println('--------------------')
}

def sample = [
            "metadata": [
                "annotations": [
                    "openshift.io/generated-by": "jenkins-Z Test DOA-Stevel-ee-morsels-e2-manage-deployment-100"
                ],
                "creationTimestamp": "NULL",
                "labels": [
                    "app": "hello",
                    "deploymentconfig": "dc-hello-ddd",
                    "vtag": "ddd"
                ]
            ],
            "spec": [
                "containers": [
                    [
                        "env": [
                            [
                                "name": "A0_VTAG_dc_const",
                                "value": "ddd"
                            ],
                            [
                                "name": "A0_POD_IP",
                                "valueFrom": [
                                    "fieldRef": [
                                        "apiVersion": "v1",
                                        "fieldPath": "status.podIP"
                                    ]
                                ]
                            ],
                            [
                                "name": "A0_SVC_ACCOUNT",
                                "valueFrom": [
                                    "fieldRef": [
                                        "apiVersion": "v1",
                                        "fieldPath": "spec.serviceAccountName"
                                    ]
                                ]
                            ],
                            [
                                "name": "A0_NODE_NAME",
                                "valueFrom": [
                                    "fieldRef": [
                                        "apiVersion": "v1",
                                        "fieldPath": "spec.nodeName"
                                    ]
                                ]
                            ],
                            [
                                "name": "A0_POD_NAME",
                                "valueFrom": [
                                    "fieldRef": [
                                        "apiVersion": "v1",
                                        "fieldPath": "metadata.name"
                                    ]
                                ]
                            ],
                            [
                                "name": "A0_POD_NAMESPACE",
                                "valueFrom": [
                                    "fieldRef": [
                                        "apiVersion": "v1",
                                        "fieldPath": "metadata.namespace"
                                    ]
                                ]
                            ]
                        ],
                        "image": "docker-registry.default.svc:5000/jenkins-ci/jk-hello@sha256:eb4517b2b3d4a467f7c76e4232de93b5cd24380c22393341ea4097ef28e356d1",
                        "imagePullPolicy": "IfNotPresent",
                        "name": "hello",
                        "resources": [],
                        "terminationMessagePath": "/dev/termination-log",
                        "terminationMessagePolicy": "File",
                        "volumeMounts": [
                            [
                                "mountPath": "/podinfo",
                                "name": "podinfo"
                            ],
                            [
                                "mountPath": "/ms-test-nfs",
                                "name": "ms-test-nfs"
                            ]
                        ]
                    ]
                ],
                "dnsPolicy": "ClusterFirst",
                "restartPolicy": "Always",
                "schedulerName": "default-scheduler",
                "securityContext": [],
                "serviceAccount": "podrunner",
                "serviceAccountName": "podrunner",
                "terminationGracePeriodSeconds": "30",
                "volumes": [
                    [
                        "downwardAPI": [
                            "defaultMode": "420",
                            "items": [
                                [
                                    "fieldRef": [
                                        "apiVersion": "v1",
                                        "fieldPath": "metadata.labels"
                                    ],
                                    "path": "pod-labels.properties"
                                ],
                                [
                                    "fieldRef": [
                                        "apiVersion": "v1",
                                        "fieldPath": "metadata.annotations"
                                    ],
                                    "path": "pod-annotations.properties"
                                ]
                            ]
                        ],
                        "metadata": [
                            "defaultMode": "420",
                            "items": [
                                [
                                    "fieldRef": [
                                        "apiVersion": "v1",
                                        "fieldPath": "metadata.labels"
                                    ],
                                    "name": "pod-labels.properties"
                                ],
                                [
                                    "fieldRef": [
                                        "apiVersion": "v1",
                                        "fieldPath": "metadata.annotations"
                                    ],
                                    "name": "pod-annotations.properties"
                                ]
                            ]
                        ],
                        "name": "podinfo"
                    ],
                    [
                        "name": "ms-test-nfs",
                        "persistentVolumeClaim": [
                            "claimName": "pvc-qvq2h"
                        ]
                    ]
                ]
            ]
        ]

// test222([a:"aaa", "b":"bbb",])// 
// test222(sample)

println("===============================================")

def sss = '''
# hash
// slslash

 a1:  aa aa, \\
 a2:'aa,bb',
 
 b\\
b : {b11:bbb,b \\
2:[11,22,33]}, 

 c-c : {"c11":"ccc", "c22":[99,88,77], "c33":""},
'd:d': {"d11":"ddd",}, "dbb": {"d22":"DDD"},
e2: "a:A, b:{b1:B}, c:{}, d:[d1,d2,d3]", /* trailing junk */
f: [ { "faa":"FAA" }, { "fbb":"FBB" }]
'''.trim()


def Object parseLaxJson(String text)
{
	// def text = test.replaceAll(/(?s)^\s*(#|(\/\/))
	return new JsonSlurper().setType(JsonParserType.LAX).parseText(text)
}

def test333(String rawJson)
{
	println("rawJson=\n${rawJson}")
	println()
	
	// splice lines
	// wipe line-comments and blanks

	def rmHashAnchored = rawJson	
		.replaceAll(/\\\s*\n/,                    '')			
		.replaceAll(/(?m)^\s*((#|(\/\/)).*)?\n/,  '')			
	
	println("rmHashAnchored=\n${rmHashAnchored}")
	println()
	
	// make each line into a separate element
	// remove final comma
	// wrap entire thing in array markers
	
	def lineWise = rmHashAnchored	
		.replaceAll(/(?m)^.*$/,    '{ $0 },' )	
		.replaceAll(/,[ \t\n]*$/,  '')			
		.replaceAll(/^/, "[\n")									
		.replaceAll(/$/, "\n]")
	println("lineWise=\n${lineWise}")
	println()
	
	def objLax = parseLaxJson("{${rmHashAnchored}}")
	println("objLax=\n${objLax}")
	println()

	println("objLax-as-Json=\n${new JsonBuilder(objLax).toPrettyString()}")
	println()
	
	def objLW = parseLaxJson("${lineWise}")
	println("objLW=\n${objLW}")
	println()

	println("objLW-as-Json=\n${new JsonBuilder(objLW).toPrettyString()}")
	println()
	
	println('--------------------')
}

test333(sss)

/* */
