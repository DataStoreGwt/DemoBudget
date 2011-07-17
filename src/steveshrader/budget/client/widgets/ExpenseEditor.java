package steveshrader.budget.client.widgets;

import steveshrader.budget.client.Budget;
import steveshrader.budget.client.FieldsHelper;
import steveshrader.budget.shared.domain.Expense;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.datepicker.client.DateBox;

/**
 * Edits Expense.
 */
public class ExpenseEditor extends Composite implements Editor<Expense>
{
  interface Binder extends UiBinder<Widget, ExpenseEditor>
  {
  }

  @UiField
  DateBox date;

  @UiField
  TextBox displayAmount;

  @UiField(provided = true)
  SuggestBox vendor;

  @UiField(provided = true)
  SuggestBox expenseType;

  @UiField(provided = true)
  SuggestBox paymentType;

  private final Budget budget_;

  public ExpenseEditor(Budget budget)
  {
    budget_ = budget;
    vendor = new SuggestBox(budget_.fieldsHelper.getVendorOracle());
    expenseType = new SuggestBox(budget_.fieldsHelper.getExpenseTypeOracle());
    paymentType = new SuggestBox(budget_.fieldsHelper.getPaymentTypeOracle());
    initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));
    date.setFormat(FieldsHelper.DATE_BOX_FORMAT);
  }

  @UiHandler("vendor")
  void onSelection(SelectionEvent<SuggestOracle.Suggestion> e)
  {
    Suggestion selectedSuggestion = e.getSelectedItem();
    String selectedVendorName = selectedSuggestion.getReplacementString();
    String mostLikelyExpenseType = budget_.fieldsHelper.getMostLikelyExpenseTypeForVendor(selectedVendorName);
    String mostLikelyPaymentType = budget_.fieldsHelper.getMostLikelyPaymentTypeForVendor(selectedVendorName);
    String currentExpenseTypeText = expenseType.getText();
    String currentPaymentTypeText = expenseType.getText();
    if (currentExpenseTypeText == null || currentExpenseTypeText.length() == 0)
    {
      expenseType.setText(mostLikelyExpenseType);
    }
    if (currentPaymentTypeText == null || currentPaymentTypeText.length() == 0)
    {
      paymentType.setText(mostLikelyPaymentType);
    }
  }
}
