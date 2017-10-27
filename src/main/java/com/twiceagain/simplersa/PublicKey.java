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

/**
 * PublicKey can be shared. It can be applied to any message (BigInteger).
 *
 * @author xavier
 */
public class PublicKey extends Key implements Comparable<PublicKey> {

    public PublicKey(BigInteger exponent, BigInteger pubKey) {
        this.pubKey = pubKey;
        this.exponent = exponent;
    }

    public BigInteger encrypt(BigInteger input) {
        return input.modPow(exponent, pubKey);
    }

    /**
     * Verify signature.
     *
     * @param signature
     * @param message
     * @return true if signature is valid.
     */
    public boolean verify(BigInteger signature, String message) {
        BigInteger d = digest(message);
        return d.equals(encrypt(signature));
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
        String s = exponent.toString() + "\n" + pubKey.toString() + "\n";
        Files.write(p, s.getBytes(), StandardOpenOption.CREATE);
        return p.toAbsolutePath().toString();
    }

    public static PublicKey load(String filename) throws IOException {
        Path p = Paths.get(filename);
        List<String> lines = Files.readAllLines(p);
        if (lines.size() < 2) {
            throw new IOException("Expected at least 2 lines to read ?!");
        }
        return new PublicKey(new BigInteger(lines.get(0)), new BigInteger(lines.get(1)));
    }

    /**
     * Compares two public keys.
     * @param o
     * @return 
     */
    @Override
    public int compareTo(PublicKey o) {
        if (o == null) {
            return 1;
        }
        int c = exponent.compareTo(o.exponent);
        if (c != 0) {
            return c;
        }
        c = pubKey.compareTo(o.pubKey);
        return c;
    }

}
