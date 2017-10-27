/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.simplersa;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test generation speed vs. nb of bits.
 *
 * @author xavier
 */
public class PerformanceTest {
    
    private static final Logger LOG = Logger.getLogger(PerformanceTest.class.getName());
    private static final BigInteger THREE = new BigInteger("3");

    @Test
    @Ignore // Comment this annotation to run the performance test
    public void speedtest() {

        Random rdm = new Random(0);
        long end = System.currentTimeMillis() + 1000*60*10; // 10 mns global runtime
                long loop;
        Map<Integer, Long> results = new HashMap<>();

        for (int i = 16; System.currentTimeMillis() < end; i = (int)Math.round(16 + 1.1*i) ){
            loop = System.currentTimeMillis();
            PrivateKey s = new PrivateKey(i, THREE, rdm);
            loop = System.currentTimeMillis() - loop;
            results.put(i, loop);
            LOG.log(Level.INFO, "Nb bits : {0}", i);
        }
        display(results);
    }

    /**
     * Display result table.
     *
     * @param res
     */
    protected void display(Map<Integer, Long> res) {

        List<Integer> keys = new ArrayList<>(res.keySet());
        Collections.sort(keys);
        System.out.printf("\nbits\t|\t millis (log(millis)/bits)");
        keys.forEach(
                (i) -> {
                    System.out.printf("\n  %d\t|\t  %d\t(%f)", i, res.get(i), Math.log(res.get(i))/i);
                });
        System.out.println();
    }

}
