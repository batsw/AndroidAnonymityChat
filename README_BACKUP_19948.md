# AndroidAnonymityChat
<<<<<<< .merge_file_a10312

## Purpose
Anonymity Chat is an Android app that provides secure messaging. It is using TOR Network to send the messages.
Features:
=======
A chat application that uses the Tor network to send the messages

### Features:
>>>>>>> .merge_file_a12784
- TOR Network communication (about the TOR Network or Deep Web you can find [here] (https://www.torproject.org/docs/faq.html.en#WhatIsTor)
- the anonymous and secure communication the TOR Network comes with
- serverless application (it is created based on a PURE SERVERLESS application architecture (you do not need any servers, third-party services or other resources to use this application))
- message history is kept on each client side as long as they want (no one can retrieve it after it has been deleted)
- the messaging services are always available and infinitely scalable from the networking point of view
- it can be used in any employer scale firm/corporation for communication purposes

<<<<<<< .merge_file_a10312
## Contributor Community and Help FAQ

### Current development state

- the app can receive messages from any desktop TOR environment which has the TORPublisher class set to the Hidden Services of the phone.
- due to Android environment deployment differences to 'normal' desktop environment, the Android Anonymity Chat cannot initiate communication and
send messages (only to receive)
- it has been developed a simple and basic user interface to facilitate development of the core functionality:
        - Open Port functionality
        - Connect to Partner Hostname (just enter xxxxxxxx.onion of the partner and click on Connect button)

### Setting Up Desktop test environment
    In order to test the app you need another partner to send/receive messages to/from.
So far the app has been tested using a partner from Windows OS using [TorExpertBundle] https://github.com/batsw/TorExpertBundleController
and using the same two classes TorPublisher and TorReceiver. At the specified link you have all the necessary details to set it up.

### Development environment details
- Platform: Windows and MacOS
- IDE: Android Studio IntelliJ based IDE
- Build system: Gradle
- Dependencies: look into build.gradle

### Building project in Android Studio Intellij based IDE
Steps:
- use gitbash or any other Git client tool to clone the  repository
- Start Intellij
- File -> Open -> Select project location
- if you already have configured gradle it will automatically build at opening time

=======
## The project state is in Pre-Release form, therefore please access the Development branch to find more details
>>>>>>> .merge_file_a12784
