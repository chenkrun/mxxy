package com.mxxy.game.utils;

import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.mxxy.game.base.Panel;
import com.mxxy.game.event.PanelEvent;
import com.mxxy.game.ui.IWindows;
import com.mxxy.game.widget.ImageComponentButton;
import com.mxxy.game.widget.Label;
import com.mxxy.game.widget.PromptLabel;

/**
 * UI 辅助
 * 
 * @author ZAB 邮箱 ：624284779@qq.com
 */
public class UIHelp {

	static {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			/** 自动匹配当前系统风格 */
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private IWindows windows;

	public UIHelp(IWindows windows) {
		this.windows = windows;
	}

	public void showPanel(String key) {
		Panel panel = getPanel(key);
		if (panel != null) {
			showPanel(panel);
		}
	}

	/**
	 * 显示面板
	 * 
	 * @param dialog
	 */
	public void showPanel(Panel panel) {
		Container canvas = windows.getContainers();
		if (panel != null && panel.getParent() != canvas) {
			panel.handleEvent(new PanelEvent(panel, PanelEvent.INITIAL)); // 触发面板init事件
			canvas.add(panel, 0);
		}
	}

	/**
	 * 隐藏面板
	 * 
	 * @param panel
	 */
	public void hidePanel(Panel panel) {
		if (panel != null) {
			Container canvas = windows.getContainers();
			if (panel.getParent() == canvas) {
				canvas.remove(panel);
				/* Confuse: 需要下面这句事件分发语句吗？ */
				// panel.fireEvent(new PanelEvent(panel, PanelEvent.DISPOSE)); // 触发面板dispose事件
				PanelManager.dispose(panel.getName(), panel);
			}
		}
	}

	public void hidePanel(String key) {
		Panel panel = getPanel(key);
		if (panel != null) {
			hidePanel(panel);
		}
	}

	public void switchPanel(Panel panel) {
		if (panel.isShowing()) {
			hidePanel(panel);
		} else {
			showPanel(panel);
		}
	}

	public Panel getPanel(String id) {
		Panel dlg = PanelManager.getPanel(id);
		return dlg;
	}

	public Panel getPanel(ActionEvent event) {
		Panel dlg = PanelManager.getPanel(((ImageComponentButton) event
				.getSource()).getName());
		return dlg;
	}

	
	private List<PromptLabel> prompts = new ArrayList<PromptLabel>();

	public void prompt(JComponent component, String text, long delay) {
		final PromptLabel label = new PromptLabel(text);
		ImageIcon icon = new ImageIcon("res/componentsRes/tts.png");
		Label label2 = new Label(null, icon, 0);
		int offset = prompts.size() * 15;
		label2.setBounds(240 + offset, 210 + offset, icon.getIconWidth(),
				icon.getIconHeight());
		label.setLocation((Constant.WINDOW_WIDTH - label.getWidth()) / 2
				+ offset, 230 + offset);
		Container jComponent = component == null ? windows.getContainers()
				: component;
		jComponent.add(label, 0);
		jComponent.add(label2, 0);
		prompts.add(label);
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				jComponent.remove(label);
				jComponent.remove(label2);
				prompts.set(prompts.indexOf(label), null);
				boolean empty = true;
				for (int i = 0; i < prompts.size(); i++) {
					if (prompts.get(i) != null) {
						empty = false;
					}
				}
				if (empty) {
					prompts.clear();
				}
			}
		};
		new Timer().schedule(task, delay);
	}
	
	public void showToolTip(JComponent c,JComponent src,MouseEvent e) {
		final Container canvas = windows.getContainers();
		Point p = SwingUtilities.convertPoint(src, src.getToolTipLocation(e), canvas);
		p.translate(20, 30);
		p.x = (p.x+c.getWidth() < canvas.getWidth()-2)?p.x:(canvas.getWidth()-c.getWidth()-2);
		p.y = (p.y+c.getHeight() < canvas.getHeight() - 2)?p.y:(canvas.getHeight()-c.getHeight()-2);
		c.setLocation(p);
		canvas.add(c,0);
	}
	
	/**
	 * 隐藏tooltip
	 * @param c
	 */
	public void hideToolTip(JComponent c) {
		final Container canvas = windows.getContainers();
		canvas.remove(c);		
	}
}
