import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
// import javax.swing.ImageIcon;

class TestWindow extends JFrame {

	private static TestWindow frame;
	JButton sendButton;
	JButton nextButton;
	JButton readButton;
	JButton nameButton;
	JButton loopButton;
	String username;
	JTextArea text;
	JTextArea chatRoom;
	private final static String title = "TOEIC金フレ演習";
	private JLabel nameLabel;
	String useranswer = "";
	static List<String> questions = new ArrayList<String>();
	Word curproblem;
	static boolean isStudy = false;
	int loop;
	Tango data;
	List<Map<Double, List<Word>>> wronglist = new ArrayList<Map<Double, List<Word>>>();

	public void Showwindow() {
		frame = getInstance();
		frame.setLocationRelativeTo(null);
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// JLabel background = new JLabel(new ImageIcon("backgroud.JPG"));
		// frame.add(background);
		frame.setVisible(true);
	}

	public static TestWindow getInstance() {
		if (frame == null) {
			frame = new TestWindow();
			return frame;
		}
		return frame;
	}

	public void exitWidow(String killmsg) {
		chatRoom.append(killmsg);
		chatRoom.append("テストを終了します...");
		frame.dispose();
	}

	public void showQuestion(Word word, int num) {
		String promNum = "第" + num + "問";
		nameLabel.setText(promNum);
		chatRoom.setText("");
		chatRoom.append(word.showQuestion());
		curproblem = word;
	}

	public void setName(String name) {
		nameLabel.setText(name);
	}

	public void clearchatRoom() {
		chatRoom.setText("");
	}

	public void ReceiveMessage(String message) {
		chatRoom.append(message + "\n");
	}

	public void showResult(List<Word> wordlist) {
		chatRoom.setText("");
		int wrongnum = wordlist.size();
		System.out.println(wrongnum + "wrongnum");
		double accuracy = (double) 100 * (loop - wrongnum) / loop;
		Map<Double, List<Word>> curwrong = new HashMap<Double, List<Word>>();
		List<Word> tmp = new ArrayList<Word>(wordlist);
		curwrong.put(accuracy, tmp);
		wronglist.add(curwrong);
		int testcount = wronglist.size();
		for (int i = 0; i < testcount; ++i) {
			Map<Double, List<Word>> wrong = wronglist.get(i);
			accuracy = wrong.keySet().toArray(new Double[0])[0];
			List<Word> list = wrong.get(accuracy);
			chatRoom.append("----------------------\n");
			chatRoom.append(String.valueOf(i + 1) + "回目\n");
			if (list.size() > 0) {
				chatRoom.append("show wrong words below.\n");
			}
			for (Word word : list)
				chatRoom.append(word.showWord());
			ReceiveMessage("The accuracy rate is " + accuracy + "%");
		}
	}

	public int getNumber() {
		if (useranswer.length() <= 0)
			return -1;
		try {
			int num = Integer.parseInt(useranswer);
			return num;
		} catch (NumberFormatException nume) {
			return -1;
		}
	}

	public String getUserAnswer() {
		return useranswer;
	}

