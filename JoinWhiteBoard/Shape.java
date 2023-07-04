package JoinWhiteBoard;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author Rui Liu 1111181
 * @create 2022-05-24 15:31
 */

// to keep the original painting function after minimization.
public class Shape {
    private int x, x1, y, y1;
    private String type;
    private Color color;
    private ArrayList<Point> pencil;
    private Stroke stroke;
    private String input;
    //This Shape is design for rect, oval, circle
    public Shape(Graphics g, int x, int y, int x1, int y1, String type, Color color, Stroke stroke)
    {
        this.x = x;
        this.y = y;
        this.x1 = x1;
        this.y1 = y1;
        this.type = type;
        this.color = color;
        this.stroke = stroke;

    }
    //This Shape is design for pencil, erase
    public Shape(Graphics g, ArrayList<Point> pencil, String type, Color color, Stroke stroke)
    {
        this.pencil = pencil;
        this.type = type;
        this.color = color;
        this.stroke = stroke;
    }
    //Repaint function

    public Shape(Graphics g, int x, int y, String in, String type, Color color)
    {
        this.x =x;
        this.y = y;
        this.input = in;
        this.type = type;
        this.color =color;
    }

    public void rePaint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if(type.equals("text")){
            g.setColor(color);
            g.drawString(input,x,y);
        }
        else {
            g2.setColor(color);
            g2.setStroke(stroke);
            switch (type) {
                case "line":
                    g.drawLine(x, y, x1, y1);
                    break;
                case "rect":
                    g.drawRect(Math.min(x, x1), Math.min(y, y1), Math.abs(x - x1), Math.abs(y - y1));
                    break;
                case "tri":
                    g.drawLine(x, y1, x1, y1);
                    g.drawLine(x, y1, (x1 + x) / 2, y);
                    g.drawLine((x1 + x) / 2, y, x1, y1);
                    break;
                case "oval":
                    g.drawOval(Math.min(x, x1), Math.min(y, y1), Math.abs(x - x1), Math.abs(y - y1));
                    break;
                case "circle":
                    g.drawOval(Math.min(x, x1), Math.min(y, y1), Math.max(Math.abs(x - x1), Math.abs(y - y1)), Math.max(Math.abs(x - x1), Math.abs(y - y1)));
                    break;
                case "pencil":
                    for (int i = 1; i < pencil.size(); i++) {
                        g.drawLine(pencil.get(i - 1).x, pencil.get(i - 1).y, pencil.get(i).x, pencil.get(i).y);
                    }
                    break;
                case "erase":
                    for (int i = 1; i < pencil.size(); i++) {
                        g.drawLine(pencil.get(i - 1).x, pencil.get(i - 1).y, pencil.get(i).x, pencil.get(i).y);
                    }
                    break;
            }
        }
    }

    public String getType(){
        return type;
    }
}
