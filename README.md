# simpleRsa
A simple, naive, strong public key cryptography implementation (RSA) in java, 
using only standadrd librairy.

## How to use/test

* create a private key of selected length,
* derive the public key from the private key,

then, either :
* encrypt with the public key, 
* and decript with the private key

or :
* sign with the private key, 
* verify with the public key

See the unit tests as an example. **Do not use in production !**