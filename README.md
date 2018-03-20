# Kalah Game with 6 stones

## Intro
This project serves as a Rest API for a Kalah game with 6 stones.  
for more info on Kalah see https://en.wikipedia.org/wiki/Kalah 

## Locally Run
Install Java JDK 9
See http://www.oracle.com/technetwork/java/javase/downloads/jdk9-downloads-3848520.html

Make sure you activate the Annotation Processor. This is needed to make the Lombok Annotations work.
In Intellij:
Settings/Build, Execution, Deployment/Compiler/Annotation Processor

To run the application.
Use from cmd prompt
Linux
cmd> gradle build bootRun
Windows
cmd> gradlew build bootRun

To get access to the in memory database you can go to the following url:
http://localhost:8080/h2-console

And use 'kalahDB' as database name

## Profiles
There are 2 profiles defined for the moment it and dev
The active profile is by default dev

For dev and it there will be an H2 database created in memory


## Notes
- No frontend avaiable.
- Created integration test to check if the App would work.
- Only main services are Unit tested. If i would have a nbit more time the transformer package would have been tested.


## Thoughts
- First thought about using a caching mechanism, maybe Redis, but went for good old H2 in memory database
- No Authorization/Authorization
- First instance wanted to make it configurable with the amount of stones...
- CQRS would have been nice... But Ok

