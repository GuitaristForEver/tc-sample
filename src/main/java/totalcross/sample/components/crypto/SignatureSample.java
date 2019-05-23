/*********************************************************************************
 *  TotalCross Software Development Kit                                          *
 *  Copyright (C) 2000-2012 SuperWaba Ltda.                                      *
 *  All Rights Reserved                                                          *
 *                                                                               *
 *  This library and virtual machine is distributed in the hope that it will     *
 *  be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of    *
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.                         *
 *                                                                               *
 *********************************************************************************/

package totalcross.sample.components.crypto;

import totalcross.crypto.CryptoException;
import totalcross.crypto.cipher.Key;
import totalcross.crypto.cipher.RSAPrivateKey;
import totalcross.crypto.cipher.RSAPublicKey;
import totalcross.crypto.digest.MD5Digest;
import totalcross.crypto.digest.SHA1Digest;
import totalcross.crypto.digest.SHA256Digest;
import totalcross.crypto.signature.PKCS1Signature;
import totalcross.crypto.signature.Signature;
import totalcross.sample.util.Colors;
import totalcross.sys.Convert;
import totalcross.sys.Settings;
import totalcross.ui.Button;
import totalcross.ui.ComboBox;
import totalcross.ui.Container;
import totalcross.ui.Edit;
import totalcross.ui.Label;
import totalcross.ui.ScrollContainer;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.font.Font;
import totalcross.util.UnitsConverter;

public class SignatureSample extends ScrollContainer {
	private Object[] signatures;
	private Key[] sigKeys;
	private Key[] verKeys;

	private final int gap = UnitsConverter.toPixels(DP + 8);
	private Container menu;
	private ScrollContainer result;
	private Edit edtInput;
	private ComboBox cboSignatures;
	private Button btnGo;

	private static final byte[] RSA_N = new byte[] { (byte) 0, (byte) -60, (byte) -106, (byte) -118, (byte) -19,
			(byte) 57, (byte) -63, (byte) -18, (byte) 102, (byte) 111, (byte) -56, (byte) 1, (byte) 50, (byte) -101,
			(byte) -90, (byte) -85, (byte) -96, (byte) -66, (byte) -70, (byte) -49, (byte) -52, (byte) -3, (byte) 70,
			(byte) -120, (byte) 63, (byte) -76, (byte) -34, (byte) -114, (byte) 13, (byte) 8, (byte) 45, (byte) -124,
			(byte) -12, (byte) -6, (byte) 87, (byte) 90, (byte) 61, (byte) -124, (byte) -42, (byte) 34, (byte) 21,
			(byte) 14, (byte) -73, (byte) 21, (byte) -104, (byte) 70, (byte) 11, (byte) -59, (byte) 58, (byte) -72,
			(byte) -55, (byte) -98, (byte) 68, (byte) 123, (byte) -63, (byte) -11, (byte) -7, (byte) -115, (byte) 32,
			(byte) 57, (byte) -38, (byte) -41, (byte) -9, (byte) -108, (byte) 79 };

	private static final byte[] RSA_D = new byte[] { (byte) 122, (byte) -69, (byte) 13, (byte) -94, (byte) -54,
			(byte) -61, (byte) 67, (byte) 37, (byte) -38, (byte) -75, (byte) 127, (byte) -31, (byte) -21, (byte) -128,
			(byte) -29, (byte) 119, (byte) 104, (byte) 123, (byte) -46, (byte) -115, (byte) -60, (byte) -75, (byte) -53,
			(byte) 12, (byte) 18, (byte) -52, (byte) 58, (byte) -36, (byte) -15, (byte) -11, (byte) 17, (byte) 34,
			(byte) -109, (byte) -121, (byte) 5, (byte) 117, (byte) 109, (byte) -72, (byte) -27, (byte) -103, (byte) -85,
			(byte) -1, (byte) 37, (byte) -30, (byte) 38, (byte) -86, (byte) 88, (byte) -28, (byte) -26, (byte) -102,
			(byte) -10, (byte) 124, (byte) -97, (byte) -18, (byte) -118, (byte) 2, (byte) 36, (byte) 40, (byte) -47,
			(byte) -75, (byte) -44, (byte) 69, (byte) 10, (byte) 1 };

