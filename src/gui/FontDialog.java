package gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class FontDialog extends JFrame {

	private static final long serialVersionUID = -3534757517776450564L;

	private JPanel jContentPane = null;

	private JPanel jPanel = null;

	private JButton jButton = null;

	private JPanel jPanel1 = null;

	private JPanel jPanel2 = null;

	private JPanel jPanel3 = null;

	private JLabel lblFont = null;

	private JLabel jLabel = null;

	private JList listFontName = null;

	private JList listFontSize = null;

	private DefaultListModel lmFontName = new DefaultListModel();

	private DefaultListModel lmFontSize = new DefaultListModel();

	private MainForm mainForm;

	private JScrollPane jScrollPane = null;

	private JScrollPane jScrollPane1 = null;

	public FontDialog(MainForm mainForm) {
		super();
		this.mainForm = mainForm;
		initialize();

	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setTitle("Шрифт");
		this.setContentPane(getJContentPane());

	}

	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJPanel(), java.awt.BorderLayout.SOUTH);
			jContentPane.add(getJPanel1(), java.awt.BorderLayout.CENTER);
		}
		return jContentPane;
	}

	void reloadFontNames() {
		String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getAvailableFontFamilyNames();
		lmFontName.clear();
		for (String s : fontNames)
			lmFontName.addElement(s);
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.add(getJButton(), null);
		}
		return jPanel;
	}

	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton("Закрыть");
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setVisible(false);
				}
			});
		}
		return jButton;
	}

	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jPanel1 = new JPanel();
			jPanel1.setLayout(new BoxLayout(getJPanel1(), BoxLayout.X_AXIS));
			jPanel1.add(getJPanel2(), null);
			jPanel1.add(getJPanel3(), null);
		}
		return jPanel1;
	}

	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			lblFont = new JLabel();
			lblFont.setText("Шрифт");
			jPanel2 = new JPanel();
			jPanel2.setLayout(new BorderLayout());
			jPanel2.add(lblFont, java.awt.BorderLayout.NORTH);
			jPanel2.add(getJScrollPane(), java.awt.BorderLayout.CENTER);
		}
		return jPanel2;
	}

	private JPanel getJPanel3() {
		if (jPanel3 == null) {
			jLabel = new JLabel();
			jLabel.setText("Размер");
			jPanel3 = new JPanel();
			jPanel3.setLayout(new BorderLayout());
			jPanel3.add(jLabel, java.awt.BorderLayout.NORTH);
			jPanel3.add(getJScrollPane1(), java.awt.BorderLayout.CENTER);
		}
		return jPanel3;
	}

	String prevFontName = "";

	private JList getListFontName() {
		if (listFontName == null) {
			listFontName = new JList(lmFontName);
			reloadFontNames();
			listFontName
					.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
						public void valueChanged(
								javax.swing.event.ListSelectionEvent e) {
							String newName = (String) listFontName
									.getSelectedValue();
							if (prevFontName.equals(newName))
								return;
							prevFontName = newName;
							setFont(newName, (String) listFontSize
									.getSelectedValue());
						}
					});

		}
		return listFontName;
	}

	void setupFontSizeList() {
		lmFontSize.addElement("8");
		lmFontSize.addElement("9");
		lmFontSize.addElement("10");
		lmFontSize.addElement("11");
		lmFontSize.addElement("12");
		lmFontSize.addElement("14");
		lmFontSize.addElement("16");
		lmFontSize.addElement("18");
		lmFontSize.addElement("20");
		lmFontSize.addElement("22");
		lmFontSize.addElement("24");
		lmFontSize.addElement("26");
		lmFontSize.addElement("28");
		lmFontSize.addElement("36");
		lmFontSize.addElement("48");
	}

	String prevFontSize = "";

	private JList getListFontSize() {
		if (listFontSize == null) {
			listFontSize = new JList(lmFontSize);
			setupFontSizeList();
			listFontSize
					.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
						public void valueChanged(
								javax.swing.event.ListSelectionEvent e) {
							String newSize = (String) listFontSize
									.getSelectedValue();
							if (prevFontSize.equals(newSize))
								return;
							prevFontSize = newSize;
							setFont((String) listFontName.getSelectedValue(),
									newSize);
						}
					});
		}
		return listFontSize;
	}

	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getListFontName());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jScrollPane1
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setViewportView(getListFontSize());
		}
		return jScrollPane1;
	}

	void setFont(String name, String size) {
		Font f = null;
		try {
			f = new Font(name, Font.PLAIN, Integer.parseInt(size));
		} catch (Exception e) {
			return;
		}
		mainForm.setCommonFont(f);
	}

	public void setDefaultFont() {
		listFontName.setSelectedValue("Arial Unicode MS", true);
		listFontSize.setSelectedValue("16", true);
	}
}
