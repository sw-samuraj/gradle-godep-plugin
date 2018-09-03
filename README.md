# Gradle Golang plugin

[![Build Status](https://travis-ci.org/sw-samuraj/gradle-godep-plugin.svg?branch=master)](https://travis-ci.org/sw-samuraj/gradle-godep-plugin)
[![Gradle Plugins Release](https://img.shields.io/github/release/sw-samuraj/gradle-godep-plugin.svg)](https://plugins.gradle.org/plugin/cz.swsamuraj.godep)

A simple Gradle wrapper for _go_ commands, allowing to build and test
_Golang_ programs and manage their dependencies either via _Go modules_, or via _dep_ tool. The project
directory doesn't have to be in the standard `$GOAPTH` repository, therefore
you can locate your project repository anywhere on filesystem.

For usage of _Go modules_ you have to have installed at least **go1.11**.

The plugin supports 3 ways how to deal with external dependencies:

1. _Go modules_
1. _go_ + _dep_ + optional management of _"proprietary vendors"_
1. _go_ + manually/externally managed _vendors_

Plugin expects that _go_ and _dep_ (if used) commands are already installed on given system and that they are available on `$PATH`.

In case of _dep_ usage:

* Only Unix systems are supported. (Windows support can be added on demand.)
* The plugin can deal with _"proprietary vendors"_ - package imports which are not in public repositories
like [GitHub](https://github.com), or [Bitbucket](https://bitbucket.org), but are proprietary, e.g. repositories
behind a company firewall etc. (see [Limitations](https://github.com/sw-samuraj/gradle-godep-plugin#limitations))

## Contents

1. [Applying the plugin](https://github.com/sw-samuraj/gradle-godep-plugin#applying-the-plugin)
1. [Using the plugin](https://github.com/sw-samuraj/gradle-godep-plugin#using-the-plugin)
    1. [Go modules](https://github.com/sw-samuraj/gradle-godep-plugin#go-modules)
    1. [Go + dep](https://github.com/sw-samuraj/gradle-godep-plugin#go--dep)
    1. [Go + manually managed vendors](https://github.com/sw-samuraj/gradle-godep-plugin#go--manually-managed-vendors)
1. [Tasks](https://github.com/sw-samuraj/gradle-godep-plugin#tasks)
1. [How to handle proprietary vendors](https://github.com/sw-samuraj/gradle-godep-plugin#how-to-handle-proprietary-vendors)
1. [Examples](https://github.com/sw-samuraj/gradle-godep-plugin#examples)
1. [License](https://github.com/sw-samuraj/gradle-godep-plugin#license)

## Applying the plugin

### Gradle 2.1+

```groovy
plugins {
    id "cz.swsamuraj.godep" version "0.6.1"
}
```
### All Gradle versions (or local repository)

```groovy
buildscript {
    repositories {
        maven {
            url "https://plugins.gradle.org/m2/"
        }
    }
    dependencies {
        classpath "gradle.plugin.cz.swsamuraj:gradle-godep-plugin:0.6.1"
    }
}

apply plugin: "cz.swsamuraj.godep"
```

## Using the plugin

### Go modules

The plugin expects that you have a local installation of `go1.11` and that
this command is available on `$PATH`. Further, there should be:
 
* a `go.mod` file in your project directory, which contains **at least** the `module` statement.

No configuration is needed.

#### Build life-cycle

The plugin has following life-cycle.

1. `test`
1. `build`

### Go + dep

The plugin requires that you have a local installation of `go` and `dep` tools and that
those commands are available on `$PATH`. There shouldn't be any `go.mod` file in the project.

A minimal necessary configuration:

```groovy
godep {
    importPath = 'github.com/sw-samuraj/hello'
}
```

#### Build life-cycle

The plugin uses following life-cycle. You can skip certain tasks via configuration switches.

1. `prepareWorkspace`
1. `dep` can be optionally disabled (see [Config options](https://github.com/sw-samuraj/gradle-godep-plugin#config-options))
1. `proprietaryVendors` can be optionally disabled (see [Config options](https://github.com/sw-samuraj/gradle-godep-plugin#config-options))
1. `test`
1. `build`

#### Config options

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

    // Skips the proprietaryVendors task in the build life-cycle.
    proprietaryVendorsOptional = true

    // Map of import packages from non-public repositories.
    // The item in the map has an import path as a key and a tag
    // (or a branch) as a value.
    proprietaryVendors = [
            'my.private.repo/my-org/my-package':   'v0.1.0',
            'my.private.repo/my-org/your-package': 'v0.3.2',
            'my.private.repo/my-org/his-package':  'master'
    ]
}
```

### Go + manually managed vendors

The plugin expects that you have a local installation of `go1.11` and that
this command is available on `$PATH`. Further, there should be:

* a `go.mod` file in your project directory, which contains **only** the `module` statement
* and a `vendor` directory containing all needed dependencies.

In this case, the dependencies are not managed neither by plugin, nor _go_  itself.

A minimal necessary configuration:

```groovy
godep {
    disableGoModule = true
}
```

#### Build life-cycle

The plugin has following life-cycle.

1. `test`
1. `build`

## Tasks

### clean

Deletes the `build` directory.

### cleanVendors

Deletes the `vendor` directory. If you want to clean both, the `build` and the `vendor` directory every time, you can
define following task dependency:

```groovy
clean.dependsOn cleanVendors
```

### prepareWorkspace

Creates a "fake" `$GOPATH` directory structure inside the `build/go` directory
with a symbolic link targeting the project directory. Every `go` command is then
executed under this "real" `$GOPATH`.

(This is just a technical detail explanation in case you are curious.) :wink:

### dep

Calls `dep init` command in case there is no `Gopkg.toml` file presented.
Otherwise calls `dep ensure` command.

### proprietaryVendors

Clones all the defined import packages from non-public repositories to the `vendor/<importPath>` directory.
Currently, it supports only clonning via _https_.

### test

Calls `go test` command.

### build

Calls `go build` command. Compiled binary file is stored in the `build/out` directory.

## How to handle proprietary vendors

**Applies for dep approach only.**

Go tools currently don't support package imports which are not in public repositories,
such as [GitHub](https://github.com), [Bitbucket](https://bitbucket.org) etc. Therefore if you have a proprietary
repository, e.g. behind a company firewall, you are on your own to solve all the dependency and build problems.
The `gradle-godep-plugin` took a specific approach, how to handle this situation:

1. Use `dep` tool for solving of public imports, which are stored in the `vendor` directory.
1. Clone proprietary imports via Gradle `proprietaryVendors` task to the `vendor` directory.

### Configuration

Unfortunately, configuration of the proprietary package imports has to be done on two places:

* **`build.gradle`** defines a map of package imports and their versions. These packages are clonned
  during execution of the `proprietaryVendors` task.
* **`Gopkg.toml`** defines the same package imports (without versions) as `ignored`, so they can be ignored
  during execution of the `dep` task.
* Eventually, if the proprietary package imports contain transitive dependencies on packages in public
  repositories, those have to be defined in the `Gopkg.toml` file as `required`.

Let's consider following example: we have a proprietary package `my.company.repo/cool-project/shared-package`
which has a transitive dependency on the `github.com/coreos/etcd/clientv3` package.

**`build.gradle`:**

```groovy
godep {
    // Map of import packages from non-public repositories.
    // The item in the map has an import path as a key and a tag
    // (or a branch) as a value.
    proprietaryVendors = [
            'my.company.repo/cool-project/shared-package': 'v0.1.0'
    ]
}
```
**`Gopkg.toml`:**

```toml
# Needs to be filled only in case of transitive dependencies, otherwise can be omitted.
required = [
  "github.com/coreos/etcd/clientv3"
]

# It's the same package as in godep.proprietaryVendors map, only without version.
ignored = [
  "my.company.repo/cool-project/shared-package"
]

```

### Limitations

Currently, cloning of the proprietary vendors have these limitations:

* Cloning is done via _https_ only (e.g. `git clone https://xxx`).
* Cloned repository has to be anonymously accessible.

_SSL_, or _https_ authentication could be added later, based on demand
(please, fill an [issue](https://github.com/sw-samuraj/gradle-godep-plugin/issues)).

## Examples

Usage of the plugin and example projects can be found in the `examples` directory:

* `go-and-modules` shows configuration for plain _Go modules_ support.
* `go-and-dep` shows configuration for _go_ + _dep_ support (including _"proprietary vendors"_).
* `go-and-disabled-modules` shows configuration for _Go_ build with manually managed _vendors_.

## License

The **gradle-godep-plugin** is published under [BSD 3-Clause](http://opensource.org/licenses/BSD-3-Clause) license.
