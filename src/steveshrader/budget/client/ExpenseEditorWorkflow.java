package steveshrader.budget.client;

import steveshrader.budget.client.widgets.ExpenseEditor;
import steveshrader.budget.shared.domain.Expense;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTMLPanel;
/**
 * This class shows how the UI for editing an expense is wired up. It is also
 * responsible for showing and dismissing the ExpenseEditor.
 */
public class ExpenseEditorWorkflow
{
  interface Binder extends UiBinder<DialogBox, ExpenseEditorWorkflow>
  {
    Binder BINDER = GWT.create(Binder.class);
  }

  interface Driver extends SimpleBeanEditorDriver<Expense, ExpenseEditor>
  {
  }

  @UiField
  HTMLPanel contents;

  @UiField
  DialogBox dialog;

  @UiField(provided = true)
  ExpenseEditor expenseEditor;

  private Expense expense;
  private Driver editorDriver;
  private Budget budget_ = null;

  public ExpenseEditorWorkflow(Budget budget, Expense expense)
  {
    budget_ = budget;
    this.expense = expense;
    expenseEditor = new ExpenseEditor(budget);
    Binder.BINDER.createAndBindUi(this);
    contents.addDomHandler(new KeyUpHandler()
      {
        public void onKeyUp(KeyUpEvent event)
        {
          if (event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE)
          {
            onCancel(null);
          }
        }
      }, KeyUpEvent.getType());
  }

  /**
   * Called by the cancel button when it is clicked. This method will just tear
   * down the UI and clear the state of the workflow.
   */
  @UiHandler("cancel")
  void onCancel(ClickEvent event)
  {
    dialog.hide();
  }

  // Called by the edit dialog's save button.
  @UiHandler("save")
  void onSave(ClickEvent event)
  {
    // push gui data to expense object
    editorDriver.flush();
    budget_.budgetUser_.addExpense(expense);
    // persist in Google App Engine
    budget_.dataStoreGwt_.save(expense);
    budget_.dataStoreGwt_.save(budget_.budgetUser_);
    budget_.dataStoreGwt_.commit(new AsyncCallback<Void>()
      {
        @Override
        public void onSuccess(Void result)
        {
          dialog.hide();
          budget_.updateGUI();
        }
        @Override
        public void onFailure(Throwable caught)
        {
          Window.alert("error in saving " + caught);
        }
      });
  }
  /**
   * Called by the edit dialog's delete button.
   */
  @UiHandler("delete")
  void onDelete(ClickEvent event)
  {
    if (expense == null)
    { // this was on a new expense not editing an existing one
      dialog.hide();
    }
    else
    {
      budget_.budgetUser_.deleteExpense(expense);
      // save budgetUser_
      budget_.dataStoreGwt_.save(budget_.budgetUser_);
      
      budget_.dataStoreGwt_.commit(new AsyncCallback<Void>()
        {
          @Override
          public void onSuccess(Void result)
          {
            dialog.hide();
            budget_.updateGUI();
          }
          @Override
          public void onFailure(Throwable caught)
          {
            Window.alert("error in saving " + caught);
          }
        });
    }
  }
  /**
   * Construct and display the UI that will be used to edit the current Expense
   */
  public void edit(Expense expense)
  {
    editorDriver = GWT.create(Driver.class);
    editorDriver.initialize(expenseEditor);
    editorDriver.edit(expense);
    dialog.center();
  }
}
