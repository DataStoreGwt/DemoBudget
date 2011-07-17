package steveshrader.budget.client;
 
import java.util.ArrayList;
import java.util.Date;
import steveshrader.budget.client.widgets.ChartFilterWidget;
import steveshrader.budget.shared.domain.Expense;
import com.google.gwt.core.client.GWT; 
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
/**
 * The main layout for viewing charts
 */
public class ChartsLayout extends Composite {
  interface Binder extends UiBinder<Widget, ChartsLayout> { }

  @UiField(provided = true)
  Image chartImageTotal;

  @UiField(provided = true)
  Image chartImageDetail;

  @UiField(provided = true)
  Image chartImageSummary;

  @UiField(provided = true)
  ChartFilterWidget filter;

  @UiConstructor
  public ChartsLayout( Budget budget) {
    filter = new ChartFilterWidget( budget );    
    chartImageTotal = new Image("imageHolder800x160.png");
    chartImageDetail = new Image("imageHolder800x350.png");
    chartImageSummary = new Image("imageHolder220x280.png");
    initWidget(GWT.<Binder> create(Binder.class).createAndBindUi(this));   
  }
     
  private void createChart( ChartDisplayData e ) {
    String lineChartTotalData = "http://chart.apis.google.com/chart" +
    	"?chtt=Total+Expenses+Per+Month" +	//chart title
    	"&chts=4B4A4A,14" +					//chart title in black w/ 14pt font
		"&chxl=" + e.chxlLineChartTotal() +	//0:|Jan|Feb|Mar|1:|2010|2011|2011" +	//chart labels
    	"&chxp=" + e.chxpLineChartTotal() +	//0,1,2,3|1,1,2,3" +	//chart label positions
    	"&chxr=" + e.chxrLineChartTotal() +	//0,1,4|1,1,4|2,2000,8000|3,2000,8000" +	//chart ranges
    	"&chxt=x,x,y,r" +					//chart axis to display...2 x, y, and right
    	"&chxs=3,60A6BF,12,-1,t,60A6BF" + 	//Axis label styles for the r-axis: text color, text size, left-aligned, with red tick marks.
    	"&chxtc=3,-1000" +					//Axis tick lengths for the r-axes. The first value specifies 400-pixel-long ticks inside the axis; the negative number means that the tick goes inside the axis, and the tick is cropped to fit inside the chart.
    	"&chs=800x160" +					//chart size
    	"&cht=lc" +							//chart type = line chart
    	"&chco=828282" +					//data line colors
    	"&chd=" + e.chdLineChartTotal() +	//t:98,45,72,98|27,12,31,50|69,28,63,50" +	//data
    	"&chds=" + e.chdsLineChartTotal() +	//data scaling between min and max amounts
    	"&chm=N*cUSD1*,000000,0,-1,10" +	//data points display amounts
    	"&chls=1" +							//line sizes
    	"&chma=40,20,20,30";				//margins   
    chartImageTotal.setUrl(lineChartTotalData);
    
    String lineChartDetailData = "http://chart.apis.google.com/chart" +
		"?chxl=" + e.chxlLineChartDetail() + 	//0:|Jan|Feb|Mar|1:|2010|2011|2011" +	//chart labels
		"&chxp=" + e.chxpLineChartDetail() + 	//0,1,2,3|1,1,2,3" +	//chart label positions
		"&chxr=" + e.chxrLineChartDetail() +	//0,1,4|1,1,4|2,2000,8000|3,2000,8000" +	//chart ranges
		"&chxt=x,x,y" +							//chart axis to display...2 x, y, and right
		"&chs=800x350" +						//chart size
		"&cht=lc" +								//chart type = line chart
		"&chco=FF0000,FF9900,FFFF00,33CC33,0066FF,66FFFF,9900CC,FF6666,FFC266,FFFF99,99E699,99CCFF,B29980,CC80E6,FFCCCC,CF7C00,CC9900,339933,0000CC,99CC00,FF3399" +	//data line colors
		"&chd=" + e.chdLineChartDetail() +		//t:98,45,72,98|27,12,31,50|69,28,63,50" +	//data
		"&chds=" + e.chdsLineChartDetail() +	//data scaling between min and max amounts
		"&chdl=" + e.chdlLineChartDetail() +	//Total|Gas|Restaurant" +	//chart legend labels
		"&chdlp=b" +							//chart legend = bottom
		"&chls=2|1" +							//line sizes
		"&chma=40,20,20,30";					//margins   
    chartImageDetail.setUrl(lineChartDetailData);
    
	String pieChartSummaryData = "http://chart.apis.google.com/chart" +
    	"?chtt=Total+Expenses+For+All+Months" +	//chart title
		"&chts=4B4A4A,14" +						//title in black 14pt font
		"&chs=220x380" +						//chart size
    	"&cht=p3" +								//3d pie chart
    	"&chco=FF0000,FFFF00,33CC33,3366FF,CC3399" +	//color range
    	"&chd=" + e.chdPieChart() +	//t:32.787,50.82,100,42.623" +
    	"&chds=" + e.chdsPieChart() + //data scaling between 0 and max value
    	"&chdl=" + e.chdlPieChart() + 	//Gas|Restaurant|etc." +
    	"&chdlp=b";    
    chartImageSummary.setUrl(pieChartSummaryData);
  }
  
  private void clearChart() {
	chartImageTotal.setUrl("imageHolder800x160.png");
	chartImageDetail.setUrl("imageHolder800x350.png");
	chartImageSummary.setUrl("imageHolder220x280.png");
  }
  
  public void updateGUI( ArrayList<Expense> expenseAL , Date start , Date end  )
  {
	  ChartDisplayData c = new ChartDisplayData( expenseAL , start , end ) ;
	  createChart( c ) ;
  }
}
