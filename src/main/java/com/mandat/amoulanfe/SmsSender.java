package com.mandat.amoulanfe;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.twilio.Twilio;
import com.twilio.base.ResourceSet;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;

import java.net.URI;
import java.util.Arrays;

public class SmsSender {
    // Find your Account Sid and Auth Token at twilio.com/console
    public static final String ACCOUNT_SID =
            "ACb2b09d6c13e24a012d88500ce2e00d0b";
    public static final String AUTH_TOKEN =
            "bca5981bd6a286096630939127c88e47";

    public static void main(String[] args) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        MessageCreator sms = Message
                .creator(new PhoneNumber("+33753136341"), // to
                        new PhoneNumber("+12183187152"), // from
                        "\n\n\tSalut, comment vas \n\ntu ?")
        .setMediaUrl(Arrays.asList(URI.create("https://c1.staticflickr.com/3/2899/14341091933_1e92e62d12_b.jpg")));

        Message message = sms.create();
        System.out.println("\n\t[Sid] " + message.getSid());

        //AFFICHAGE DES STATUS DES MESSAGES ENVOYÉS
        ResourceSet<Message> messages = Message.reader().read();
        for (Message m : messages) {
            System.out.println(message.getSid() + " : " + m.getStatus());
        }

        Call call = Call.creator(
                new com.twilio.type.PhoneNumber("+33753136341"),
                new com.twilio.type.PhoneNumber("+12183187152"),
                URI.create("http://demo.twilio.com/docs/voice.xml"))
                .create();

        System.out.println(call.getSid());

        //VERIFICATION DES STATUS DES MESSAGES EN MODE ASSYNCHRONE
        ListenableFuture<ResourceSet<Message>> future = Message.reader().readAsync();
        Futures.addCallback(
                future,
                new FutureCallback<>() {
                    public void onSuccess(ResourceSet<Message> messages) {
                        for (Message message : messages) {
                            System.out.println("[MODE ASSYNCHRONE] " + message.getSid() + " : " + message.getStatus());
                        }
                    }

                    public void onFailure(Throwable t) {
                        System.out.println("Failed to get message status: " + t.getMessage());
                    }
                });
    }
}
