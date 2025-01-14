package com.littlepig.controller.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class PageResponseAbstract {
    public int pageNo;
    public int pageSize;
    public long totalElements;
    public long totalPages;
}
