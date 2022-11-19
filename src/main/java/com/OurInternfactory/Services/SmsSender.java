package com.OurInternfactory.Services;

import com.OurInternfactory.Payloads.SmsRequest;

public interface SmsSender {
    void sendSms(SmsRequest smsRequest);
}
