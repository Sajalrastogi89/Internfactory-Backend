package com.OurInternfactory.Payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter@Setter@AllArgsConstructor@NoArgsConstructor
public class InternshipResponse {
    private List<InternshipsDto> content;
    private int pageNumber;
    private int pageSize;
    private int totalPage;
    private long totalElements;
    private boolean lastPage;
}
