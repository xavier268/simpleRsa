/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.simplersa;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.Format;

/**
 * PublicKey can be shared. It can be applied to any message (BigInteger).
 *
 * @author xavier
 */
public class PublicKey extends Key {

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
        Files.write(p,s.getBytes(), StandardOpenOption.CREATE);
        return p.toAbsolutePath().toString();
    }

}
