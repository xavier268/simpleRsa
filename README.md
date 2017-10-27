# simpleRsa
A simple, unlimited strength, cryptographic RSA implementation in java, 
using only the java standard librairy.

## How to use/test

* create a private key of selected length,
* derive the public key from the private key,
* you can save either or both to file for sharing. 

** The file format is a text file with, in that order, the public exponent, the modulus, the private exponent (if secretKey). Any following lines are comments.

then, either :
* encrypt with the public key, 
* and decript with the private key

or :
* sign with the private key, 
* verify with the public key

** digest uses SHA-256

See the unit tests as an example.

**note** : File format is different fron the openssh rsa file format
