package steveshrader.budget.shared.domain;

import com.persistGwt.shared.helper.MinimalSavableImpl;
public class Vendor extends MinimalSavableImpl
{
  protected String name;
  protected String lastExpenseType;
  protected String lastPaymentType;
  protected int version = 0;

  public Vendor()
  {
    super();
  }

  public Vendor(String name)
  {
    this();
    this.name = name;
  }

  public Vendor(String name, String lastExpenseType, String lastPaymentType)
  {
    this(name);
    this.lastExpenseType = lastExpenseType;
    this.lastPaymentType = lastPaymentType;
  }

  public String getName()
  {
    return name;
  }

  public String getLastExpenseType()
  {
    return lastExpenseType;
  }

  public String getLastPaymentType()
  {
    return lastPaymentType;
  }

}