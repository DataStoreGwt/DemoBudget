package steveshrader.budget.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import steveshrader.budget.shared.domain.Expense;
import com.google.gwt.user.datepicker.client.CalendarUtil;

/**
 * An event to indicate a request for an expense chart. Based on Google Chart
 * API
 */
public class ChartDisplayData
{
  private final List<Expense> expenses;
  private Date startDate;
  private Date endDate;
  private String chdLineChartTotal = ""; // so the visual isn't skewed create a
  // Line Chart with just the Total
  private String chdsLineChartTotal = ""; // scale by the range in data
  private String chdLineChartDetail = ""; // and create a line chart with all
  // the different expense types
  private String chdsLineChartDetail = ""; // scale by the range in data
  private String chdlLineChartDetail = "";
  private String chdPieChart = "";
  private String chdsPieChart = "";
  private String chdlPieChart = "";
  private String chxlLineChartTotal = "";
  private String chxpLineChartTotal = "";
  private String chxlLineChartDetail = "";
  private String chxpLineChartDetail = "";
  private String chxrLineChartTotal = "";
  private String chxrLineChartDetail = "";

  private static final String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October",
      "November", "December" };
  private static final String TOTAL_LABEL = "Total";

  public ChartDisplayData(List<Expense> expenses, Date startDate, Date endDate)
  {
    this.expenses = expenses;
    this.startDate = startDate;
    this.endDate = endDate;
    calculateChartParameters();
  }

  /**
   * Calculate the Chart parameters required by Google Chart API
   */
  private void calculateChartParameters()
  {
    ChartData chartData = new ChartData(expenses);

    StringBuffer xLabel_0 = new StringBuffer("0:"); // chxl - Month name will
    // appear on x-axis 0
    StringBuffer xLabel_1 = new StringBuffer("1:"); // chxl - Year will appear
    // on x-axis 1
    StringBuffer xLabelPosition_0 = new StringBuffer("0"); // chxp - Where to
    // position the label
    // on the x-axis 0
    StringBuffer xLabelPosition_1 = new StringBuffer("1"); // chxp - Where to
    // position the label
    // on the x-axis 1
    Map<String, String> chartDataSets = new HashMap<String, String>(); // mapped expenseType->Google Chart chd          
    Long maxLineChartTotalAmount = 0L;
    Long minLineChartTotalAmount = 1000000L;
    Long maxLineChartDetailAmount = 0L;
    Long minLineChartDetailAmount = 1000000L;
    Long maxPieChartAmount = 0L;
    Date begin = new Date(startDate.getTime());
    Date stop = new Date(endDate.getTime());
    int numberOfMonths = 0; // start at 0 but increment at top of loop so it
    // ends with accurate count of months

    while (begin.getTime() <= stop.getTime())
    { // loop through each month of the request
      numberOfMonths++;
      @SuppressWarnings("deprecation")
      String month = months[begin.getMonth()];
      @SuppressWarnings("deprecation")
      String year = "" + (begin.getYear() + 1900);
      xLabel_0.append("|").append(month);
      xLabel_1.append("|").append(year);
      xLabelPosition_0.append(",").append(numberOfMonths);
      xLabelPosition_1.append(",").append(numberOfMonths);
      // update the data set with current month data
      for (String expenseType : chartData.getExpenseTypes())
      {
        Long amount = chartData.getAmount(year, month, expenseType);
        String dataSet = chartDataSets.get(expenseType);
        if (dataSet == null)
        {
          dataSet = "" + amount;
        }
        else
        {
          dataSet = dataSet + "," + amount;
        }
        chartDataSets.put(expenseType, dataSet);

        // keep track of the max and min Total of all months as well as the max
        // and min for any expense type
        if (TOTAL_LABEL.equals(expenseType))
        {
          if (amount > maxLineChartTotalAmount)
          {
            maxLineChartTotalAmount = amount;
          }
          if (amount < minLineChartTotalAmount)
          {
            minLineChartTotalAmount = amount;
          }
        }
        else
        {
          if (amount > maxLineChartDetailAmount)
          {
            maxLineChartDetailAmount = amount;
          }
          if (amount < minLineChartDetailAmount)
          {
            minLineChartDetailAmount = amount;
          }
        }
      }

      CalendarUtil.addMonthsToDate(begin, 1);
    }
    // combine the pieces into the full info needed by Google Chart API
    Long average = chartData.getGrandTotal() / numberOfMonths;
    chxlLineChartDetail = xLabel_0.append("|").append(xLabel_1).toString();
    chxpLineChartDetail = xLabelPosition_0.append("|").append(xLabelPosition_1).toString();
    chxlLineChartTotal = chxlLineChartDetail + "|3:|avg+$" + average; // add average and amount label on right      
    chxpLineChartTotal = chxpLineChartDetail + "|3," + average;
    chdsLineChartTotal = minLineChartTotalAmount + "," + maxLineChartTotalAmount;
    chdsLineChartDetail = minLineChartDetailAmount + "," + maxLineChartDetailAmount;
    // create the data string for Total
    chdLineChartTotal = "t:" + chartDataSets.get(TOTAL_LABEL);
    List<String> expenseTypes = chartData.getExpenseTypes();
    expenseTypes.remove(TOTAL_LABEL);
    for (String expenseType : expenseTypes)
    {
      if (chdLineChartDetail.length() > 0)
      {
        chdLineChartDetail = chdLineChartDetail + "|";
      }
      chdLineChartDetail = chdLineChartDetail + chartDataSets.get(expenseType);

      if (chdlLineChartDetail.length() > 0)
      {
        chdlLineChartDetail = chdlLineChartDetail + "|";
      }
      chdlLineChartDetail = chdlLineChartDetail + expenseType;
    }
    chdLineChartDetail = "t:" + chdLineChartDetail;
    // the range format the x, x2, y and right axis of the Total line chart
    chxrLineChartTotal = "0,1," + numberOfMonths + "|1,1," + numberOfMonths + "|2," + minLineChartTotalAmount + "," + maxLineChartTotalAmount + "|3,"
        + minLineChartTotalAmount + "," + maxLineChartTotalAmount;
    // the range format the x, x2, y and right axis of the Detail line chart
    chxrLineChartDetail = "0,1," + numberOfMonths + "|1,1," + numberOfMonths + "|2," + minLineChartDetailAmount + "," + maxLineChartDetailAmount
        + "|3," + minLineChartDetailAmount + "," + maxLineChartDetailAmount;
    // compute some added info for the Pie Chart
    chdPieChart = "";
    chdlPieChart = "";
    Map<String, Long> expenseTypeTotals = chartData.getExpenseTypeTotals();
    for (String expenseType : expenseTypeTotals.keySet())
    {
      if (chdPieChart.length() > 0)
      {
        chdPieChart = chdPieChart + ",";
      }
      Long pieChartAmount = expenseTypeTotals.get(expenseType) / 100; 
      if (pieChartAmount > maxPieChartAmount)
      {
        maxPieChartAmount = pieChartAmount;
      }
      chdPieChart = chdPieChart + pieChartAmount;
      if (chdlPieChart.length() > 0)
      {
        chdlPieChart = chdlPieChart + "|";
      }
      chdlPieChart = chdlPieChart + expenseType + "+$" + pieChartAmount; 
    }
    chdPieChart = "t:" + chdPieChart;
    chdsPieChart = "0," + maxPieChartAmount;
  }

  /**
   * encapsulate the Expense data in the most usable way for Google Chart API
   */
  private class ChartData
  {
    // map expenses by Year->Month->ExpenseType->Amount
    Map<String, Map<String, Map<String, Long>>> chartDataMap = new HashMap<String, Map<String, Map<String, Long>>>();

    // also keep track of each unique ExpenseType to use for the label on the
    // bottom of the chart...Total will be added separate after the sort
    List<String> expenseTypes = new ArrayList<String>();
    // and compute overall totals for each ExpenseType
    Map<String, Long> expenseTypeTotals = new HashMap<String, Long>();
    Long grandTotal = 0L; // sum for every expense in List

    public ChartData(List<Expense> expenses)
    {
      Set<String> expenseTypeSet = new HashSet<String>();
      if (expenses != null)
      {
        for (Expense expense : expenses)
        { // loop through every expense and put it in the correct spot
          String expenseType = expense.getExpenseType();
          long expenseAmount = expense.getAmount() == null ? 0 : expense.getAmount();
          Date date = expense.getDate();
          @SuppressWarnings("deprecation")
          String year = "" + (date.getYear() + 1900);
          @SuppressWarnings("deprecation")
          String month = months[date.getMonth()];

          expenseTypeSet.add(expenseType); // keep track of unique expense types
          Map<String, Map<String, Long>> months = chartDataMap.get(year); 
          if (months == null)
          {
            months = new HashMap<String, Map<String, Long>>(); // create a new month Map if this is the first expense for the current yerar
          }

          Map<String, Long> monthlyExpenseTypes = months.get(month); // for the month of the current expense get all the expense types
          if (monthlyExpenseTypes == null)
          {
            monthlyExpenseTypes = new HashMap<String, Long>(); // create a new expense type map if this is first expense for the current month               
          }

          long monthlyExpenseTypeTotal = monthlyExpenseTypes.get(expenseType) == null ? 0 : monthlyExpenseTypes.get(expenseType);
          long monthTotal = monthlyExpenseTypes.get(TOTAL_LABEL) == null ? 0 : monthlyExpenseTypes.get(TOTAL_LABEL);

          monthlyExpenseTypeTotal = monthlyExpenseTypeTotal + expenseAmount; // increment amount the for year->month->expenseType for the current expense
         
          monthTotal = monthTotal + expenseAmount; // increment the total for the year->month for the current expense
               
          grandTotal = grandTotal + expenseAmount; // increment the total of all expenses
          monthlyExpenseTypes.put(expenseType, monthlyExpenseTypeTotal); // update
          // expenseType->amount
          monthlyExpenseTypes.put(TOTAL_LABEL, monthTotal); // update
          // total->amount
          months.put(month, monthlyExpenseTypes); // update month->expenseTypes
          chartDataMap.put(year, months); // update year->months

          // also keep track of overall totals for each ExpenseType
          long overallExpenseTypeTotal = expenseTypeTotals.get(expenseType) == null ? 0 : expenseTypeTotals.get(expenseType);
          overallExpenseTypeTotal = overallExpenseTypeTotal + expenseAmount;
          expenseTypeTotals.put(expenseType, overallExpenseTypeTotal);
        }
      }

      // put the set of unique ExpenseTypes into an alphabetically sorted List
      // and then put the Total at the top of the list
      expenseTypes = new ArrayList<String>(expenseTypeSet);
      Collections.sort(expenseTypes);
      expenseTypes.add(0, TOTAL_LABEL);
    }

    /**
     * @return the total of all expenses found
     */
    public Long getGrandTotal()
    {
      return grandTotal / 100; // convert from pennies to dollars
    }

    /**
     * @return Alphabetically sorted List with "Total" at top
     */
    public List<String> getExpenseTypes()
    {
      return expenseTypes;
    }

    /**
     * @return a Map of ExpenseTypes and the totaled amount for each
     */
    public Map<String, Long> getExpenseTypeTotals()
    {
      return expenseTypeTotals;
    }

    /**
     * @return the amount for an expenseType in a specific year and month
     */
    public Long getAmount(String year, String month, String expenseType)
    {
      Long amount = 0L;

      Map<String, Map<String, Long>> yearMap = chartDataMap.get(year);
      if (yearMap != null)
      {
        Map<String, Long> monthMap = yearMap.get(month);
        if (monthMap != null)
        {
          amount = monthMap.get(expenseType);
          if (amount == null)
          {
            amount = 0L;
          }
          else
          {// convert from pennies to dollars
            return amount / 100;
          }
        }
      }

      return amount;
    }
  }

  // return the Google Chart API specific data
  public String chdLineChartTotal()
  {
    return chdLineChartTotal;
  }

  public String chdsLineChartTotal()
  {
    return chdsLineChartTotal;
  }

  public String chxlLineChartTotal()
  {
    return chxlLineChartTotal;
  }

  public String chxpLineChartTotal()
  {
    return chxpLineChartTotal;
  }

  public String chxrLineChartTotal()
  {
    return chxrLineChartTotal;
  }

  public String chdLineChartDetail()
  {
    return chdLineChartDetail;
  }

  public String chdsLineChartDetail()
  {
    return chdsLineChartDetail;
  }

  public String chxlLineChartDetail()
  {
    return chxlLineChartDetail;
  }

  public String chxpLineChartDetail()
  {
    return chxpLineChartDetail;
  }

  public String chxrLineChartDetail()
  {
    return chxrLineChartDetail;
  }

  public String chdlLineChartDetail()
  {
    return chdlLineChartDetail;
  }

  public String chdPieChart()
  {
    return chdPieChart;
  }

  public String chdsPieChart()
  {
    return chdsPieChart;
  }

  public String chdlPieChart()
  {
    return chdlPieChart;
  }

}
