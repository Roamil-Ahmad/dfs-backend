package com.dfs.backoffice.dto;

import com.dfs.backoffice.model.TblReport;
import com.dfs.backoffice.model.TblReportFilter;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReportFilterResponse {
    private TblReport tblReport;
    List<TblReportFilter> tblReportFilters;
}
