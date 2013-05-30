package gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.FileOutputStream;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;

import trans.Jamo;
import trans.Transliterator;

public class MainForm extends JFrame {
	static Transliterator trn = new Transliterator();
	
	private static final long serialVersionUID = 4419643840840411149L;

	private JPanel jContentPane = null;
	private KoreanToRuScheme koreanToRu = new KoreanToRuScheme(this);
	private FontDialog fontDlg = new FontDialog(this);
	private AboutForm aboutForm = new AboutForm();

	public MainForm() {
		super();
		initialize();
	}

	private void initialize() {
		this.setSize(681, 627);
		this.setJMenuBar(getMainMenuBar());
		this.setTitle("Транслитератор");
		this.setContentPane(getJContentPane());
		fontDlg.setDefaultFont();
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				FileOutputStream outStr;
				try {
					outStr = new FileOutputStream(KoreanToRuScheme.defaultFileName);
					outStr.write(koreanToRu.getSelectedScheme().getBytes("UTF-8"));
					outStr.close();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}				
				System.exit(0);
			}
		});
	}

	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJSplitPane(), java.awt.BorderLayout.CENTER);
			jContentPane.add(getPnlButtons(), java.awt.BorderLayout.SOUTH);
		}
		return jContentPane;
	}

	public static MainForm fm;

	private JEditorPane txtEditorFrom = null;

	private JSplitPane jSplitPane = null;

	private JScrollPane scrollFrom = null;

	private JScrollPane scrollTo = null;

	private JEditorPane txtEditorTo = null;

	private JButton btnConcatSyllables = null;

	private JButton btnSeparateSyllables = null;

	private JButton btnToCo = null;

	private JButton btnToNonCo = null;

	private JPanel pnlButtons = null;

	private JPanel jPanel = null;

	private JPanel jPanel1 = null;

	private JLabel jLabel = null;

	private JPanel jPanel2 = null;

	private JLabel jLabel1 = null;

	public void setCommonFont(Font f) {
		txtEditorFrom.setFont(f);
		txtEditorTo.setFont(f);
		koreanToRu.getTxtTransScheme().setFont(f);
		aboutForm.setLabelFont(f);
	}

	private JEditorPane getTxtPane() {
		if (txtEditorFrom == null) {
			txtEditorFrom = new JEditorPane();
		}
		return txtEditorFrom;
	}


	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane();
			jSplitPane.setDividerLocation(this.getWidth() / 2);
			jSplitPane.setRightComponent(getJPanel2());
			jSplitPane.setLeftComponent(getJPanel1());
		}
		return jSplitPane;
	}

	/**
	 * This method initializes scrollFrom
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getScrollFrom() {
		if (scrollFrom == null) {
			scrollFrom = new JScrollPane();
			scrollFrom
					.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			scrollFrom
					.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			scrollFrom.setViewportView(getTxtPane());
		}
		return scrollFrom;
	}

	private JScrollPane getScrollTo() {
		if (scrollTo == null) {
			scrollTo = new JScrollPane();
			scrollTo
					.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			scrollTo
					.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			scrollTo.setViewportView(getTxtEditorTo());
		}
		return scrollTo;
	}

	private JEditorPane getTxtEditorTo() {
		if (txtEditorTo == null) {
			txtEditorTo = new JEditorPane();
		}
		return txtEditorTo;
	}

	/**
	 * This method initializes btnOrganizeSyllables
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnOrganizeSyllables() {
		if (btnConcatSyllables == null) {
			btnConcatSyllables = new JButton("Собрать слоги");
			btnConcatSyllables
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							concatSyllables();
						}
					});
		}
		return btnConcatSyllables;
	}

	/**
	 * This method initializes btnSeparateSyllables
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnSeparateSyllables() {
		if (btnSeparateSyllables == null) {
			btnSeparateSyllables = new JButton("Разбить слоги");
			btnSeparateSyllables
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							splitSyllables();
						}
					});
		}
		return btnSeparateSyllables;
	}

	private JButton getBtnToCo() {
		if (btnToCo == null) {
			btnToCo = new JButton("Т/л на корейский");
			btnToCo.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					transToCo();
				}
			});
		}
		return btnToCo;
	}

	private JButton getBtnToNonCo() {
		if (btnToNonCo == null) {
			btnToNonCo = new JButton("Т/л с корейского");
			btnToNonCo.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					transFromCo();
				}
			});
		}
		return btnToNonCo;
	}

	/**
	 * This method initializes pnlButtons
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getPnlButtons() {
		if (pnlButtons == null) {
			pnlButtons = new JPanel();
	
			pnlButtons.add(getJPanel(), null);
		}
		return pnlButtons;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(2);
			jPanel = new JPanel();
			jPanel.setLayout(gridLayout);
			jPanel.add(getBtnOrganizeSyllables(), null);
			jPanel.add(getBtnToCo(), null);
			jPanel.add(getBtnKoncevic(), null);
			jPanel.add(getBtnSeparateSyllables(), null);			
			jPanel.add(getBtnToNonCo(), null);
			
		}
		return jPanel;
	}

	/**
	 * This method initializes jPanel1
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jLabel = new JLabel();
			jLabel.setText("Оригинал");
			jPanel1 = new JPanel();
			jPanel1.setLayout(new BorderLayout());
			jPanel1.add(getScrollFrom(), java.awt.BorderLayout.CENTER);
			jPanel1.add(jLabel, java.awt.BorderLayout.NORTH);
		}
		return jPanel1;
	}

	/**
	 * This method initializes jPanel2
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			jLabel1 = new JLabel();
			jLabel1.setText("Результат");
			jPanel2 = new JPanel();
			jPanel2.setLayout(new BorderLayout());
			jPanel2.add(getScrollTo(), java.awt.BorderLayout.CENTER);
			jPanel2.add(jLabel1, java.awt.BorderLayout.NORTH);
		}
		return jPanel2;
	}

	private JButton getBtnKoncevic() {
		if (btnKoncevic == null) {
			btnKoncevic = new JButton("Транскрипция Концевича");
			btnKoncevic.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					koncevicCoToRu();
				}
			});
		}
		return btnKoncevic;
	}

	private JMenuBar getMainMenuBar() {
		if (mainMenuBar == null) {
			mainMenuBar = new JMenuBar();
			mainMenuBar.add(getJMenu());
			mainMenuBar.add(getJMenu1());
			mainMenuBar.add(getJMenu2());
		}
		return mainMenuBar;
	}

	/**
	 * This method initializes jMenu
	 * 
	 * @return javax.swing.JMenu
	 */
	private JMenu getJMenu() {
		if (jMenu == null) {
			jMenu = new JMenu("Транслитерировать");
			jMenu.add(getMiConcatSyllables());
			jMenu.add(getMiSplitSyllables());
			jMenu.addSeparator();
			jMenu.add(getMiToCo());
			jMenu.add(getMiTransFromCo());
			jMenu.add(getMiKoncevic());
		}
		return jMenu;
	}

	private JMenu getJMenu1() {
		if (jMenu1 == null) {
			jMenu1 = new JMenu("Настройки");
			jMenu1.add(getMiSelectFont());
			jMenu1.add(getMiTransScheme());
			jMenu1.addSeparator();
			jMenu1.add(getMiSeparateDigraph());
			jMenu1.add(getMiAutoOrganize());
		}
		return jMenu1;
	}

	/**
	 * This method initializes miConcatSyllables
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getMiConcatSyllables() {
		if (miConcatSyllables == null) {
			miConcatSyllables = new JMenuItem("Собрать слоги");	
			miConcatSyllables
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							concatSyllables();
						}
					});
		}
		return miConcatSyllables;
	}

	/**
	 * This method initializes miSplitSyllables
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getMiSplitSyllables() {
		if (miSplitSyllables == null) {
			miSplitSyllables = new JMenuItem("Разбить слоги");			
			miSplitSyllables
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							splitSyllables();
						}
					});
		}
		return miSplitSyllables;
	}

	/**
	 * This method initializes miToCo
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getMiToCo() {
		if (miToCo == null) {
			miToCo = new JMenuItem("Транслитерировать на корейский");			
			miToCo.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					transToCo();
				}
			});
		}
		return miToCo;
	}

	/**
	 * This method initializes miTransFromCo
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getMiTransFromCo() {
		if (miTransFromCo == null) {
			miTransFromCo = new JMenuItem("Транслитерировать с корейского");			
			miTransFromCo
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							transFromCo();
						}
					});
		}
		return miTransFromCo;
	}

	/**
	 * This method initializes miKoncevic
	 * 
	 * @return javax.swing.JMenuItem
	 */
	private JMenuItem getMiKoncevic() {
		if (miKoncevic == null) {
			miKoncevic = new JMenuItem("Транскрибировать по Концевичу");			
			miKoncevic.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					koncevicCoToRu();
				}
			});
		}
		return miKoncevic;
	}

	private JMenuItem getMiSelectFont() {
		if (miSelectFont == null) {
			miSelectFont = new JMenuItem("Шрифт");
			miSelectFont.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					fontDlg.setVisible(true);
				}
			});
		}
		return miSelectFont;
	}

	
	private JMenuItem getMiTransScheme() {
		if (miTransScheme == null) {
			miTransScheme = new JMenuItem("Схема транслитерации");
			miTransScheme.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					koreanToRu.setVisible(true);
				}
			});
		}
		return miTransScheme;
	}

	/**
	 * This method initializes miSeparateDigraph	
	 * 	
	 * @return javax.swing.JCheckBoxMenuItem	
	 */
	private JCheckBoxMenuItem getMiSeparateDigraph() {
		if (miSeparateDigraph == null) {
			miSeparateDigraph = new JCheckBoxMenuItem("Разделять диграфы при разбиении слогов");
		}
		return miSeparateDigraph;
	}

	private JCheckBoxMenuItem getMiAutoOrganize() {
		if (miAutoOrganize == null) {
			miAutoOrganize = new JCheckBoxMenuItem("Собирать слоги при транслитерации");
			miAutoOrganize.setSelected(true);
		}
		return miAutoOrganize;
	}

	private JMenu getJMenu2() {
		if (jMenu2 == null) {
			jMenu2 = new JMenu("Справка");
			jMenu2.add(getMiAbout());
		}
		return jMenu2;
	}

	/**
	 * This method initializes miAbout	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getMiAbout() {
		if (miAbout == null) {
			miAbout = new JMenuItem("О программе");
			miAbout.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					aboutForm.setVisible(true);
				}
			});
		}
		return miAbout;
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex2) {
		}	
		fm = new MainForm();
		fm.setDefaultCloseOperation(EXIT_ON_CLOSE);
		fm.setVisible(true);
	}

	private JButton btnKoncevic = null;

	private JMenuBar mainMenuBar = null;

	private JMenu jMenu = null;

	private JMenu jMenu1 = null;

	private JMenuItem miConcatSyllables = null;

	private JMenuItem miSplitSyllables = null;

	private JMenuItem miToCo = null;

	private JMenuItem miTransFromCo = null;

	private JMenuItem miKoncevic = null;

	private JMenuItem miSelectFont = null;

	private JMenuItem miTransScheme = null;

	private JCheckBoxMenuItem miSeparateDigraph = null;

	private JCheckBoxMenuItem miAutoOrganize = null;

	private JMenu jMenu2 = null;

	private JMenuItem miAbout = null;

	void exec() {
		/*
		 * "ㅏᄉ-ᄉㅏ", "ᄉᄉᄉㅏ", "설음", "초밟중", "ᄉᄉㅏㅇㄱㅣ", "초바ᄅㅂ중", "ᅥᆯ ᅳᆷ d ᅳᆷ
		 * d으--ᆷᅳ-d-", "ᅡᄅ리ㄴᅡ" , "모타는사라미", "한글" "예/아니요", "저는 러시아 사람입니다", "이름이
		 * 뭣입니까?", "향찰/鄕札", "한국어 /", " 서둘러주세요." }
		 */
	}

	private void concatSyllables() {
		String in = txtEditorFrom.getText();
		String out = Jamo.concatSyllables(in);
		txtEditorTo.setText(out);
	}

	private void splitSyllables() {
		String in = txtEditorFrom.getText();
		String out = miSeparateDigraph.isSelected() ? Jamo
				.splitDigraphsSyllables(in) : Jamo.splitSyllable(in);
		txtEditorTo.setText(out);
	}

	private void transToCo() {
		String in = txtEditorFrom.getText();
		String out = trn.transToCo(in);
		if (miAutoOrganize.isSelected())
			out = Jamo.concatSyllables(out);
		txtEditorTo.setText(out);
	}

	private void transFromCo() {
		String in = txtEditorFrom.getText();
		String out = trn.transToRu(in);
		txtEditorTo.setText(out);
	}

	private void koncevicCoToRu() {
		String in = txtEditorFrom.getText();
		String out = trn.koncevicCorToRu(in);
		txtEditorTo.setText(out);
	}

	public void setTransScheme(String text) {
		trn.loadFromString(text);		
	}

} // @jve:decl-index=0:visual-constraint="10,10"
