package steveshrader.budget.client.widgets;

import java.util.ArrayList;
import java.util.Date;
import steveshrader.budget.client.Budget;
import steveshrader.budget.client.ExpenseEditorWorkflow;
import steveshrader.budget.client.FieldsHelper;
import steveshrader.budget.shared.domain.BudgetHelper;
import steveshrader.budget.shared.domain.Expense;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SafeHtmlHeader;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * A paging table with summaries of all expenses.
 */
public class ExpenseListWidget extends Composite
{

  interface Binder extends UiBinder<Widget, ExpenseListWidget>
  {
  }

  private static final Binder BINDER = GWT.create(Binder.class);

  interface Style extends CssResource
  {
  }

  Budget budget_ = null;

  interface TableResources extends CellTable.Resources
  {
    @Source(value = { CellTable.Style.DEFAULT_CSS, "CellTableStyle.css" })
    CellTable.Style cellTableStyle();
  }

  public ExpenseListWidget(Budget budget)
  {
    super();
    budget_ = budget;
    init();
  }

  private class DateColumn extends Column<Expense, Date>
  {
    public DateColumn()
    {
      super(new DateCell(FieldsHelper.DATE_TIME_FORMAT));
    }

    @Override
    public Date getValue(Expense object)
    {
      return object.getDate();
    }
  }

  private class AmountColumn extends Column<Expense, String>
  {
    public AmountColumn()
    {
      super(new TextCell());
    }

    @Override
    public String getValue(Expense object)
    {
      return BudgetHelper.convertAmount(object.getAmount());
    }
  }

  private class VendorColumn extends Column<Expense, String>
  {
    public VendorColumn()
    {
      super(new TextCell());
    }

    @Override
    public String getValue(Expense object)
    {
      return object.getVendor();
    }
  }

  private class ExpenseTypeColumn extends Column<Expense, String>
  {
    public ExpenseTypeColumn()
    {
      super(new TextCell());
    }

    @Override
    public String getValue(Expense object)
    {
      return object.getExpenseType();
    }
  }

  private class PaymentTypeColumn extends Column<Expense, String>
  {
    public PaymentTypeColumn()
    {
      super(new TextCell());
    }

    @Override
    public String getValue(Expense object)
    {
      return object.getPaymentType();
    }
  }

  @UiField
  DockLayoutPanel dock;

  @UiField(provided = true)
  CellTable<Expense> table;

  private final SingleSelectionModel<Expense> selectionModel = new SingleSelectionModel<Expense>();

  public void init()
  {
    table = new CellTable<Expense>(15, GWT.<TableResources> create(TableResources.class));
    initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
    dock.getWidgetContainerElement(table).getStyle().setProperty("overflowY", "visible");

    Column<Expense, Date> dateColumn = new DateColumn();
    table.addColumn(dateColumn, "Date");
    table.setColumnWidth(dateColumn, "15ex");
    Column<Expense, String> amountColumn = new AmountColumn();
    amountColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LOCALE_END);
    table.addColumn(amountColumn, new SafeHtmlHeader(new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(
        "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Amount"))); // cannot
    // right align header on currency column so use spaces to force it right
    table.setColumnWidth(amountColumn, "25ex");
    Column<Expense, String> vendorColumn = new VendorColumn();
    table.addColumn(vendorColumn, "Vendor");
    table.setColumnWidth(vendorColumn,"25ex");
    Column<Expense, String> expenseTypeColumn = new ExpenseTypeColumn();
    table.addColumn(expenseTypeColumn, "Expense Type");
    table.setColumnWidth(expenseTypeColumn, "25ex");
    Column<Expense, String> paymentTypeColumn = new PaymentTypeColumn();
    table.addColumn(paymentTypeColumn, "Payment Type");
    table.setColumnWidth(paymentTypeColumn, "25ex");
    table.setSelectionModel(selectionModel);
    table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler()
      {
        public void onSelectionChange(SelectionChangeEvent event)
        {
          ExpenseListWidget.this.refreshSelection();
        }
      });
  }

  @UiHandler("create")
  void onCreate(ClickEvent event)
  {
    Expense expense = new Expense();
    new ExpenseEditorWorkflow(budget_, expense).edit(expense);
  }

  void refreshSelection()
  {
    Expense expense = selectionModel.getSelectedObject();
    if (expense == null)
    {
      return;
    }
    else
    {
      new ExpenseEditorWorkflow(budget_, expense).edit(expense);
    }
    selectionModel.setSelected(expense, false);
  }

  public void updateGUI(ArrayList<Expense> expenseAL)
  {
    table.setRowData(expenseAL);
  }

}
