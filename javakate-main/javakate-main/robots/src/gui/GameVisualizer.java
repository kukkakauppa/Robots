package gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.Timer;
import java.util.TimerTask;
import model.IRobot;

import javax.swing.*;
import static java.awt.Color.*;

import static gui.Geom.round;

public class GameVisualizer extends JPanel {
    private final model.IRobot robot;

    public GameVisualizer(model.IRobot r) {
        this.robot = r;
        Timer timer = initTimer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onRedrawEvent();
            }
        }, 0, 50);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onModelUpdateEvent();
            }
        }, 0, 50);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                robot.setTarget(e.getPoint());
                repaint();
            }
        });
        setDoubleBuffered(true);
    }

    private static Timer initTimer() {
        return new Timer("events generator", true);
    }

    protected void onRedrawEvent() {
        EventQueue.invokeLater(this::repaint);
    }

    protected void onModelUpdateEvent() {
        robot.go();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        drawRobot(g2d, robot);
        drawTarget(g2d, robot);
    }

    private void drawRobot(Graphics2D g, IRobot r) {
        int centerX = round(r.getX());
        int centerY = round(r.getY());
        double direction = r.getAngle();
        AffineTransform t = AffineTransform.getRotateInstance(direction, centerX, centerY);
        g.setTransform(t);
        g.setColor(MAGENTA);
        fillOval(g, centerX, centerY, 30, 10);
        g.setColor(BLACK);
        drawOval(g, centerX, centerY, 30, 10);
        g.setColor(WHITE);
        fillOval(g, centerX + 10, centerY, 5, 5);
        g.setColor(BLACK);
        drawOval(g, centerX + 10, centerY, 5, 5);
    }

    private void drawTarget(Graphics2D g, IRobot robot) {
        int x = robot.getTargetX();
        int y = robot.getTargetY();
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(GREEN);
        fillOval(g, x, y, 5, 5);
        g.setColor(BLACK);
        drawOval(g, x, y, 5, 5);
    }

    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }
}
