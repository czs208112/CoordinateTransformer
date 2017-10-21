package com.tigerwho.view;

import java.awt.Toolkit;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import com.tigerwho.constant.CommonConstant;
import com.tigerwho.controller.Transformtools;
import org.eclipse.wb.swt.SWTResourceManager;

public class CTApp {
	Display display;// 创建一个display对象。
	protected Shell shell;
	private Text text_to;
	private Text text_from;
	Button btn_trans;
	Combo combo_type;
	private Button button_clear;
	private Text text_length;
	private Label label_length;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			CTApp window = new CTApp();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		display = Display.getDefault();
		createContents();
		initListener();
		shell.open();
		shell.layout();

		int screenH = Toolkit.getDefaultToolkit().getScreenSize().height;
		int screenW = Toolkit.getDefaultToolkit().getScreenSize().width;
		int shellH = shell.getBounds().height;
		int shellW = shell.getBounds().width;
		if (shellH > screenH)
			shellH = screenH;
		if (shellW > screenW)
			shellW = screenW;
		shell.setLocation(((screenW - shellW) / 2), ((screenH - shellH) / 2));

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell(display, SWT.CLOSE | SWT.MIN);
		shell.setSize(508, 434);
		shell.setText("坐标转换  --by zs");

		Group group_1 = new Group(shell, SWT.NONE);
		group_1.setLocation(10, 0);
		group_1.setSize(482, 98);

		btn_trans = new Button(group_1, SWT.NONE);

		btn_trans.setBounds(310, 25, 70, 30);
		btn_trans.setText("转换");

		combo_type = new Combo(group_1, SWT.READ_ONLY);

		combo_type.setTouchEnabled(true);
		combo_type.setBounds(81, 27, 188, 28);
		combo_type.setItems(new String[] { "直角坐标->经纬度坐标", "经纬度坐标->直角坐标" });
		combo_type.select(0);

		Label label = new Label(group_1, SWT.NONE);
		label.setAlignment(SWT.RIGHT);
		label.setBounds(10, 32, 65, 20);
		label.setText("类型:");

		button_clear = new Button(group_1, SWT.NONE);
		button_clear.setText("清空");
		button_clear.setBounds(386, 25, 70, 30);

		Label label_2 = new Label(group_1, SWT.NONE);
		label_2.setAlignment(SWT.RIGHT);
		label_2.setBounds(10, 61, 65, 20);
		label_2.setText("小数位:");

		text_length = new IntText(group_1, SWT.BORDER);
		text_length.setEnabled(false);
		text_length.setBounds(81, 61, 39, 26);

		label_length = new Label(group_1, SWT.NONE);
		label_length.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		label_length.setBounds(126, 61, 170, 20);
		label_length.setText("最多20位小数,默认6位");

		Label label_1 = new Label(shell, SWT.NONE);
		label_1.setLocation(241, 231);
		label_1.setSize(19, 20);
		label_1.setText("->");

		text_from = new Text(shell, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);

		text_from.setLocation(10, 104);
		text_from.setSize(225, 282);

		text_to = new Text(shell, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		text_to.setEditable(false);
		text_to.setLocation(260, 104);
		text_to.setSize(229, 278);

		text_to.setSelection(0);

		label_length.setVisible(false);
	}

	protected void initListener() {
		btn_trans.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				String from = text_from.getText();
				int type = combo_type.getSelectionIndex();
				String length = text_length.getText();
				int dig_leg = 0;
				if ("".equals(length)) {
					dig_leg = CommonConstant.DEFAULT_LEN;
				} else {
					dig_leg = Integer.valueOf(length);
				}
				// if (type == null || type.equals("")) {
				// MessageBox dialog = new MessageBox(shell, SWT.YES |
				// SWT.ICON_WARNING);
				// dialog.setText("信息");
				// dialog.setMessage("请选择转换类型!");
				// dialog.open();
				// return;
				// }

				if (from == null || from.equals("")) {
					MessageBox dialog = new MessageBox(shell, SWT.YES | SWT.ICON_WARNING);
					dialog.setText("信息");
					dialog.setMessage("没有待转换的坐标!");
					dialog.open();
					return;
				}

				String[] split = from.split("\r\n");
				List<String> outputList = Transformtools.transformAll(split, type, dig_leg);

				String tmp = "";
				for (String outStr : outputList) {
					tmp += outStr + "\n";
				}
				text_to.setText(tmp);
			}
		});

		button_clear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				text_from.setText("");
				text_to.setText("");
			}
		});

		combo_type.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				int type = combo_type.getSelectionIndex();
				if (type == 0) {
					text_length.setEnabled(false);
					label_length.setVisible(false);
					text_length.setText("");
				} else {
					label_length.setVisible(true);
					text_length.setEnabled(true);
					text_length.setText("6");
				}
			}
		});

		text_to.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {

				if (e.stateMask == SWT.CTRL && e.keyCode == 'a') {
					text_to.selectAll();
				} else if (e.stateMask == SWT.CTRL && e.keyCode == 'c') {
					Clipboard clipboard = new Clipboard(display);
					clipboard.setContents(new String[] { text_to.getSelectionText() },
							new Transfer[] { TextTransfer.getInstance() }); // 复制内容
					clipboard.dispose();
				}
			}
		});

		text_from.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.stateMask == SWT.CTRL && e.keyCode == 'a') {
					text_from.selectAll();
				} else if (e.stateMask == SWT.CTRL && e.keyCode == 'c') {
					// Clipboard clipboard = new Clipboard(display);
					// clipboard.setContents(new String[] {
					// text_from.getSelectionText() },
					// new Transfer[] { TextTransfer.getInstance() }); // 复制内容
					// clipboard.dispose();
				} else if (e.stateMask == SWT.CTRL && e.keyCode == 'v') {
					// Clipboard clipboard = new Clipboard(display);
					// String result =
					// clipboard.getContents(TextTransfer.getInstance()).toString();//
					// 获取粘贴
					// text_from.setText("222222");
					// clipboard.dispose();
				}
			}
		});
	}
}
