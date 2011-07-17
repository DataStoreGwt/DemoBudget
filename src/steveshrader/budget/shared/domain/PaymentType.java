package steveshrader.budget.shared.domain;

import com.persistGwt.shared.helper.MinimalSavableImpl;
public class PaymentType extends MinimalSavableImpl
{
  protected String name;  
  public PaymentType()
  {
    super();
  }

  public PaymentType(String name)
  {
    this.name = name;
  }

  public String getName()
  {
    return name;
  }

}