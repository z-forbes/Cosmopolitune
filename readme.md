# in process of cleaning up code!

## [demo video](https://ed-ac-uk.zoom.us/rec/play/BPXN4VskzRS-DQeKuFWTIHrMbDZejTTCFSM2issGKxspfJl9cCYRM9C-ciLeRID4D0-KBUZSJve4wJ5I.Gb3GQjbgyUMnUHg1)

## to run (I'm on Windows 11):

1. Get a Java JRE (I used 17.0.12)
2. Download and install Maven (I used 3.9.9)
3. Run `java -version` and `mvn -version` to verify installation.
4. Run `git clone https://github.com/z-forbes/Cosmopolitune`
5. Get all API keys listed in [this file](src/main/java/program/extras/Confidential.java) and add them to the file.
6. Run `mvn clean package && mvn tomcat7:run-war`.
7. Open `http://localhost:8080/cosmopolitune/` in browser to use.
