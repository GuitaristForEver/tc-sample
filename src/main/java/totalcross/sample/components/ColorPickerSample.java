package totalcross.sample.components;

import totalcross.sys.Convert;
import totalcross.sys.Settings;
import totalcross.ui.AlignedLabelsContainer;
import totalcross.ui.Container;
import totalcross.ui.Edit;
import totalcross.ui.ImageControl;
import totalcross.ui.Label;
import totalcross.ui.dialog.MessageBox;
import totalcross.ui.event.DragEvent;
import totalcross.ui.event.PenEvent;
import totalcross.ui.event.PenListener;
import totalcross.ui.gfx.Color;
import totalcross.ui.gfx.Graphics;
import totalcross.ui.image.Image;

public class ColorPickerSample extends Container {
	private final int mar = (int)(Math.min(Settings.screenWidth, Settings.screenHeight) * 0.15);
	private double h, s, v;
	private Graphics gImage, gImValue;
	final int imWidth = Settings.screenWidth-70;
	final int imHeight = Settings.screenHeight/2;
	
	private AlignedLabelsContainer alc;
	private Label lblTitulo, lblCursor, lblValue, lblColor;
	private Image imValue, imGen;
	private ImageControl imcGen, imcValue;
	private Edit cr, cg, cb;
	
	public ColorPickerSample() {
		Settings.uiAdjustmentsBasedOnFontHeight = true;
	}
	
	@Override
	public void initUI() {
		try {
			//instantiating the title with bold
			lblTitulo = new Label("Test the color picker here:", CENTER, Color.BLACK, true);
			add(lblTitulo, CENTER, TOP + mar, PREFERRED, PREFERRED);
			
			//instantiating the square for the touch feedback
			lblCursor = new Label();
			lblCursor.borderColor = Color.WHITE;
			lblCursor.transparentBackground = true;
			
			//instantiating an image sample
			imGen = new Image(imWidth, imHeight);
			
			//getting the image graphics to edit it
			gImage = imGen.getGraphics();
			gImage.foreColor = Color.GREEN;
			gImage.drawCircle(imWidth/2, imHeight/2, imHeight/2);

			//generating the image
			int r = imHeight/2;
			int cx = imWidth/2;
			int cy = imHeight/2;
			for (int x = 0; x < imWidth; x++) {
				for (int y = 0; y < imHeight; y++) {
					double dc = Math.sqrt((double) Math.pow(x - cx, 2) +  Math.pow(y - cy, 2));
					double vAux = 1;
					double sAux = dc/r;
					int yAux = y;
					double hAux = Math.acos(((x - cx)/dc));
					if (yAux > cy)
						hAux *= -1;
					double[] rgb = hsvToRgb(hAux, sAux, vAux);
					if(sAux > 1 || sAux == 0)
						gImage.backColor = Color.WHITE;
					else
						gImage.backColor = Color.getRGB((int)rgb[0], (int)rgb[1], (int)rgb[2]);
					gImage.fillRect(x, y, 1, 1);
				}
			}
			
			//showing the generated image
			imcGen = new ImageControl(imGen);
			add(imcGen, LEFT+mar, AFTER+mar, PREFERRED, PREFERRED);
			
			//instantiating the value chooser of HSV color type
			imValue = new Image(20, imHeight);
			imcValue = new ImageControl(imValue);
			add(imcValue, AFTER+mar, SAME, 20, SAME);
			//getting the graphics to edit the image
			gImValue = imValue.getGraphics();
			
			//instantiating the value indicator
			lblValue = new Label("<");
			lblValue.getFont().adjustedBy(4, true);
			lblValue.transparentBackground = true;
			add(lblValue, AFTER+(mar/2), SAME, PREFERRED, PREFERRED, imcValue);
			//setting the initial value of v from HSV to maximum
			v = 1;
			
			//changing background color of the screen
			setBackColor(Color.WHITE);
			
			//setting the labels to be displayed on the AlignedLabel.
			alc = new AlignedLabelsContainer(new String[] { "Color:", "R:", "G:", "B:"});
			alc.foreColors = new int[] { Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK};
			//putting on screen the aligned label
			add(alc, CENTER, AFTER+mar*2, SCREENSIZE+95, PREFERRED, imcGen);
			
			lblColor = new Label();
			lblColor.setBackColor(Color.BLACK);
			cr = new Edit("999");
			cg = new Edit("999");
			cb = new Edit("999");
			cr.setText("0");
			cg.setText("0");
			cb.setText("0");

			alc.add(lblColor, LEFT + mar, alc.getLineY(0), FILL, 20);
			alc.add(cr, LEFT + mar, alc.getLineY(1));
			alc.add(cg, LEFT + mar, alc.getLineY(2));
			alc.add(cb, LEFT + mar, alc.getLineY(3));
			
			// adding all the necessary event listeners
			
			//event listener of dragging on the imGen
			imcGen.addPenListener(new PenListener() {

				@Override
				public void penUp(PenEvent e) {}

				@Override
				public void penDragStart(DragEvent e) {}

				@Override
				public void penDragEnd(DragEvent e) {}

				@Override
				public void penDrag(DragEvent e) {
					//ignore other drag events, like the side menu one.
					e.consumed = true;
					//putting some listening limits
					if(e.x >= 0 && e.x <= imWidth && e.y >= 0 && e.y <= imHeight) {
						// TODO Auto-generated method stub
						int r = imHeight/2;
						int cx = imWidth/2;
						int cy = imHeight/2;
						double dc = Math.sqrt((double) Math.pow(e.x - cx, 2) +  Math.pow(e.y - cy, 2));
						double sAux = dc/r;
						
						//check if this is within the generated circle
						if(sAux <=1)
						{
							//set the h and s values while dragging
							s = sAux;
							h = Math.acos(((e.x - cx)/dc));
							if (e.y > cy)
								h *= -1;
							//add a tracking label
							add(lblCursor, e.x+4, e.y+32, 8, 8, imcGen);
							for(int i = imHeight/10; i > 0; i--) {
								double vAux = (double)i/(imHeight/10);
								double rgb[] = hsvToRgb(h, s, vAux);
								gImValue.backColor = Color.getRGB((int)rgb[0], (int)rgb[1], (int)rgb[2]);
								gImValue.fillRect(0, (imHeight)-i*10, imcValue.getWidth(), 10);
							}
							updateActualColor(e);
							repaintNow();
						}
					}
				}

				@Override
				public void penDown(PenEvent e) {
					//same code as the previous one
					if(e.x >= 0 && e.x <= imWidth && e.y >= 0 && e.y <= imHeight) {
						int r = imHeight/2;
						int cx = imWidth/2;
						int cy = imHeight/2;
						double dc = Math.sqrt((double) Math.pow(e.x - cx, 2) +  Math.pow(e.y - cy, 2));
						double sAux = dc/r;
						if(sAux <=1)
						{
							s = sAux;
							h = Math.acos(((e.x - cx)/dc));
							if (e.y > cy)
								h *= -1;
								add(lblCursor, e.x+4, e.y+32, 8, 8, imcGen);
								for(int i = imHeight/10; i > 0; i--) {
									double vAux = (double)i/(imHeight/10);
									double rgb[] = hsvToRgb(h, s, vAux);
									gImValue.backColor = Color.getRGB((int)rgb[0], (int)rgb[1], (int)rgb[2]);
									gImValue.fillRect(0, (imHeight)-i*10, imcValue.getWidth(), 10);
								}
								updateActualColor(e);
								repaintNow();
						}
					}
				}
			});
		} catch (Exception e) {
			MessageBox.showException(e, true);
		}
		
		//listening when the user drags on the imValue
		imcValue.addPenListener(new PenListener() {
			
			@Override
			public void penUp(PenEvent e) {}
			
			@Override
			public void penDragStart(DragEvent e) {}
			
			@Override
			public void penDragEnd(DragEvent e) {}
			
			@Override
			public void penDrag(DragEvent e) {
				//setting some limits to the value's scroll bar
				if(e.y <= imHeight && e.y >= -2) {
					add(lblValue, SAME, e.y+30, lblValue);
					double dv = ((double)e.y)/(imHeight);
					if(dv > 1) dv = 1;
					else if(dv < 0) dv = 0;
					v = 1-dv;
					updateActualColor(e);
					e.consumed = true;
				}
			}
			
			@Override
			public void penDown(PenEvent e) {
				//setting some limits to the value's scroll bar
				if(e.y <= imHeight+2 && e.y >= -1) {
					add(lblValue, SAME, e.y+30, lblValue);
					double dv = ((double)e.y)/(imHeight);
					if(dv > 1) dv = 1;
					else if(dv < 0) dv = 0;
					v = 1-dv;
					updateActualColor(e);
					e.consumed = true;
				}
			}
		});
	}
	
