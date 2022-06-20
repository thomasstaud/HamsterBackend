# Backend Spring

## Info

This is the API Background where our frontend framework (= VueJS) sends request to.

## Enable HTTPS for Spring
source: https://www.tutorialspoint.com/spring_boot/spring_boot_enabling_https.htm

1. keytool -genkey -alias tomcat -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore keystore.p12 -validity 3650
2. configure application.properties
=> not working yet
