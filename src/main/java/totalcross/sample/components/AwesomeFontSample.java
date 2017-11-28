package totalcross.sample.components;

import totalcross.sample.util.Colors;
import totalcross.sys.Convert;
import totalcross.sys.Settings;
import totalcross.ui.Container;
import totalcross.ui.Control;
import totalcross.ui.Label;
import totalcross.ui.ScrollContainer;
import totalcross.ui.ToolTip;
import totalcross.ui.font.Font;

public class AwesomeFontSample extends Container{
	
	private ScrollContainer sc;

	private int GAP = fmH*4;
	
	@Override
	public void initUI() {
		
		super.initUI();
	    
		sc = new ScrollContainer(false,true);
		setAwesome(sc, fmH*2);
	    add(sc,LEFT,TOP+fmH/4,FILL,FILL-fmH/4);
	    
   
	    Label lbInfo = new Label("Hold char to see its unicode value");
	    lbInfo.setFont(lbInfo.getFont().asBold());
	    lbInfo.setForeColor(Colors.RED);
	    sc.add(lbInfo, LEFT + GAP, AFTER + GAP, FILL - GAP, PREFERRED);
	    
	    int cols = Math.min(Settings.screenWidth, Settings.screenHeight) / (fmH*3);
	    
	    for (int i = 0xF000,j=0; i <= 0xF27F; i++,j++)
	    {
	      Label l = new Label(String.valueOf((char)i),CENTER);
	      new ToolTip(l, "\\u"+Convert.unsigned2hex(i,4));
	      sc.add(l, (j%cols) == 0 ? LEFT : AFTER, (j%cols) == 0 ? AFTER : SAME, PARENTSIZE-cols,fmH*3);
	    }
	}
	
	public <T extends Control> T setAwesome(T c, int fmH) {
		
	    c.setFont(Font.getFont("FontAwesome", false, fmH));
	    return c;
	}
	
}