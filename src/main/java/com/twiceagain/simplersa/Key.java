/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.simplersa;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract class, common to public and private keys.
 *
 * @author xavier
 */
public abstract class Key {

    protected BigInteger exponent;
    protected BigInteger pubKey;

    public static final String DIGEST_ALGO = "SHA-256";

    private MessageDigest md;

    {
        try {
            md = MessageDigest.getInstance(DIGEST_ALGO);
        } catch (NoSuchAlgorithmException ex) {
            // Should never be thrown, unless jvm does not provides message digest.
            System.out.printf("\nYour jvm implementation is not providing "
                    + DIGEST_ALGO + " message digest ...\n");
            Logger.getLogger(Key.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Compute the hash as a BigInteger modulus.
     *
     * @param message
     * @return
     */
    protected BigInteger digest(String message) {
        byte[] digest = md.digest(message.getBytes());
        BigInteger bd = new BigInteger(digest);
        return bd.mod(pubKey);
    }
}
