def echo(msg) { println msg }
def IMAGE_BASES   = "hello\ndtest | loctn\nxxxxx".split(/\n/) as String[]
def IMAGE_VTAGS   = "ccc"      .split(/\\n/) as String[]


			for (baseSpec in IMAGE_BASES) {
				baseSpec = baseSpec.trim()
				//X	echo "baseSpec=${baseSpec}"
				
				def baseParts = baseSpec.split(/ *\| */)
				//X	echo "baseParts=${baseParts}"
				def baseSrc   = baseParts[0]
				def baseTgt   = (baseParts.size() == 1) ? baseSrc : baseParts[1]
				
				for (tag in IMAGE_VTAGS) {
					tag = tag.trim()
					//X	echo "tag=${tag}"
					
					echo "${baseSrc}:${tag} -> ${baseTgt}:${tag}"
				}
			} 
