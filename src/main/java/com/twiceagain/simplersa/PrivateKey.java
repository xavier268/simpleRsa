/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.simplersa;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Random;

/**
 * Generates a private key first. Then, derive the public key from the private
 * key.
 *
 * @author xavier
 */
public class PrivateKey extends Key {

    /**
     * Secret factors.
     */
    /**
     * Random generator - this is a sensitive matter if you are thinking of
     * doing serious encryption.
     */
    private final Random rnd = new Random();

    private BigInteger secretExponent;

    /**
     * Generate a new private key.
     *
     * @param nbBits
     * @param exponent - should be prime, greater or equals to 3, or will throw
     * Runtime Exception.
     */
    public PrivateKey(int nbBits, BigInteger exponent) {

        BigInteger a;
        BigInteger b;

        if (exponent.compareTo(new BigInteger("3")) <= 0) {
            throw new RuntimeException("Exponent should be bigger or equal to 3");
        }

        // Ensure exponent is prime ...
        if (!exponent.isProbablePrime(
                20)) {
            throw new RuntimeException(
                    "You must use a prime number as exponent. "
                    + exponent.toString()
                    + " is not a prime number.");
        }

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
                System.out.println("********** Exponent not invertible - choosing another value ...");
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

    /**
     * Definitely invalidate the privateKey object.
     */
    public void invalidate() {
        secretExponent = null;
    }

    /**
     * Save to file name (relative or absolute).
     *
     * @param filename
     * @return Absolute file path used.
     * @throws java.io.IOException
     */
    public String save(String filename) throws IOException {
        Path p = Paths.get(filename);
        if (Files.exists(p)) {
            throw new IOException("You cannot save to "
                    + p.toAbsolutePath()
                    + " because file already exists.");
        }
        String s = exponent.toString() + "\n"
                + pubKey.toString() + "\n"
                + secretExponent.toString() + "\n";
        Files.write(p, s.getBytes(), StandardOpenOption.CREATE);
        return p.toAbsolutePath().toString();
    }

}
