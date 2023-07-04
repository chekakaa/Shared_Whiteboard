package JoinWhiteBoard;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import CreateWhiteBoard.Shape;
import rmiRemote.IRemoteWhiteBoard;
import java.lang.Math;
/**
 * @author Rui Liu 1111181
 * @create 2022-05-24 15:35
 */
// to implement image painting function and synchronizes images to all users by sending pictures.
public class sketchpad extends JPanel{
    private String type = "line";
    private int x,y;
    private Color selectColor = Color.BLACK;
    private Stroke selectStroke = new BasicStroke(1.0f);
    private IRemoteWhiteBoard whiteBoard;
    private BufferedImage image;

    //Points record the trace of pencil and erase
    private static ArrayList<Point> points = new ArrayList<Point>();
    //shapelist record all shapes in whiteboard
    private static ArrayList<Shape> shapelist = new ArrayList<Shape>();

    public void setwb(IRemoteWhiteBoard whiteBoard) {
        this.whiteBoard = whiteBoard;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setStroke(Stroke stroke) {
        this.selectStroke = stroke;
    }
    public void setColor(Color color){
        this.selectColor = color;
    }
    //new
    public void clear() {
        shapelist = new ArrayList<Shape>();
        image = null;
    }
    //save the image
    public BufferedImage save() {
        Dimension imageSize = this.getSize();
        BufferedImage image = new BufferedImage(imageSize.width,imageSize.height,BufferedImage.TYPE_INT_BGR);
        Graphics2D graphics = image.createGraphics();//draw the image
        this.paint(graphics);
        graphics.dispose();
        return image;
    }
    //load the image from other resource
    public void load(BufferedImage image) {
        clear();
        repaint();
        this.image = image;
    }

    // to keep shapes on graphic
    public void paint(Graphics g){
        super.paint(g);
        if(image != null) {
            g.drawImage(image, 0, 0, this);
        }
        for (int i = 0; i < shapelist.size(); i++) {
            if (shapelist.get(i) == null) {break;}
            shapelist.get(i).rePaint(g);
        }
    }
    // to draw a image
    public void draw(int x,int y,int x1,int y1,String type) {
        Graphics2D g = (Graphics2D)getGraphics();
        g.setColor(selectColor);
        g.setStroke(selectStroke);
        if(type.equals("line")) {
            shapelist.add(new Shape(g,x,y,x1,y1,type,selectColor,selectStroke));
            g.drawLine(x,y, x1, y1);
        }
        else {
            int height = Math.abs(y1 - y);
            int width = Math.abs(x1 - x);
            if(type.equals("rect")) {
                shapelist.add(new Shape(g,x,y,x1,y1,type,selectColor,selectStroke));
                g.drawRect(Math.min(x, x1),Math.min(y, y1), width, height);
            }
            if(type.equals("tri")) {
                shapelist.add(new Shape(g,x,y,x1,y1,type,selectColor,selectStroke));
                g.drawLine(x,y1, x1, y1);
                g.drawLine(x,y1,(x1+x)/2,y);
                g.drawLine((x1+x)/2,y,x1,y1);
            }
            if(type.equals("oval")) {
                shapelist.add(new Shape(g,x,y,x1,y1,type,selectColor,selectStroke));
                g.drawOval(Math.min(x, x1),Math.min(y, y1), width, height);
            }
            if(type.equals("pencil")) {
                ArrayList<Point> s = new ArrayList<Point>(1000);
                s.addAll(points);
                shapelist.add(new Shape(g,s,type,selectColor,selectStroke));
            }
            if(type.equals("erase")) {
                ArrayList<Point> s = new ArrayList<Point>(1000);
                s.addAll(points);
                shapelist.add(new Shape(g,s,type,Color.white,selectStroke));
            }
            if(type.equals("circle")) {
                shapelist.add(new Shape(g,x,y,x1,y1,type,selectColor,selectStroke));
                int round = Math.max(width, height);
                g.drawOval(Math.min(x, x1),Math.min(y, y1), round,round);
            }
            points.clear();
        }
    }
    //synchronize the board with other users
    public void synchronize() {
        try {
            //wb.draw(list);
            BufferedImage image = save();
            ByteArrayOutputStream paint = new ByteArrayOutputStream();
            ImageIO.write(image,"png", paint);
            byte[] bytes = paint.toByteArray();
            whiteBoard.draw(bytes);
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null, "The manager has left the room", "error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public sketchpad() {
        addMouseListener(new MouseListener() {


                             @Override
                             public void mousePressed(MouseEvent e) {
                                 //press,get start position
                                 x = e.getX();
                                 y = e.getY();
                                 if(whiteBoard !=null && type.equals("text")){
                                     Graphics2D g = (Graphics2D)getGraphics();
                                     String textType;
                                     textType = JOptionPane.showInputDialog(
                                             "Please input the text you want!");
                                     if(textType != null) {
                                         g.setColor(selectColor);
                                         g.drawString(textType,x,y);
                                         shapelist.add(new Shape(g,x,y,textType,type,selectColor));
                                         synchronize();
                                     }
                                 }
                             }
                             @Override
                             public void mouseReleased(MouseEvent e) {
                                 if(whiteBoard != null) {
                                     int x1 = e.getX();
                                     int y1 = e.getY();
                                     String text = (x + " " + y + " " + x1 + " " + y1 + " " + type);
                                     draw(x, y, x1, y1, type);
                                     synchronize();
                                 }
                             }

                             @Override
                             public void mouseClicked(MouseEvent e) {

                             }

                             @Override
                             public void mouseEntered(MouseEvent e) {

                             }

                             @Override
                             public void mouseExited(MouseEvent e) {

                             }

                         }
        );
        addMouseMotionListener(new MouseMotionListener() {
                                   @Override
                                   public void mouseDragged(MouseEvent e) {
                                       if(whiteBoard != null) {
                                           int x2 = e.getX();
                                           int y2 = e.getY();
                                           int x3;
                                           int y3;
                                           Graphics2D g = (Graphics2D)getGraphics();
                                           g.setColor(selectColor);
                                           g.setStroke(selectStroke);
                                           //pencil and erase
                                           if(type.equals("pencil")){
                                               if(points.size()!=0){
                                                   x3=points.get(points.size()-1).x;
                                                   y3=points.get(points.size()-1).y;}
                                               else{
                                                   x3=x;
                                                   y3=y;
                                               }
                                               g.drawLine(x3,y3,x2,y2);
                                               points.add(new Point(x2,y2));
                                           }
                                           else if(type.equals("erase")){
                                               if(points.size()!=0){
                                                   x3=points.get(points.size()-1).x;
                                                   y3=points.get(points.size()-1).y;}
                                               else{
                                                   x3=x;
                                                   y3=y;
                                               }
                                               Color color = new Color(selectColor.getRGB());
                                               g.setColor(Color.WHITE);
                                               g.drawLine(x3,y3,x2,y2);
                                               points.add(new Point(x2,y2));
                                               g.setColor(color);
                                           }
                                           //Other shapes
                                           else {
                                               if (type.equals("line")) {
                                                   g.drawLine(x, y, x2, y2);
                                               }
                                               else {
                                                   int height = Math.abs(y2 - y);
                                                   int width = Math.abs(x2 - x);
                                                   if (type.equals("rect")) {
                                                       g.drawRect(Math.min(x, x2), Math.min(y, y2), width, height);
                                                   }
                                                   if (type.equals("tri")) {
                                                       g.drawLine(x, y2, x2, y2);
                                                       g.drawLine(x,y2,(x2+x)/2,y);
                                                       g.drawLine((x2+x)/2,y,x2,y2);
                                                   }
                                                   if (type.equals("oval")) {
                                                       g.drawOval(Math.min(x, x2), Math.min(y, y2), width, height);
                                                   }
                                                   if (type.equals("circle")) {
                                                       int round = Math.max(width, height);
                                                       g.drawOval(Math.min(x, x2),Math.min(y, y2), round,round);
                                                   }
                                               }
                                               repaint();
                                           }
                                       }
                                   }
                                   @Override
                                   public void mouseMoved(MouseEvent e) {
                                   }
                               }
        );
    }
}