	/* Transform HSV to RGB color */
	private double[] hsvToRgb(double h, double s, double v)
	{
	    double r = 0;
	    double g = 0;
	    double b = 0;
	    double hh, p, q, t, ff;
	    int i;

		h = h*180/Math.PI;
		if(h < 0)
			h += 360;
	    if (s <= 0)
	    {
	        r = v;
	        g = v;
	        b = v;
	        return new double[]{r, g, b};
	    }
	    hh = h;
	    if(hh >= 360)
	    {
	        hh = 0;
	    }
	    hh /= 60.0;
	    i = (int) hh;
	    ff = hh - i;
	    p = v * (1 - s);
	    q = v * (1 - (s * ff));
	    t = v * (1 - (s * (1 - ff)));
	    switch(i)
	    {
	        case 0:
	            r = v;
	            g = t;
	            b = p;
	            break;
	        case 1:
	            r = q;
	            g = v;
	            b = p;
	            break;
	        case 2:
	            r = p;
	            g = v;
	            b = t;
	            break;
	        case 3:
	            r = p;
	            g = q;
	            b = v;
	            break;
	        case 4:
	            r = t;
	            g = p;
	            b = v;
	            break;
	        case 5:
	        dafault:
	            r = v;
	            g = p;
	            b = q;
	            break;
	    }
	    r *= 255;
	    g *= 255;
	    b *= 255;
	    if(s == 0)
	    	return new double[] {255,255,255};
	    else
	    	return new double[]{r, g, b};
	}
	
	/*Changes the value of Edit ui*/
	private void updateActualColor(PenEvent e) {
		double rgb[] = hsvToRgb(h, s, v);
		int color = Color.getRGB((int)rgb[0], (int)rgb[1], (int)rgb[2]);
		lblColor.setBackColor(color);
		cr.setText(Convert.toString(Color.getRed(color)));
		cg.setText(Convert.toString(Color.getGreen(color)));
		cb.setText(Convert.toString(Color.getBlue(color)));
	}
}
