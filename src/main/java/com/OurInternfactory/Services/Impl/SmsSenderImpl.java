package com.OurInternfactory.Services.Impl;

import com.OurInternfactory.Configs.TwlioConfiguration;
import com.OurInternfactory.Payloads.SmsRequest;
import com.OurInternfactory.Services.SmsSender;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("twilio")
public class SmsSenderImpl implements SmsSender {
    private static Logger LOGGER = LoggerFactory.getLogger(SmsSenderImpl.class);
    private final TwlioConfiguration twlioConfiguration;

    @Autowired
    public SmsSenderImpl(TwlioConfiguration twlioConfiguration) {
        this.twlioConfiguration = twlioConfiguration;
    }

    @Override
    public void sendSms(SmsRequest smsRequest) {
                PhoneNumber to = new PhoneNumber(smsRequest.getPhoneNumber());
                PhoneNumber from = new PhoneNumber(twlioConfiguration.getTrialnumber());
                String message = smsRequest.getMessage();
        MessageCreator creator = Message.creator(to, from, message);
        creator.create();
        LOGGER.info("Send sms {}"+ smsRequest);
    }
}
