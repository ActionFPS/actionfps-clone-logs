# actionfps-clone-logs

> Clone live ActionFPS logs into a file.
 
[![NPM](https://nodei.co/npm/actionfps-clone-logs.png?compact=true)](https://nodei.co/npm/actionfps-clone-logs/)

## Rationale

In order to turn ActionFPS into a platform, we need to share
raw logs with the potential users, and we'd also like to do it live
because there are some use cases where only live data is useful.

The simplest solution with features like authentication that is available
is [EventSource](https://www.w3.org/TR/2015/REC-eventsource-20150203/)
over HTTP/S.

EventSource is supported by [Node.js](https://github.com/EventSource/eventsource/)
and HTML5/Google Chrome, and ActionFPS supplies these events live using an HTTPS
endpoint

The project is written in [Scala.js](https://www.scala-js.org/) and is based on 
[scalajs-cli-demo](https://github.com/ScalaWilliam/scalajs-cli-demo).

Node/JavaScript platform is chosen because it is a good distribution platform with easy access.

Scala is chosen as this is what ActionFPS is built on and provides great testing 
and refactoring capabilities.

## Usage
Use the pre-built npmjs package.

```
$ npm install -g actionfps-clone-logs
$ touch actionfps.tsv
$ actionfps-clone-logs actionfps.tsv
Reading file actionfps.tsv...
Resuming from time 2016-01-02T03:04:05Z, with 0 lines at this time
```

### Authorization

An authorization token can be specified via an environment variable:
 
```
$ AUTHORIZATION="Bearer xyz..." actionfps-clone-logs actionfps.tsv
```

This may let you see full IP addresses for example.

### Default start time

`DEFAULT_START_TIME` (ISO8601 datetime) can be specified
for a default start of the stream.

This may be useful if you aren't interested in very historical data.

## Development
I recommend IntelliJ IDEA. 

To continuously test [inside SBT](https://www.scalawilliam.com/essential-sbt/), run: `~test`.

To test app locally, run:

```
$ sbt publishLocal
$ ./bin/actionfps-clone-logs
```

## Publishing
Requires [SBT](https://www.scalawilliam.com/essential-sbt/).

```
$ npm publish
```
