package steveshrader.budget.shared.domain;
/**
 * Contains Helper methods used throughout the code
 * 
 * @author steve.shrader
 */
public class BudgetHelper
{
  /**
   * When the amount is stored as a long it is in pennies...so $10.25 is stored
   * as 1025 pennies. This method will convert 1025 into the string 10.25
   * 
   * @param amount
   * @return
   */
  public static String convertAmount(Long amount)
  {
    String displayAmount = "0.00";
    if (amount != null)
    {
      displayAmount = amount.toString();
      if (displayAmount.length() > 2)
      {
        displayAmount = displayAmount.substring(0, displayAmount.length() - 2) + "."
            + displayAmount.substring(displayAmount.length() - 2, displayAmount.length());
      }
      else
      {
        displayAmount = "0." + displayAmount;
      }
    }
    return displayAmount;
  }

  /**
   * When the amount is displayed as a string it is 10.25 but when stored as a
   * long it needs to be converted to pennies as 1025
   * 
   * @param amount
   * @return
   */
  public static Long convertAmount(String displayAmount)
  {
    Long amount = 0L;
    if (displayAmount != null && displayAmount.matches("^\\d+.\\d\\d$"))
    {
      displayAmount = displayAmount.substring(0, displayAmount.length() - 3)
          + displayAmount.substring(displayAmount.length() - 2, displayAmount.length());
      amount = new Long(displayAmount);
    }
    return amount;
  }
}
