import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TesteDoGPT {

    private static WebDriver driver;

    @BeforeAll
    public static void setUp() {
        driver = new ChromeDriver();
    }

    @Test
    public void testTitle() {
        driver.get("https://www.youtube.com");
        String title = driver.getTitle();
        assertEquals("YouTube", title);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}