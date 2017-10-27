/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.simplersa;

import java.math.BigInteger;
import java.util.Random;

/**
 * Generates a private key first. Then, derive the public key from the private key.
 * @author xavier
 */
public class PrivateKey {

    private final BigInteger a;
    private final BigInteger b;
    private final Random rnd = new Random();
    /**
     * Public exponent.
     */
    private final BigInteger exponent;
    private final BigInteger secretExponent;

    /**
     * Generate a new private key.
     * @param nbBits
     * @param exponent 
     */
    public PrivateKey(int nbBits, BigInteger exponent) {
        a = BigInteger.probablePrime(nbBits, rnd);
        b = BigInteger.probablePrime(nbBits, rnd);
        this.exponent = exponent;
        
        // now, compute the inverse of the exponent modulo a-1 x b-1
        secretExponent = exponent.modInverse(a.subtract(BigInteger.ONE).multiply(b.subtract(BigInteger.ONE)));
        
    }
    
    /**
     * Get the public key.
     * @return 
     */
    public PublicKey getPublicKey() {        
        return new PublicKey(exponent, b.multiply(a));
        
    }
    
    public BigInteger apply(BigInteger input){
        return input.modPow(secretExponent, a.multiply(b));
    }

}
