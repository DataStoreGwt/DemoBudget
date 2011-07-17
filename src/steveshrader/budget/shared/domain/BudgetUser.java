package steveshrader.budget.shared.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import steveshrader.budget.client.ExpenseSummaryEntry;
import com.persistGwt.shared.DataStoreGwt;
import com.persistGwt.shared.helper.MinimalSavableImpl;
import com.persistGwt.shared.interfaces.Savable;
// the root object for a given user
// container for all other domain objects
public class BudgetUser extends MinimalSavableImpl 
{
  protected String nickname;
  protected String userId;
  
  public ArrayList<Savable> expenseAL_ = new ArrayList<Savable>(10);
  public ArrayList<Savable> paymentAL_ = new ArrayList<Savable>(10);
  public ArrayList<Savable> vendorAL_ = new ArrayList<Savable>(10);
  public ArrayList<Savable> expenseTypeAL_ = new ArrayList<Savable>(10);
  public ArrayList<Savable> paymentTypeAL_ = new ArrayList<Savable>(10);

  public BudgetUser()
  {
    super();
  }

  public BudgetUser(String nickname, String userId)
  {
    this.nickname = nickname;
    this.userId = userId;
  }

  public String getNickname()
  {
    return nickname;
  }

  public String getUserId()
  {
    return userId;
  }

  public void addExpense(Expense expense)
  {
    if (expenseAL_.contains(expense))
    {
      // dupliate. ignore
    }
    else
    {
      expenseAL_.add(expense);
    }
  }

  public void deleteExpense(Expense expense)
  {
    expenseAL_.remove(expense);
  }

  public void addVendor(Vendor vendor)
  {
    vendorAL_.add(vendor);
  }

  public void addExpenseType(ExpenseType expenseType)
  {
    expenseTypeAL_.add(expenseType);
  }

  public void addPaymentType(PaymentType paymentType)
  {
    paymentTypeAL_.add(paymentType);
  }

  public ArrayList<ExpenseSummaryEntry> getExpenseSummaryEntryAL(ArrayList<Expense> expenseAL)
  {
    ArrayList<ExpenseSummaryEntry> result = new ArrayList<ExpenseSummaryEntry>(10);
    HashMap<ExpenseType, Long> summaryHM = new HashMap<ExpenseType, Long>();
    for (Expense expense : expenseAL)
    {
      Long amt = summaryHM.get(expense.getExpenseTypeRef());
      if (amt == null)
      {
        summaryHM.put(expense.getExpenseTypeRef(), expense.getAmount());
      }
      else
      {
        amt = amt + expense.getAmount();
        summaryHM.put(expense.getExpenseTypeRef(), amt);
      }
    }
    for (ExpenseType e : summaryHM.keySet())
    {
      ExpenseSummaryEntry ese = new ExpenseSummaryEntry(e.getName(), summaryHM.get(e));
      result.add(ese);
    }
    return result;
  }

  public ArrayList<Expense> getFilteredExpense(Date start, Date end, Vendor vendor)
  {
    ArrayList<Expense> result = new ArrayList<Expense>(10);
    for (Savable s : expenseAL_)
    {
      Expense e = (Expense) s;
      if ((vendor == null || e.vendor == vendor) && e.getDate().after(start) && e.getDate().before(end))
      {
        result.add(e);
      }
    }
    return result;
  }

}