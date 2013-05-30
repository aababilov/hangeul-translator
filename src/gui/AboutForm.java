package gui;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;

public class AboutForm extends JFrame {

	private static final long serialVersionUID = 8752996191271329329L;

	private JPanel jContentPane = null;

	private JPanel jPanel = null;

	private JButton btnClose = null;

	private JPanel jPanel1 = null;

	private JLabel jLabel = null;

	/**
	 * This is the default constructor
	 */
	public AboutForm() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setContentPane(getJContentPane());
		this.setTitle("О программе");
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJPanel(), java.awt.BorderLayout.SOUTH);
			jContentPane.add(getJPanel1(), java.awt.BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.add(getBtnClose(), null);
		}
		return jPanel;
	}

	private JButton getBtnClose() {
		if (btnClose == null) {
			btnClose = new JButton("OK");
			btnClose.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setVisible(false);
				}
			});
		}
		return btnClose;
	}

	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jLabel = new JLabel();
			jLabel.setText("<html><h1>ㄷ란슬리데라돌</h1>\n"
					+ "<p>ㅂ록람마 나비사나 </p><p>스벧시알ㄹ노 ㄷㄹ랴 </p><p>알리느</p></html>");
			jPanel1 = new JPanel();
			jPanel1.add(jLabel, null);
		}
		return jPanel1;
	}

	public void setLabelFont(Font f) {
		jLabel.setFont(f);
	}
}
