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
    id "cz.swsamuraj.godep" version "0.2.1"
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
        classpath "gradle.plugin.cz.swsamuraj:gradle-godep-plugin:0.2.1"
    }
}

apply plugin: "cz.swsamuraj.godep"
```

## Using the plugin ##

The plugin requires that you have a local installation of `go` and `dep` tools and that
those commands are available on `$PATH`.

### Build life-cycle ###

TBD

### Tasks ###

**clean**

TBD

**prepareWorkspace**

TBD

**dep**

TBD

**test**

TBD

**build**

TBD

### Config options ###

There must be a `godep` part in the `build.gradle` file which defines a mandatory parameter `importPath` which emulates
directory structure inside standard `$GOPATH` repository.

```groovy
godep {
    importPath = 'github.com/sw-samuraj/hello'
}
```

## Example ##

Usage of the plugin and example project can be found in the `example` directory.

## License ##

The **gradle-godep-plugin** is published under [BSD 3-Clause](http://opensource.org/licenses/BSD-3-Clause) license.
