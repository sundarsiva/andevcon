# Microsoft Hackathon Project - *TraveLog*

[![launcher](https://raw.githubusercontent.com/sundarsiva/andevcon/master/ms_hackathon/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png)](https://github.com/sundarsiva/andevcon/blob/master/ms_hackathon/output/Demo.apk?raw=true)

**TraveLog** is an android app that allows users to log their journey when they're out and about (to new places, events, etc.). The app utilizes [Microsoft Open365 Graph APIs](http://graph.microsoft.io/docs/overview/overview).

## Contributors

* **Sundar Siva** (@sundarsiva)
* **Prerak Trivedi** (@preraktrivedi)
* **Krunal Shah** (@krunalsshah)

## User Stories

The following **functionalities** are completed:

* [x] User can **authenticate** using the [Microsoft Open365 Account] (https://login.microsoftonline.com/)
* [x] User can **view their profile information** (using the Navigation Menu)
  * [x] User can see their Name, Profile Photo, Email, Phone Number
  * [x] User can view a list of their **Friends** (Contacts) 
* [x] On the Homescreen, user can swipe between **different categories of their logs** (sections in the notebook)
  * [x] In each section on the homepage, user can see a **list of the log entries** (pages in that section)
  * [x] User can **click** on a single log entry to view the detail of that Log
    * [x] User can view the text & HTML content along with rich media such as Images that are part of that Log entry (page).
    * [x] User can share this log entry via [Microsoft Graph Email API] with their friends.
  * [x] User can **long click** on a single log entry to view more options such as:
    * [x] **Share** the Log with other apps (using ShareIntent)
    * [x] **Copy** a URL to that Log entry
    * [x] **Delete** the Log entry from that section
  * [x] User can **create a new Log entry** in that section by adding at least a Title and Description
    * [x] User can create a **plain text** Log entry 
    * [x] User can **add images** to the Log entry
  * [x] User can create a new Category on their Homepage (by using the Toolbar action)
  

The following **[Microsoft Graph APIs were Used] (https://github.com/sundarsiva/andevcon/blob/master/ms_hackathon/app/src/main/java/com/andevcon/hackathon/msft/api/ApiClient.java)** are used:

* [Authorization] (http://graph.microsoft.io/docs/authorization/app_authorization) using the [AuthenticationManager] (https://github.com/sundarsiva/andevcon/blob/master/ms_hackathon/app/src/main/java/com/microsoft/office365/connectmicrosoftgraph/AuthenticationManager.java)
* Get the [User Detail] (http://graph.microsoft.io/docs/api-reference/v1.0/api/user_get)
* Get the [User's profile photo] (http://graph.microsoft.io/docs/api-reference/v1.0/api/profilephoto_get)
* Get the [User's friend list] (http://graph.microsoft.io/docs/api-reference/v1.0/api/user_list_contacts)
* Get the [User's sections] (https://graph.microsoft.io/docs/api-reference/beta/api/notes_list_sections)
* Get the [Pages in a section] (http://graph.microsoft.io/docs/api-reference/beta/api/notes_list_pages)
* Get the [Details of a page] (http://graph.microsoft.io/docs/api-reference/beta/api/page_get)
* Get the [image/resource content of a Page] (http://graph.microsoft.io/docs/api-reference/beta/resources/resource)
* Create a [simple page or with embedded media] (http://graph.microsoft.io/docs/api-reference/beta/api/section_post_pages)
* Create a [section along with a page] (http://graph.microsoft.io/docs/api-reference/beta/api/notebook_post_sections)
* Mail the page via the [send mail API] (http://graph.microsoft.io/docs/api-reference/beta/api/user_post_messages)
* Delete a [Page] (http://graph.microsoft.io/docs/api-reference/beta/api/page_delete)

## App APK

Here's a demo apk file:

[![launcher](https://raw.githubusercontent.com/sundarsiva/andevcon/master/ms_hackathon/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png)](https://github.com/sundarsiva/andevcon/blob/master/ms_hackathon/output/Demo.apk?raw=true)

### Demo credentials:
* Username: krunalshah@travelog.onmicrosoft.com
* Password: Krunal123$

## Video Walkthrough 

Here's a video walkthrough of the implemented user stories:

[![launcher](https://raw.githubusercontent.com/sundarsiva/andevcon/master/ms_hackathon/output/youtube.png)](https://www.youtube.com/watch?v=uRNpuboekm4)

## GIF Walkthrough 

Here's a walkthrough of implemented user stories:

![output/travelogdemo.gif](https://raw.githubusercontent.com/sundarsiva/andevcon/master/ms_hackathon/output/travelogdemo.gif)

## General Feedback

* The team present was really helpful at debugging/answering queries.
* The [GitHub sample repository] (https://github.com/OfficeDev/O365-Android-Microsoft-Graph-Snippets) is helpful but not fully comprehensive.
* Registration process was too dispersed 
* The documentation for some of the APIs were incorrect/incomplete.
* Provide an explicit resolved URLs for the image content instead of providing byte streams
* The [/me/notes/pages] endpoint creates a new notebook and not a section inside the existing primary notebook
* The APIs take a really long time to respond sometimes.

## Open-source libraries used

- [Retrofit](https://github.com/square/retrofit) - For network calls
- [Picasso](http://square.github.io/picasso/) - Image loading and caching library for Android
- [GSON](https://github.com/google/gson) - For deserializing the JSON to POJOs etc.

## License

    Copyright [2015] [Team TraveLog]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
