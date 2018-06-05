# Changelog of _Gradle Golang Plugin_

## 0.5.0 (next version)

* The `fmt` task added to the life-cycle ([#])
* The `vet` task added to the life-cycle ([#])
* Code refactoring ([#])

## 0.4.2

* Fix delete non-empty proprietary vendor directory error.
  ([#11](https://github.com/sw-samuraj/gradle-godep-plugin/pull/11))

## 0.4.1

* Fix error `proprietaryVendors` property without provider.
  ([#10](https://github.com/sw-samuraj/gradle-godep-plugin/pull/10))

## 0.4.0

* The `proprietaryVendors` task for dealing with import packages in private repositories.
  ([#9](https://github.com/sw-samuraj/gradle-godep-plugin/pull/9))

## 0.3.3

* Fix optional `dep` task in multi-module build
  ([#7](https://github.com/sw-samuraj/gradle-godep-plugin/pull/7))
* CHANGELOG.md added
  ([#6](https://github.com/sw-samuraj/gradle-godep-plugin/pull/6))

## 0.3.2

**No new features.** This release has been created via automated _Travis CI_ deployment.
([#5](https://github.com/sw-samuraj/gradle-godep-plugin/pull/5))

* Publishing to _Gradle Plugin Portal_.
* Creating of the _GitHub_ release.

## 0.3.0

* The `dep` task could be optional via configuration.
  ([#2](https://github.com/sw-samuraj/gradle-godep-plugin/pull/2))

## 0.2.1

Supports three basic Gradle tasks, chained in following order:

* `dep`
* `test`
* `build`

## 0.1.0

Initial release.
