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

    PrivateKey s;
    PublicKey p;

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
        s = new PrivateKey(100, new BigInteger("13"));
        p = s.getPublicKey();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void encryptDecrypt() {

        BigInteger m = new BigInteger("321654654654");

        BigInteger c = p.encrypt(m);

        assertEquals(m, s.decrypt(c));

        assertNotEquals(c, m);
    }

    @Test
    public void signVerify() {

        BigInteger m = new BigInteger("3546654444444654");
        String message = "this is a test message for signature";        

        BigInteger signature = s.sign(message);

        assertTrue(p.verify(signature, message));

        assertFalse(p.verify(signature.add(BigInteger.TEN), message));
        assertFalse(p.verify(signature, message + "x"));
    }

}
