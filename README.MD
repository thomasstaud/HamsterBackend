# Backend Spring

This is the API Background where our frontend framework (= VueJS) sends request to.

## Installation guide for developer (not using docker) to edit program

- git clone https://github.com/xJHamster/BackendSpring
- open maven spring project in your IDE 
- create mysql database called "springserver"
- run ProjectWebsiteApplication 

You can apply some changes in settings.properties, but be careful! You might need to reinstall your database!

## The commands 

- vor(); 
- linksUm(); 
- nimm(); 
- gib(); 
- vornFrei(); 
- kornDa(); 
- maulLeer();

## Rest Services

Standard user to log in with admin rights:  
username: admin  
password: admin  

Should be changed when publishing it!

### Objects 

1. <ins>user:</ins>  
{  
  &ensp;username: (String),  
  &ensp;password: (String),  
  &ensp;email: (String)  
}  
3. <ins>hamster:</ins>  
{  
  &ensp;programName: (String),  
  &ensp;program: (String),  
  &ensp;terrainName: (String),   
  &ensp;laenge: (int),  
  &ensp;breite: (int),  
  &ensp;x: (int),  
  &ensp;y: (int),  
  &ensp;cornAnzahl: (int[])  
  &ensp;corn: (int[][])
  &ensp;wall: (int[][])  
}  

### Services 
  
All Services starts with /api!

1. /user services - available from DEV
This are all Services which involves user data beginning with /user (links in brackets not made yet)
- /getAllUsers (GET)
- /logout (GET)
- /getUser (POST - byUsername)
- /createUser (PUT)
- /updateUser (POST)
- /addRole (PUT)
- /removeRole (DELETE)
2. /hamster services - for all registered user available: 
All services which handles requests containing hamstercode beginning with /hamster
- /defaultTerrain (POST)
- /existingTerrain (POST)
- /newTerrain (POST)  
3. Teacher services - available from TEACHER
Services which are available for teachers to take control over their students begin with /teacher
- /getAllStudents (GET)
- /createClass (PUT)
- /deleteClass (DELETE)
- /addStudentClass (POST)
- /removeStudentClass (DELETE)

3. /login service  
The login service; request sent as json => {"username": "username", "password": "password"}
4. /register service  
The register service; request sent as json => {"username": "username", "password": "password", "email": "email"}  

## Enable HTTPS for Spring (maybe better not to do)
source: https://www.tutorialspoint.com/spring_boot/spring_boot_enabling_https.htm

1. keytool -genkey -alias tomcat -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore keystore.p12 -validity 3650
2. configure application.properties
=> maybe better using the same ssl certificate as in nginx 
