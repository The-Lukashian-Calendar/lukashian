This is the Java Reference Implementation of The Lukashian Calendar, a calendar that is exceptionally simple, highly accurate and radically different.

For the artifacts, please visit the [Maven Central](https://central.sonatype.com/artifact/org.lukashian/lukashian).

For more information, please visit [lukashian.org](https://www.lukashian.org)!

## Changelog

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
