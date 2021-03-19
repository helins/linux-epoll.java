# Linux-Epoll


[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.helins/linux-epoll/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.helins/linux-epoll)

[![Javadoc](https://javadoc.io/badge2/io.helins/linux-epoll/javadoc.svg)](https://javadoc.io/doc/io.helins/linux-epoll)

Sometimes, when messing around with java and JNI/JNA, file descriptors need to
be polled efficiently. This library provides an API for using
[epoll](https://en.wikipedia.org/wiki/Epoll) on a Linux system.


## Usage

Understand the [native API](http://man7.org/linux/man-pages/man7/epoll.7.html).

Read the
[javadoc](https://javadoc.io/doc/io.helins/linux-epoll).


## License

Copyright © 2018 Adam Helinski

Licensed under the term of the Mozilla Public License 2.0, see LICENSE.
