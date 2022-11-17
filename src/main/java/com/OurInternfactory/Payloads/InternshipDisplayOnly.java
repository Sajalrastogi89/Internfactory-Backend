package com.OurInternfactory.Payloads;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Future;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InternshipDisplayOnly {
    private Integer id;
    private String displayName;
    private String provider;
    private String type;
    private String tenure;
    private String stipend;
    @JsonFormat(pattern="dd/MM/yyyy")
    @Future(message = "Enter valid date.")
    private Date lastDate;
    private String imageUrl;
}
