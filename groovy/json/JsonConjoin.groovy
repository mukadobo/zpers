package com.mukadobo.zpers.groovy.json

import groovy.json.JsonBuilder

import org.codehaus.groovy.runtime.NullObject

import com.mukadobo.zpers.groovy.langx.ListX
import com.mukadobo.zpers.groovy.langx.MapX

class JsonConjoin implements Serializable
{
    private Object base

    JsonConjoin(Object base)
    {
        this.base = base
    }

    static JsonConjoin basedOn(Object base)
    {
        new JsonConjoin(base)
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

        // when base is null, return deep copy of exempli UNLESS exempli is a list of prototypes

        if (base  == null)
        {
            switch (exempli)
            {
                case List:     return null
                case Exempli:  return exempli.whenNull()

                default:       return deepCopy(exempli)
            }
        }

        if ((base in Number) && (exempli in Number)) return base

        if ((base in Map ) && (exempli in Map )) return applyMap (exempli)
        if ((base in List) && (exempli in List)) return applyList(exempli)

        if (exempli in Exempli)
        {
            if (base in List) return exempli.applyTo(base)

            throw new IllegalArgumentException("Exempli class may only be applied to List class instances")
        }

        if (base in exempli.getClass()) return deepCopy(base)

        throw new IllegalArgumentException("Can't conjoin types: base is ${base.getClass()}, exempli is ${exempli.getClass()}")
    }

    private Object recurse(Object subBase, Object subExempli)
    {
        of(subBase, subExempli)
    }

    private Map applyMap(Map exempli)
    {    
        Map filteredExempli    = MapX.collectEntries (exempli) 
        { k, v ->
            if (base.containsKey(k)) return null

            if (v in Exempli)
            {
                Object whenNull = v.whenNull()

                return whenNull == null ? null : [(k): whenNull]
            }

            return [(k): deepCopy(v)]
        }

        Map recursiveBaseValues = MapX.collectEntries (base)
        { k, v ->
            def e = exempli[k]

            (e in Exempli
                ? [(k) : e.applyTo(v)] 
                : [(k) : recurse(v, e)]
            )
        }

        return filteredExempli << recursiveBaseValues
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

            case Exempli:

                throw new IllegalArgumentException("Exempli instance should not be copied directly")

            default:

                throw new IllegalArgumentException("Don't know how to handle class: ${item.getClass()}")
            
        }
    }

    String toString()
    {
        "base=$base"
    }

    static Exempli exempli(Map args)
    {
        new Exempli(args)
    }

    static class Exempli implements Serializable
    {
        final List prepend
        final List append
        final List ifEmpty
        final List apply

        Exempli(Map args = [:])
        {
            this.prepend = args.prepend ? args.prepend : []
            this.append  = args.append  ? args.append  : []
            this.ifEmpty = args.ifEmpty ? args.ifEmpty : []
            this.apply   = args.apply   ? args.apply   : []
        }

        List applyTo(List base)
        {
            List rval = []
            
            rval.addAll(this.prepend)
            rval.addAll(base.isEmpty() ? this.ifEmpty : base)
            rval.addAll(this.append)

            if (apply)
            {
                rval = ListX.collect(rval)
                { r -> 
                    [ apply.inject(r) { v, x -> JsonConjoin.of(v, x) } ]
                }
            }

            rval
        }
        
        List whenNull()
        {
            (this.ifEmpty) ? applyTo([]) : []
        }
        
        String toString()
        {
            new JsonBuilder(this).toString()
        }
    }
}