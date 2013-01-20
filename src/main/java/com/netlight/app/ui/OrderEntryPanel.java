/*******************************************************************************
 * Copyright (c) quickfixengine.org  All rights reserved. 
 * 
 * This file is part of the QuickFIX FIX Engine 
 * 
 * This file may be distributed under the terms of the quickfixengine.org 
 * license as defined by quickfixengine.org and appearing in the file 
 * LICENSE included in the packaging of this file. 
 * 
 * This file is provided AS IS with NO WARRANTY OF ANY KIND, INCLUDING 
 * THE WARRANTY OF DESIGN, MERCHANTABILITY AND FITNESS FOR A 
 * PARTICULAR PURPOSE. 
 * 
 * See http://www.quickfixengine.org/LICENSE for licensing information. 
 * 
 * Contact ask@quickfixengine.org if any conditions of this licensing 
 * are not clear to you.
 ******************************************************************************/

package com.netlight.app.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import quickfix.SessionID;
import com.netlight.app.BanzaiApplication;
import com.netlight.app.DoubleNumberTextField;
import com.netlight.app.IntegerNumberTextField;
import com.netlight.app.LogonEvent;
import com.netlight.app.Order;
import com.netlight.app.OrderSide;
import com.netlight.app.OrderTIF;
import com.netlight.app.OrderTableModel;
import com.netlight.app.OrderType;

public class OrderEntryPanel extends JPanel implements Observer {
    private boolean symbolEntered = false;
    private boolean quantityEntered = false;
    private boolean limitEntered = false;
//    private boolean stopEntered = false;
    private boolean sessionEntered = false;

//    private JTextField symbolTextField = new JTextField();
    String [] symbols = {"ERIC B XSTO", "ERIC B BURG"};
    private JComboBox<String> symbolComboBox = new JComboBox<String>(symbols);
    
    private IntegerNumberTextField quantityTextField =
        new IntegerNumberTextField();

    private JComboBox<OrderSide> sideComboBox = new JComboBox(OrderSide.getSimpleSides());
//    private JComboBox typeComboBox = new JComboBox(OrderType.toArray());
//    private JComboBox tifComboBox = new JComboBox(OrderTIF.toArray());
    private JLabel typeBox = new JLabel("   LIMIT");
    private JLabel tifBox = new JLabel("   DAY");

    private DoubleNumberTextField limitPriceTextField =
        new DoubleNumberTextField();
//    private DoubleNumberTextField stopPriceTextField =
//        new DoubleNumberTextField();

    private JComboBox sessionComboBox = new JComboBox();

    private JLabel limitPriceLabel = new JLabel("Limit (Price)");
//    private JLabel stopPriceLabel = new JLabel("Stop");

    private JLabel messageLabel = new JLabel(" ");
    private JButton submitButton = new JButton("Submit");
    
    private JButton startStrategyButton = new JButton("Start Strategy");
    private JButton stopStrategyButton = new JButton("Stop Strategy");

    private OrderTableModel orderTableModel = null;
    private transient BanzaiApplication application = null;

    private GridBagConstraints constraints = new GridBagConstraints();

    public OrderEntryPanel(final OrderTableModel orderTableModel,
                           final BanzaiApplication application) {
        setName("OrderEntryPanel");
        this.orderTableModel = orderTableModel;
        this.application = application;

        application.addLogonObserver(this);

        SubmitActivator activator = new SubmitActivator();
//        symbolTextField.addKeyListener(activator);
        quantityTextField.addKeyListener(activator);
        limitPriceTextField.addKeyListener(activator);
//        stopPriceTextField.addKeyListener(activator);
        sessionComboBox.addItemListener(activator);

        setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        setLayout(new GridBagLayout());
        createComponents();
    }

    public void addActionListener(ActionListener listener) {
        submitButton.addActionListener(listener);
    }

    public void setMessage(String message) {
        messageLabel.setText(message);
        if(message == null || message.equals(""))
            messageLabel.setText(" ");
    }

    public void clearMessage() {
        setMessage(null);
    }

    private void createComponents() {
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;

        int x = 0;
        int y = 0;
        
        startStrategyButton.setEnabled(true);
        startStrategyButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				application.getTradingStrategy().OnStrategyStart();
				
			}
		});
        stopStrategyButton.setEnabled(true);
        stopStrategyButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				application.getTradingStrategy().OnStrategyStop();				
			}
		});
        
        add(startStrategyButton, x=0, ++y);
        add(stopStrategyButton, ++x, y);
        

        add(new JLabel("Side"), x=0, ++y);
        add(new JLabel("Quantity"), ++x, y );
        add(new JLabel("Symbol (Stock)"), ++x, y );
        add(limitPriceLabel, ++x, y );
        constraints.ipadx = 30;
        add(new JLabel("Type"), ++x, y );
//        add(stopPriceLabel, ++x, y );
        constraints.ipadx = 0;
        add(new JLabel("TIF"), ++x, y);
        constraints.ipadx = 30;

        sideComboBox.setName("SideComboBox");
        add(sideComboBox, x=0, ++y);
        quantityTextField.setName("QuantityTextField");
        add(quantityTextField, ++x, y);
//        symbolTextField.setName("SymbolTextField");
//        add(symbolTextField, ++x, y);
        symbolComboBox.setName("SymbolComboBox");
        add(symbolComboBox, ++x, y);
        
        constraints.ipadx = 0;
        limitPriceTextField.setName("LimitPriceTextField");
        add(limitPriceTextField, ++x, y);
        
//        typeComboBox.setName("TypeComboBox");
        add(typeBox, ++x, y);
        
