package totalcross.sample.components.ui;

import totalcross.sample.components.BaseScreen;
import totalcross.sample.util.Colors;
import totalcross.sys.Convert;
import totalcross.sys.Settings;
import totalcross.ui.*;
import totalcross.ui.dialog.ProgressBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.font.Font;
import totalcross.ui.gfx.Color;
import totalcross.util.UnitsConverter;

public class DynScrollContainerSample extends BaseScreen {
	final int gap = UnitsConverter.toPixels(DP + 8);
	private Button goButton;
	private Edit numberRangeEdit;
	String oldOdd, oldEven;
	private DynamicScrollContainer vsc;
	private Check onlyPrimeChk;
	private Container c1;
	private Label warning;
	
	private int rowCount0;

	public DynScrollContainerSample() {
		super("https://totalcross.gitbook.io/playbook/components/dynamic-scroll");
		setFont(Font.getFont("Roboto", false, 16));
	}
	
	public static class DynSCTestView extends DynamicScrollContainer.AbstractView {
		public static boolean dynamicHeight;

		private int id;

		public String text;

		private Font f;
		
		

		public DynSCTestView(int id, Font f) {
			super();
			this.f = f;
			this.id = id;
		}

		@Override
		public int getHeight() {
			text =" " + id + "\n\n";
			if (dynamicHeight) {
				/*
				 * if your views height can only be determined on rendering then you will need
				 * to work out the height dynamically based on font, line wrapping etc
				 */
				if (id % 5 == 0) {
					text += "Lorem ipsum pretium class porta gravida lobortis ipsum, etiam posuere elit torquent duis nostra elit, sagittis etiam dapibus libero tellus facilisis curabitur facilisis sed sagittis posuere imperdiet bibendum.";

				} else if (id % 4 == 0) {
					text += "Felis turpis mauris per nullam donec lobortis maecenas metus orci viverra vulputate non nostra platea conubia, lectus proin mauris ligula aenean, adipiscing purus accumsan commodo laoreet facilisis tellus nisl litora vehicula. Aliquam aptent netus nisi rutrum fringilla arcu vehicula ut ante nam in.";

				} else if (id % 3 == 0) {
					text += "Venenatis consequat adipiscing ullamcorper etiam tellus nam mattis vehicula nostra leo rutrum lorem at suscipit, suspendisse aliquam aptent netus nisi rutrum fringilla arcu vehicula ut ante nam in.";

				} else if (id % 2 == 0) {
					text += "Hendrerit fermentum blandit conubia fringilla class duis non neque blandit condimentum, maecenas neque vel justo magna ornare tempor purus quis porttitor quisque.";

				} else {
					text += "Lorem ipsum pretium class porta gravida lobortis ipsum, etiam posuere elit torquent.";
				}

				int lines = Convert.numberOf(Convert.insertLineBreak(parentWidth - 10, f.fm, text), '\n') + 1;
				height = (f.fm.height * lines) + Edit.prefH;
				return height;
			} else {
				return height;
			}
		}

		@Override
		public void initUI() {
			Container ui = new Container();
		    ui.setBackColor((isPrime(id)) ? Color.getRGB(215, 215, 215) : Color.WHITE);
			ui.setRect(0, yStart, parentWidth, height);
			try {
				Label l = new Label(Convert.insertLineBreak(parentWidth - 10, f.fm, text));
				ui.add(l, Control.LEFT, Control.TOP, Control.FILL, Control.FILL);
			} catch (Exception e) {
				e.printStackTrace();
				// MessageBox.showException(e, true);
			}
			this.c = ui;
		}
	}

	private Edit add(String text, Container parent, int labelBackColor, int labelForeColor) {
		if (parent == null) {	
			parent = this;
		}
		Label label = new Label(text+" ");
		label.setBackForeColors(labelBackColor, labelForeColor);
		parent.add(label, LEFT + gap, AFTER, PREFERRED, PREFERRED);
		Edit ed = new Edit();
		ed.setKeyboard(Edit.KBD_NUMERIC);
		parent.add(ed, RIGHT - gap, SAME, SCREENSIZE + 20, PREFERRED);
		
		return ed;
	}



