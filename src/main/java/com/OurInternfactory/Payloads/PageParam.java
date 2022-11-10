package com.OurInternfactory.Payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageParam {
    private Integer pageNumber;
    private Integer pageSize;
    private String sortBy;
    private String sortDir;
}
