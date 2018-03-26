import groovy.json.JsonBuilder
import com.mukadobo.zpers.groovy.json.JsonConjoin

Map  mapE  = [:]
List listE = []

Map aaMaps = 
[
    aaNull      : [ aa : null ],
    aaString    : [ aa : "AA" ],
    aaBoolean   : [ aa : true ],
    aaInteger   : [ aa : 42   ],
    aaLong      : [ aa : 43L  ],
    aaFloat     : [ aa : 44.0 ],
    aaDate      : [ aa : new Date() ],
    aaMapE      : [ aa : mapE ],
    
    asMapJ1     : [ aa : [jj:"J1"] ],
    asMapK1     : [ aa : [kk:"K1"] ],
    asMapJ2L1   : [ aa : [jj:"J2", ll:"L1"] ],
    asMapK2L2   : [ aa : [kk:"K2", ll:"L2"] ],
    
    asJ1        : [jj:'J1'],
    asJ2        : [jj:'J2'],
    asK1        : [kk:'K1'],
    asK2        : [kk:'K2'],
    asL1        : [ll:'L1'],
    asL2        : [ll:'L2'],

    asJ2L1      : [jj:"J2", ll:"L1"],
    asK2L2      : [kk:"K2", ll:"L2"],
]

aaMaps.each
{ nm, map ->

    println ("aaMaps vs N.E.R.: $nm = $map")

    assert JsonConjoin.of(null, map ) == map
    assert JsonConjoin.of(map , null) == map

    assert JsonConjoin.of(mapE, map ) == map
    assert JsonConjoin.of(map , mapE) == map

    assert JsonConjoin.of(map , map ) == map
}
println()

lrcMaps = 
[
    ['aaNull   ', 'aaString ', aaMaps.aaString ],
    ['aaString ', 'aaNull   ', aaMaps.aaString ],
    ['aaInteger', 'aaNull   ', aaMaps.aaInteger],
    ['aaInteger', 'aaFloat  ', aaMaps.aaInteger],
    ['aaInteger', 'aaString ', IllegalArgumentException.class],
    ['aaInteger', 'aaMapE   ', IllegalArgumentException.class],
    ['asJ1     ', 'asK1     ', [jj:'J1', kk:'K1'] ],
    ['asK1     ', 'asJ1     ', [jj:'J1', kk:'K1'] ],
    ['asJ2L1   ', 'asK2L2   ', [jj:'J2', kk:'K2', ll:'L1'] ],
    ['asK2L2   ', 'asJ2L1   ', [jj:'J2', kk:'K2', ll:'L2'] ],
]

lrcMaps.each
{ spec ->
    String nmLeft    = spec[0].trim(); Object itLeft =aaMaps[nmLeft ]
    String nmRight   = spec[1].trim(); Object itRight=aaMaps[nmRight] 
    Object expected  = spec[2]

    println ("lrcMaps: L<$nmLeft>=$itLeft  -|- R<$nmRight>=$itRight  => ${expected}")

    Object    result = null
    Exception caught = null
    try
    {
        result = JsonConjoin.of(itLeft, itRight)
    }
    catch (e)
    {
        caught = e
    }

    if (expected in Exception)
    {
        assert caught
        assert caught in expected
    }
    else
    {
        assert !caught
        assert result == expected
    }
}
println()

