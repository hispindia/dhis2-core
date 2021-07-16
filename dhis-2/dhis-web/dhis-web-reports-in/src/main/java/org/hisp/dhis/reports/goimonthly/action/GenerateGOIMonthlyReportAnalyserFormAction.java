package org.hisp.dhis.reports.goimonthly.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hisp.dhis.period.MonthlyPeriodType;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodService;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.periods.comparator.PeriodsComparator;
import org.hisp.dhis.reports.ReportType;

import com.opensymphony.xwork2.Action;

/**
 * @author Mithilesh Kumar Thakur
 */
public class GenerateGOIMonthlyReportAnalyserFormAction implements Action
{

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private PeriodService periodService;

    public void setPeriodService( PeriodService periodService )
    {
        this.periodService = periodService;
    }

    // -------------------------------------------------------------------------
    // Properties
    // -------------------------------------------------------------------------

    private List<Period> monthlyPeriods;

    public List<Period> getMonthlyPeriods()
    {
        return monthlyPeriods;
    }

    private SimpleDateFormat simpleDateFormat;

    public SimpleDateFormat getSimpleDateFormat()
    {
        return simpleDateFormat;
    }

    private PeriodType monthlyPeriodType;

    public PeriodType getMonthlyPeriodType()
    {
        return monthlyPeriodType;
    }
    
    private String reportTypeName;

    public String getReportTypeName()
    {
        return reportTypeName;
    }
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute() throws Exception
    {
        
        reportTypeName = ReportType.RT_GOI;
        
        /* Monthly Periods */
        monthlyPeriodType = new MonthlyPeriodType();

        monthlyPeriods = new ArrayList<Period>( periodService.getPeriodsByPeriodType( monthlyPeriodType ) );
        
        Iterator<Period> periodIterator = monthlyPeriods.iterator();
        while ( periodIterator.hasNext() )
        {
            Period p1 = periodIterator.next();

            if ( p1.getStartDate().compareTo( new Date() ) > 0 )
            {
                periodIterator.remove();
            }
        }
        
        Collections.sort( monthlyPeriods, new PeriodsComparator() );
        simpleDateFormat = new SimpleDateFormat( "MMM-yyyy" );

        return SUCCESS;
    }

}
