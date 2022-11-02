package com.OurInternfactory.Payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter


public class CVGenerator {
    private String name;
    private String skill1;

    private String skill2;
    private String skill3;

    private String project1Heading;
    private String project1Description;
    private String project2Heading;
    private String project2Description;

    private String experience1;
    private String experience1role;
    private String experience2;
    private String experience2role;

    private String award1;
    private String award2;

    private String universityMarks;
    private String twelth_marks;
    private String tenth_marks;

}
