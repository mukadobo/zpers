package com.mukadobo.zpers.groovy.json

import org.codehaus.groovy.runtime.NullObject

import com.mukadobo.zpers.groovy.langx.ListX
import com.mukadobo.zpers.groovy.langx.MapX

class JsonConjoin
{
    private Object base

    JsonConjoin(Object base)
    {
        this.base = base
    }
    
    static Object of(Object base, Object exempli)
    {
        new JsonConjoin(base).apply(exempli)
    }
    
    Object apply(Object exempli)
    {
        // when exempli is null, just return a deep copy of base

        if (exempli == null)
        {
            def dpcp = deepCopy(base)

            return dpcp
        }

        // when base is null,  return deep copy of exempli UNLESS exempli is a list of prototypes

        if (base  == null)
        {
            Boolean underIsProto = exempli in List
            Object  rval         = underIsProto ? null : deepCopy(exempli)

            return rval
        }

        if ((base in Number) && (exempli in Number)) return base

        if ((base in Map ) && (exempli in Map )) return applyMap (exempli)
        if ((base in List) && (exempli in List)) return applyList(exempli)

        if (base in exempli.getClass()) return deepCopy(base)

        throw new IllegalArgumentException("Can't conjoin types: base is ${base.getClass()}, exempli is ${exempli.getClass()}")
    }

    private Object recurse(Object subBase, Object subExempli)
    {
        of(subBase, subExempli)
    }

    private Map applyMap(Map exempli)
    {    
        Map filteredDefaults    = MapX.collectEntries (exempli) { k, v -> base.containsKey(k) ? null : [(k): deepCopy(v) ] }
        Map recursiveBaseValues = MapX.collectEntries (base   ) { k, v -> [(k) : recurse(base[k], exempli[k])] }

        return filteredDefaults << recursiveBaseValues
    }

    private List applyList(List exempli)
    {
        if (exempli.isEmpty() ) return deepCopy(base)
        if (exempli.size() > 1) throw new IllegalArgumentException("Can't process exempli-side array item with length > 1")

        Object underItem = exempli[0]

        ListX.collect(base) { it -> [recurse(it, underItem)] }
    }

    static private Object deepCopy(Object item)
    {
        if (item == null) return null

        // NullObject class doesn't behave well in the switch, like the others...

        if (item.getClass() == NullObject.class) return item

        switch (item)
        {
            case Boolean:
            case Number:
            case String:
                
                return item

            case Date:

                return new Date(item.getTime())

            case Map:

                Map rval = MapX.collectEntries(item)  { k, v ->  [(k) : deepCopy(v)] }
                return rval

            case List:

                List rval = ListX.collect(item) { it -> [deepCopy(it)] }
                return rval

            default:

                throw new IllegalArgumentException("Don't know how to handle class: ${item.getClass()}")
            
        }
    }
}