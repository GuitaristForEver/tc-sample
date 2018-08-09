package totalcross.sample.main;

import totalcross.io.IOException;
import totalcross.sample.components.Home;
import totalcross.sample.components.cards.CardsSample;
import totalcross.sample.components.crypto.CipherSample;
import totalcross.sample.components.crypto.DigestSample;
import totalcross.sample.components.crypto.SignatureSample;
import totalcross.sample.components.io.FileSample;
import totalcross.sample.components.json.JSONSample;
import totalcross.sample.components.lang.thread.ThreadSample;
import totalcross.sample.components.net.SocketSample;
import totalcross.sample.components.phone.PhoneDialerSample;
import totalcross.sample.components.sql.SQLiteBenchSample;
import totalcross.sample.components.sql.SQLiteFormGridTabbedContainer;
import totalcross.sample.components.sys.SettingsSample;
import totalcross.sample.components.ui.*;
import totalcross.sample.components.util.ZLibSample;
import totalcross.sample.components.xml.SoapSample;
import totalcross.sample.components.xml.XMLParseSample;
import totalcross.sample.util.Colors;
import totalcross.sys.Settings;
import totalcross.ui.Container;
import totalcross.ui.ImageControl;
import totalcross.ui.Label;
import totalcross.ui.MainWindow;
import totalcross.ui.SideMenuContainer;
import totalcross.ui.SideMenuContainer.Sub;
import totalcross.ui.font.Font;
import totalcross.ui.gfx.Color;
import totalcross.ui.icon.MaterialIcons;
import totalcross.ui.image.Image;
import totalcross.ui.image.ImageException;

public class TCSample extends MainWindow {

  SideMenuContainer sideMenu;

  static {
    Settings.applicationId = "VKSS";
    Settings.appVersion = "2.0.1";
    Settings.iosCFBundleIdentifier = "com.totalcross.showcase";
  }
	
  public TCSample() {

    super("TotalCross Showcase", NO_BORDER);

    setUIStyle(Settings.Material);
    Settings.uiAdjustmentsBasedOnFontHeight = true;

    setBackForeColors(Colors.BACKGROUND, Colors.SURFACE);
  }

  @Override
  public void initUI() {
	MainWindow.getMainWindow().addTimer(100);
	//General
	SideMenuContainer.Item home = new SideMenuContainer.Item("Home", MaterialIcons._HOME, Color.BLACK, false, () -> { return new Home();});
	SideMenuContainer.Sub uiGroup = createUISubGroup();
    SideMenuContainer.Sub sqlGroup = createSQLSubGroup();
    SideMenuContainer.Sub cryptoGroup = createCryptoSubGroup();
    SideMenuContainer.Sub ioGroup = createIOSubGroup();
    SideMenuContainer.Sub jsonGroup = createJSONSubGroup();
    SideMenuContainer.Sub langGroup = createLangSubGroup();
	SideMenuContainer.Sub netGroup = createNetSubGroup();
	SideMenuContainer.Sub phoneGroup = createPhoneSubGroup();
	SideMenuContainer.Sub sysGroup = createSystemSubGroup();
	SideMenuContainer.Sub utilGroup = createUtilSubGroup();
	SideMenuContainer.Sub xmlGroup = createXMLSubGroup();
	
    sideMenu =
        new SideMenuContainer(
            null,
            home,            		
            uiGroup,
            sqlGroup,
            cryptoGroup,
            ioGroup,
            jsonGroup,
            langGroup,
            netGroup,
            phoneGroup,
            sysGroup,
            utilGroup,
            xmlGroup
            );

    sideMenu.topMenu.header =
        new Container() {
          @Override
          public void initUI() {

            try {
              setBackColor(Colors.SECONDARY);

              Label title = new Label("Showcase", LEFT, Color.WHITE, false);
              title.setFont(Font.getFont("Lato Bold", false, this.getFont().size + 5));
              title.setForeColor(Color.WHITE);
              add(title, LEFT + 45, BOTTOM - 30, FILL, DP + 56);

              ImageControl profile = new ImageControl(new Image("images/logoV.png"));
              profile.scaleToFit = true;
              profile.transparentBackground = true;
              add(profile, LEFT + 45, TOP + 150, PREFERRED, FIT);

            } catch (IOException | ImageException e) {
              e.printStackTrace();
            }
          }
        };

    sideMenu.setBarFont(Font.getFont(Font.getDefaultFontSize() + 5));
    sideMenu.setBackColor(Colors.PRIMARY);
    sideMenu.setForeColor(Color.WHITE);
    sideMenu.setItemForeColor(Color.BLACK);
    sideMenu.topMenu.drawSeparators = false;
    sideMenu.topMenu.itemHeightFactor = 3;

    add(sideMenu, LEFT, TOP, PARENTSIZE, PARENTSIZE);
  }

