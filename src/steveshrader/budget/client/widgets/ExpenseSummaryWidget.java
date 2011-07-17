package steveshrader.budget.client.widgets;

import java.util.ArrayList;

import steveshrader.budget.client.Budget;
import steveshrader.budget.client.ExpenseSummaryEntry;
import steveshrader.budget.shared.domain.BudgetHelper;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextHeader;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;

/**
 * A table with summaries of all expenses.
 */
public class ExpenseSummaryWidget extends Composite
{

  interface Binder extends UiBinder<Widget, ExpenseSummaryWidget>
  {
  }

  interface Style extends CssResource
  {
  }

  interface TableResources extends CellTable.Resources
  {
    @Source(value = { CellTable.Style.DEFAULT_CSS, "CellTableStyle.css" })
    CellTable.Style cellTableStyle();
  }

  private class ExpenseTypeColumn extends Column<ExpenseSummaryEntry, String>
  {
    public ExpenseTypeColumn()
    {
      super(new TextCell());
    }

    @Override
    public String getValue(ExpenseSummaryEntry object)
    {
      return object.getCategory();
    }
  }

  private class AmountColumn extends Column<ExpenseSummaryEntry, String>
  {
    public AmountColumn()
    {
      super(new TextCell());
    }

    @Override
    public String getValue(ExpenseSummaryEntry object)
    {
      return BudgetHelper.convertAmount(object.getAmount());
    }
  }

  @UiField
  DockLayoutPanel dock;

  @UiField(provided = true)
  CellTable<ExpenseSummaryEntry> table;

  Budget budget_ = null;

  public ExpenseSummaryWidget(Budget budget)
  {
    budget_ = budget;
    table = new CellTable<ExpenseSummaryEntry>(15, GWT.<TableResources> create(TableResources.class));
    initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
    dock.getWidgetContainerElement(table).getStyle().setProperty("overflowY", "visible");
    TextHeader header = new TextHeader("Expense Summary");
    Column<ExpenseSummaryEntry, String> expenseTypeColumn = new ExpenseTypeColumn();
    table.addColumn(expenseTypeColumn, header);
    table.setColumnWidth(expenseTypeColumn, "6ex");
    Column<ExpenseSummaryEntry, String> amountColumn = new AmountColumn();
    amountColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LOCALE_END);
    table.addColumn(amountColumn, header);
    table.setColumnWidth(amountColumn, "6ex");
    table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
  }

  public void updateGUI(ArrayList<ExpenseSummaryEntry> expenseSummaryEntryAL)
  {
    table.setRowData(expenseSummaryEntryAL);

  }

}
