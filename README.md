# AndroidAnonymityChat

## Purpose
Anonymity Chat is an Android app that provides secure and anonymous messaging. It is using TOR Network to send the messages.
### Features:

1. _**Security**_ ---> (we are offering the security in communication by using the Tor Network to send messages between cotnacts)
2.  _**Anonymity**_ ---> (by connecting to Tor Network, the user becomes anonymous on the Internet, the only identity he has is the TOR HOSTNAME (Example: xxxxxxxxxxxxxxxx.onion))
3.  _**Availability**_ ---> (This is a **SERVERLESS** application. This means that there is no server involved in offering the communication services. There is NO 'Central Server' or 'Server in the Cloud' to provide any services. The application is functional and available 100% of the time as long as you and your partner are using **Anonymity Chat**).
4. _**Scalability**_ ---> (Having a **SERVERLESS** software architecture, the application and the communication services are **INFINITELY** scalable (you may encounter technical holdbacks  depending on **YOUR INTERNET CONNECTION**))

Please follow our [WIKI Page](https://github.com/batsw/AndroidAnonymityChat/wiki)

#### Details
- TOR Network communication (about the TOR Network or Deep Web you can find [here] (https://www.torproject.org/docs/faq.html.en#WhatIsTor)
- the anonymous and secure communication the TOR Network comes with
- serverless application (it is created based on a SERVERLESS application architecture (you do not need any servers, third-party services or other resources to use this application))
- message history is kept on each client side as long as they want (no one can retrieve it after it has been deleted)
- the messaging services are always available and infinitely scalable from the networking point of view

- it can be used in any employer scale firm/corporation for communication purposes OR in any personal communication purposes

## How to USE/Install the App

Installation steps are comming soon ...

For more details about features and User Help please refer to our [Wiki Pages] (https://github.com/batsw/AndroidAnonymityChat/wiki).

## Contributor Community, Help and FAQ (Want to Contribute ?)

### Current development state
The project development state can be tracked at the configured Kanban board and by accessing the ISSUES page. There is also available the milestones page that describe the focus of the development effort.
Please follow the following resources:
- [Project Kanban Page] (https://github.com/batsw/AndroidAnonymityChat/projects/1)
- [Milestones] (https://github.com/batsw/AndroidAnonymityChat/milestones)
- [ISSUES page] (https://github.com/batsw/AndroidAnonymityChat/issues)

### Setting Up Development Environment

Please refer to our [Wiki Pages] (https://github.com/batsw/AndroidAnonymityChat/wiki/Contributor-Guide).

#### Setting Up Desktop test environment
    In order to test the app you need another partner to send/receive messages to/from.
So far the app has been tested using a partner from Windows OS using [TorExpertBundle] https://github.com/batsw/TorExpertBundleController
and using the same two classes TorPublisher and TorReceiver. At the specified link you have all the necessary details to set it up.

##### Development environment details
- Platform: Windows and MacOS (so far) but it also works on Linux
- IDE: Android Studio IntelliJ based IDE
- Build system: Gradle
- Dependencies: look into build.gradle and libs folder

##### Building project in Android Studio Intellij based IDE
Steps:
- use gitbash or any other Git client tool to clone the  repository
- Start Intellij
- File -> Open -> Select project location
- if you already have configured gradle it will automatically build at project opening time

## BAT Software Technical SUPPORT
If you have any questions or you encounter difficulties regarding Development or Testing Environment please create an [ISSUE labeled SUPPORT and HELP WANTED] (https://github.com/batsw/AndroidAnonymityChat/issues).
