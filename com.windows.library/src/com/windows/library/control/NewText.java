package com.windows.library.control;
/**
 * NewText：支持垂直居中的Text；
 * 说明：不支持换行；
 * @author Ray
 */




import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;


public class NewText extends Composite implements ControlListener, MouseTrackListener, MouseListener {


private Text text;
private int style;
private Color textBackground;
private Color background;


private int rightPadding = 0;
private int leftPadding = 0;


public NewText(Composite parent, int style) {
super(parent, style);
this.style = style;
this.setCursor(new Cursor(Display.getDefault(), SWT.CURSOR_IBEAM));
createText(style);
textBackground = text.getBackground();
background =new Color(getDisplay(), 255, 255, 255);
super.setBackground(background);
addControlListener(this);
addMouseTrackListener(this);
addMouseListener(this);
textAddListener();
}


private void createText(int style) {
if (hasBorder())
text = new Text(this, setBorderBitTo0(style));
else
text = new Text(this, style);
}


private void textAddListener() {
text.addMouseTrackListener(this);
text.addFocusListener(new FocusListener() {
@Override
public void focusLost(FocusEvent e) {
setLostBackground();
}


@Override
public void focusGained(FocusEvent e) {
setGainedBackground();
}
});
}


/**
* 去掉style中的SWT.BORDER
* 
* @return
*/


private int setBorderBitTo0(int style) {
return style & ~SWT.BORDER;
}


/**
* 判断传入的style是否包含边框
* 
* @return
*/


private boolean hasBorder() {
return (style & SWT.BORDER) != 0;
}


@Override
public Point computeSize(int wHint, int hHint, boolean changed) {
Point pt = text.computeSize(wHint, hHint);
return new Point(pt.x + 4, pt.y + 4);
}


@Override
public void controlMoved(ControlEvent e) {
}


@Override
public void controlResized(ControlEvent e) {
setTextBounds();
}


private int getY() {
return (getSize().y - text.getSize().y) / 2;
}


private void setTextBounds() {
text.pack();
if (hasBorder()) {
text.setLocation(2 + leftPadding, getY() - 2);
text.setSize(getBounds().width - 8 + (rightPadding + leftPadding), text.getSize().y);
} else {
text.setLocation(0 + leftPadding, getY());
text.setSize(getBounds().width - 2 + (rightPadding + leftPadding), text.getSize().y);
}
}


@Override
public void pack() {
text.pack();
if (hasBorder()) {
setSize(text.getSize().x + 4, text.getSize().y + 8);
} else {
setSize(text.getSize().x, text.getSize().y);
}
}


public void setText(String string) {
text.setText(string);
}


public void setFont(Font font) {
text.setFont(font);
setTextBounds();
}


public String getText() {
return text.getText();
}


public void selectAll() {
text.selectAll();
}


public boolean forceFocus() {
return text.forceFocus();
}


public boolean setFocus() {
return text.setFocus();
}


@Override
public void setBackground(Color color) {
textBackground = color;
background = color;
super.setBackground(color);
text.setBackground(color);
}


@Override
public void setForeground(Color color) {
super.setForeground(color);
text.setForeground(color);
}


@Override
public void addFocusListener(FocusListener listener) {
text.addFocusListener(listener);
}


public void addSelectionListener(SelectionListener listener) {
text.addSelectionListener(listener);
}


public void addModifyListener(ModifyListener listener) {
text.addModifyListener(listener);
}


public void addVerifyListener(VerifyListener listener) {
text.addVerifyListener(listener);
}

public void removeModifyListener(ModifyListener listener) {
text.removeModifyListener(listener);
}


public void removeVerifyListener(VerifyListener listener) {
text.removeVerifyListener(listener);
}

public boolean getEditable() {
  return text.getEditable();
}

public void setEditable(boolean editable) {
	  text.setEditable(editable);
	  if(editable)text.setForeground(new Color(null,0,0,0));
	  else this.setForeground(new Color(null,100,100,100));
	}


private void setGainedBackground() {
super.setBackground(textBackground);
}


private void setLostBackground() {
super.setBackground(background);
}


@Override
public void mouseDown(MouseEvent e) {
relocate(e);
}


public void relocate(MouseEvent me) {
if (me.widget != this)
return;


final Point pt = getDisplay().map(this, null, me.x, getSize().y / 2);


Event event = new Event();
event.widget = text;
event.type = SWT.MouseMove;
event.x = pt.x;
event.y = pt.y;
getDisplay().post(event);


event.type = SWT.MouseDown;
event.button = 1;
getDisplay().post(event);


event.type = SWT.MouseUp;
getDisplay().post(event);
}


@Override
public void mouseUp(MouseEvent e) {
}


@Override
public void mouseEnter(MouseEvent e) {
}


@Override
public void mouseExit(MouseEvent e) {


}


@Override
public void mouseHover(MouseEvent e) {


}


@Override
public void mouseDoubleClick(MouseEvent e) {


}


public int getRightPadding() {
return rightPadding;
}


public void setRightPadding(int rightPadding) {
this.rightPadding = rightPadding;
}


public int getLeftPadding() {
return leftPadding;
}


public void setLeftPadding(int leftPadding) {
this.leftPadding = leftPadding;
}


}