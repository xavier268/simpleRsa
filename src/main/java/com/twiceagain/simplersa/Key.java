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
 * @author xavier
 */
public abstract class Key {

    protected BigInteger exponent;
    protected BigInteger pubKey;

    private MessageDigest md;

    {
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Key.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Compute the hash as a BigInteger modulus.
     * @param message
     * @return 
     */
    protected BigInteger digest(String message) {
        return new BigInteger(message.getBytes()).mod(pubKey);
    }
}
