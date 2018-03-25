package com.mukadobo.zpers.groovy.langx

class ListX
{
    static List take(List list, Integer n)
    {
        return list?.take(n)
    }

    static List collect(List list, Closure closure)
    {
        // GRRR: Jenkins CPS is broken ~ List#collect // similar to https://issues.jenkins-ci.org/browse/JENKINS-28276

        if (!list || !closure) return []

        List rval = []
        for(e in list)
        {
            List transformedEntry = closure(e)
            if (transformedEntry)
            {
                rval.addAll(transformedEntry)
            }
        }

        return rval
    }
}