Map aaLists = 
[
    aaNull      : [ aaMaps['aaNull   '.trim()] ],
    aaString    : [ aaMaps['aaString '.trim()] ],
    aaBoolean   : [ aaMaps['aaBoolean'.trim()] ],
    aaInteger   : [ aaMaps['aaInteger'.trim()] ],
    aaLong      : [ aaMaps['aaLong   '.trim()] ],
    aaFloat     : [ aaMaps['aaFloat  '.trim()] ],
    aaDate      : [ aaMaps['aaDate   '.trim()] ],
    aaMapE      : [ aaMaps['aaMapE   '.trim()] ],
    
    asMapJ1     : [ aaMaps['asMapJ1  '.trim()] ],
    asMapK1     : [ aaMaps['asMapK1  '.trim()] ],
    asMapJ2L1   : [ aaMaps['asMapJ2L1'.trim()] ],
    asMapK2L2   : [ aaMaps['asMapK2L2'.trim()] ],

    asJ1        : [ aaMaps['asJ1       '.trim()] ],
    asK1        : [ aaMaps['asK1       '.trim()] ],
    asJ2L1      : [ aaMaps['asJ2L1     '.trim()] ],
    asK2L2      : [ aaMaps['asK2L2     '.trim()] ],

    asJ1x2      : [ aaMaps['asJ1       '.trim()], aaMaps['asJ1       '.trim()] ],
    asK1x2      : [ aaMaps['asK1       '.trim()], aaMaps['asK1       '.trim()] ],
    asJ2L1x2    : [ aaMaps['asJ2L1     '.trim()], aaMaps['asJ2L1     '.trim()] ],
    asJ2L1x2    : [ aaMaps['asK2L2     '.trim()], aaMaps['asK2L2     '.trim()] ],
]

aaLists.each
{ nm, list ->

    println ("aaLists vs N.E.R.: $nm = $list")

    Boolean isPlural = list?.size() > 1

    assert JsonConjoin.of(null , isPlural ? list.take(1) : list ) == null
    assert JsonConjoin.of(list , null ) == list

    assert JsonConjoin.of(listE, isPlural ? list.take(1) : list ) == listE
    assert JsonConjoin.of(list , listE) == list

   assert JsonConjoin.of(list  , isPlural ? list.take(1) : list ) == list
}
println()

List lrcAALists = 
[
    ['aaNull   ', 'aaString ', aaLists.aaString ],
    ['aaString ', 'aaNull   ', aaLists.aaString ],
    ['aaInteger', 'aaNull   ', aaLists.aaInteger],
    ['aaInteger', 'aaFloat  ', aaLists.aaInteger],
    ['aaInteger', 'aaString ', IllegalArgumentException.class],
    ['aaInteger', 'aaMapE   ', IllegalArgumentException.class],
    ['asJ1     ', 'asK1     ', [ [jj:'J1', kk:'K1']          ] ],
    ['asK1     ', 'asJ1     ', [ [jj:'J1', kk:'K1']          ] ],
    ['asJ2L1   ', 'asK2L2   ', [ [jj:'J2', kk:'K2', ll:'L1'] ] ],
    ['asK2L2   ', 'asJ2L1   ', [ [jj:'J2', kk:'K2', ll:'L2'] ] ],
    ['asJ1x2   ', 'asK1     ', [ [jj:'J1', kk:'K1',], [jj:'J1', kk:'K1',] ] ],
]

lrcAALists.each
{ spec ->
    String nmLeft    = spec[0].trim(); Object itLeft =aaLists[nmLeft ]
    String nmRight   = spec[1].trim(); Object itRight=aaLists[nmRight] 
    Object expected  = spec[2]

    println ("lrcAALists: L<$nmLeft>=$itLeft  -|- R<$nmRight>=$itRight  => ${expected}")

    Object    result = null
    Exception caught = null
    try
    {
        result = JsonConjoin.of(itLeft, itRight)
    }
    catch (e)
    {
        caught = e
    }

    if (expected in Exception)
    {
        assert caught
        assert caught in expected
    }
    else
    {
        assert !caught
        assert result == expected
    }
}
println()

Map bbLists = 
[
    asJ1        : [ aaMaps['asJ1       '.trim()] ],
    asK1        : [ aaMaps['asK1       '.trim()] ],
    asJ2L1      : [ aaMaps['asJ2L1     '.trim()] ],
    asK2L2      : [ aaMaps['asK2L2     '.trim()] ],

    asJ1cL1     : [ aaMaps['asJ1       '.trim()], aaMaps['asL1       '.trim()] ],
    asJ2cL2     : [ aaMaps['asJ2       '.trim()], aaMaps['asL2       '.trim()] ],
]

