package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MainExecutionTest {

    @Test
    public void main() {
        ShareItServer.main(new String[] {});
    }
}
