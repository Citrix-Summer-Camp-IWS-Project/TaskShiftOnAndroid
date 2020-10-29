# Task shifting feature on Intelligent Workspace client
## Description
Our app implements a client for IWS, which is a product of Citrix that integrates employees’ tasks on multiple work management systems, such as Jira. The main different feature from the current Jira client that we have is the task-shifting feature, which enables users to safely transfer tasks with only one swiping operation. 
## Features
#### Data fetching
Jira: Use OkHttp to send request Rest API to get personal data including task name, task description, completion status. Then, we will display all the information as a card  list.
#### OAuth
Using OAuth SSO feature to enable users log in. Users don’t need to tell the app username and password which ensures security. 
#### Bluetooth auto connection
In order to reduce unnecessary operations for users, our App, when booted, would try to auto connect phones that have been connected before for at least once.  At this time, if the other phone also has our app open and recognizes the phone, it would automatically accept the request, which then steps into Identity confirmation described below.
#### Identity confirmation
In order to ensure the integrity and confidentiality of the tasks we are sending, we use RSA algorithms to confirm the identity of people trying to shift tasks to us. Only after identity confirmation can we send tasks to each other.
#### Task shifting
To enable users to shift tasks within one operation yet be able to regret, we synchronized the swiping between two clients. This enables the receivers to preview the task and senders to keep tasks until they finally decide to shift the tasks.  This dynamic synchronization is achieved by continuous coordinates sharing through Bluetooth and customized RecyclerView for task displaying.

## Visual

We use Material design’s color themes and  components for UI and animation. 

After the Identity confirmation, the connected users' Avatar would shown at the right corner of the screen, indicating the shifting-status is ready.

User could review and choose the tasks shown as a cards list. Swiping a card horizontally from left, when half of the card disappeared from the sender's screen, the corresponding half part of the card would show dynamically on the screen of receiver; If the sender wants to stop while swiping, he could just hold and put the card back so that correspond contents would disappear dynamically on receiver's device. The users could control "how much" they want to swipe.

## Installation and Usage

Requirement: Android 9.0 and above, Jira account, Permission of GPS location as well as Bluetooth Management for the app

Usage: Use Oauth to log inAuthorize the use of APIAllow the use of bluetoothConnect the colleague via bluetoothUse swipe to send tasks

## Development

We used Java 8 with Android Studio as our IDE. For version control, we used Github with continuous integration. For each commit merging into Master Branch, we enabled Lint to scan our code and kept 0 warnings.	At present, the major task-shifting apps would need sophisticated operations, including the enter receivers’ emails, search list, confirmations, which are low efficient. 