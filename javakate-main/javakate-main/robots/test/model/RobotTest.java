package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RobotTest {

    Robot robot;
    @Mock
    RobotListener mockSubscriber;

    @BeforeEach
    void setUp() {
        robot = new Robot();
        robot.subscribe(mockSubscriber);
    }
}