  	private Sub createIOSubGroup() {
  		return new SideMenuContainer.Sub("IO", 
			new SideMenuContainer.Item("File", MaterialIcons._FOLDER, Color.BLACK, () -> { return new FileSample(); }));
  	}

  	private Sub createSQLSubGroup() {
  		return new SideMenuContainer.Sub("SQL", 
			new SideMenuContainer.Item("SQLite / Grid", MaterialIcons._STORAGE, Color.BLACK,  () -> { return new SQLiteFormGridTabbedContainer(); }),
			new SideMenuContainer.Item("SQLite Bench", MaterialIcons._INPUT, Color.BLACK, () -> { return new SQLiteBenchSample(); }));
  	}

  	private Sub createXMLSubGroup() {
  		return new SideMenuContainer.Sub("XML",
  			new SideMenuContainer.Item("XML", MaterialIcons._CODE, Color.BLACK, () -> { return new XMLParseSample(); }),
  			new SideMenuContainer.Item("Soap", MaterialIcons._FILTER_LIST, Color.BLACK, () -> { return new SoapSample(); }));
	}

	private Sub createUISubGroup() {
	   return new SideMenuContainer.Sub("UI",
			   new SideMenuContainer.Item("Accordion Container", MaterialIcons._FORMAT_ALIGN_LEFT, Color.BLACK,  () -> { return new AccordionSample(); }),
			   new SideMenuContainer.Item("Aligned Labels", MaterialIcons._MENU, Color.BLACK,  () -> { return new AlignedLabelsSample(); }),
			   new SideMenuContainer.Item("Button", MaterialIcons._TOUCH_APP, Color.BLACK,  () -> { return new ButtonSample(); }),
			   new SideMenuContainer.Item("Camera", MaterialIcons._PHOTO_CAMERA, Color.BLACK,  () -> { return new CameraSample(); }),
			   new SideMenuContainer.Item("Cards", MaterialIcons._VIEW_AGENDA, Color.BLACK,  () -> { return new CardsSample(); }),
			   new SideMenuContainer.Item("Charts", MaterialIcons. _EDIT, Color.BLACK,  () -> { return new ChartSample(); }),
			   new SideMenuContainer.Item("Check and Radio", MaterialIcons._CHECK_BOX, Color.BLACK,  () -> { return new CheckRadioSample(); }),
			   new SideMenuContainer.Item("Combo and List", MaterialIcons._ARROW_DROP_DOWN_CIRCLE, Color.BLACK,  () -> { return new ComboListSample(); }),
			   new SideMenuContainer.Item("Edit", MaterialIcons._TEXT_FORMAT, Color.BLACK,  () -> { return new EditSample(); }),
			   new SideMenuContainer.Item("Font sizes", MaterialIcons._SORT_BY_ALPHA, Color.BLACK,  () -> { return new FontSample(); }),
			   new SideMenuContainer.Item("Grid", MaterialIcons._GRID_ON, Color.BLACK,  () -> { return new GridSample(); }),
			   new SideMenuContainer.Item("HandWrite Signature", MaterialIcons._GESTURE, Color.BLACK,  () -> { return new HWSignatureSample(); }),
			   new SideMenuContainer.Item("Image Animation", MaterialIcons._GIF, Color.BLACK,  () -> { return new ImageAnimationSample(); }),
			   new SideMenuContainer.Item("Image Modifiers", MaterialIcons._IMAGE, Color.BLACK,  () -> { return new ImageModifiersSample(); }),
			   new SideMenuContainer.Item("Login", MaterialIcons._PERSON, Color.BLACK,  () -> { return new Login(); }),
			   new SideMenuContainer.Item("Material Icons", MaterialIcons._FONT_DOWNLOAD, Color.BLACK,  () -> { return new MaterialIconsSample(); }),
			   new SideMenuContainer.Item("MessageBox", MaterialIcons._QUESTION_ANSWER, Color.BLACK,  () -> { return new MessageBoxSample(); }),
			   new SideMenuContainer.Item("Other Controls", MaterialIcons._SLIDESHOW, Color.BLACK,  () -> { return new OtherControlsSample(); }),	
			   new SideMenuContainer.Item("ProgressBox", MaterialIcons._INDETERMINATE_CHECK_BOX, Color.BLACK,  () -> { return new ProgressBoxSample(); }),
			   new SideMenuContainer.Item("Spinner Inside Loop", MaterialIcons._LOOP , Color.BLACK,  () -> { return new SpinnerSample(); }),	
			   new SideMenuContainer.Item("Slider and switch", MaterialIcons._LINEAR_SCALE, Color.BLACK,  () -> { return new SliderSwitchSample(); }),	
			   new SideMenuContainer.Item("Tabbed Container", MaterialIcons._VIEW_ARRAY, Color.BLACK,  () -> { return new TabbedContainerSample(); }),
			   new SideMenuContainer.Item("Velocimeter", MaterialIcons._LOOKS, Color.BLACK,  () -> { return new VelocimeterSample(); }));
	}

