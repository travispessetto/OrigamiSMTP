[![gitcheese.com](https://s3.amazonaws.com/gitcheese-ui-master/images/badge.svg)](https://www.gitcheese.com/donate/users/930497/repos/81178106) [![Gitter chat](https://badges.gitter.im/OrigamiSMTP/gitter.png)](https://gitter.im/OrigamiSMTP) [![Build Status](https://travis-ci.org/travispessetto/OrigamiSMTP.svg?branch=master)](https://travis-ci.org/travispessetto/OrigamiSMTP)

# Origami SMTP

Origami SMTP is a fake SMTP server with SSL (STARTTLS) support. The
reason it was developed was so that developers could test their
applications that require a secure SMTP server without having
to change more than just a couple settings.

This project is the runnable jar file but you can download a easier to
use application (also know as Origami SMTP) from the [official website][10]

## Requirements

This server requires:

* [Java 1.8 or higher][6]
* [Java JCE][1]

## Starting the Server

To start the server run the following from the terminal or command line.

`java -jar origami.jar 2525`

You may replace 2525 with the port you would like to use.

## Working with STARTLS

Since this service requires some custom configuration, you will have to load
the [Origami CA Certificate][4] into your Trusted Root Store. If you are
unsure how to do this on Windows [follow this guide][5].  You will have
to do this on other operating systems too but at this time we have no
guides provided.

Next make sure your SMTP settings set the host to localhost and not
the IP address 127.0.0.1 or the validation may fail, notably in
C#.

You may have to download the [Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files 8][1].  This extension may be
restricted in some countries and may have export controls enforced by the United State Government.  Since we do not have the funds for legal
council this file must be downloaded from the link provided.  Follow the instructions in README.txt to install.

## Debugging

### Using Swaks

To use Swaks to debug you will need Perl installed.  To do this on Windows we suggest
the use of [ActivePerl][3] as we could not get it to work with Strawberry Perl. After
that is installed you can use the following command to help with debugging.

```sh
swaks.pl -t john.doe@example.com -f jane.doe@example.com -s localhost -p 2525 -tls --tls-verify --tls-ca-path /path/to/origami/ca.crt
```

### Using OpenSSL

To use OpenSSL s_client to try and debug use the following command

```sh
openssl s_client -connect localhost:2525 -starttls smtp
```

Previously 127.0.0.1 was used in place of localhost so it may validate
correctly now but has not been tested.

## Helpful Tips

* When working in Eclipse it is recommended to set the VM arguments to
-Djavax.net.debug=ssl,handshake
* You can use openssl to help you debug
* You can use [Swaks][2] to help debug 

## Contributing

Contributing is simple just fork this on GitHub and then send a pull request.

## License

[MIT License](license.txt)

[1]: http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html
[2]: http://www.jetmore.org/john/code/swaks/
[3]: https://www.activestate.com/activeperl/downloads
[4]: https://raw.githubusercontent.com/travispessetto/OrigamiSMTP/master/src/main/resources/certs/CA/Origami_CA.crt
[5]: https://technet.microsoft.com/en-us/library/cc754841(v=ws.11).aspx
[6]: https://java.com
[7]: https://github.com/travispessetto/OrigamiGUI
[8]: https://github.com/travispessetto/OrigamiGUI/releases
[9]: https://github.com/travispessetto/OrigamiSMTP/releases
[10]: https://travispessetto.github.io/OrigamiSMTP