//        stopPriceTextField.setName("StopPriceTextField");
//        add(stopPriceTextField, ++x, y);
//        tifComboBox.setName("TifComboBox");
        add(tifBox, ++x, y);

        constraints.insets = new Insets(3, 0, 0, 0);
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        sessionComboBox.setName("SessionComboBox");
        add(sessionComboBox, 0, ++y);
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        submitButton.setName("SubmitButton");
        add(submitButton, x, y);
        constraints.gridwidth = 0;
        
        
        
        add(messageLabel, 0, ++y);

//        typeComboBox.addItemListener(new PriceListener());
//        typeComboBox.setSelectedItem(OrderType.STOP);
//        typeComboBox.setSelectedItem(OrderType.MARKET);

        Font font = new Font(messageLabel.getFont().getFontName(),
                             Font.BOLD, 12);
        messageLabel.setFont(font);
        messageLabel.setForeground(Color.red);
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        submitButton.setEnabled(false);
        submitButton.addActionListener(new SubmitListener());
        activateSubmit();
    }

    private JComponent add(JComponent component, int x, int y) {
        constraints.gridx = x;
        constraints.gridy = y;
        add(component, constraints);
        return component;
    }

    private void activateSubmit() {
//        OrderType type = (OrderType) typeComboBox.getSelectedItem();
        boolean activate = symbolEntered && quantityEntered
                           && sessionEntered;
        
//        if(type == OrderType.LIMIT)
          submitButton.setEnabled(activate && limitEntered);
//        if(type == OrderType.MARKET)
//            submitButton.setEnabled(activate);
//        else if(type == OrderType.LIMIT)
//            submitButton.setEnabled(activate && limitEntered);
//        else if(type == OrderType.STOP)
//            submitButton.setEnabled(activate && stopEntered);
//        else if(type == OrderType.STOP_LIMIT)
//            submitButton.setEnabled(activate && limitEntered
//                                    && stopEntered);
    }

//    private class PriceListener implements ItemListener {
//        public void itemStateChanged(ItemEvent e) {
//            OrderType item = (OrderType) typeComboBox.getSelectedItem();
//            if (item == OrderType.MARKET) {
//                enableLimitPrice(false);
//                enableStopPrice(false);
//            } else if(item == OrderType.STOP) {
//                enableLimitPrice(false);
//                enableStopPrice(true);
//            } else if(item == OrderType.LIMIT) {
//                enableLimitPrice(true);
//                enableStopPrice(false);
//            } else {
//                enableLimitPrice(true);
//                enableStopPrice(true);
//            }
//            activateSubmit();
//        }

//        private void enableLimitPrice(boolean enabled) {
//            Color labelColor = enabled ? Color.black : Color.gray;
//            Color bgColor = enabled ? Color.white : Color.gray;
//            limitPriceTextField.setEnabled(enabled);
//            limitPriceTextField.setBackground(bgColor);
//            limitPriceLabel.setForeground(labelColor);
//        }

//        private void enableStopPrice(boolean enabled) {
//            Color labelColor = enabled ? Color.black : Color.gray;
//            Color bgColor = enabled ? Color.white : Color.gray;
//            stopPriceTextField.setEnabled(enabled);
//            stopPriceTextField.setBackground(bgColor);
//            stopPriceLabel.setForeground(labelColor);
//        }
//    }

    public void update(Observable o, Object arg) {
        LogonEvent logonEvent = (LogonEvent)arg;
        if(logonEvent.isLoggedOn())
            sessionComboBox.addItem(logonEvent.getSessionID());
        else
            sessionComboBox.removeItem(logonEvent.getSessionID());
    }

    private class SubmitListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Order order = new Order();
            order.setSide((OrderSide) sideComboBox.getSelectedItem());
            
            // Day and trading types are fixed for simplicity
            order.setType((OrderType) OrderType.LIMIT);
            order.setTIF((OrderTIF) OrderTIF.DAY);

//            order.setSymbol(symbolTextField.getText());
            order.setSymbol((String)symbolComboBox.getSelectedItem());

            order.setQuantity(Integer.parseInt
                              (quantityTextField.getText()));
            order.setOpen(order.getQuantity());
            
            OrderType type = order.getType();
            if(type == OrderType.LIMIT || type == OrderType.STOP_LIMIT)
                order.setLimit(limitPriceTextField.getText());
//            if(type == OrderType.STOP || type == OrderType.STOP_LIMIT)
//                order.setStop(stopPriceTextField.getText());
            order.setSessionID((SessionID)sessionComboBox.getSelectedItem());

            orderTableModel.addOrder(order);
            application.send(order);

        }
    }

    private class SubmitActivator
        implements KeyListener, ItemListener {
        public void keyReleased(KeyEvent e) {
            Object obj = e.getSource();
//            if(obj == symbolTextField) {
//                symbolEntered = testField(obj);
//            } else 
        	if(obj == quantityTextField) {
                quantityEntered = testField(obj);
            } else if(obj == limitPriceTextField) {
                limitEntered = testField(obj);
//            } else if(obj == stopPriceTextField) {
//                stopEntered = testField(obj);
            }
            activateSubmit();
        }

        public void itemStateChanged(ItemEvent e) {
            sessionEntered = sessionComboBox.getSelectedItem() != null;
            activateSubmit();
        }

        private boolean testField(Object o) {
            String value = ((JTextField)o).getText();
            value = value.trim();
            return value.length() > 0;
        }

        public void keyTyped(KeyEvent e) {}
        public void keyPressed(KeyEvent e) {}
    }
}
