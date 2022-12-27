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
This are all Services which involves user data beginning with /user (services in brackets are coming soon!)
- /users (GET, POST, PUT, DELETE)
- /logout (GET)
- /roles (POST, DELETE)
2. /hamster services - for all registered user available: 
All services which handles requests containing hamstercode beginning with /hamster
- /defaultTerrain (POST)
- /existingTerrain (POST)
- /newTerrain (POST)  
3. Teacher services - available from TEACHER
Services which are available for teachers to take control over their students begin with /teacher
- /students (GET)
- /course (GET, POST, DELETE)
- /course/students (GET, POST, DELETE)
- /course/teachers (GET)
- /exercises (GET, DELETE, POST, PUT)
- (/rateExercises)
3. /login service  
The login service; request sent as json => {"username": "username", "password": "password"}
4. /register service  
The register service; request sent as json => {"username": "username", "password": "password", "email": "email"}  

### Test
To check if you installed everything correctly, I provided you with JUnit tests, which you can find in src/test/java/io/github/Hattinger04/