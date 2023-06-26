#!/bin/bash
rm -rf server.keystore
#keytool -genkey -alias tomcat -keyalg RSA -keystore server.keystore -storepass 123456
keytool -genkeypair -alias tomcat_https -keypass 123456 -keyalg RSA -storetype PKCS12 -keysize 1024 -validity 365 -keystore  server.keystore -storepass 123456
#keytool -importkeystore -srckeystore server.keystore -destkeystore server.keystore -deststoretype pkcs12