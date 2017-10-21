package com.tigerwho.view;

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class IntText extends Text {
	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public IntText(Composite parent, int style) {
		super(parent, style);
		this.addVerifyListener(new VerifyListener() {
			@Override
			public void verifyText(VerifyEvent e) {
				try {
					// 以Text中已经输入的内容创建StringBuffer对象
					StringBuffer buffer = new StringBuffer(IntText.this.getText());
					// 删除e.start, e.end指定范围的内容
					// 并将要插入的内容e.text插入指定的位置，模拟输入e.text后Text对象中的内容
					// 末尾添一个0，以保证buffer中只有一个字符为且为+-.时，不会触发NumberFormatException
					buffer.delete(e.start, e.end).insert(e.start, e.text).append('0');
					// 尝试将buffer中的内容转换成Float，如果不抛出异常说明输入内容有效

					if (buffer.toString().startsWith("-")) { // 不能输入负数
						e.doit = false;
					}

					int value = Integer.valueOf(buffer.toString());
					if (value > 200) { // 最多20位数字
						e.doit = false;

					}
				} catch (NumberFormatException ex) {
					e.doit = false;
				}
			}
		});
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}