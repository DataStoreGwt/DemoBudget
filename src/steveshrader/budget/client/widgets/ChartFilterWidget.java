package steveshrader.budget.client.widgets;

import java.util.Date;
import java.util.List;

import steveshrader.budget.client.Budget;
import steveshrader.budget.client.FieldsHelper;
 
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.requestfactory.shared.Receiver;
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
 * A UI Widget that allows a user to filter the expenses being charted by the Google Chart API
 */
public class ChartFilterWidget extends Composite {

  interface Binder extends UiBinder<Widget, ChartFilterWidget> {
  };

  @UiField
  DateBox startDate;
  @UiField
  DateBox endDate;
  @UiField(provided = true)
  SuggestBox vendor;
  @UiField
  Button chart;
  @UiField
  Button clear;
  
  Budget budget_ = null ;

   
 
  
  @UiConstructor
  public ChartFilterWidget( Budget budget) {
    
	 budget_ = budget ;
    //setup suggest box for vendor
    vendor = new SuggestBox(budget.fieldsHelper.getVendorOracle());
    
    initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
    
    //setup default date info...
    startDate.setFormat(FieldsHelper.DATE_BOX_CHART_FORMAT);
    endDate.setFormat(FieldsHelper.DATE_BOX_CHART_FORMAT);    
    
    defaultFieldValues();
  }

  @UiHandler("chart")
  public void handleRefreshClick(ClickEvent e) 
  {
    budget_.updateGUIForChartLayout(startDate.getValue(), endDate.getValue(), budget_.fieldsHelper.getVendor(vendor.getText()));
  }

  @UiHandler("clear")
  public void handleClearClick(ClickEvent e) {
	  
	  defaultFieldValues();

  }

  private void defaultFieldValues() {
	  Date date1 = new Date();
	  CalendarUtil.addMonthsToDate(date1, -12);
	  CalendarUtil.setToFirstDayOfMonth(date1);        //find the 1st day of the current month -1 year
	  startDate.setValue(date1);    					//default to 1st day of the current month
	  
	  Date date2 = new Date();
	  CalendarUtil.addMonthsToDate(date2, 1);          //find the the next month
	  CalendarUtil.setToFirstDayOfMonth(date2);			//the 1st of that month
	  CalendarUtil.addDaysToDate(date2, -1);           //go back one day to the last day of the current month
	  endDate.setValue(date2);              			//default to the last day of the current month
	  
	  vendor.setValue(null);  
  }
  
  private void getExpenses() {
	   
  }
  
  
}