	private Sub createCryptoSubGroup() {
		return new SideMenuContainer.Sub("Crypto", 
				new SideMenuContainer.Item("Cipher", MaterialIcons._INDETERMINATE_CHECK_BOX, Color.BLACK,  () -> { return new CipherSample(); }),
				new SideMenuContainer.Item("Digest", MaterialIcons._SLIDESHOW, Color.BLACK,  () -> { return new DigestSample(); }),
				new SideMenuContainer.Item("Signature", MaterialIcons._SETTINGS_ETHERNET, Color.BLACK,  () -> { return new SignatureSample(); }));
	}
	
	private Sub createJSONSubGroup() {
		return new SideMenuContainer.Sub("JSON", 
				new SideMenuContainer.Item("JSON", MaterialIcons._SETTINGS_ETHERNET, Color.BLACK, () -> { return new JSONSample();}));
	}

	private Sub createLangSubGroup() {
		return new SideMenuContainer.Sub("Lang", 
			new SideMenuContainer.Item("Thread", MaterialIcons._LINE_STYLE, Color.BLACK, () -> { return new ThreadSample(); }));
	}

	private Sub createNetSubGroup() {
		return new SideMenuContainer.Sub("Net",
				new SideMenuContainer.Item("Socket", MaterialIcons._HTTP, Color.BLACK, () -> { return new SocketSample(); }));
	}

	private Sub createPhoneSubGroup() {
		return new SideMenuContainer.Sub("Phone",
				new SideMenuContainer.Item("Dialer", MaterialIcons._CALL, Color.BLACK, () -> { return new PhoneDialerSample(); }));
	}

	private Sub createSystemSubGroup() {
		return new SideMenuContainer.Sub("System",
				new SideMenuContainer.Item("Settings", MaterialIcons._SETTINGS_APPLICATIONS, Color.BLACK, () -> { return new SettingsSample(); }));
	}

	private Sub createUtilSubGroup() {
		return new SideMenuContainer.Sub("Util",
				new SideMenuContainer.Item("ZLib", MaterialIcons._SORT, Color.BLACK, () -> { return new ZLibSample(); }));
		}
}