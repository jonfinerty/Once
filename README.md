# Once
[![Android Weekly](http://img.shields.io/badge/Android%20Weekly-%23164-33B5E5.svg?style=flat)](http://androidweekly.net/issues/issue-164)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Once-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/2206)
[![Build Status](https://travis-ci.org/jonfinerty/Once.svg?branch=master)](https://travis-ci.org/jonfinerty/Once)

A small Android library to manage one-off operations for API 14 and higher.

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


### Checking if something has been _done_

Now you're ready to go. Say you wanted to navigate to a 'WhatsNew' Activity every time your app is upgraded:

```java
String showWhatsNew = "showWhatsNewTag";

if (!Once.beenDone(Once.THIS_APP_VERSION, showWhatsNew)) {
    startActivity(new Intent(this, WhatsNewActivity.class));
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

Your app operations can also be rate-limited by time spans. So for example if you only want to phone back to your server a maximum of once per hour, you'd do the following: 
```java
if (!Once.beenDone(TimeUnit.HOURS, 1, phonedHome) { ... }
```

If checking by time bounds is not enough you can manually get the `Date` of last time a tag was marked done by:
```java
Date lastDone = Once.lastDone(brushedTeeth);
```
This will return null if the tag has never been marked as done.

### Marking something as _to do_

Say one part of your app triggers functionality elsewhere. For example you might have some advanced feature onboarding to show on the main activity, but you only want to show it once the user has seen the basic functionality.

```java

// in the basic functionality activity
Once.toDo(Once.THIS_APP_INSTALL, "show feature onboarding");
...

// back in the home activity
if (Once.needToDo(showAppTour)) {
    // do some operations
    ...

    // after task has been done, mark it as done as normal
    Once.markDone(showAppTour);
}
```

When a task is marked done it is removed from the set of tasks 'to do' so subsequent `needToDo(tag)` calls will return `false`. To stop the tag from being added back to your todo list each time the user looks at the basic functionality task, we've added a scope to the todo call: `toDo(Once.THIS_APP_INSTALL, tag)`. You could also use the `THIS_APP_VERSION` scope for todo's which should happen once per app version, or leave off scope complete for tasks which should be repeated every time.

### Doing something once per X events

Sometimes you need to keep track of how many times something has happened before you act on it. For example, you could prompt the user to rate your app after they've used the core functionality three times.

```java
// Once again in the basic functionality activity
Once.markDone("action");
if (Once.beenDone("action", Amount.exactly(3))) {
    showRateTheAppDialog();
}
```

You can also change the count checking from `exactly(int x)` times, to either `Amount.lessThan(int x)` times or `Amount.moreThan(int x)` times. When you don't specific a particular amount, Once will default to `Amount.moreThan(0)` i.e. checking if it's ever been done at all.

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
    compile 'com.jonathanfinerty.once:once:1.3.1'
}
```

You'll need to have `mavenCentral()` in your list of repositories

## Example

Try out the sample app here: https://play.google.com/store/apps/details?id=jonathanfinerty.onceexample and have a look at it's source code in `once-example/` for more simple usage.

## Contributing

`Once` was made in '20%' time at [Huddle](https://talentcommunity.huddle.com/), where its used to help build our [Android apps](https://play.google.com/store/apps/details?id=com.huddle.huddle). [Pete O'Grady](https://twitter.com/peteog) and [Paul Simmons](https://twitter.com/slamminsoup) also provided invaluable feedback.

Pull requests and github issues are more than welcome and you can get in touch with me directly [@jonfinerty](https://twitter.com/jonfinerty).

## License

```
Copyright 2021 Jon Finerty

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
