# Origami SMTP

The aim or Origami SMTP is to provide a local development client that is
compatible with STARTTLS.  This way messages that must be sent over TLS
can be used on a development machine.

### Helpful Tips

* When working in Eclipse it is recommended to set the VM arguments to
-Djavax.net.debug=ssl,handshake
* You can use openssl to help you debug the program with the command `openssl s_client -connect 127.0.0.1:2525 -starttls smtp`

### Contributing

Contributing is simple just fork this on GitHub and then send a pull request.

### License

I am still trying to determine the license on this
