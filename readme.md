# Spring Boot Mail Management with using EWS Api 

Microsoft’s Exchange Web Services (EWS) provides an Exchange email API that provides access to all of the data and functionality in Exchange mailboxes; it enables developers to parse email data, create email drafts, send emails, manage attachments, and organize an email inbox with folders. 

### Configure Mail Account
You can configure your mail account in application.properties or .env(for docker containerisation) in this project. 
* exchange.server.url=${EXCHANGE_SERVER_URL:https://outlook.office365.com/EWS/Exchange.asmx}
* exchange.server.domain=${EXCHANGE_SERVER_DOMAIN:} ``if your mail server has domain u need to enter it here. Outlook's ews doesn't need to use domain``
* exchange.mail.username=${EXCHANGE_MAIL_USERNAME:mailadress@hotmail.com} ``if your exchange server has username you need to use username instead of mail adress``
* exchange.mail.password=${EXCHANGE_MAIL_PASSWORD:????????}

### Run

* ``` mvn spring-boot:run ```

OR Run with docker containerisation.
* ``` ./build.sh ```

### API

You can reach the web services in ``http://localhost:5000/swagger-ui.html``