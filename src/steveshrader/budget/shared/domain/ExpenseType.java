package steveshrader.budget.shared.domain;

import com.persistGwt.shared.helper.MinimalSavableImpl;
public class ExpenseType extends MinimalSavableImpl
{
  protected String name;
  public ExpenseType()
  {
    super();
  }

  public ExpenseType(String name)
  {
    this.name = name;
  }

  public String getName()
  {
    return name;
  }

}