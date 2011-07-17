package steveshrader.budget.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import steveshrader.budget.client.widgets.Header;
import steveshrader.budget.shared.domain.BudgetUser;
import steveshrader.budget.shared.domain.Expense;
import steveshrader.budget.shared.domain.Vendor;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import com.persistGwt.server.db.database.DbEntryPoint;
import com.persistGwt.shared.DataStoreGwt;
import com.persistGwt.shared.interfaces.Savable;

/**
 * The entry point class which performs the initial loading of the Budget
 * application.
 */
public class Budget implements EntryPoint
{

  protected static Budget singleton_ = null;

  public Budget()
  {
    super();
    singleton_ = this;
  }

  interface Binder extends UiBinder<Widget, Budget>
  {
  }

  interface GlobalResources extends ClientBundle
  {
    @NotStrict
    @Source("global.css")
    CssResource css();
  }

  private static final Logger log = Logger.getLogger(Budget.class.getName());

  @UiField(provided = true)
  Header header;

  @UiField
  LayoutPanel container;

  @UiField
  ToggleButton expensesButton;

  @UiField
  ToggleButton chartsButton;

  @UiField(provided = true)
  ExpensesLayout expensesLayout;

  ChartsLayout chartsLayout;

  public FieldsHelper fieldsHelper;

  public DataStoreGwt dataStoreGwt_ = null;
  // root object
  public BudgetUser budgetUser_ = null;

  /**
   * This method sets up the top-level services used by the application.
   */
  public void onModuleLoad()
  {
    GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler()
      {
        public void onUncaughtException(Throwable e)
        {
          log.log(Level.SEVERE, e.getMessage(), e);
        }
      });
    
    // Inject global styles.
    GWT.<GlobalResources> create(GlobalResources.class).css().ensureInjected();
    // authenticate user
    RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, GWT.getHostPageBaseURL()
        + "GetUserInfoServlet");

    rb.setCallback(new RequestCallback()
      {
        @Override
        public void onError(Request request, Throwable exception)
        {
          Window.alert("The user could not be authenticated " + exception);
        }

        @Override
        public void onResponseReceived(Request request, Response response)
        {
          String payLoadS = response.getText();
          if (payLoadS == null)
          {
            // something is wrong.
            Window.alert("User could not be authenticated");
          }
          else
          {
            // tokenize
            String[] userInfoA = payLoadS.split("\n");
            String userName = userInfoA[0];
            if (userName == "null")
            {
              // something is wrong
              Window.alert("User could not be authenticated");
            }
            else
            {
              String userId = userInfoA[1];
              userId = userId.trim();
              String logoutUrl = userInfoA[2];
              // create or open datastore
              openDataStore(userName, userId);
              header = new Header(userName, userId, logoutUrl);
            }
          }
        }
      });
    try
    {
      rb.send();
    }
    catch (RequestException e)
    {
      Window.alert("RequestException" + e);
    }
  }

  @UiHandler("expensesButton")
  public void handleExpensesButtonClick(ClickEvent e)
  {
    container.clear();
    chartsButton.setDown(false);
    expensesButton.setDown(true);
    container.add(expensesLayout);
  }

  @UiHandler("chartsButton")
  public void handleChartsButtonClick(ClickEvent e)
  {
    container.clear();
    expensesButton.setDown(false);
    chartsButton.setDown(true);
    container.add(chartsLayout);
  }

  private void openDataStore(final String userName, final String userId)
  {
    // if a datastore exists for the user, open it. Else create
    // a datastore for the user
    dataStoreGwt_ = new DataStoreGwt(GWT.getHostPageBaseURL()
        + DataStoreGwt.dataStoreGwtServletName_, userId, new AsyncCallback<Savable>()
      {
        public void onSuccess(Savable root)
        {
          // the user logging in for the first time
          if (root == null)
          {
            // create root
            budgetUser_ = new BudgetUser(userName, userId);
            dataStoreGwt_.createRoot(budgetUser_, new AsyncCallback<Void>()
              {
                @Override
                public void onFailure(Throwable caught)
                {
                  Window.alert("Unable to access server.");
                }
                @Override
                public void onSuccess(Void v)
                {
                 initGUI();             
                              
                }
              });
          }
          else
          {
            // old user
            // keep the reference of the root.
            budgetUser_ = (BudgetUser) root;
            initGUI();
          }
        }

        public void onFailure(java.lang.Throwable caught)
        {
          Window.alert("Unable to access server.");
        }
      });

  }

  protected void initGUI()
  {
    fieldsHelper = new FieldsHelper(this);
    expensesLayout = new ExpensesLayout(this);
    chartsLayout = new ChartsLayout(this);
    RootLayoutPanel.get().add(GWT.<Binder> create(Binder.class).createAndBindUi(this));
    updateGUI();
  }

  // called whenever a display is required. will have all the expenses.
  public void updateGUI()
  {
    ArrayList<Expense> temp = new ArrayList<Expense>();
    // for type compliance, add all to a temp
    for (Savable s : budgetUser_.expenseAL_)
    {
      temp.add((Expense) s);
    }
    updateGUI(temp);
  }

  // filter the expenses and display
  public void updateGUI(Date start, Date end, Vendor vendor)
  {
    ArrayList<Expense> expenseAL = budgetUser_.getFilteredExpense(start, end, vendor);
    updateGUI(expenseAL);
  }

  protected void updateGUI(ArrayList<Expense> expenseAL)
  {
    expensesLayout.calendar.updateGUI(expenseAL);
    expensesLayout.summary.updateGUI(budgetUser_.getExpenseSummaryEntryAL(expenseAL));
  }

  // to display chart
  public void updateGUIForChartLayout(Date start, Date end, Vendor vendor)
  {
    ArrayList<Expense> expenseAL = budgetUser_.getFilteredExpense(start, end, vendor);
    chartsLayout.updateGUI(expenseAL, start, end);
  }

  public static Budget getInstance()
  {
     
    return singleton_;
  }

}
