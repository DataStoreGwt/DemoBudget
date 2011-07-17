package steveshrader.budget.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import steveshrader.budget.shared.domain.BudgetUser;
import steveshrader.budget.shared.domain.Expense;
import steveshrader.budget.shared.domain.ExpenseType;
import steveshrader.budget.shared.domain.PaymentType;
import steveshrader.budget.shared.domain.Vendor;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.requestfactory.shared.Receiver;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.datepicker.client.DateBox;
import com.persistGwt.shared.interfaces.Savable;


/**
 * Contains Helper methods used for UI Fields
 * @author steve.shrader
 */
public class FieldsHelper {
	
	public static final DateTimeFormat DATE_MONTH_FORMAT = DateTimeFormat.getFormat("MMMM yyyy");
	public static final DateBox.DefaultFormat DATE_BOX_CHART_FORMAT = new DateBox.DefaultFormat(DATE_MONTH_FORMAT);    //apply default format to DateBox
    public static final DateTimeFormat DATE_TIME_FORMAT = DateTimeFormat.getFormat("MM/dd/yyyy");
    public static final DateBox.DefaultFormat DATE_BOX_FORMAT = new DateBox.DefaultFormat(DATE_TIME_FORMAT);    //apply default format to DateBox
    private MultiWordSuggestOracle vendorOracle = new MultiWordSuggestOracle();     //list Vendor names for selection
    private MultiWordSuggestOracle expenseTypeOracle = new MultiWordSuggestOracle();     //list ExpenseType names for selection
    private MultiWordSuggestOracle paymentTypeOracle = new MultiWordSuggestOracle();     //list PaymentType names for selection
    private Map<String, Vendor> vendors = new HashMap<String, Vendor>();    //all Vendors in the DB to select from...mapped as vendorName->Vendor
    private Map<String, PaymentType> paymentTypes = new HashMap<String, PaymentType>();    //all Vendors in the DB to select from...mapped as vendorName->Vendor
    private Map<String, ExpenseType> expenseTypes = new HashMap<String, ExpenseType>();    //all Vendors
       
    public FieldsHelper( Budget budget )
    {    	 
    	for( Savable s : budget.budgetUser_.vendorAL_ )
    	{
    		String vendorName = ((Vendor)s).getName() ;
    		vendorOracle.add(vendorName) ;
    		vendors.put(vendorName, ((Vendor)s));
    	}
    	
    	for( Savable s : budget.budgetUser_.expenseTypeAL_ )
    	{
    		String expenseName = ((ExpenseType)s).getName() ;
    		expenseTypeOracle.add(expenseName) ;
    		expenseTypes.put(expenseName, ((ExpenseType)s));
    		 
    	}
    	
    	for( Savable s : budget.budgetUser_.paymentTypeAL_ )
    	{
    		String paymentTypeName = ((PaymentType)s).getName() ;
    		paymentTypeOracle.add(paymentTypeName) ;
    		paymentTypes.put(paymentTypeName, ((PaymentType)s));
    		 
    	}
    	
    }
     
    
    public MultiWordSuggestOracle getVendorOracle() {
    	return vendorOracle;
    }
    
    public MultiWordSuggestOracle getExpenseTypeOracle() {
    	return expenseTypeOracle;
    }
    
    public MultiWordSuggestOracle getPaymentTypeOracle() {
    	return paymentTypeOracle;
    }
    
    /**
     * Try to determine the ExpenseType for a Vendor by using
     * the last one saved by the user.
     */
    public String getMostLikelyExpenseTypeForVendor(String vendorName) {
    	String expenseType = "";
    	Vendor vendor = vendors.get(vendorName);
    	if(vendor != null) {
    		expenseType = vendor.getLastExpenseType();
    	}
    	return expenseType;
    }
    
    /**
     * Try to determine the PaymentType for a Vendor by using
     * the last one saved by the user.
     */
    public String getMostLikelyPaymentTypeForVendor(String vendorName) {
    	String paymentType = "";
    	Vendor vendor = vendors.get(vendorName);
    	if(vendor != null) {
    		paymentType = vendor.getLastPaymentType();
    	}
    	return paymentType;
    }
    
    public Vendor getVendor( String vendorName)
    {
    	return vendors.get(vendorName) ;
    }
    
    public ExpenseType getExpenseType( String expenseTypeName)
    {
    	return expenseTypes.get(expenseTypeName) ;
    }
    public PaymentType getPaymentType( String paymentTypeName)
    {
    	return paymentTypes.get(paymentTypeName) ;
    }
}
