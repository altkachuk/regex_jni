package atproj.com.linesorter.jni;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import atproj.com.linesorter.app.LinesorterApp;
import atproj.com.linesorter.ui.activities.MainActivity;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class RegexTest {

    private JNILinesorter linesorter;

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        linesorter = LinesorterApp.getInstance().getJniLinesorter();
    }

    @Test
    public void testRegex() {
        linesorter.SetFilter("1*3");
        assertEquals(linesorter.NumOfMatches("123"), 1);
    }

    @Test
    public void testRegex2() {
        linesorter.SetFilter("1?3");
        assertEquals(linesorter.NumOfMatches("123"), 1);
    }

    @Test
    public void testRegex3() {
        linesorter.SetFilter("1??3");
        assertEquals(linesorter.NumOfMatches("123"), 0);
    }

    @Test
    public void testRegex4() {
        linesorter.SetFilter("123");
        assertEquals(linesorter.NumOfMatches("123"), 1);
    }

    @Test
    public void testRegex5() {
        linesorter.SetFilter("123");
        assertEquals(linesorter.NumOfMatches("12345"), 0);
    }

    @Test
    public void testRegex6() {
        linesorter.SetFilter("123*");
        assertEquals(linesorter.NumOfMatches("12345"), 1);
    }

    @Test
    public void testRegex7() {
        linesorter.SetFilter("123**");
        assertEquals(linesorter.NumOfMatches("12345"), 1);
    }

    @Test
    public void testRegex8() {
        linesorter.SetFilter("123***");
        assertEquals(linesorter.NumOfMatches("12345"), 1);
    }

    @Test
    public void testRegex9() {
        linesorter.SetFilter("*345");
        assertEquals(linesorter.NumOfMatches("12345"), 1);
    }

    @Test
    public void testRegex10() {
        linesorter.SetFilter("**345");
        assertEquals(linesorter.NumOfMatches("12345"), 1);
    }

    @Test
    public void testRegex11() {
        linesorter.SetFilter("***345");
        assertEquals(linesorter.NumOfMatches("12345"), 1);
    }

    @Test
    public void testRegex12() {
        linesorter.SetFilter("*123*");
        assertEquals(linesorter.NumOfMatches("123"), 1);
    }

    @Test
    public void testRegex13() {
        linesorter.SetFilter("**123**");
        assertEquals(linesorter.NumOfMatches("123"), 1);
    }

    @Test
    public void testRegex14() {
        linesorter.SetFilter("1?3");
        assertEquals(linesorter.NumOfMatches("123"), 1);
    }

    @Test
    public void testRegex15() {
        linesorter.SetFilter("1??3");
        assertEquals(linesorter.NumOfMatches("123"), 0);
    }

    @Test
    public void testRegex16() {
        linesorter.SetFilter("*45?");
        assertEquals(linesorter.NumOfMatches("123456"), 1);
    }

    @Test
    public void testRegex17() {
        linesorter.SetFilter("*45??");
        assertEquals(linesorter.NumOfMatches("123456"), 0);
    }

    @Test
    public void testRegex18() {
        linesorter.SetFilter("*34??");
        assertEquals(linesorter.NumOfMatches("123456"), 1);
    }

    @Test
    public void testRegex19() {
        linesorter.SetFilter("1?*");
        assertEquals(linesorter.NumOfMatches("123456"), 1);
    }

    @Test
    public void testRegex20() {
        linesorter.SetFilter("1?3?*");
        assertEquals(linesorter.NumOfMatches("123456"), 1);
    }

    @Test
    public void testRegex21() {
        linesorter.SetFilter("1*?*3?*");
        assertEquals(linesorter.NumOfMatches("123456"), 1);
    }

    @Test
    public void testRegex22() {
        linesorter.SetFilter("1??4");
        String string = "123\n1234";
        assertEquals(linesorter.NumOfMatches("123\n1234"), 1);
    }

    @Test
    public void testRegex23() {
        linesorter.SetFilter("1??4?");
        assertEquals(linesorter.NumOfMatches("123\n1234\n12345\n123456"), 1);
    }

    @Test
    public void testRegex24() {
        linesorter.SetFilter("*23*");
        assertEquals(linesorter.NumOfMatches("123\n1234\n12345\n123456"), 4);
    }

    @Test
    public void testRegex25() {
        linesorter.SetFilter("*23*?");
        assertEquals(linesorter.NumOfMatches("123\n1234\n12345\n123456"), 3);
    }

    @Test
    public void testRegex26() {
        linesorter.SetFilter("?*23*");
        assertEquals(linesorter.NumOfMatches("123\n234\n12345\n23456"), 2);
    }

    @Test
    public void testRegex27() {
        linesorter.SetFilter("??");
        assertEquals(linesorter.NumOfMatches("12\n345\n6\n7890"), 1);
    }

    @Test
    public void testRegex28() {
        linesorter.SetFilter("*??");
        assertEquals(linesorter.NumOfMatches("12\n345\n6\n7890"), 3);
    }

    @Test
    public void testRegex29() {
        linesorter.SetFilter("*??*");
        assertEquals(linesorter.NumOfMatches("12\n345\n6\n7890"), 3);
    }

    @Test
    public void testRegex30() {
        linesorter.SetFilter("?*?");
        assertEquals(linesorter.NumOfMatches("12\n345\n6\n7890"), 3);
    }

    @Test
    public void testRegex31() {
        linesorter.SetFilter("*?*?*");
        assertEquals(linesorter.NumOfMatches("12\n345\n6\n7890"), 3);
    }
}