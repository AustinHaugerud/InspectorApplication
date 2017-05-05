package org.honest;

import java.util.HashMap;
import org.apache.commons.IOUtils;

// CS -001 Santa Clauss

public class Main
{

    private int add(int a, int b)
    {
        int sum = a + b;
        return sum;
    }

    public static void main(String args[])
    {
        int summation = 0;
        for(int i = 10; i > 0; --i)
        {
            summation = sum(i, summation);
        }

        System.out.println(summation);
    }
};

