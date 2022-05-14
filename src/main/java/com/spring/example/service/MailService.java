package com.spring.example.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.PropertySet;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.BasePropertySet;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.enumeration.search.LogicalOperator;
import microsoft.exchange.webservices.data.core.enumeration.service.ConflictResolutionMode;
import microsoft.exchange.webservices.data.core.service.folder.CalendarFolder;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.core.service.item.Appointment;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.core.service.schema.EmailMessageSchema;
import microsoft.exchange.webservices.data.core.service.schema.ItemSchema;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.*;
import microsoft.exchange.webservices.data.search.CalendarView;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import microsoft.exchange.webservices.data.search.ItemView;
import microsoft.exchange.webservices.data.search.filter.SearchFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class MailService {

    ExchangeService service;

    @SneakyThrows
    public String testMailAccount(String username, String password, String domain, String serverUrl){

        if (domain == null) domain = "";
        ExchangeService exchangeService;

        exchangeService = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
        exchangeService.setUrl(new URI(serverUrl));

        ExchangeCredentials credentials = new WebCredentials(username,password,domain);

        exchangeService.setCredentials(credentials);

        Folder folder = Folder.bind(exchangeService, WellKnownFolderName.Inbox);

        return "you have " + folder.getUnreadCount() + " unread messages";

    }

    @SneakyThrows
    public List<Map> readMails(int size) {

        ItemView itemview = new ItemView(size);

        FindItemsResults<Item> results = service.findItems(WellKnownFolderName.Inbox, itemview);


        ArrayList result = new ArrayList();
        for (Item email : results) {
            Map mailInformations = new HashMap<String,String>();
            email.load();
            mailInformations.put("Subject",email.getSubject());
            mailInformations.put("Email Size",email.getSize());
            mailInformations.put("Reply Address",email.getInReplyTo());
            mailInformations.put("Date of Sent",email.getDateTimeSent());
            mailInformations.put("Date of Received",email.getDateTimeReceived());
            mailInformations.put("Is New",email.getIsNew()+"");
            mailInformations.put("Mail Importance",email.getImportance());
            mailInformations.put("Has attachment",email.getHasAttachments());
            mailInformations.put("Attachment Names ",email.getAttachments().getItems().stream().map(attachment -> attachment.getName()).collect(Collectors.joining("\n")));
            result.add(mailInformations);
        }

        return result;

    }

    @SneakyThrows
    public List<Map> readAllUnreadMails(){
        Folder folder = Folder.bind(service, WellKnownFolderName.Inbox);
        int unreadCount = folder.getUnreadCount();

        ItemView itemview = new ItemView(unreadCount);
        SearchFilter unreadFilter = new SearchFilter.SearchFilterCollection(LogicalOperator.And, new SearchFilter.IsEqualTo(EmailMessageSchema.IsRead, false));

        FindItemsResults<Item> results = service.findItems(WellKnownFolderName.Inbox, unreadFilter, itemview);

        ArrayList result = new ArrayList();
        for (Item item : results) {
            EmailMessage email = (EmailMessage) item;
            Map mailInformations = new HashMap<String,String>();
            email.load();
            mailInformations.put("Subject",email.getSubject());
            mailInformations.put("Email Size",email.getSize());
            mailInformations.put("Reply Address",email.getInReplyTo());
            mailInformations.put("Date of Sent",email.getDateTimeSent());
            mailInformations.put("Date of Received",email.getDateTimeReceived());
            mailInformations.put("Is New",email.getIsNew()+"");
            mailInformations.put("Mail Importance",email.getImportance());
            mailInformations.put("Has attachment",email.getHasAttachments());
            mailInformations.put("Attachment Names ",email.getAttachments().getItems().stream().map(attachment -> attachment.getName()).collect(Collectors.joining("\n")));

            email.setIsRead(true );
            email.update(ConflictResolutionMode.AlwaysOverwrite);
            result.add(mailInformations);
        }

        return result;
    }

    @SneakyThrows
    public void sendEmailMessage(String body, String subject, String recipient, String from){
        EmailMessage emailMessage = new EmailMessage(service);
        EmailAddress fromEmailAddress = new EmailAddress(from);
        emailMessage.setFrom(fromEmailAddress);
        emailMessage.getToRecipients().add(recipient);
        emailMessage.setSubject(subject);
        emailMessage.setBody(new MessageBody(body));
        emailMessage.getAttachments().addFileAttachment("test.txt","Test file".getBytes(StandardCharsets.UTF_8));
        emailMessage.send();
    }

}
