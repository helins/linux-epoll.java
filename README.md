# linux-epoll

[
![Download](https://api.bintray.com/packages/dvlopt/maven/linux-epoll/images/download.svg)
](https://bintray.com/dvlopt/maven/linux-epoll/_latestVersion)

Sometimes, when messing around with java and JNI/JNA, file descriptors need to
be polled efficiently. This library provides an API for using
[epoll](https://en.wikipedia.org/wiki/Epoll) on a Linux system.

## Usage

Understand the [native API](http://man7.org/linux/man-pages/man7/epoll.7.html).

Read the [javadoc](https://dvlopt.github.io/doc/java/linux-epoll/index.html?overview-summary.html).

## License

MIT License

Copyright © 2018 Adam Helinski

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the “Software”), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
