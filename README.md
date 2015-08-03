# Once
A small Android library to manage one-off operations.

----

Some things should happen **once**.
* Users should only get the guided tour _once_. 
* Release notes should only pop up _once every app upgrade_. 
* Your app should only phone home to update content _once every hour_.

`Once` provides a simple API to track whether or not your app has already performed an action within a given scope.

## Usage

First things first, you'll need to initialise Once on start up. In your `Application` class's `onCreate()` override add the follow:

```java
Once.initialise(this);
```

Now you're ready to go. Say you wanted to navigate to a 'WhatsNew' Activity every time your app is upgraded:

```java
String showWhatsNew = "showWhatsNewTag";

if (!Once.beenDone(Once.THIS_APP_VERSION, showWhatsNew)) {
    startActivity(this, WhatsNewActivity.class);
    Once.markDone(showWhatsNew);
}
```

Or if you want your app tour to be shown only when a user install, simply check the tag using the `THIS_APP_INSTALL` scope:

```java
if (!Once.beenDone(Once.THIS_APP_INSTALL, showAppTour)) {
    ...
    Once.markDone(showAppTour);
}
```

Your app operations can also be rate-limited by time spans. So for example if you only want to phone back to your a maximum of server once per hour, you'd do the following: 
```java
if (!Once.beenDone(TimeUnit.HOURS, 1, phonedHome) { ... }
```
 
To de-noise your code a bit more you can also static-import the `Once` methods, so usage looks a bit cleaner

```java
import static jonathanfinerty.once.Once.THIS_APP_INSTALL;
import static jonathanfinerty.once.Once.beenDone;
import static jonathanfinerty.once.Once.markDone;

...
...

if (!beenDone(THIS_APP_VERSION, tagName)) {
    ...
    markDone(showWhatsNew);
}
```

## Installation

Add a library dependency to your app module's `build.gradle`:

```
dependencies {
    compile 'com.jonathanfinerty.once:once:0.3.2'
}
```

You'll need to have `jcenter()` in your list of repositories

## Example

Have a look at the example app in `once-example/` for more simple usage.

## Contributing

`Once` was made in '20%' time at [Huddle](https://talentcommunity.huddle.com/), where its used to help build our Android apps. [Pete O'Grady](https://twitter.com/peteog) and [Paul Simmons](https://twitter.com/slamminsoup) also provided invaluable feedback.

Pull requests and github issues are more than welcome and you can get in touch with me directly [@jonfinerty](https://twitter.com/jonfinerty).

## License

```
Copyright 2015 Jon Finerty

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
