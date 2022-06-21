# Backend Spring

## Info

This is the API Background where our frontend framework (= VueJS) sends request to.

## The commands 

- vor(); 
- linksUm(); 
- nimm(); 
- gib(); 
- vornFrei(); 
- kornDa(); 
- maulLeer();

## Rest Services

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

1. /user servives
This are all Services which involves user data (links in brackets not made yet)
- /getAllUsers (GET)
- (/getUser/<int:id> (GET))
- /logout (GET)
- /createUser (POST)
2. /hamster services 
All services which handles requests containing hamstercode
- /defaultTerrain (POST)
- /existingTerrain (POST)
- /newTerrain (POST)


3. /login service
The login service; request sent as json => {username: "<username>", password: "<password>"}
4. /register service
The register service; request sent as json => {}
  
## Enable HTTPS for Spring (maybe better not to do)
source: https://www.tutorialspoint.com/spring_boot/spring_boot_enabling_https.htm

1. keytool -genkey -alias tomcat -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore keystore.p12 -validity 3650
2. configure application.properties
=> maybe better using the same ssl certificate as in nginx 
