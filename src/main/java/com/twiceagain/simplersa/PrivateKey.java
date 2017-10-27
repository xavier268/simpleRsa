/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.simplersa;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

/**
 * Generates a private key first. Then, derive the public key from the private
 * key.
 *
 * @author xavier
 */
public class PrivateKey extends Key {

    private BigInteger a;
    private BigInteger b;
    private final Random rnd = new Random();

    private BigInteger secretExponent;

    /**
     * Generate a new private key.
     *
     * @param nbBits
     * @param exponent
     */
    public PrivateKey(int nbBits, BigInteger exponent) {
        boolean done = false;

        while (!done) {
            try {
                a = BigInteger.probablePrime(nbBits, rnd);
                b = BigInteger.probablePrime(nbBits, rnd);
                this.exponent = exponent;
                this.pubKey = a.multiply(b);
                // now, compute the inverse of the exponent modulo a-1 x b-1
                // It is sometimes impossible to compute the inverse, if exponent is not invertible in that field. 
                // In such case, we should modify the factors, and try again ...            
                secretExponent = exponent.modInverse(a.subtract(BigInteger.ONE).multiply(b.subtract(BigInteger.ONE)));
                done = true;
            } catch (ArithmeticException ex) {
                System.out.printf("\n****\nExponent not invertible - choosing another value ...\n*****\n");
            }
        }
    }

    /**
     * Get the public key.
     *
     * @return
     */
    public PublicKey getPublicKey() {
        return new PublicKey(exponent, pubKey);

    }

    public BigInteger decrypt(BigInteger input) {
        return input.modPow(secretExponent, pubKey);
    }

    /**
     * Sign a message.
     *
     * @param message
     * @return
     */
    public BigInteger sign(String message) {
        BigInteger d = digest(message);
        return decrypt(d);

    }

}
