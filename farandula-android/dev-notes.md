# Dev notes
## List of files needed for a refactor to support android integration:


## Needs research or experiments
 - The best practice for Android Libraries is to make them on Java 7/6 in order to allow easier integration.


## Needed refactors
 - Since LocalDateTime is a class from Java 8 we need to change it for another class or use a backported library of java 8 time. We found http://www.threeten.org/threetenbp/ which also has an optimized version for android.
 
 - We need to refactor all the code that is using streams and lambdas over collections since it is not suppoprted by the latest Android SDK (API 25).
 
 - We need to refactor the usage of fuctional interfaces

