# Event Management Project

This event management project is spring boot based implementation project which is aiming to cover the event management functionality.

## How to setup or run the project on local system.
1. Backend
- Download the Java 17 and configured.
- Download the Apache Maven from https://maven.apache.org/download.cgi site.
- Unzip the Apache Maven in any folder.
- Please change the path in the compile.bat file to where apache maven is installed on your local system
- Please create a schema called `eventdb` in mysql
- Run the `compile.bat` command.
- In mysql, username should be "root" and password is empty.
- Run the `run.bat` command to start the application backend.

2. Users' Interface
- Download and install node.js
- Run `ui_run.bat` command to start the front-end of the application, which is hosted on localhost:3000
- Below are the login pages of the different user types:
    + Customer: `http://localhost:3000/customer-login`
    + Event Manger: `http://localhost:3000/event-manager-login`
    + Ticket Officer: `http://localhost:3000/ticket-officer-login`


## Note:
The application may lag at times, please be patient. Thank you! \n\n

** Currently the creation and updating of events allows for events to be edited and updated in the past. This is because validation is not implemented for testing reasons, but it is commented out if you would like to test it in the following files: \n
/ui/eventManager/eventManaging/addevent.html: lines 77 & 109-110 \n
/ui/eventManager/eventManaging/viewevent.html: lines 135 & 182




