import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

import tk.erdmko.Solution;

import static org.junit.Assert.assertEquals;


public class BasicTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void cleanUpStreams() {
        System.setOut(null);
        System.setErr(null);
    }
    private void fileTest(String fileName1, String fileName2) throws FileNotFoundException {
        System.setIn(new FileInputStream(fileName1));
        Solution.main(new String[]{});
        assertEquals(
                new Scanner(new File(fileName2) ).useDelimiter("\\A").next(),
                outContent.toString()
        );
    }
    @Test
    public void bigNumbers() throws FileNotFoundException {
        fileTest("input12.txt", "output12.txt");
    }
    @Test
    public void smallNumbers() throws FileNotFoundException {
        fileTest("input01.txt", "output01.txt");
    }
}