	private TestWindow() {
		data = Tango.getInstance();
		setTitle(title);
		setBounds(100, 100, 450, 530);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		nameLabel = new JLabel("第　問");
		nameLabel.setFont(new Font(null, Font.BOLD, 30));
		nameLabel.setAlignmentX(0.5f);
		topPanel.add(nameLabel);

		chatRoom = new JTextArea();
		chatRoom.setLineWrap(true);
		chatRoom.setEnabled(false);
		chatRoom.setFont(new Font(null, Font.PLAIN, 20));
		chatRoom.setDisabledTextColor(Color.BLACK);
		JScrollPane chatScrollpane = new JScrollPane(chatRoom);
		chatScrollpane.setPreferredSize(new Dimension(400, 250));

		text = new JTextArea();
		text.setLineWrap(true);
		text.setFont(new Font(null, Font.PLAIN, 20));
		JScrollPane textScrollpane = new JScrollPane(text);
		textScrollpane.setPreferredSize(new Dimension(350, 35));

		JPanel buttonpanel = new JPanel();
		buttonpanel.setLayout(new BoxLayout(buttonpanel, BoxLayout.X_AXIS));

		sendButton = new JButton("解答");
		sendButton.setAlignmentY(0.5f);
		sendButton.setAlignmentX(0.5f);
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				useranswer = text.getText();
				if (useranswer.length() > 0) {
					data.judge(curproblem);
					sendButton.setVisible(false);
					nextButton.setVisible(true);
					text.setText("");
				}
				// System.out.println("write!!");
			}
		});
		sendButton.setVisible(false);

		nextButton = new JButton("次へ");
		nextButton.setAlignmentY(0.5f);
		nextButton.setAlignmentX(0.5f);
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				text.setText("");
				int cur = data.curpromnum;
				if (!isStudy)
					isStudy = true;
				if (useranswer.length() <= 0) {
					if (cur < loop) {
						JLabel labelmsg = new JLabel("解答してください");
						JOptionPane.showMessageDialog(frame, labelmsg);
					} else {
						JLabel labelmsg = new JLabel("続けますか？");
						int option = JOptionPane.showConfirmDialog(frame, labelmsg, "継続確認", JOptionPane.YES_NO_OPTION,
								JOptionPane.INFORMATION_MESSAGE);
						if (option == JOptionPane.YES_OPTION) {
							// sendButton.setVisible(true);
							nextButton.setVisible(false);
							// buttonpanel.setVisible(false);
							loopButton.setVisible(true);
							data.resetGUI();
							isStudy = false;
						} else {
							frame.dispose();
							WriteTex.createTex(wronglist);
						}
					}
				} else {
					if (cur < loop) {
						data.createProblem();
						nextButton.setVisible(false);
						sendButton.setVisible(true);
					} else
						data.showResult();
				}
				useranswer = "";
			}
		});
		nextButton.setVisible(false);
		// buttonpanel.add(sendButton);
		// buttonpanel.add(nextButton);
		// buttonpanel.setVisible(false);

		readButton = new JButton("問題数選択へ");
		readButton.setAlignmentY(0.5f);
		readButton.setAlignmentX(0.5f);
		readButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				text.setText("");
				data.resetGUI();
				readButton.setVisible(false);
				loopButton.setVisible(true);
				useranswer = "";
			}
		});
		readButton.setVisible(false);

		nameButton = new JButton("名前入力");
		nameButton.setAlignmentY(0.5f);
		nameButton.setAlignmentX(0.5f);
		nameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				username = text.getText();
				if (data.setName()) {
					nameButton.setVisible(false);
					// readButton.setVisible(true);
					loopButton.setVisible(true);
					data.resetGUI();
					text.setText("");
					useranswer = "";
				}
			}
		});

		loopButton = new JButton("確定");
		loopButton.setAlignmentY(0.5f);
		loopButton.setAlignmentX(0.5f);
		loopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				useranswer = text.getText();
				if (data.setLoopcount()) {
					loop = data.loopcount;
					chatRoom.append(useranswer);
					data.createProblem();
					text.setText("");
					isStudy = true;
					useranswer = "";
					loopButton.setVisible(false);
					// buttonpanel.setVisible(true);
					sendButton.setVisible(true);
					// nextButton.setVisible(false);
				}
			}
		});
		loopButton.setVisible(false);

		Container contentPane = getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		contentPane.add(topPanel);
		contentPane.add(chatScrollpane);
		contentPane.add(textScrollpane);
		// contentPane.add(buttonpanel);
		contentPane.add(sendButton);
		contentPane.add(nextButton);
		contentPane.add(readButton);
		contentPane.add(nameButton);
		contentPane.add(loopButton);
	}

}