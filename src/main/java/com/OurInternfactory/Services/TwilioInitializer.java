package com.OurInternfactory.Services;

import com.OurInternfactory.Configs.TwlioConfiguration;
import com.twilio.Twilio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioInitializer {
    private final static Logger LOGGER = LoggerFactory.getLogger(TwilioInitializer.class);
    private TwlioConfiguration twlioConfiguration;

    @Autowired
    public TwilioInitializer(TwlioConfiguration twlioConfiguration) {
        this.twlioConfiguration = twlioConfiguration;
        Twilio.init(
                twlioConfiguration.getAccountsid(),
                twlioConfiguration.getAuthtoken()
        );
        LOGGER.info("Twilio Initialised....with account sid {"+twlioConfiguration.getAccountsid()+"}", twlioConfiguration.getAccountsid());
    }
}
