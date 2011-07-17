package steveshrader.budget.client.widgets;

import com.google.gwt.core.client.GWT;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The top panel, the header
 */
public class Header extends Composite
{

  interface Binder extends UiBinder<Widget, Header>
  {
  }

  private static final Binder binder = GWT.create(Binder.class);

  @UiField
  Anchor signOutLink;
  @UiField
  InlineLabel userNickname;
  @UiField
  Hidden userId;
  @UiField
  InlineLabel quote;

  public Header(String userName, String userIdS, String logoutUrl)
  {
    initWidget(binder.createAndBindUi(this));
    userNickname.setText(userName);
    userId.setDefaultValue(userIdS);
    signOutLink.setHref(logoutUrl);
  }

  public Header()
  {
    initWidget(binder.createAndBindUi(this));

  }
}
