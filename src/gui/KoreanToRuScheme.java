package gui;

import java.awt.BorderLayout;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JEditorPane;
import javax.swing.JButton;
import javax.swing.JScrollPane;

public class KoreanToRuScheme extends JFrame {
	private static final long serialVersionUID = 4419643840840411149L;

	private JPanel jContentPane = null; // @jve:decl-index=0:visual-constraint="10,10"

	private JEditorPane txtTransScheme = null;

	private JPanel jPanel = null;

	private JButton btnOk = null;

	private JButton btnCancel = null;

	private MainForm mainForm;

	public static String defaultFileName = "kor2ru.txt";
	private String selectedScheme = "";
	private void setScheme(String s) {
		selectedScheme = s;
		mainForm.setTransScheme(s);
	}
	public KoreanToRuScheme(MainForm mainForm) {
		super();
		this.mainForm = mainForm;
		initialize();
	}

	private void initialize() {
		this.setSize(640, 480);
		this.setTitle("Схема транслитерации");
		this.setContentPane(getJContentPane());
		String s = loadStringFromFile(defaultFileName);
		txtTransScheme.setText(s);
		setScheme(s);
	}

	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJPanel(), java.awt.BorderLayout.SOUTH);
			jContentPane.add(getJScrollPane(), java.awt.BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes txtTransScheme
	 * 
	 * @return javax.swing.JEditorPane
	 */
	JEditorPane getTxtTransScheme() {
		if (txtTransScheme == null) {
			txtTransScheme = new JEditorPane();
		}
		return txtTransScheme;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.add(getBtnOk(), null);			
			jPanel.add(getBtnApply(), null);
			jPanel.add(getBtnCancel(), null);
		}
		return jPanel;
	}

	/**
	 * This method initializes btnOk
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnOk() {
		if (btnOk == null) {
			btnOk = new JButton("OK");
			btnOk.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setScheme(txtTransScheme.getText());	
					setVisible(false);
				}
			});
		}
		return btnOk;
	}

	private JButton getBtnCancel() {
		if (btnCancel == null) {
			btnCancel = new JButton("Закрыть");
			btnCancel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setVisible(false);
				}
			});
		}
		return btnCancel;
	}

	static String defaultScheme = "ㄱ=к\n" + "ㄱ=г\n" + "ㄴ=н\n" + "ㄷ=т\n"
			+ "ㄷ=д\n" +  "ㄹㄹ=лл\n" + "ㄹ=л\n" + "ㄹ=р\n" +"ㅁ=м\n"
			+ "ㅂ=п\n" + "ㅂ=б\n" + "ㅅ=с\n" + "ㅅ=з\n" + "ㅇ=нъ\n" + "ㅈ=ч\n"
			+ "ㅈ=дж\n" + "ㅊ=чх\n" + "ㅋ=кх\n" + "ㅌ=тх\n" + "ㅍ=пх\n" + "ㅎ=х\n"
			+ "ㅏ=а\n" + "ㅑ=я\n" + "ㅓ=о_\n" + "ㅕ=ё_\n" + "ㅗ=о\n" + "ㅛ=ё\n"
			+ "ㅜ=у\n" + "ㅠ=ю\n" + "ㅡ=ы\n" + "ㅣ=и\n" + "ㅣ=й\n" + "ㅐ=э\n"
			+ "ㅒ=йя\n" + "ㅔ=е\n" + "ㅖ=йе\n" + "ㅚ=ве\n" + "ㅟ=ви\n" + "ㅝ=во\n"
			+ "ㅙ=вэ\n" + "ㅞ=ве\n" + "ㅢ=ый\n" + "ㅘ=ва";

	private JScrollPane jScrollPane = null;

	private JButton btnApply = null;

	public static String loadStringFromFile(String fileName) {
		String s = "";
		try {
			InputStreamReader ireader = new InputStreamReader(
					new FileInputStream(fileName), "UTF-8");
			final int BUF_LEN = 1024;
			char[] buf = new char[BUF_LEN];
			int count_read;
			for (;;) {
				count_read = ireader.read(buf);
				if (count_read == -1)
					break;
				if (count_read != 0)
					s += new String(buf, 0, count_read);
			}
			ireader.close();
		} catch (IOException e) {
			return defaultScheme;
		}
		return s;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getTxtTransScheme());
		}
		return jScrollPane;
	}
	public String getSelectedScheme() {
		return selectedScheme;
	}
	/**
	 * This method initializes btnApply	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnApply() {
		if (btnApply == null) {
			btnApply = new JButton("Применить");
			btnApply.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setScheme(txtTransScheme.getText());
				}
			});
		}
		return btnApply;
	}

}
