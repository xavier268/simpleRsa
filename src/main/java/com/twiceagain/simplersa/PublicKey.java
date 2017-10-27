/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twiceagain.simplersa;

import java.math.BigInteger;

/**
 * PublicKey can be shared. It can be aplied to any message (BigInteger).
 * @author xavier
 */
public class PublicKey {
    
    private final BigInteger pubKey;
    private  final BigInteger exponent ;

    public PublicKey(BigInteger exponent, BigInteger pubKey ) {
        this.pubKey = pubKey;
        this.exponent = exponent;
    }
    
    public BigInteger apply(BigInteger input) {
        return input.modPow(exponent, pubKey);
    }
    
    
    
}
