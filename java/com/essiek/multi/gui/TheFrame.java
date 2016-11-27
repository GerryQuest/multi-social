package com.essiek.multi.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.MenuBar;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowAdapter;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicMenuBarUI;
import javax.swing.plaf.basic.BasicMenuItemUI;

import org.cef.CefApp;
import org.cef.CefApp.CefAppState;
/*import org.cef.CefApp.CefAppState;*/
import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefMessageRouter;
import org.cef.browser.CefRequestContext;
import org.cef.handler.CefAppHandlerAdapter;

import com.essiek.multi.handler.MessageRouterHandler;


public class TheFrame extends JFrame {
	private static final long serialVersionUID = -5719532792958777336L;
	private final CefApp cefApp;
	private final CefClient client;
	private final CefBrowser browser;
//	private final CefContextMenuParams context;
//	private final CefRequestContext requestContext;
	private final Component browserUI;
	private final String startPage = "index.html";
	
	private final JMenuBar menuBar = new JMenuBar();
	private final JMenu fileMenu, editMenu, helpMenu;
	private final JMenuItem fileItem1, fileItem2, fileItem3;
	private final JMenuItem helpItem1, helpItem2;
	
	
	class ExitDialog extends JDialog {
		private static final long serialVersionUID = -7245936083509686364L;
		private JButton ok = new JButton("OK");
		private JButton cancel = new JButton("Cancel");
		private JLabel msg = new JLabel("Are you sure you want to exit the application?");
		
		
		
		public ExitDialog (JFrame frame, JFrame theFrame) {
			super(frame, true);
			Color bg = new Color(69, 69, 69);
			Color fg = new Color(255, 255, 255, 170);
			
//			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			setUndecorated(true);
			Point p = new Point(400, 300);
			setLocation(p.x, p.y);
			
			JPanel main = new JPanel();
			JPanel panel = new JPanel();
			panel.setForeground(fg);
			panel.setBackground(bg);
			msg.setForeground(fg);
			panel.add(msg);
			main.add(panel);
			
			
			ok.setBackground(fg);
			cancel.setBackground(fg);
			ok.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.out.println("bye");
					browser.stopLoad();
					browser.close();
					client.dispose();
					CefApp.getInstance().dispose();
					/*cefApp.dispose();*/
//					requestContext.dispose();
					theFrame.dispose();
					/*dispose();*/
					
					
					
					// CefApp takes a few seconds to shutdown
//					Timer timer = new Timer();
//					
//					timer.schedule(new TimerTask() {
//						@Override
//						public void run() {
//							System.out.println("Bye");
//							System.exit(0);
//						}
//					}, 2 * 100);
//					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				}
			});
			cancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getContentPane().setVisible(false);
					dispose();
					frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
					
				}
			});
			JPanel buttonPanel = new JPanel();
//			buttonPanel.setLayout(mgr);
//			buttonPanel.setLayout(new BorderLayout());
			buttonPanel.add(ok);
			buttonPanel.add(cancel);
			buttonPanel.setBackground(bg);
//			
			setLayout(new FlowLayout(FlowLayout.CENTER));
			Border border = BorderFactory.createLineBorder(new Color (41, 41, 41), 2);
//			
			main.setBorder(border);
			main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
			main.add(buttonPanel, BorderLayout.PAGE_END);
			main.setPreferredSize(new Dimension(300, 100));
			getContentPane().add(main);