List lrcBBLists = 
[
    ['asJ1cL1   ', 'asK1     ', [ [jj:'J1', kk:'K1'         ], [ll:'L1', kk:'K1'] ] ],
    ['asJ1cL1   ', 'asK2L2   ', [ [jj:'J1', kk:'K2', ll:'L2'], [ll:'L1', kk:'K2'] ] ],
]

lrcBBLists.each
{ spec ->
    String nmLeft    = spec[0].trim(); Object itLeft =bbLists[nmLeft ]
    String nmRight   = spec[1].trim(); Object itRight=bbLists[nmRight] 
    Object expected  = spec[2]

    println ("lrcBBLists: L<$nmLeft>=$itLeft  -|- R<$nmRight>=$itRight  => ${expected}")

    Object    result = null
    Exception caught = null
    try
    {
        result = JsonConjoin.of(itLeft, itRight)
    }
    catch (e)
    {
        caught = e
    }

    if (expected in Exception)
    {
        assert caught
        assert caught in expected
    }
    else
    {
        assert !caught
        assert result == expected
    }
}
println()

//     ['asK2L2   ', 'asJ2L1   ', [jj:'J2', kk:'K2', ll:'L2'] ],

def Object doit(base, exempli) 
{
    println()
    println("base=${new JsonBuilder(base).toPrettyString()}")
    println("exempli=${new JsonBuilder(exempli).toPrettyString()}")

    def rval = JsonConjoin.basedOn(base).apply(exempli)
    println("rval=${new JsonBuilder(rval).toPrettyString()}")

    rval
}

def Map ccJCE = [
    'Peas' : JsonConjoin.exempli(
        'prepend' : [[aa:'A1'], [bb:'B1']], 
    ),
    'pEas' : JsonConjoin.exempli(
        'ifEmpty' : [[jj:'J1'], [kk:'K1']], 
    ),
    'peAs' : JsonConjoin.exempli(
        'append'  : [[rr:'R1'], [ss:'S1']], 
    ),
    'PEAs' : JsonConjoin.exempli(
        'prepend' : [[aa:'A1'], [bb:'B1']], 
        'ifEmpty' : [[jj:'J1'], [kk:'K1']], 
        'append'  : [[rr:'R1'], [ss:'S1']], 
    ),

    'peaS' : JsonConjoin.exempli(
        'apply'   : [
            [jj:'J2', ll:'L2'],
        ]
    ),

    'PEAS' : JsonConjoin.exempli(
        'prepend' : [[aa:'A1'], [bb:'B1']], 
        'ifEmpty' : [[jj:'J1'], [kk:'K1']], 
        'append'  : [[rr:'R1'], [ss:'S1']], 
        'apply'   : [
            [aa:'A2', cc:'C2'],
            [jj:'J2', ll:'L2'],
            [rr:'R2', tt:'T2'],
        ]
    ),
]
def Map ccExempli = [ 
    ('extant_'+'Peas') : [ 'extant' : ccJCE.Peas ], ('nosuch_'+'Peas') : [ 'nosuch' : ccJCE.Peas ], 
    ('extant_'+'pEas') : [ 'extant' : ccJCE.pEas ], ('nosuch_'+'pEas') : [ 'nosuch' : ccJCE.pEas ], 
    ('extant_'+'peAs') : [ 'extant' : ccJCE.peAs ], ('nosuch_'+'peAs') : [ 'nosuch' : ccJCE.peAs ], 
    ('extant_'+'PEAs') : [ 'extant' : ccJCE.PEAs ], ('nosuch_'+'PEAs') : [ 'nosuch' : ccJCE.PEAs ], 

    ('extant_'+'peaS') : [ 'extant' : ccJCE.peaS ], ('nosuch_'+'peaS') : [ 'nosuch' : ccJCE.peaS ], 
    ('extant_'+'PEAS') : [ 'extant' : ccJCE.PEAS ], ('nosuch_'+'PEAS') : [ 'nosuch' : ccJCE.PEAS ], 
]

