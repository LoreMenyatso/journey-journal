Mobile Application Core Functionality - - - - 
User Registration and Authentication: The app has the functionality of creating user 
accounts and authenticating user accounts through firebase third party authentication cloud 
service. 

Create Entries: Once the user has been authenticated, the application has the ability to 
create new entries and store the data remotely on a firebase real-time cloud database. This 
ensures data could be access from any device with an internet connection. 

Read Entries: The application retrieves data from a storage database and a real-time 
database Stored data in the form of plain text and images and displays the content through 
an inflatable recycler view model layout. 

Share/ send data to third party applications: The application can communicate with other 
applications installed on the device and exchange data. The is only done in plain text. 
Storage of data 
Login and Sign-up data: User login and sign-up data is stored in the firebase authentication service. 


CRUD operations data: For storing entry data two services were used from the firebase cloud 
service. These are: 
1. Real-time database: This was for storing data in the form of plain text. Below is a screen 
shot of the prototype realtime database.