//			getContentPane();
//			setPreferredSize(new Dimension(300, 400));
//			getContentPane().setPreferredSize(new Dimension(300, 400));
			
			pack();
	        setVisible(true);
		}
	}

	
	public TheFrame(String startURL, boolean useOSR, boolean isTransparent){
		
		CefApp.addAppHandler(new CefAppHandlerAdapter(null) {
			@Override
			public void stateHasChanged(org.cef.CefApp.CefAppState state) {
				if (state == CefAppState.TERMINATED)
					System.exit(0);
			}
		});
		CefSettings settings = new CefSettings();
	    settings.windowless_rendering_enabled = useOSR;
		cefApp = CefApp.getInstance(settings);
		client = cefApp.createClient();
		browser = client.createBrowser(startURL, useOSR, false);
		browserUI = browser.getUIComponent();
//		requestContext = CefRequestContext.getGlobalContext();
		
		// Handle JS calls
		CefMessageRouter router = CefMessageRouter.create();
		router.addHandler(new MessageRouterHandler(), true);
		client.addMessageRouter(router);
		
		browser.loadURL(startURL);
		
		Color fg = new Color(255, 255, 255, 170);
		Color background = new Color(51, 51, 51);
		
		fileMenu = new JMenu("File");
		fileMenu.setForeground(fg);
		fileMenu.setBackground(background);
		Border border = BorderFactory.createLineBorder(new Color (31, 31, 31));
		fileMenu.getPopupMenu().setBorder(border);
		
		
		fileItem1 = new JMenuItem("New User", KeyEvent.VK_N);
		fileItem1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		fileItem2 = new JMenuItem("Export");
		fileItem3 = new JMenuItem("Exit", KeyEvent.VK_E);
		fileItem1.setBorder(null);
		fileItem2.setBorder(null);
		fileItem3.setBorder(null);
		
		fileItem1.setForeground(fg);
		fileItem2.setForeground(fg);
		fileItem3.setForeground(fg);
		fileItem1.setBackground(background);
		fileItem2.setBackground(background);
		fileItem3.setBackground(background);
		
		fileItem3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("BYE BYE");
				browser.close();
				client.dispose();
				CefApp.getInstance().dispose();
				dispose();
			}
			
		});
		
		JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
		/*Color sepColour = new Color(234, 159, 29);*/
		Color sepFore = new Color(255, 162, 0);
		sep.setBackground(sepFore);
		sep.setForeground(null);
		sep.setBorder(null);
		
		fileMenu.add(fileItem1);
		/*fileMenu.addSeparator();*/
		fileMenu.add(fileItem2);
		fileMenu.add(sep);
		fileMenu.add(fileItem3);
		
		editMenu = new JMenu("Edit");
		editMenu.setMnemonic(KeyEvent.VK_E);
		editMenu.setForeground(fg);
		editMenu.setBackground(background);
		
		helpMenu = new JMenu("Help");
		helpMenu.setForeground(fg);
		helpMenu.setBackground(background);
		helpMenu.getPopupMenu().setBorder(null);
		helpMenu.setBorder(border);
		
		helpMenu.setMnemonic(KeyEvent.VK_H);
		helpItem1 = new JMenuItem("Help");
		helpItem1.setBackground(background);
		helpItem1.setForeground(fg);
		helpItem1.setBorder(border);
		
		helpItem2 = new JMenuItem("About");
		helpItem2.setBackground(background);
		helpItem2.setForeground(fg);
		helpItem2.setBorder(null);
		
		helpMenu.add(helpItem1);
		helpMenu.add(helpItem2);
		
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(helpMenu);
		
		// Customizations for Menubar
		
		menuBar.setBackground(background);
		menuBar.setOpaque(true);
		menuBar.setBorderPainted(false);
		
		
		
		this.setJMenuBar(menuBar);
		this.getContentPane().add(browserUI);
		this.setSize(1000, 600);
		this.setVisible(true);
		this.setDefaultCloseOperation(this.DO_NOTHING_ON_CLOSE);
		/*JFrame frame = this;*/
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				JFrame frame = (JFrame) e.getSource();
				/*JOptionPane pane = new JOptionPane();
				JLabel msg = new JLabel("Are you sure you want to exit the application?");
				msg.setForeground(fg);
				int result = JOptionPane.showConfirmDialog(
						frame, msg,
						"Exit Application",
						JOptionPane.YES_NO_OPTION);
				ExitDialog exit = new ExitDialog(new JFrame());
				if (result == JOptionPane.YES_OPTION) {
					
//					browser.stopLoad();
					cefApp.dispose();
					dispose();
					System.exit(0);
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				}*/
//				browser.stopLoad();
//				browser.close();
//				client.dispose();
//				CefApp.getInstance().dispose();
//		        dispose();
				ExitDialog exit = new ExitDialog(new JFrame(), frame);
//				CefApp.getInstance().dispose();
//				dispose();
				
				
			}
		});
		
	}

}