def Map ccBase = [
    'simple' : [ 
        'extant' : [ 
            [jj:'J0'], 
            [kk:'K0'], 
        ],
    ],
    'others' : [ 
        'others' : [ 
            [yy:'Y0'], 
        ],
    ],
    'monty' : [ 
        'extant' : [ 
            [jj:'J0'], 
            [kk:'K0'],
            [   aa:'A0', bb:'B0', cc:'C0',
                jj:'J0', kk:'K0', ll:'L0',
                rr:'R0', ss:'S0', tt:'T0',
                xx:'X0',
            ],
        ],
        'others' : [ 
            [yy:'Y0'], 
        ],
    ],
]

def expect  = [ 
    'extant' : [
        [aa:'A1',          cc:'C2',     jj:'J2',          ll:'L2',     rr:'R2',          tt:'T2'], 
        [aa:'A2', bb:'B1', cc:'C2',     jj:'J2',          ll:'L2',     rr:'R2',          tt:'T2'], 

        [aa:'A2',          cc:'C2',     jj:'J0',          ll:'L2',     rr:'R2',          tt:'T2'], 
        [aa:'A2',          cc:'C2',     jj:'J2', kk:'K0', ll:'L2',     rr:'R2',          tt:'T2'], 

        [aa:'A2',          cc:'C2',     jj:'J2',          ll:'L2',     rr:'R1',          tt:'T2'], 
        [aa:'A2',          cc:'C2',     jj:'J2',          ll:'L2',     rr:'R2', ss:'S1', tt:'T2'], 

        [aa:'A0', bb:'B0', cc:'C0',     jj:'J0', kk:'K0', ll:'L0',     rr:'R0', ss:'S0', tt:'T0',   xx:'X0' ],
    ],
    'nosuch' : [
        [aa:'A1',          cc:'C2',     jj:'J2',          ll:'L2',     rr:'R2',          tt:'T2'], 
        [aa:'A2', bb:'B1', cc:'C2',     jj:'J2',          ll:'L2',     rr:'R2',          tt:'T2'], 

        [aa:'A2',          cc:'C2',     jj:'J1',          ll:'L2',     rr:'R2',          tt:'T2'], 
        [aa:'A2',          cc:'C2',     jj:'J2', kk:'K1', ll:'L2',     rr:'R2',          tt:'T2'], 

        [aa:'A2',          cc:'C2',     jj:'J2',          ll:'L2',     rr:'R1',          tt:'T2'], 
        [aa:'A2',          cc:'C2',     jj:'J2',          ll:'L2',     rr:'R2', ss:'S1', tt:'T2'], 
    ]
]