	@Override
	public void onContent(ScrollContainer content) {
		setBackForeColors(Colors.BACKGROUND, Colors.ON_BACKGROUND);
		c1 = new Container();
		content.add(c1, LEFT + gap, TOP + gap, FILL - gap, WILL_RESIZE);

		c1.add(new Spacer(0, 0), LEFT, TOP, 1, gap/2);
		numberRangeEdit = add("Select your number range: ", c1, Color.WHITE, Color.BLACK);
		numberRangeEdit.setBackForeColors(Color.WHITE, Color.BLACK);
		numberRangeEdit.useNativeNumericPad = true;

		c1.add(new Spacer(0, 0), LEFT, AFTER + gap/2);
		onlyPrimeChk = new Check("Show only prime numbers");
		onlyPrimeChk.setBackForeColors(Color.WHITE, Color.BLACK);
		c1.add(onlyPrimeChk, LEFT+gap, AFTER + gap + gap/2);
		goButton = new Button("Show");
		int backColor = Color.getRGB(93,151,244);
		goButton.setBackForeColors(backColor, Color.WHITE);
		goButton.borderColor = backColor;
		c1.add(goButton, RIGHT - gap, AFTER + gap, SCREENSIZE + 25, PREFERRED + gap, onlyPrimeChk);
		c1.add(warning = new Label("Prime in grey"),CENTER, AFTER + gap/2, PREFERRED, PREFERRED);
		warning.transparentBackground = true;
		warning.setForeColor(Color.RED);
		c1.add(new Spacer(0, 0), LEFT, AFTER, 1, gap/2);
		c1.resizeHeight();
		c1.setBackColor(Color.WHITE);

		vsc = new DynamicScrollContainer();
		vsc.setBackColor(Color.WHITE);
		vsc.setBorderStyle(BORDER_SIMPLE);
		content.add(vsc, LEFT + gap, AFTER + gap*2, FILL - gap, FILL - gap*2);
		numberRangeEdit.setText(String.valueOf(rowCount0 = 30));
	}

	@Override
	public void onEvent(Event event) {
		if (event.type == ControlEvent.PRESSED && (event.target == goButton || event.target == onlyPrimeChk) ) {
			int rowCount = rowCount0;
			try {
				rowCount = Convert.toInt(numberRangeEdit.getText());
			} catch (Exception e) {
				e.printStackTrace();
			}
			ProgressBox pb = new ProgressBox("Calculating", "Please wait...", null);
			pb.setBackColor(Color.getRGB(12, 98, 200));
			pb.popupNonBlocking();
			DynamicScrollContainer.DataSource datasource = new DynamicScrollContainer.DataSource(rowCount);
			
			if (onlyPrimeChk.isChecked()) {
				for (int i = 0; i < rowCount; i++) {
					System.out.print("i : " + i);
					if (isPrime(i)) {
						DynSCTestView view = new DynSCTestView(i, font);
						view.height = Settings.screenHeight / 20;
						datasource.addView(view);
					}
					System.out.print("\n");
				}
			} else {
				for (int i = 0; i < rowCount; i++) {
					if (isPrime(i)) {
						DynSCTestView view = new DynSCTestView(i, font);
						view.height = Settings.screenHeight / 20;
						datasource.addView(view);
					} else {
						DynSCTestView view = new DynSCTestView(i, font);
						view.height = Settings.screenHeight / 20;
						datasource.addView(view);
					}
				}
			}

			pb.unpop();
			vsc.setDataSource(datasource);
			if(!onlyPrimeChk.isChecked())
				vsc.scrollToView(datasource.getView(0));
		}
	}
	private static boolean isPrime(int n) {
		if (n < 2) {
			return false;
		}
		if (n < 4) {
			return true;
		}
		if (n % 2 == 0 || n % 3 == 0) {
			return false;
		}
		for (int i = 5; i * i <= n; i += 6) {
			if (n % i == 0) {
				return false;
			}
			if (n % (i + 2) == 0) {
				return false;
			}
		}
		return true;
	}
}
