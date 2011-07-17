package steveshrader.budget.shared.domain;

import java.util.Date;
import javax.validation.constraints.NotNull;
import steveshrader.budget.client.Budget;
import com.persistGwt.shared.helper.MinimalSavableImpl;
public class Expense extends MinimalSavableImpl
{
  @NotNull
  protected long date;
  protected long amount;
  @NotNull
  protected Vendor vendor;
  @NotNull
  protected ExpenseType expenseType;
  @NotNull
  protected PaymentType paymentType;
  public Expense()
  {
    super();
    setDate(new Date());
  }
  public void persist()
  {

  }
  public Date getDate()
  {
    return new Date(date);
  }

  public void setDate(Date date)
  {
    this.date = date.getTime();
  }

  public Long getAmount()
  {
    return amount;
  }

  public String getDisplayAmount()
  {
    return BudgetHelper.convertAmount(amount);
  }

  public void setAmount(Long amount)
  {
    this.amount = amount;
  }

  public void setDisplayAmount(String amount)
  {
    this.amount = BudgetHelper.convertAmount(amount);
  }

  public String getVendor()
  {
    if (vendor == null)
    {
      return null;
    }
    else
    {
      return vendor.getName();
    }
  }

  public void setVendor(String vendorName)
  {
    Budget budget = Budget.getInstance();
    this.vendor = budget.fieldsHelper.getVendor(vendorName);
    if (this.vendor == null)
    {
      // create
      vendor = new Vendor(vendorName);
      budget.budgetUser_.addVendor(vendor);
      budget.dataStoreGwt_.save(vendor);
      budget.dataStoreGwt_.save(budget.budgetUser_);
    }
  }

  public String getExpenseType()
  {
    if (expenseType == null)
    {
      return null;
    }
    else
    {
      return expenseType.getName();
    }
  }

  public void setExpenseType(String expenseTypeName)
  {
    Budget budget = Budget.getInstance();
    this.expenseType = budget.fieldsHelper.getExpenseType(expenseTypeName);
    if (this.expenseType == null)
    {
      // create
      expenseType = new ExpenseType(expenseTypeName);
      budget.budgetUser_.addExpenseType(expenseType);
      budget.dataStoreGwt_.save(expenseType);
      budget.dataStoreGwt_.save(budget.budgetUser_);
    }
  }

  public String getPaymentType()
  {
    if (paymentType == null)
    {
      return null;
    }
    else
    {
      return paymentType.getName();
    }
  }

  public void setPaymentType(String paymentTypeName)
  {
    Budget budget = Budget.getInstance();
    paymentType = budget.fieldsHelper.getPaymentType(paymentTypeName);
    if (this.paymentType == null)
    {
      // create
      paymentType = new PaymentType(paymentTypeName);
      budget.budgetUser_.addPaymentType(paymentType);
      budget.dataStoreGwt_.save(paymentType);
      budget.dataStoreGwt_.save(budget.budgetUser_);
    }
  }

  public ExpenseType getExpenseTypeRef()
  {
    return expenseType;
  }

  public String toString()
  {
    return super.toString() + "objectid " + objectId_;
  }
}
