def bcName = 'bc-pull-to-cir-flect-ccc'
def caughtMsg = """Error running logs on at least one item: [buildconfig/bc-pull-to-cir-flect-ccc];
{reference={}, err=Error from server (BadRequest): container "docker-build" in pod "bc-pull-to-cir-flect-ccc-1-build" is waiting to start: ContainerCreating, verb=logs, cmd=oc logs buildconfig/bc-pull-to-cir-flect-ccc -f --insecure-skip-tls-verify --server=https://oc4con.osc.utdlab.com:8443 --namespace=jenkins-ci --token=XXXXX , out=, status=1}"""

if (caughtMsg =~ /(?s)\blogs.*\[buildconfig\/${bcName}\];.*\bwaiting to start: ContainerCreating\b/)
{
	println "YES"
}
else
	println "NO"
