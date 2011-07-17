package steveshrader.budget.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.TextArea;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * A simple glasspanel popup that terminates interaction with the application.
 */
class ErrorDialog {
  interface Binder extends UiBinder<DialogBox, ErrorDialog> {
  }

  @UiField
  DialogBox errorDialog;

  @UiField
  TextArea errorMessage;

  public ErrorDialog() {
    GWT.<Binder> create(Binder.class).createAndBindUi(this);
  }

  /**
   * @return
   */
  public Handler getHandler() {
    return new Handler() {
      {
        setLevel(Level.SEVERE);
      }

      @Override
      public void close() {
      }

      @Override
      public void flush() {
      }

      @Override
      public void publish(LogRecord record) {
        if (isLoggable(record)) {
          errorMessage.setText(record.getMessage());
          errorDialog.center();
        }
      }
    };
  }

  @UiHandler("dismiss")
  void onDismiss(ClickEvent event) {
    errorDialog.hide();
  }

  @UiHandler("reload")
  void onReload(ClickEvent event) {
    Window.Location.reload();
  }
}