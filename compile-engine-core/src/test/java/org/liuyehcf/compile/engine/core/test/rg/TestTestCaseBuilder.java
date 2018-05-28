package org.liuyehcf.compile.engine.core.test.rg;

import org.junit.Test;
import org.liuyehcf.compile.engine.core.rg.utils.AbstractTestCaseBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Liuye on 2017/10/24.
 */
public class TestTestCaseBuilder {

    private void testRegexGroup(String[] regexGroup,
                                boolean testAllPossibleCases,
                                boolean testRandomCases,
                                int randomTimes) {
        for (String regex : regexGroup) {
            testEachRegex(regex,
                    testAllPossibleCases,
                    testRandomCases,
                    randomTimes);
        }
    }

    private void testEachRegex(String regex,
                               boolean testAllPossibleCases,
                               boolean testRandomCases,
                               int randomTimes) {
        if (testAllPossibleCases) {
            Set<String> testCases = AbstractTestCaseBuilder.createAllOptionalTestCasesWithRegex(regex);
            verifyTestCasesWithRegex(testCases, regex);
        }

        if (testRandomCases) {
            Set<String> testCases = AbstractTestCaseBuilder.createRandomTestCasesWithRegex(regex, randomTimes);
            verifyTestCasesWithRegex(testCases, regex);
        }
    }

    private void verifyTestCasesWithRegex(Set<String> testCases, String regex) {
        Pattern p = Pattern.compile(regex);
        List<String> wrongCases = new ArrayList<>();
        for (String testCase : testCases) {
            Matcher m = p.matcher(testCase);
            if (!m.matches()) {
                wrongCases.add(testCase);
            }
        }

        if (!wrongCases.isEmpty()) {
            System.out.println(wrongCases);
            throw new RuntimeException();
        }
    }

    @Test
    public void testRegexGroup1() {
        testRegexGroup(TestRegex.REGEX_GROUP_1,
                true,
                true,
                10);
    }

    @Test
    public void testRegexGroup2() {
        testRegexGroup(TestRegex.REGEX_GROUP_2,
                true,
                true,
                10);
    }

    @Test
    public void testRegexGroup3() {
        testRegexGroup(TestRegex.REGEX_GROUP_3,
                true,
                true,
                10);
    }

    @Test
    public void testRegexGroup4() {
        testRegexGroup(TestRegex.REGEX_GROUP_4,
                true,
                true,
                10);
    }

    @Test
    public void testRegexGroup5() {
        testRegexGroup(TestRegex.REGEX_GROUP_5,
                true,
                true,
                10);
    }

    @Test
    public void testRegexGroup6() {
        testRegexGroup(TestRegex.REGEX_GROUP_6,
                true,
                true,
                1000);
    }

    @Test
    public void testRegexGroup7() {
        testRegexGroup(TestRegex.REGEX_GROUP_7,
                true,
                true,
                1000);
    }

    @Test
    public void testRegexGroup8() {
        testRegexGroup(TestRegex.REGEX_GROUP_8,
                false,
                true,
                1000);
    }

    @Test
    public void testRegexGroup9() {
        testRegexGroup(TestRegex.REGEX_GROUP_9,
                false,
                true,
                1000);
    }

    @Test
    public void testRegexGroupSpecial() {
        testRegexGroup(TestRegex.REGEX_GROUP_SPECIAL,
                false,
                true,
                100);
    }
}
