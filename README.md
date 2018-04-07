# Gradle Golang plugin #

[![Build Status](https://travis-ci.org/sw-samuraj/gradle-godep-plugin.svg?branch=master)](https://travis-ci.org/sw-samuraj/gradle-godep-plugin)

A simple Gradle wrapper for _go_ and _dep_ commands, allowing to build and test
_Golang_ programs and manage their dependencies via _dep_ tool. The project
directory doesn't have to be in the standard `$GOAPTH` repository, therefore
you can locate your project repository anywhere on filesystem.

Plugin expects that _go_ and _dep_ commands are already installed on given system and that they are available on `$PATH`.

**Currently, only Unix systems are supported. Windows support can be added on demand.**

## Applying the plugin ##

### Gradle 2.1+ ###

```groovy
plugins {
    id "cz.swsamuraj.godep" version "0.3.0"
}
```
### All Gradle versions (or local repository) ##

```groovy
buildscript {
    repositories {
        maven {
            url "https://plugins.gradle.org/m2/"
        }
    }
    dependencies {
        classpath "gradle.plugin.cz.swsamuraj:gradle-godep-plugin:0.3.0"
    }
}

apply plugin: "cz.swsamuraj.godep"
```

## Using the plugin ##

The plugin requires that you have a local installation of `go` and `dep` tools and that
those commands are available on `$PATH`.

A minimal necessary configuration:

```groovy
godep {
    importPath = 'github.com/sw-samuraj/hello'
}
```

### Build life-cycle ###

The plugin uses following life-cycle. You can skip certain tasks via configuration switches.

1. `prepareWorkspace`
1. `dep` can be optionally disabled (see https://github.com/sw-samuraj/gradle-godep-plugin#config-options)
1. `test`
1. `build`

### Tasks ###

**clean**

Deletes the `build` directory.

**prepareWorkspace**

Creates a "fake" `$GOPATH` directory structure inside the `build/go` directory
with a symbolic link targeting the project directory. Every `go` command is then
executed under this "real" `$GOPATH`.

(This is just a technical detail explanation in case you are curious.) :wink:

**dep**

Calls `dep init` command in case there is no `Gopkg.toml` file presented.
Otherwise calls `dep ensure` command.

**test**

Calls `go test` command.

**build**

Calls `go build` command. Compiled binary file is stored in the `build/out` directory.

### Config options ###

There must be a `godep` part in the `build.gradle` file which defines a mandatory parameter `importPath` which emulates
directory structure inside standard `$GOPATH` repository.

You can make certain tasks optional, or mandatory via `boolean` switch (see example below).

```groovy
godep {
    // A mandatory value which defines the full import path for the package.
    // Basically, it's the directory structure under $GOPATH/src.
    importPath = 'github.com/sw-samuraj/hello'

    // Skips the dep task in the build life-cycle.
    depOptional = true
}
```

## Example ##

Usage of the plugin and example project can be found in the `example` directory.

## License ##

The **gradle-godep-plugin** is published under [BSD 3-Clause](http://opensource.org/licenses/BSD-3-Clause) license.
