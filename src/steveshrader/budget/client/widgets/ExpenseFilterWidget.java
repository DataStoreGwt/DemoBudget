package steveshrader.budget.client.widgets;

import java.util.Date;
import java.util.List;

import steveshrader.budget.client.Budget;
import steveshrader.budget.client.FieldsHelper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.google.gwt.user.datepicker.client.DateBox;
/**
 * A UI Widget that allows a user to filter the expenses being displayed in the
 * dynamic table.
 */
public class ExpenseFilterWidget extends Composite
{

  interface Binder extends UiBinder<Widget, ExpenseFilterWidget>
  {
  };

  @UiField
  DateBox startDate;
  @UiField
  DateBox endDate;
  @UiField(provided = true)
  SuggestBox vendor;
  @UiField
  Button refresh;
  @UiField
  Button clear;

  Budget budget_ = null;

  @UiConstructor
  public ExpenseFilterWidget(Budget budget)
  {

    budget_ = budget;
    // setup suggest box for vendor
    vendor = new SuggestBox(budget_.fieldsHelper.getVendorOracle());

    initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));

    // setup default date info...
    startDate.setFormat(FieldsHelper.DATE_BOX_FORMAT);
    endDate.setFormat(FieldsHelper.DATE_BOX_FORMAT);
    defaultFieldValues();
  }

  @UiHandler("refresh")
  public void handleRefreshClick(ClickEvent e)
  {
    final Date filterStartDate = startDate.getValue();
    CalendarUtil.setToFirstDayOfMonth(filterStartDate);
    final Date filterEndDate = endDate.getValue();
    CalendarUtil.addMonthsToDate(filterEndDate, 1);          //find the 1st day of the next month
    CalendarUtil.setToFirstDayOfMonth(filterEndDate);    //the 1st of that month
    CalendarUtil.addDaysToDate(filterEndDate, -1);           //go back one day to the last day of the current month
    budget_.updateGUI(filterStartDate , filterEndDate , budget_.fieldsHelper.getVendor(vendor.getText())) ;
   
  }

  @UiHandler("clear")
  public void handleClearClick(ClickEvent e)
  {
    defaultFieldValues();
  }

  private void defaultFieldValues()
  {
    Date date = new Date();
    CalendarUtil.setToFirstDayOfMonth(date); // find the 1st day of the current month                                            
    startDate.setValue(date); // default to 1st day of the current month
    CalendarUtil.addMonthsToDate(date, 1); // find the 1st day of the next month
    CalendarUtil.addDaysToDate(date, -1); // go back one day to the last day of the current month                                         
    endDate.setValue(date); // default to the last day of the current month
    vendor.setValue(null);
  }

}
