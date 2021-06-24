# Changelog

## In Development

## [1.3.1]

### External

- Publishing / Hosting changed from jcenter to mavenCentral
- Fix session scope concurrency issue
- Fix race condition with app shutdown

### Internal

- Bump to Gradle 6.7.1 and build tools 4.2.1
- Bump JUnit and Robolectric

## [1.3.0] - 2019-08-19

### External

- Drop support for pre-API 14
- Update to using AndroidX annotation library (from support library)
- Removed Async Initialisation (advice is to enable StrictMode after calling `Once.initialise(this)`)
- Crash fixes for issues #39, #32

### Internal

- Update to target android-29
- Update to gradle 5.1.1 and build tools 3.4.2
- Update test app to AndroidX libraries

## [1.2.2] - 2017-04-30

- previous versions TBC