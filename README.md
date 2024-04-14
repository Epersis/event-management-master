# Event Management Project

This event management project is spring boot based implementation project which is aiming to cover the event management functionality.

## How to setup or run the project on local system.

- Download the Java 17 and configured.
- Download the Apache Maven from https://maven.apache.org/download.cgi site.
- Unzip the Apache Maven in any folder.
- Please change the path in the compile.bat file to where apache maven is installed on your local system
- Please create a schema called eventdb in mysql
- Run the `compile.bat` command.
- in mysql, username should be "root" and password is empty.
- Go to `target` directory.
- There should be a jar `event-management-0.0.1-SNAPSHOT.jar` in `target` directory.
- Run the command `java -jar event-management-0.0.1-SNAPSHOT.jar` command to start the project the embedded server.





