# Origami SMTP

The aim or Origami SMTP is to provide a local development client that is
compatible with STARTTLS.  This way messages that must be sent over TLS
can be used on a development machine.

### Requirements

This server requires:

* Java 1.8 or higher

### Starting the Server

To start the server run the following from the terminal or command line.

`java -jar origami.jar 2525`

You may replace 2525 with the port you would like to use.

### Getting your Messages

Your messages will be stored in a folder named "messages" that will appear in
the same folder as origami.jar.  Unfortunately, the only way we have found
to view these messages is Internet Explorer.

### Working with STARTLS

OrigamiSMTP does not work with all SMTP clients notably s\_client from OpenSSL
does not seem to be working too well with it.  This may be because of how the
cipher suites are chosen.  Anyways, this SMTP server uses a self-signed
certificate so you must lower your security settings not to validate it.
 The following sections will tell you how to do it in your programming language.

You may have to download the [Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files 8][1].  This extension may be
restricted in some countries and may have export controls enforced by the United State Government.  Since we do not have the funds for legal
council this file must be downloaded from the link provided.  Follow the instructions in README.txt to install.

### Swaks for Debugging STARTLS

There is a Perl script called [Swaks][2] that can be used for debugging purposes.
If you are using this script on Windows it will require [ActivePerl][3] by ActiveState.
Just download the community edition.  We tried to use Strawberry Perl and found that
this script is not compatible.

To use Swaks the command is simply:

```swaks.pl -t john.doe@example.com -f jane.doe@example.com -s localhost -p 2525 --tls-verify --tls-ca-path \Origami\CA\Origami_CA.crt -tls
```

#### C&#35;

In C-Sharp if you have `EnableSsl` set to true like so:

```csharp
var client = new SmtpClient
{
    enableSsl = true
};
```

Then before you use the SMTP Client you will want to override the validation
callback like so

```csharp
System.Net.ServicePointManager.ServerCertificateValidationCallback += delegate{return true;};
```

### Helpful Tips

* When working in Eclipse it is recommended to set the VM arguments to
-Djavax.net.debug=ssl,handshake
* You can use openssl to help you debug the program with the command `openssl s_client -connect 127.0.0.1:2525 -starttls smtp`

### Contributing

Contributing is simple just fork this on GitHub and then send a pull request.

### License

I am still trying to determine the license on this

[1]: http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html
[2]: http://www.jetmore.org/john/code/swaks/
[3]: https://www.activestate.com/activeperl/downloads