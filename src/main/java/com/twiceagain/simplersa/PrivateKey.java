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
import java.util.List;
import java.util.Random;

/**
 * Generates a private key first. Then, derive the public key from the private
 * key.
 *
 * @author xavier
 */
public class PrivateKey extends Key implements Comparable<PrivateKey> {
    
    /**
     * This is the most sensitive component.
     */
    private BigInteger secretExponent;

    /**
     * protected contructor used when loading from file.
     *
     * @param secretExponent
     * @param exponent
     * @param modulus
     */
    private PrivateKey(BigInteger secretExponent, BigInteger exponent, BigInteger modulus) {
        this.secretExponent = secretExponent;
        this.exponent = exponent;
        this.modulus = modulus;
    }

    /**
     * Generate a new private key.
     *
     * @param nbBits
     * @param exponent - should be prime, greater or equals to 3, or will throw
     * Runtime Exception.
     * @param randomGenerator provide your own random generator, or null to use default.
     */
    public PrivateKey(int nbBits, BigInteger exponent, Random randomGenerator) {

        BigInteger a;
        BigInteger b;
        Random rnd = randomGenerator;
        if(rnd == null) rnd = new Random();

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
                this.modulus = a.multiply(b);
                // now, compute the inverse of the exponent modulo a-1 x b-1
                // It is sometimes impossible to compute the inverse, if exponent is not invertible in that field. 
                // In such case, we should modify the factors, and try again ...            
                secretExponent = exponent.modInverse(a.subtract(BigInteger.ONE).multiply(b.subtract(BigInteger.ONE)));
                done = true;
            } catch (ArithmeticException ex) {
                LOG.info("********** Exponent not invertible - choosing another value ...");
            }
        }
    }

    /**
     * Get the public key.
     *
     * @return
     */
    public PublicKey getPublicKey() {
        return new PublicKey(exponent, modulus);

    }

    public BigInteger decrypt(BigInteger input) {
        return input.modPow(secretExponent, modulus);
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
                + modulus.toString() + "\n"
                + secretExponent.toString() + "\n";
        Files.write(p, s.getBytes(), StandardOpenOption.CREATE);
        return p.toAbsolutePath().toString();
    }

    @Override
    public int compareTo(PrivateKey o) {
        if (o == null) {
            return 1;
        }
        int c = exponent.compareTo(o.exponent);
        if (c != 0) {
            return c;
        }
        c = modulus.compareTo(o.modulus);
        if (c != 0) {
            return c;
        }

        c = secretExponent.compareTo(o.secretExponent);
        return c;
    }

    public static PrivateKey load(String filename) throws IOException {
        Path p = Paths.get(filename);
        List<String> lines = Files.readAllLines(p);
        if (lines.size() < 3) {
            throw new IOException("Expected at least 2 lines to read ?!");
        }
        PrivateKey ss = new PrivateKey(
                new BigInteger(lines.get(2)),
                new BigInteger(lines.get(0)),
                new BigInteger(lines.get(1)));
        
        if(! ss.isValid()) throw new IOException("Private key read from file is invalid.");
        
        return ss;

    }

    /**
     * Test validity of privateKey, doing a single decrypt/encrypt test.
     *
     * @return
     */
    public boolean isValid() {
        BigInteger tt = new BigInteger("789456123").mod(modulus);
        return (getPublicKey().encrypt(decrypt(tt)).compareTo(tt) == 0);
    }
}
