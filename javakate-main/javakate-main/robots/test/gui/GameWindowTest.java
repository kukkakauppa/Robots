package gui;

import model.Robot;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;

import javax.swing.*;

import static org.junit.Assert.assertNotNull;

public class GameWindowTest extends AssertJSwingJUnitTestCase {
    private FrameFixture window;

    @Override
    protected void onSetUp() {
        JFrame frame = GuiActionRunner.execute(() -> {
            JFrame f = new JFrame();
            f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            Robot robot = new Robot();
            GameWindow gameWindow = new GameWindow(robot);
            f.getContentPane().add(gameWindow);
            f.pack();
            return f;
        });
        window = new FrameFixture(robot(), frame);
        window.show(); // shows the frame to test
    }

    @Test
    public void testShouldContainGameVisualizer() {
        setUpRobot(); // java.lang.UnsatisfiedLinkError at GameWindowTest.java:33
        JFrame frame = GuiActionRunner.execute(() -> {
            JFrame f = new JFrame();
            f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            Robot robot = new Robot();
            GameWindow gameWindow = new GameWindow(robot);
            f.getContentPane().add(gameWindow);
            f.pack();
            return f;
        });
        window = new FrameFixture(robot(), frame);
        window.show(); // shows the frame to test
        assertNotNull(this.window);
        //TODO: window.panel("Игровое поле").requireVisible();
    }

    @Override
    protected void onTearDown() {
        window.cleanUp();
    }
}
