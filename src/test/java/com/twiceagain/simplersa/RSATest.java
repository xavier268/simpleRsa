/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.simplersa;

import java.math.BigInteger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author xavier
 */
public class RSATest {
    
    public RSATest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    
    @Test
    public void firstTest() {
    
        BigInteger m = new BigInteger("321654654654");
        PrivateKey s = new PrivateKey(100, new BigInteger("13"));
        PublicKey  p = s.getPublicKey();
        BigInteger c = p.apply(m);
        assertEquals(m, s.apply(c));
        assertNotEquals(c, m);   
    }
}
