# Saw
A small Android library to manage one-off, per-app-version and per-app-launch operations.

Somethings should happen once. 
* Users should only get the guided tour _once_. 
* Release notes should only pop up _once every app upgrade_. 
* Your app should only phone home to update content _once every launch_.

`Saw` provides a simple API to track whether or not your app has already performed an action within a given scope.

## Usage

First things first, you'll need to initialise Saw on start up. In your `Application` class's `onCreate()` override add the follow:

```java
Saw.initialise(this);
```

Now you're ready to go. Say you wanted to navigate to a 'WhatsNew' Activity on every app upgrade:

```java
String whatsNewTag = "WhatsNew";

if (!Saw.hasSeen(whatsNewTag, Saw.SCOPE_APP_VERSION)) {
    startActivity(this, WhatsNewActivity.class);
    Saw.markSeen(whatsNewTag);
}
```

## Installation
