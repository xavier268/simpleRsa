/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.simplersa;

import java.math.BigInteger;

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

}