	private static final byte[] RSA_E = new byte[] { (byte) 1, (byte) 0, (byte) 1 };

	@Override
	public void initUI() {
		super.initUI();
		this.setScrollBars(true, true);
		signatures = new Object[3];
		try {
			signatures[0] = new PKCS1Signature(new MD5Digest());
			signatures[1] = new PKCS1Signature(new SHA1Digest());
			signatures[2] = new PKCS1Signature(new SHA256Digest());
		} catch (CryptoException e) {
			throw new RuntimeException(e.getMessage());
		}

		verKeys = new Key[3];
		verKeys[0] = verKeys[1] = verKeys[2] = new RSAPublicKey(RSA_E, RSA_N);

		sigKeys = new Key[3];
		sigKeys[0] = sigKeys[1] = sigKeys[2] = new RSAPrivateKey(RSA_E, RSA_D, RSA_N);

		result = new ScrollContainer();

		menu = new Container();
		menu.setBackForeColors(Colors.P_300, Colors.ON_P_300);

		edtInput = new Edit();
		edtInput.setText("0123456789ABCDEF");

		cboSignatures = new ComboBox(signatures);
		cboSignatures.setSelectedIndex(0);

		btnGo = new Button(" Go! ");
		btnGo.setBackForeColors(Colors.S_600, Colors.ON_S_600);

		add(menu, LEFT + gap, TOP + gap, SCREENSIZE + 80, WILL_RESIZE);
		Label lbl = new Label("Message:");
		menu.add(lbl, LEFT + gap, TOP + gap / 2);
		menu.add(edtInput, AFTER + gap, SAME, FILL - gap, PREFERRED);
		menu.add(cboSignatures, LEFT + gap, AFTER + gap, edtInput);
		menu.resizeHeight();
		add(btnGo, AFTER + gap, SAME, FILL - gap, SAME);
	}

	@Override
	public void onEvent(Event e) {
		switch (e.type) {
		case ControlEvent.PRESSED:
			if (e.target == btnGo) {
				int index = cboSignatures.getSelectedIndex();
				String message = edtInput.getText();
				byte[] data = message.getBytes();

				Signature signer = (Signature) signatures[index];
				result = new ScrollContainer();
				result.setBackForeColors(Colors.P_600, Colors.ON_P_600);
				try {

					String sgn = cboSignatures.getSelectedItem().toString();
					add(result, LEFT + gap * 3, AFTER + gap, FILL - gap * 3, (int) (Settings.screenHeight * 0.22));

					Label title = new Label(sgn.toString(), CENTER);
					title.setFont(Font.getFont(true, 16));

					result.add(title, CENTER, TOP + gap / 2);
					result.add(new Label("Message: '" + message + "'"), LEFT + gap, AFTER + gap / 2);

					signer.reset(Signature.OPERATION_SIGN, sigKeys[index]);
					signer.update(data);
					byte[] signature = signer.sign();

					result.add(new Label(
							"Signature: " + Convert.bytesToHexString(signature) + " (" + signature.length + " bytes)"),
							LEFT + gap, AFTER + gap / 2);

					signer.reset(Signature.OPERATION_VERIFY, verKeys[index]);
					signer.update(data);

					result.add(
							new Label("" + (signer.verify(signature) ? "Signature verified!" : "Invalid signature!")),
							LEFT + gap, AFTER + gap / 2);
				} catch (CryptoException ex) {
					result.add(new Label("Exception: " + ex.toString()), LEFT + gap, AFTER + gap / 2);
				}
			}
			break;
		}
	}
}
