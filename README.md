This is the Java Reference Implementation of The Lukashian Calendar, a calendar that is exceptionally simple, highly accurate and radically different.

For the artifacts, please visit the [Maven Central](https://central.sonatype.com/artifact/org.lukashian/lukashian).

For more information on the calendar mechanism itself, please visit [lukashian.org](https://www.lukashian.org)!

&nbsp;
## How to use the Lukashian Calendar

Using the Lukashian Calendar is easy. First, add the most recent version of the [jar file](https://central.sonatype.com/artifact/org.lukashian/lukashian)
as a dependency. Then, you can simply use the `org.lukashian.Instant`, `org.lukashian.Day` and `org.lukashian.Year` classes like you're used to. For example, to
get the current time, use `Instant.now()`. Please see the [Javadoc](https://www.lukashian.org/javadoc/overview-tree.html) for full documentation on
these classes.

### Formatting

Use the `org.lukashian.Formatter` class to format Instants, Days and Years. Please see the [Javadoc](https://www.lukashian.org/javadoc/org/lukashian/Formatter.html)
for all available formatting options.

### Using different calendar implementations

By default, the Lukashian Calendar uses the implementation for Planet Earth. However, because the Lukashian Calendar mechanism makes no assumptions about
how long days and years are, or how many days a year should have, it is possible to create implementations for other planets. Please see the
[Javadoc](https://www.lukashian.org/javadoc/overview-tree.html) of the `MillisecondStore` and `MillisecondStoreDataProvider` classes for more details.

### Loading calendar implementations from an external resource

In order to facilitate central maintenance and governance of the official numbers that define an implementation of the Lukashian Calendar, the
`ExternalResourceMillisecondStoreDataProvider` can load implementations from external resources.

The `FileMillisecondStoreDataProvider` and `HttpMillisecondStoreDataProvider` provide functionality for loading implementations via files or HTTP URL's,
respectively, whereas the `StandardEarthHttpMillisecondStoreDataProvider` loads the implementation for Planet Earth directly from the official lukashian.org server.

Please see the [Javadoc](https://www.lukashian.org/javadoc/overview-tree.html) for full documentation on these classes.

#### Ports to other languages

Because of this external loading mechanism, ports of the Lukashian Calendar in other programming languages only need to port the `ExternalResourceMillisecondStoreDataProvider`
and its subclasses, not the implementations of `MillisecondStoreDataProvider` that perform the actual calculations, as the results of these calculations will also
be loadable from the official lukashian.org server.


&nbsp;
## Changelog
Please refer to the [Maven Central](https://central.sonatype.com/artifact/org.lukashian/lukashian/versions) for the release date and artifacts of each version.

### 1.12.0:
- Added method `Day.getLengthOfBeepInMilliseconds()`
- Added mechanism for loading implementations of the Lukashian Calendar from external resources, specifically, the `ExternalResourceMillisecondStoreDataProvider`,
  `FileMillisecondStoreDataProvider`, `HttpMillisecondStoreDataProvider` and `StandardEarthHttpMillisecondStoreDataProvider` classes.

### 1.11.0:
- Improved Javadoc documentation
- Updated versions of dependencies

### 1.10.0:
- Created this changelog
- Added methods `Day.plusYears()`, `Day.minusYears()`, `Instant.plusYears()` and `Instant.minusYears()`
- Added methods `Year.containsNot()`, `Day.containsNot()`, `Day.isNotIn()`, `Instant.isNotIn()`
- Added method `MillisecondStore.setMillisecondStoreDataProvider()` that allows changing the `MillisecondStoreDataProvider` on the fly
- Switched from `java.lang.BigDecimal` to `org.apache.commons.numbers.fraction.BigFraction` for `Instant` calculations, because `BigDecimal` did not provide the required accuracy
- Switched from Java 17 LTS to Java 21 LTS
- Various small improvements and optimizations

### 1.9.0 and before:
- The Lukashian Calendar did not maintain a changelog on GitHub yet...
