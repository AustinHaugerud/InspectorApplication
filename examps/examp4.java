package org.hasher;

import java.util.util1;
import java.util.util2;
import org.hashable;
import org.hash;
import org.primes;

// CS -001 Jack Frost

public class Hasher
{  

    public Hash hashHashable(Hashable hashable)
    {
        int intHashVal = doHashStep(hashable);
        Hash hash = convertHash(intHashVal);
        return hash;
    }
    
    private int doHashStep(Hashable hashable)
    {
        int hashSum = 0;

        for(Datum data : hashable.data())
        {
            hashSum += data;
        }

        return hashSum;
    }
    
    private Hash convertHash(int hashNumber)
    {
        Hash hash = new Hash(hashNum % org.primes.HashPrime);
    }
};

