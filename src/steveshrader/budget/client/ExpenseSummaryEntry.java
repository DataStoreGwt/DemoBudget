package steveshrader.budget.client;

// aids building gui   
public class ExpenseSummaryEntry
{
  String category;
  Long amount;

  public ExpenseSummaryEntry(String category, Long amount)
  {
    this.category = category;
    this.amount = amount;
  }

  public String getCategory()
  {
    return category;
  }

  public Long getAmount()
  {
    return amount;
  }

}
