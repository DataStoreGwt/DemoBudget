package steveshrader.budget.client;

import steveshrader.budget.client.widgets.ExpenseFilterWidget;
import steveshrader.budget.client.widgets.ExpenseListWidget;
import steveshrader.budget.client.widgets.ExpenseSummaryWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
/**
 * The main layout for viewing and adding expenses
 */
public class ExpensesLayout extends Composite
{
  interface Binder extends UiBinder<Widget, ExpensesLayout>
  {
  }

  @UiField(provided = true)
  ExpenseListWidget calendar;

  @UiField(provided = true)
  ExpenseSummaryWidget summary;

  @UiField(provided = true)
  ExpenseFilterWidget filter;

  @UiConstructor
  public ExpensesLayout(Budget budget)
  {
    calendar = new ExpenseListWidget(budget);
    summary = new ExpenseSummaryWidget(budget);
    filter = new ExpenseFilterWidget(budget);
    initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
  }
}
