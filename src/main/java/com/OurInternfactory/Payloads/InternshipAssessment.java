package com.OurInternfactory.Payloads;

import com.OurInternfactory.Models.QuestionModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InternshipAssessment {
    InternshipsDto internshipsDto;
    List<QuestionModel> questions;
}
