package com.OurInternfactory.Services;

import com.OurInternfactory.Payloads.InternshipResponse;
import com.OurInternfactory.Payloads.InternshipsDto;

import java.util.List;

public interface InternshipServices {
    InternshipsDto createInternship(InternshipsDto internshipsDto, Integer categoryId);

}