List lrcCCExempli = [
    ['simple', ('extant_'+'Peas'), [
        'extant': [
            [aa:'A1', ],
            [bb:'B1', ],
            [jj:'J0', ], 
            [kk:'K0', ], 
        ],
    ]],
    ['simple', ('nosuch_'+'Peas'), [
        'nosuch': [ ],
        'extant': [
            [jj:'J0', ], 
            [kk:'K0', ], 
        ],
    ]],
    ['others', ('nosuch_'+'Peas'), [
        'nosuch': [ ],
        'others': [
            [yy:'Y0', ], 
        ],
    ]],
    //------------------------------------------
    ['simple', ('extant_'+'pEas'), [
        'extant': [
            [jj:'J0', ], 
            [kk:'K0', ],  
        ],
    ]],
    ['simple', ('nosuch_'+'pEas'), [
        'nosuch': [ 
            [jj:'J1', ], 
            [kk:'K1', ], 
        ],
        'extant': [
            [jj:'J0', ], 
            [kk:'K0', ], 
        ],
    ]],
    ['others', ('nosuch_'+'pEas'), [
        'nosuch': [
            [jj:'J1', ], 
            [kk:'K1', ], 
        ],
        'others': [
            [yy:'Y0', ], 
        ],
    ]],
    //------------------------------------------
    ['simple', ('extant_'+'peAs'), [
        'extant': [
            [jj:'J0', ], 
            [kk:'K0', ], 
            [rr:'R1', ],
            [ss:'S1', ],
        ],
    ]],
    ['simple', ('nosuch_'+'peAs'), [
        'nosuch': [ ],
        'extant': [
            [jj:'J0', ], 
            [kk:'K0', ], 
        ],
    ]],
    ['others', ('nosuch_'+'peAs'), [
        'nosuch': [ ],
        'others': [
            [yy:'Y0', ], 
        ],
    ]],
    //--------------------- ---------------------
    ['simple', ('extant_'+'PEAs'), [
        'extant': [
            [aa:'A1', ],
            [bb:'B1', ],
            [jj:'J0', ], 
            [kk:'K0', ], 
            [rr:'R1', ],
            [ss:'S1', ],
        ],
    ]],
    ['simple', ('nosuch_'+'PEAs'), [
        'nosuch': [ 
            [aa:'A1', ],
            [bb:'B1', ],
            [jj:'J1', ], 
            [kk:'K1', ], 
            [rr:'R1', ],
            [ss:'S1', ],
        ],
        'extant': [
            [jj:'J0', ], 
            [kk:'K0', ], 
        ],
    ]],
    ['others', ('nosuch_'+'PEAs'), [
        'nosuch': [ 
            [aa:'A1', ],
            [bb:'B1', ],
            [jj:'J1', ],  
 
 
 
 
 
 
            [kk:'K1', ], 
            [rr:'R1', ],
            [ss:'S1', ],
        ],
        'others': [
            [yy:'Y0', ], 
        ],
    ]],
    //------------------------------------------
//  'peaS' : JsonConjoin.exempli(
//      'apply'   : [
//          [jj:'J2', ll:'L2'],
//      ]
//  ),

    ['simple', ('extant_'+'peaS'), [
        'extant': [
            [jj:'J0',          ll:'L2', ],
            [jj:'J2', kk:'K0', ll:'L2', ],
       ],
    ]],
    ['simple', ('nosuch_'+'peaS'), [
        'nosuch': [ ],
        'extant': [
            [jj:'J0', ], 
            [kk:'K0', ], 
        ],
    ]],
    ['others', ('nosuch_'+'peaS'), [
        'nosuch': [ ],
        'others': [
            [yy:'Y0', ], 
        ],
    ]],
]

lrcCCExempli.each
{ spec ->
    String nmLeft    = spec[0].trim(); Object itLeft =ccBase   [nmLeft ]
    String nmRight   = spec[1].trim(); Object itRight=ccExempli[nmRight] 
    Object expected  = spec[2]

    println ("lrcCCExempli: L<$nmLeft>=$itLeft  -|- R<$nmRight>=$itRight  => ${expected}")

    assert itLeft != null
    assert itRight != null

    Object    result = null
    Exception caught = null
    try
    {
        result = doit(itLeft, itRight)
    }
    catch (e)
    {
        caught = e
    }
    println ("lrcCCExempli: L<$nmLeft>  -|- R<$nmRight>\n")

    if (expected in Exception)
    {
        assert caught
        assert caught in expected
    }
    else
    {
        if (caught) // caught.printStackTrace(System.out)
            org.codehaus.groovy.runtime.StackTraceUtils.sanitize(new Exception(caught)).printStackTrace()

        assert !caught
        assert result == expected
    }
}
println()

/**/