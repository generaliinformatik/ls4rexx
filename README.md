# ls4rexx

**ls4rexx** is a REXX specific implementation of the 
[Language Server Protocol](https://microsoft.github.io/language-server-protocol/)
and can be used with any editor that supports the protocol. 

I have started this project since I did not find any open source Eclipse IDE plugin
for this language. REXX is widely used on the Mainframe platform comparable to Perl on Unix.
Since I use this language on a daily basis I thought it worth my while to have a look
into how much work it would be to implement an editor support for this language.

This is still a work in progress and as the license states there is no guarantee that this 
project will be helpful to anyone (or ever be finished). I do not plan a full syntax check 
for the language and think of this project more in the line of a  _good-enough-support_  for
the developer to help with navigation and code completion.

The server is based on:

* [Eclipse LSP4J](https://projects.eclipse.org/projects/technology.lsp4j), the Java binding for the Language Server Protocol.
* [JFlex](https://jflex.de/), a lexical analyzer generator (also known as scanner generator) for Java, written in Java.

I want to give kudos to [Angelo Zerr](https://github.com/angelozerr) whose implementation of the
[XML Language Server (LemMinX)](https://github.com/angelozerr/lemminx) helped me to understand
how to implement a language server using [Eclipse LSP4J](https://projects.eclipse.org/projects/technology.lsp4j).  

# Features

The following features are in progress:

* [textDocument/completion](https://microsoft.github.io/language-server-protocol/specification#textDocument_completion).
* [textDocument/documentSymbol](https://microsoft.github.io/language-server-protocol/specification#textDocument_documentSymbol).

# Get started

* Clone this repository
* Open the folder in your terminal / command line
* Run `./mvnw clean verify` (OSX, Linux) or `mvnw.cmd clean verify` (Windows)
* After successful compilation you can find the resulting `ls4rexx-{version}-jar-with-dependencies.jar` in the folder `target`

# Developer

To debug ls4rexx you can use `de.holzem.ls.test.LServerSocketLauncher`:

1. Run the `de.holzem.ls.test.LServerSocketLauncher` in debug mode (e.g. in eclipse)
2. Connect your client via socket port. Default port is 5008, but you can change it with start argument `--port` in step 1

# Clients

Currently there is only the following client available:

* Eclipse IDE support with [rexx4e](https://github.com/holzem/rexx4e "rexx4e")

# Licenses

ls4rexx is published under the Apache 2.0 License

# Building and Contributing

To build and contribute to ls4rexx consult the [Contributing Guilde](https://github.com/holzem/ls4rexx/blob/master/CONTRIBUTING.md)

# Acknowledgements

This projects makes use of third party projects. Refer to the [Acknowledgements Information](https://github.com/holzem/ls4rexx/blob/master/ACKNOWLEDGEMENT.md)   
